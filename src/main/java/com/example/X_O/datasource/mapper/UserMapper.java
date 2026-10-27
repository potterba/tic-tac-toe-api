package com.example.X_O.datasource.mapper;

import com.example.X_O.datasource.model.UserEntity;
import com.example.X_O.domain.model.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static UserEntity toEntity(User user) {
		if (user == null) {
			return null;
		}
		return new UserEntity(user.getId(), user.getLogin(), user.getPassword(), user.getRoles());
	}

	public static User toDomain(UserEntity entity) {
		if (entity == null) {
			return null;
		}
		return new User(entity.getId(), entity.getLogin(), entity.getPassword(), entity.getRoles());
	}
}
