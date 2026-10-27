package com.example.X_O.datasource.repository;

import com.example.X_O.datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

	Optional<UserEntity> findByLogin(String login);
}
