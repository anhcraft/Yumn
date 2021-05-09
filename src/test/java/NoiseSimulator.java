import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NoiseSimulator extends JPanel {
    public static final int INIT_WIDTH = 700;
    public static final int INIT_HEIGHT = 700;
    public static final OctaveNoiseGenerator[] NOISES = new OctaveNoiseGenerator[]{
            new OctaveNoiseGenerator(0)
                    .setScale(360)
                    .setFrequency(0.102)
                    .setAmplitude(1)
                    .setLacunarity(1.663)
                    .setPersistence(0.27)
                    .setOctaves(3)
                    .setNormalized(false),
            new OctaveNoiseGenerator(0)
                    .setScale(360)
                    .setFrequency(0.775)
                    .setAmplitude(0.179)
                    .setLacunarity(1.165)
                    .setPersistence(1.106)
                    .setOctaves(4)
                    .setNormalized(false),
            new OctaveNoiseGenerator(0)
                    .setScale(360)
                    .setFrequency(0.848)
                    .setAmplitude(0.494)
                    .setLacunarity(0.731)
                    .setPersistence(1.004)
                    .setOctaves(6)
                    .setNormalized(false)
    };
    public static boolean ctrlDown;
    public static boolean zoomMode;
    public static boolean freqMode;
    public static boolean amlMode;
    public static boolean octMode;
    public static boolean lanMode;
    public static boolean pstMode;
    public static boolean fMode = true;
    public static int X = 0;
    public static int Z = 0;
    public static boolean moving;
    private static JFrame f;
    private static int currentNoise;

    private double f0(int x, int z){
        double v = NOISES[0].noise(x + 1, z + 1);
        v *= Math.sin(Math.pow(Math.E, v) * NOISES[2].noise(x, z) * 0.3);
        return v;
    }

    private double f1(int x, int y, int z){
        double v = NOISES[1].noise(x, y, z);
        v += Math.pow(NOISES[1].noise(x << 2, z << 2), 5);
        v += Math.sin(Math.pow(Math.E, v) * v * 0.3);
        return v;
    }

    public static void main(String [] args) {
        f = new JFrame();
        f.setSize(INIT_WIDTH, INIT_HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addMouseWheelListener(e -> {
            if(zoomMode) {
                NOISES[currentNoise].setScale(MathUtil.clampDouble(NOISES[currentNoise].getScale() + e.getUnitsToScroll() / (ctrlDown ? -1d : -3d), 1, 500));
            } else if(freqMode) {
                NOISES[currentNoise].setFrequency(MathUtil.clampDouble(NOISES[currentNoise].getFrequency() + e.getUnitsToScroll() * (ctrlDown ? -0.01 : -0.001), 0, 1));
            } else if(amlMode) {
                NOISES[currentNoise].setAmplitude(MathUtil.clampDouble(NOISES[currentNoise].getAmplitude() + e.getUnitsToScroll() * (ctrlDown ? -0.01 : -0.001), 0, 1));
            } else if(lanMode) {
                NOISES[currentNoise].setLacunarity(MathUtil.clampDouble(NOISES[currentNoise].getLacunarity() + e.getUnitsToScroll() * (ctrlDown ? -0.01 : -0.001), 0, 10));
            } else if(pstMode) {
                NOISES[currentNoise].setPersistence(MathUtil.clampDouble(NOISES[currentNoise].getPersistence() + e.getUnitsToScroll() * (ctrlDown ? -0.01 : -0.001), 0, 10));
            } else if(octMode) {
                NOISES[currentNoise].setOctaves(MathUtil.clampInt((int) (NOISES[currentNoise].getOctaves() + e.getUnitsToScroll() / -3d), 1, 30));
            }
        });
        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            private void reset(){
                zoomMode = false;
                freqMode = false;
                amlMode = false;
                lanMode = false;
                pstMode = false;
                octMode = false;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // CTRL
                    case 17: {
                        ctrlDown = true;
                        break;
                    }
                    // W
                    case 87: {
                        reset();
                        if(ctrlDown) {
                            Z += 5;
                        } else {
                            X += 5;
                        }
                        break;
                    }
                    // S
                    case 83: {
                        reset();
                        if(ctrlDown) {
                            Z -= 5;
                        } else {
                            X -= 5;
                        }
                    }
                    // Z
                    case 90: {
                        reset();
                        zoomMode = true;
                        break;
                    }
                    // F
                    case 70: {
                        reset();
                        freqMode = true;
                        break;
                    }
                    // A
                    case 65: {
                        reset();
                        amlMode = true;
                        break;
                    }
                    // N
                    case 78: {
                        reset();
                        NOISES[currentNoise].setNormalized(!NOISES[currentNoise].isNormalized());
                        break;
                    }
                    // O
                    case 79: {
                        reset();
                        octMode = true;
                        break;
                    }
                    // L
                    case 76: {
                        reset();
                        lanMode = true;
                        break;
                    }
                    // P
                    case 80: {
                        reset();
                        pstMode = true;
                        break;
                    }
                    // Q
                    case 81: {
                        reset();
                        if(ctrlDown) {
                            moving = !moving;
                        } else {
                            fMode = !fMode;
                        }
                        break;
                    }
                    // E
                    case 69: {
                        reset();
                        currentNoise++;
                        if(currentNoise == NOISES.length) {
                            currentNoise = 0;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 17: {
                        ctrlDown = false;
                        break;
                    }
                    case 90: {
                        zoomMode = false;
                        break;
                    }
                    case 70: {
                        freqMode = false;
                        break;
                    }
                    case 65: {
                        amlMode = false;
                        break;
                    }
                    case 79: {
                        octMode = false;
                        break;
                    }
                    case 76: {
                        lanMode = false;
                        break;
                    }
                    case 80: {
                        pstMode = false;
                        break;
                    }
                }
            }
        });
        f.add(new NoiseSimulator());
        f.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(moving) Z += 10;
        double minNoise = 1;
        double maxNoise = 0;
        int minHeight = 1;
        int maxHeight = 0;
        for(int x = 0; x < f.getWidth(); x++) {
            int height = (int) (f0(X + x, Z) * f.getHeight());
            minHeight = Math.min(minHeight, height);
            maxHeight = Math.max(maxHeight, height);
            for(int y = 1; y <= height; y++) {
                double noise = f1(X + x, y, Z);
                minNoise = Math.min(minNoise, noise);
                maxNoise = Math.max(maxNoise, noise);
                noise = MathUtil.clampDouble(noise, 0, 1);
                //g.setColor(new Color(0, 0, 0, (float) MathUtil.round(noise)));
                g.setColor(noise <= 0.5 ? new Color(255, 255, 255) : new Color(0, 0, 0));
                g.fillRect(x, f.getHeight() - height + y, 1, 1);
            }
        }
        g.setFont(g.getFont().deriveFont(18f));
        g.setColor(Color.BLACK);
        g.drawString(String.format("X %s | Z %s; -H %d, +H %d (%.2f%%); -N %.2f, +N %.2f;", X, Z, minHeight, maxHeight, maxHeight * 1.0 / f.getHeight() * 100f, minNoise, maxNoise) + (ctrlDown ? "[CTRL]" : ""), 50, 35);
        int lastY = 60;
        for (int i = 0; i < NOISES.length; i++) {
            OctaveNoiseGenerator noise = NOISES[i];
            if(i == currentNoise) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(String.format("Noise#%d: (Scale) %.2f | (Octaves) %d ", i+1, noise.getScale(), noise.getOctaves()) + (noise.isNormalized() ? "[N]": ""), 50, lastY);
            g.drawString(String.format("- (F) %.3f | (A) %.3f | (L) %.3f | (P) %.3f", noise.getFrequency(), noise.getAmplitude(), noise.getLacunarity(), noise.getPersistence()), 50, lastY + 25);
            lastY += 50;
        }
        repaint();
    }
}
