package com.example.model_rgr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.stream.Collectors;

public class Labir {
    private static final Set<Integer> EXIT_POINTS = Set.of(1, 5, 9, 24, 26);
    private static final int START_POINT = 13;
    private static final int MAX_STEPS = 25;
    private final Random random = new Random();

    private final Map<Integer, Set<Integer>> connections;
    private final Map<Integer, Integer> levelMap;

    public Labir() {
        connections = new HashMap<>();
        levelMap = new HashMap<>();
        initializeLevels();
        initializeConnections();
    }

    private void initializeLevels() {
        // Центр (0 уровень)
        levelMap.put(13, 0);

        // 1 уровень
        Set.of(7, 12, 14, 16, 17).forEach(p -> levelMap.put(p, 1));

        // 2 уровень
        Set.of(6, 8, 15, 18, 22).forEach(p -> levelMap.put(p, 2));

        // 3 уровень
        Set.of(3, 10, 11, 21, 23).forEach(p -> levelMap.put(p, 3));

        // 4 уровень
        Set.of(2, 4, 19, 20, 25).forEach(p -> levelMap.put(p, 4));

        // 5 уровень (выходы)
        EXIT_POINTS.forEach(p -> levelMap.put(p, 5));
    }

    private void initializeConnections() {
        // Центр (0 уровень)
        connections.put(13, Set.of(7, 12, 14, 16, 17));

        // 1 уровень
        connections.put(7, Set.of(13, 6, 8, 12, 14));
        connections.put(12, Set.of(13, 6, 7, 15, 16));
        connections.put(14, Set.of(13, 7, 8, 17, 18));
        connections.put(16, Set.of(13, 12, 15, 17, 22));
        connections.put(17, Set.of(13, 14, 16, 18, 22));

        // 2 уровень
        connections.put(6, Set.of(7, 12, 3, 10));
        connections.put(8, Set.of(7, 14, 3, 11));
        connections.put(15, Set.of(12, 16, 10, 21));
        connections.put(18, Set.of(14, 17, 11, 23));
        connections.put(22, Set.of(16, 17, 21, 23));

        // 3 уровень
        connections.put(3, Set.of(6, 8, 2, 4));
        connections.put(10, Set.of(6, 15, 2, 20));
        connections.put(11, Set.of(8, 18, 4, 19));
        connections.put(21, Set.of(15, 22, 20, 25));
        connections.put(23, Set.of(18, 22, 19, 25));

        // 4 уровень
        connections.put(2, Set.of(3, 10, 1, 5));
        connections.put(4, Set.of(3, 11, 1, 9));
        connections.put(19, Set.of(11, 23, 9, 26));
        connections.put(20, Set.of(10, 21, 5, 24));
        connections.put(25, Set.of(21, 23, 24, 26));

        // 5 уровень (выходы) - без исходящих соединений
    }

    public Set<Integer> getPossibleMoves(int currentPosition) {
        return connections.getOrDefault(currentPosition, Set.of());
    }

    public int getRandomMove(int currentPosition) {
        Set<Integer> moves = getPossibleMoves(currentPosition);
        if (moves.isEmpty()) return currentPosition;
        return moves.stream().skip(random.nextInt(moves.size())).findFirst().get();
    }

    public boolean isExit(int position) {
        return EXIT_POINTS.contains(position);
    }

    public int getStartPoint() {
        return START_POINT;
    }

    public int getMaxSteps() {
        return MAX_STEPS;
    }

    public int getLevel(int position) {
        return levelMap.getOrDefault(position, -1);
    }

    public Set<Integer> getAvailableMovesWhenDisagree(int currentPos, int proposedMove) {
        Set<Integer> allMoves = getPossibleMoves(currentPos);
        int currentLevel = getLevel(currentPos);
        int proposedLevel = getLevel(proposedMove);

        // Особое правило для стартовой точки
        if (currentPos == 13) {
            return allMoves.stream()
                    .filter(move -> getLevel(move) == 1) // Только уровень S1
                    .filter(move -> !move.equals(proposedMove))
                    .collect(Collectors.toSet());
        }

        return allMoves.stream()
                .filter(move -> {
                    int moveLevel = getLevel(move);

                    if (proposedMove == 13) {
                        // Если предложен переход в старт (13)
                        // При отказе можно только на уровень N+1 или остаться (N)
                        return (moveLevel == currentLevel + 1) || (moveLevel == currentLevel);
                    }
                    else if (proposedLevel > currentLevel) {
                        // Если предложен переход вперед (N+1)
                        // При отказе можно только на уровень N-1 или остаться (N)
                        return (moveLevel == currentLevel - 1) || (moveLevel == currentLevel);
                    }
                    else {
                        // Если предложен переход назад (N-1) или остаться (N)
                        // При отказе можно только на уровень N+1
                        return moveLevel == currentLevel + 1;
                    }
                })
                .filter(move -> !move.equals(proposedMove)) // Исключаем предложенный ход
                .collect(Collectors.toSet());
    }
}