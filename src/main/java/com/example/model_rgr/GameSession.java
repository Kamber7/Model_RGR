package com.example.model_rgr;

import java.util.Set;
import java.util.Random;

public class GameSession {
    private final Labir labir;
    private final ResponseAlgorithm algorithm;
    private final Random random = new Random();
    private int currentPosition;
    private int stepsTaken;
    private boolean gameOver;
    private boolean playerWon;

    public GameSession(Labir labir, ResponseAlgorithm algorithm) {
        this.labir = labir;
        this.algorithm = algorithm;
        this.currentPosition = labir.getStartPoint();
        this.stepsTaken = 0;
        this.gameOver = false;
        this.playerWon = false;
    }

    public boolean makeMove(int proposedMove) {
        if (gameOver) return false;

        stepsTaken++;
        Set<Integer> possibleMoves = labir.getPossibleMoves(currentPosition);

        if (!possibleMoves.contains(proposedMove)) {
            gameOver = true;
            playerWon = false;
            return false;
        }

        boolean shouldAgree = algorithm.shouldAgree();
        if (shouldAgree) {
            currentPosition = proposedMove;
        } else {
            Set<Integer> availableMoves = labir.getAvailableMovesWhenDisagree(currentPosition, proposedMove);
            currentPosition = availableMoves.isEmpty() ? currentPosition :
                    availableMoves.stream().skip(random.nextInt(availableMoves.size())).findFirst().get();
        }

        if (labir.isExit(currentPosition)) {
            gameOver = true;
            playerWon = true;
        } else if (stepsTaken >= labir.getMaxSteps()) {
            gameOver = true;
            playerWon = false;
        }

        return shouldAgree;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }
}