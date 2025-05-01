package com.example.model_rgr;

public class GameStats {
    private int playerWins = 0;
    private int computerWins = 0;
    private int totalGames = 0;
    private int totalSteps = 0;

    public void recordGame(boolean playerWon, int steps) {
        totalGames++;
        totalSteps += steps;
        if (playerWon) {
            playerWins++;
        } else {
            computerWins++;
        }
    }

    public int getPlayerWins() { return playerWins; }
    public int getComputerWins() { return computerWins; }
    public int getTotalGames() { return totalGames; }

    public double getAverageSteps() {
        return totalGames == 0 ? 0 : (double) totalSteps / totalGames;
    }

    public void reset() {
        playerWins = 0;
        computerWins = 0;
        totalSteps = 0;
        totalGames = 0;
    }
}