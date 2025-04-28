package com.example.model_rgr;

public class AlwaysAgreeAlgorithm implements ResponseAlgorithm {
    @Override public boolean shouldAgree() { return true; }
    @Override public String getAlgorithmName() { return "Всегда согласен"; }
}
