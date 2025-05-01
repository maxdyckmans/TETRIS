package Tetris;

public record Experience(double[] state, int action, double reward, double[] nextState, boolean terminated ) {
}
