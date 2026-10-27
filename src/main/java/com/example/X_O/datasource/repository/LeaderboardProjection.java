package com.example.X_O.datasource.repository;

import java.util.UUID;

public interface LeaderboardProjection {

	UUID getUserId();

	String getLogin();

	Double getWinRatio();
}
