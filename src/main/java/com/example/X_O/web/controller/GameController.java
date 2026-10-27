package com.example.X_O.web.controller;

import com.example.X_O.datasource.mapper.GameMapper;
import com.example.X_O.datasource.repository.GameRepository;
import com.example.X_O.domain.model.Game;
import com.example.X_O.domain.model.GameField;
import com.example.X_O.domain.model.GameState;
import com.example.X_O.domain.service.GameService;
import com.example.X_O.web.mapper.GameWebMapper;
import com.example.X_O.web.model.GameDto;
import com.example.X_O.web.model.LeaderboardDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

	private final GameRepository repository;
	private final GameService gameService;

	public GameController(GameRepository repository, GameService gameService) {
		this.repository = repository;
		this.gameService = gameService;
	}

	@PostMapping
	public ResponseEntity<GameDto> createGame(@RequestBody(required = false) GameDto body) {
		UUID userId = currentUserId();
		int playerSymbol = GameField.X;
		if (body != null && body.getPlayerSymbol() == GameField.O) {
			playerSymbol = GameField.O;
		}
		boolean vsComputer = body == null || body.getOpponent() == null || !"USER".equalsIgnoreCase(body.getOpponent());
		UUID xPlayerId = playerSymbol == GameField.X ? userId : null;
		UUID oPlayerId = playerSymbol == GameField.O ? userId : null;
		Game game = new Game(
				UUID.randomUUID(),
				new GameField(),
				playerSymbol,
				vsComputer,
				vsComputer ? GameState.TURN : GameState.WAITING,
				xPlayerId,
				oPlayerId,
				null,
				null,
				Instant.now());
		if (vsComputer) {
			game = finishComputerTurn(game);
		}
		repository.save(GameMapper.toEntity(game));
		return ResponseEntity.ok(GameWebMapper.toDto(game));
	}

	@GetMapping("/available")
	public List<GameDto> availableGames() {
		UUID userId = currentUserId();
		return repository.findByState(GameState.WAITING).stream()
				.map(GameMapper::toDomain)
				.filter(game -> !game.isVsComputer())
				.filter(game -> !userId.equals(game.getXPlayerId()) && !userId.equals(game.getOPlayerId()))
				.map(GameWebMapper::toDto)
				.toList();
	}

	@GetMapping("/history")
	public List<GameDto> completedGames() {
		return gameService.getCompletedGames(currentUserId()).stream()
				.map(GameWebMapper::toDto)
				.toList();
	}

	@GetMapping("/leaderboard")
	public ResponseEntity<?> leaderboard(
			@RequestParam(name = "n", required = false) Integer n,
			@RequestParam(name = "limit", required = false) Integer limit) {
		int requestedLimit = n != null ? n : limit == null ? 0 : limit;
		if (requestedLimit <= 0) {
			return ResponseEntity.badRequest().body("Параметр n должен быть положительным");
		}
		List<LeaderboardDto> result = gameService.getTopPlayers(requestedLimit).stream()
				.map(entry -> new LeaderboardDto(entry.getUserId(), entry.getLogin(), entry.getWinRatio()))
				.toList();
		return ResponseEntity.ok(result);
	}

	@PostMapping("/{uuid}/join")
	public ResponseEntity<?> joinGame(@PathVariable UUID uuid) {
		Game current = findGame(uuid);
		if (current == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Игра не найдена: " + uuid);
		}
		if (current.isVsComputer() || current.getState() != GameState.WAITING) {
			return ResponseEntity.badRequest().body("К этой игре нельзя присоединиться");
		}
		UUID userId = currentUserId();
		if (userId.equals(current.getXPlayerId()) || userId.equals(current.getOPlayerId())) {
			return ResponseEntity.badRequest().body("Вы уже участвуете в этой игре");
		}
		UUID xPlayerId = current.getXPlayerId() == null ? userId : current.getXPlayerId();
		UUID oPlayerId = current.getOPlayerId() == null ? userId : current.getOPlayerId();
		Game joined = current.withPlayers(xPlayerId, oPlayerId).withState(GameState.TURN, null, null);
		joined = gameService.refreshState(joined);
		repository.save(GameMapper.toEntity(joined));
		return ResponseEntity.ok(GameWebMapper.toDto(joined));
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<?> getGame(@PathVariable UUID uuid) {
		Game current = findGame(uuid);
		if (current == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Игра не найдена: " + uuid);
		}
		return ResponseEntity.ok(GameWebMapper.toDto(current));
	}

	@PostMapping("/{uuid}")
	public ResponseEntity<?> makeMove(@PathVariable UUID uuid, @RequestBody GameDto body) {
		Game current = findGame(uuid);
		if (current == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Игра не найдена: " + uuid);
		}
		if (current.getState() == GameState.WAITING) {
			return ResponseEntity.badRequest().body("Ожидание второго игрока");
		}
		if (gameService.isGameOver(current)) {
			return ResponseEntity.badRequest().body("Игра уже завершена");
		}
		UUID userId = currentUserId();
		if (!userId.equals(current.getTurnPlayerId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Сейчас не ваш ход");
		}
		if (body == null || body.getField() == null) {
			return ResponseEntity.badRequest().body("Поле field обязательно");
		}
		int symbol = symbolOf(current, userId);
		if (!gameService.validateField(current, body.getField(), symbol)) {
			return ResponseEntity.badRequest().body("Некорректное поле: допустим только один новый ход в пустую клетку");
		}
		Game updated = finishComputerTurn(current.withField(new GameField(body.getField())));
		repository.save(GameMapper.toEntity(updated));
		return ResponseEntity.ok(GameWebMapper.toDto(updated));
	}

	private Game finishComputerTurn(Game game) {
		Game updated = gameService.refreshState(game);
		if (updated.isVsComputer() && updated.getState() == GameState.TURN && updated.getTurnPlayerId() == null) {
			updated = gameService.refreshState(gameService.getNextMove(updated));
		}
		return updated;
	}

	private Game findGame(UUID id) {
		return repository.findById(id).map(GameMapper::toDomain).orElse(null);
	}

	private int symbolOf(Game game, UUID playerId) {
		if (playerId.equals(game.getXPlayerId())) {
			return GameField.X;
		}
		if (playerId.equals(game.getOPlayerId())) {
			return GameField.O;
		}
		return game.getPlayerSymbol();
	}

	private UUID currentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UUID userId) {
			return userId;
		}
		throw new IllegalStateException("Пользователь не авторизован");
	}
}
