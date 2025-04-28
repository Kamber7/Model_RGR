package com.example.model_rgr;

import java.util.List;

public class CustomAlgorithm implements ResponseAlgorithm {
    private final List<Boolean> pattern;
    private int currentStep = 0;

    public CustomAlgorithm(List<Boolean> pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean shouldAgree() {
        if (pattern.isEmpty()) return true; // По умолчанию, если паттерн пустой
        boolean result = pattern.get(currentStep % pattern.size());
        currentStep++;
        return result;
    }

    @Override
    public String getAlgorithmName() {
        return "Пользовательский алгоритм (" + pattern.size() + " шагов)";
    }
}