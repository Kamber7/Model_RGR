package com.example.model_rgr;

public class AlwaysDisagreeAlgorithm implements ResponseAlgorithm {
    @Override public boolean shouldAgree() { return false; }
    @Override public String getAlgorithmName() { return "Всегда не согласен"; }
}
