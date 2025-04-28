package com.example.model_rgr;

public class CycleAlgorithm implements ResponseAlgorithm {
    private final int[] pattern = {5, 6, 2, 4, 4, 1, 1, 2};
    private int index = 0;
    private int count = 0;

    @Override
    public boolean shouldAgree() {
        count++;
        if (count > pattern[index]) {
            count = 1;
            index = (index + 1) % pattern.length;
        }
        return index % 2 == 0;
    }

    @Override
    public String getAlgorithmName() {
        return "Циклический алгоритм (5с/6н/2с/4н/4с/1н/1с/2н)";
    }
}
