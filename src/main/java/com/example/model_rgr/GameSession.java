package com.example.model_rgr;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameSession {
    private final Maze maze;
    private final ResponseAlgorithm algorithm;
    private final Random random = new Random();
    private int currentPosition;
    private int stepsTaken;
    private boolean gameOver;
    private boolean playerWon;

    public GameSession(Maze maze, ResponseAlgorithm algorithm) {
        this.maze = maze;
        this.algorithm = algorithm;
        this.currentPosition = maze.getStartPoint();
        this.stepsTaken = 0;
        this.gameOver = false;
    }

    public boolean makeMove(int proposedMove) {
        if (gameOver) return false;

        stepsTaken++;
        Set<Integer> possibleMoves = maze.getPossibleMoves(currentPosition);

        if (!possibleMoves.contains(proposedMove)) {
            gameOver = true;
            playerWon = false;
            return false;
        }

        boolean shouldAgree = algorithm.shouldAgree();
        if (shouldAgree) {
            currentPosition = proposedMove;
        } else {
            // Выбираем случайный ДРУГОЙ допустимый ход
            Set<Integer> otherMoves = new HashSet<>(possibleMoves);
            otherMoves.remove(proposedMove);
            currentPosition = otherMoves.isEmpty() ? currentPosition :
                    otherMoves.stream().skip(random.nextInt(otherMoves.size())).findFirst().get();
        }

        if (maze.isExit(currentPosition)) {
            gameOver = true;
            playerWon = true;
        } else if (stepsTaken >= maze.getMaxSteps()) {
            gameOver = true;
            playerWon = false;
        }

        return shouldAgree;
    }

    public int getCurrentPosition() { return currentPosition; }
    public int getStepsTaken() { return stepsTaken; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPlayerWon() { return playerWon; }
}