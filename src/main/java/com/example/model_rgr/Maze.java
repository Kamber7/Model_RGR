package com.example.model_rgr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Maze {
    private static final Set<Integer> EXIT_POINTS = Set.of(1, 5, 9, 24, 26);
    private static final int START_POINT = 13;
    private static final int MAX_STEPS = 25;
    private final Random random = new Random();

    private final Map<Integer, Set<Integer>> connections;

    public Maze() {
        connections = new HashMap<>();
        // Правильные соединения согласно схеме
        connections.put(2, Set.of(1, 3, 5, 10));
        connections.put(3, Set.of(2, 4, 6, 8));
        connections.put(4, Set.of(1, 3, 9, 11));
        connections.put(6, Set.of(3, 7, 10, 12));
        connections.put(7, Set.of(6, 8, 12, 14));
        connections.put(8, Set.of(3, 7, 11, 14));
        connections.put(10, Set.of(2, 6, 15, 20));
        connections.put(11, Set.of(4, 8, 18, 19));
        connections.put(12, Set.of(6, 7, 13, 15, 16));
        connections.put(13, Set.of(7, 12, 14, 16, 17)); // старт
        connections.put(14, Set.of(7, 8, 13, 17, 18));
        connections.put(15, Set.of(10, 12, 16, 21));
        connections.put(16, Set.of(12, 13, 15, 17, 22));
        connections.put(17, Set.of(13, 14, 16, 18, 22));
        connections.put(18, Set.of(11, 14, 17, 23));
        connections.put(19, Set.of(9, 11, 23, 26));
        connections.put(20, Set.of(5, 10, 21, 24));
        connections.put(21, Set.of(15, 20, 22, 25));
        connections.put(22, Set.of(16, 17, 21, 23));
        connections.put(23, Set.of(18, 19, 22, 25));
        connections.put(25, Set.of(21, 23, 24, 26));
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
}