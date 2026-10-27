package com.example.X_O.datasource.repository;

import com.example.X_O.datasource.model.GameEntity;
import com.example.X_O.domain.model.GameState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {

	List<GameEntity> findByState(GameState state);

	@Query("""
			SELECT game
			FROM GameEntity game
			WHERE (game.xPlayerId = :userId OR game.oPlayerId = :userId)
			  AND ((game.state = :winState AND game.winnerId = :userId) OR game.state = :drawState)
			ORDER BY game.createdAt DESC
			""")
	List<GameEntity> findCompletedByUserId(
			@Param("userId") UUID userId,
			@Param("winState") GameState winState,
			@Param("drawState") GameState drawState);

	@Query(value = """
			SELECT users.id AS "userId",
			       users.login AS "login",
			       CASE
			           WHEN SUM(CASE WHEN games.state = 'DRAW'
			                         OR (games.state = 'WIN' AND games.winner_id IS DISTINCT FROM users.id)
			                    THEN 1 ELSE 0 END) = 0
			           THEN CAST(SUM(CASE WHEN games.state = 'WIN' AND games.winner_id = users.id
			                              THEN 1 ELSE 0 END) AS DOUBLE PRECISION)
			           ELSE CAST(SUM(CASE WHEN games.state = 'WIN' AND games.winner_id = users.id
			                              THEN 1 ELSE 0 END) AS DOUBLE PRECISION)
			                / SUM(CASE WHEN games.state = 'DRAW'
			                                   OR (games.state = 'WIN'
			                                       AND games.winner_id IS DISTINCT FROM users.id)
			                              THEN 1 ELSE 0 END)
			       END AS "winRatio"
			FROM users
			JOIN games ON games.x_player_id = users.id OR games.o_player_id = users.id
			WHERE games.state IN ('WIN', 'DRAW')
			GROUP BY users.id, users.login
			ORDER BY "winRatio" DESC, users.login
			""", nativeQuery = true)
	List<LeaderboardProjection> findLeaderboard(Pageable pageable);
}
