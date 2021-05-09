import com.google.common.util.concurrent.AtomicDouble;
import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.biomes.BiomeManager;
import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.biomes.land.*;
import dev.anhcraft.yumn.biomes.ocean.*;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.DummyChunkData;
import dummies.DummyWorld;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorldSimulator extends JPanel {
    private enum DisplayMode {
        TERRAIN_NOISE,
        BIOMES,
        BIOME_BORDERS
    }

    public static final World WORLD = new DummyWorld();
    public static final WorldGenerator GEN = new WorldGenerator();
    public static final long SEED = WORLD.getSeed();
    public static final XoRoShiRo128StarStarRandomGenerator RAND = new XoRoShiRo128StarStarRandomGenerator(SEED);
    public static final ExecutorService EXEC = Executors.newFixedThreadPool(1);
    public static final AtomicLong LAST_CALCULATION = new AtomicLong();
    public static final int INIT_WIDTH = 500;
    public static final int INIT_HEIGHT = 500;
    public static final boolean INVERT_COLOR = false;
    public static DisplayMode mode = DisplayMode.BIOMES;
    public static int zoom = 3;
    public static boolean shiftPressing;
    public static int globalX;
    public static int globalY;
    public static AtomicInteger counter = new AtomicInteger(1);
    public static AtomicDouble renderSpeed;
    public static double lastMouseX;
    public static double lastMouseY;
    public static double[][] heightMap = new double[0][0];
    public static boolean[][] biomeEdges;
    public static boolean[][] sharpness;
    public static YumnBiome[][] biomeMap;
    private static JFrame f;

    public static void recalculate(){
        recalculate(() -> {});
    }

    public static synchronized void recalculate(Runnable callback){
        if(System.currentTimeMillis() - LAST_CALCULATION.get() <= 1500){
            return;
        }
        LAST_CALCULATION.set(System.currentTimeMillis());
        EXEC.submit(() -> {
            System.out.println("Generating chunks... "+f.getWidth()+"x"+f.getHeight());
            counter.set(0);
            renderSpeed = null;
            long time = System.currentTimeMillis();
            double[][] hm2 = new double[f.getWidth()][f.getHeight()];
            boolean[][] be = new boolean[f.getWidth()][f.getHeight()];
            boolean[][] sh = new boolean[f.getWidth()][f.getHeight()];
            YumnBiome[][] bm = new YumnBiome[f.getWidth()][f.getHeight()];
            int x = 0;
            do {
                int y = 0;
                do {
                    long t = System.currentTimeMillis();
                    ChunkGenerator.ChunkData cd = new DummyChunkData(256);
                    Context ctx = new Context(
                            GEN, WORLD,
                            (globalX + x) >> 4,
                            (globalY + y) >> 4,
                            SEED,
                            RAND, cd
                    );
                    //long time1 = System.currentTimeMillis();
                    HeightMapCell[][] chunkHeightMap = GEN.requestHeightMap(WORLD, ctx.getChunkX(), ctx.getChunkZ(), () -> ctx, false).getBackend();
                    //long delta = System.currentTimeMillis() - time1;
                    //System.out.println(String.format("Heightmap piece at %s %s generated in %s ms (~ %.3f s)", globalX + x, globalX + y, delta, delta/1000d));
                    for (int cx = 0; cx < Math.min(f.getWidth()-x, chunkHeightMap.length); cx++) {
                        for (int cz = 0; cz < Math.min(f.getHeight()-y, chunkHeightMap[cx].length); cz++) {
                            hm2[x + cx][y + cz] = chunkHeightMap[cx][cz].getNoise();
                            be[x + cx][y + cz] = chunkHeightMap[cx][cz].isBiomeEdge();
                            sh[x + cx][y + cz] = chunkHeightMap[cx][cz].isSharpness();
                            bm[x + cx][y + cz] = chunkHeightMap[cx][cz].getBiome();
                            counter.incrementAndGet();
                        }
                    }
                    if(renderSpeed == null) {
                        renderSpeed = new AtomicDouble(System.currentTimeMillis() - t);
                    } else {
                        renderSpeed.set((System.currentTimeMillis() - t + renderSpeed.get()) / 2);
                    }
                } while ((y += 16) < f.getHeight());
            } while ((x += 16) < f.getWidth());
            heightMap = hm2;
            biomeEdges = be;
            biomeMap = bm;
            sharpness = sh;
            long delta = System.currentTimeMillis() - time;
            System.out.printf("Chunks generated in %s ms (~ %.3f s)%n", delta, delta/1000d);
            callback.run();
       });
    }

    public static void main(String [] args) {
        f = new JFrame();
        f.setSize(INIT_WIDTH, INIT_HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new WorldSimulator());
        f.addMouseWheelListener(e -> {
            zoom = MathUtil.clampInt(zoom + e.getUnitsToScroll() / -3, 1, 15);
            recalculate();
        });
        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isShiftDown()){
                    shiftPressing = true;
                } else if(e.isAltDown()){
                    globalX += RAND.nextInt(500);
                    globalY += RAND.nextInt(500);
                } else if(e.isControlDown()){
                    if(mode.ordinal() == DisplayMode.values().length - 1){
                        mode = DisplayMode.values()[0];
                    } else {
                        mode = DisplayMode.values()[mode.ordinal() + 1];
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                shiftPressing = false;
            }
        });
        f.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                globalX += (lastMouseX - e.getX()) / 2;
                globalY += (lastMouseY - e.getY()) / 2;
                recalculate();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                recalculate();
            }
        });
        //noinspection ResultOfMethodCallIgnored
        BiomeManager.getInstance(); // init biome manager
        f.setVisible(true);
        recalculate();
    }

    public Color getBiomeColor(YumnBiome biome, int a){
        /*if(biome instanceof PlainCoast){
            return new Color(197, 234, 119);
        } else if(biome instanceof Coast){
            return new Color(233, 234, 177);
        } else*/ if(biome instanceof Desert){
            return new Color(244, 208, 82);
        } else if(biome instanceof Swamp){
            return new Color(75, 84, 64);
        } else if(biome instanceof Jungle){
            return new Color(0, 80, 29);
        } else if(biome instanceof JungleHills){
            return new Color(0, 112, 52);
        } else if(biome instanceof Plains){
            return new Color(92, 179, 156);
        } else if(biome instanceof FlowerForest){
            return new Color(146, 206, 123);
        } else if(biome instanceof SunflowerPlains){
            return new Color(215, 114, 130);
        } else if(biome instanceof BambooForest){
            return new Color(31, 182, 117);
        } else if(biome instanceof BambooForestHills){
            return new Color(1, 145, 82);
        } else if(biome instanceof OakForest){
            return new Color(20, 99, 63);
        } else if(biome instanceof OakForestHills){
            return new Color(15, 75, 48);
        } else if(biome instanceof BirchForest){
            return new Color(158, 194, 51);
        } else if(biome instanceof BirchForestHills){
            return new Color(117, 142, 37);
        } else if(biome instanceof PlainHills){
            return new Color(127, 177, 120);
        } else if(biome instanceof FrozenMountain){
            return new Color(207, 207, 207);
        } else if(biome instanceof Mountain){
            return new Color(117, 117, 117);
        } /*else if(biome instanceof MountainEdge){
            return new Color(73, 73, 73);
        }*/ else if(biome instanceof FrozenOcean){
            return new Color(9, 140, 255);
        } else if(biome instanceof Ocean){
            return new Color(36, 95, 203);
        } else if(biome instanceof WarmOcean){
            return new Color(53, 191, 191);
        } else if(biome instanceof DeepWarmOcean){
            return new Color(30, 109, 110);
        } else if(biome instanceof DeepFrozenOcean){
            return new Color(5, 72, 130);
        } else if(biome instanceof DeepOcean){
            return new Color(21, 25, 169);
        } /*else if(biome instanceof FrozenShallow){
            return new Color(93, 121, 169);
        } */else if(biome instanceof Shallow){
            return new Color(67, 86, 169);
        } else {
            return Color.BLACK;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double selectedHeight = 0;
        Point cursor = MouseInfo.getPointerInfo().getLocation();
        int posX = cursor.x - f.getLocationOnScreen().x;
        int posY = cursor.y - f.getLocationOnScreen().y - 32;
        int gx = globalX, gy = globalY;
        String biomeName = null;
        for(int x = 0; x < Math.min(g.getClipBounds().width, heightMap.length); x += zoom) {
            for(int y = 0; y < Math.min(g.getClipBounds().height, heightMap[x].length); y += zoom) {
                double height = heightMap[x][y];
                YumnBiome biome = biomeMap[x][y];
                boolean edge = biomeEdges[x][y];
                boolean sh = sharpness[x][y];
                float height_ = MathUtil.clampFloat((float) height, 0, 1);
                if(INVERT_COLOR) height_ = 1 - height_;
                int color = (int) (height_ * 255);
                if(Math.abs(x - posX) <= zoom && Math.abs(y - posY) <= zoom) {
                    selectedHeight = height;
                    gx = x + globalX;
                    gy = y + globalY;
                    g.setColor(Color.red);
                    biomeName = biome == null ? "?" : biome.getClass().getSimpleName();
                } else {
                    if(mode == DisplayMode.BIOMES || mode == DisplayMode.BIOME_BORDERS) {
                        if(edge && mode == DisplayMode.BIOME_BORDERS) {
                            g.setColor(sh ? new Color(205, 0, 215) : Color.YELLOW);
                        } else if(sh && mode == DisplayMode.BIOME_BORDERS) {
                            g.setColor(Color.RED);
                        } else {
                            g.setColor(getBiomeColor(biome, color));
                        }
                    } else {
                        g.setColor(new Color(height_, height_, height_));
                    }
                }
                g.fillRect(x, y, zoom, zoom);
            }
        }
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(18f));
        g.drawString(String.format("Mode %s | Zoom x%s" + (shiftPressing ? "| [SHIFT] " : ""), mode, zoom), 50, 35);
        g.drawString(String.format("Position: %s %s | Biome: %s", gx, gy, biomeName), 50, 60);
        g.drawString(String.format("Terrain noise: %.5f (y = %s)", selectedHeight, GEN.getHeight(selectedHeight)), 50, 110);
        g.drawString(String.format("Blocks: %s | Speed: %.2f ms/chunk", counter.get(), renderSpeed == null ? 0 : renderSpeed.get()), 50, 135);
        g.drawString(String.format("pcp (min=%.3f; avg=%.3f; max=%.3f)", BiomeManager.e, BiomeManager.a, BiomeManager.c), 50, 160);
        g.drawString(String.format("chance (min=%.3f; avg=%.3f; max=%.3f)", BiomeManager.f, BiomeManager.b, BiomeManager.d), 50, 185);
        repaint();
    }
}
