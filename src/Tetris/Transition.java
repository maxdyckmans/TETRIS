package Tetris;

public record Transition(int[] state, double reward, boolean terminated) {}