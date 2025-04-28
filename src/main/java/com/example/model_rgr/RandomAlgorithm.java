package com.example.model_rgr;

public class RandomAlgorithm implements ResponseAlgorithm {
    @Override public boolean shouldAgree() { return Math.random() > 0.5; }
    @Override public String getAlgorithmName() { return "Случайный выбор"; }
}
