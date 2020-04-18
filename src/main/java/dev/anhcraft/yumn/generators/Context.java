package dev.anhcraft.yumn.generators;

import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class Context<T extends YumnGenerator> {
    private final long seed;
    private final XoRoShiRo128StarStarRandomGenerator randomizer;
    private final T generator;
    private final World world;
    private final int chunkX;
    private final int chunkZ;
    private final ChunkGenerator.ChunkData chunkData;

    public Context(T generator, World world, int chunkX, int chunkZ, long seed, @NotNull XoRoShiRo128StarStarRandomGenerator randomizer, ChunkGenerator.ChunkData chunkData) {
        this.generator = generator;
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.seed = seed;
        this.randomizer = randomizer;
        this.chunkData = chunkData;
    }

    public T getGenerator() {
        return generator;
    }

    /**
     * @deprecated Except important usages, don't get the current world during the terrain generation.
     * @return the world to be generated.
     */
    @NotNull
    @Deprecated
    public World getWorld() {
        return world;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    /**
     * @deprecated When doing terrain generation, this method should not be called!
     *             As the order of the calls to this method may return different results.
     * @return the randomizer.
     */
    @Deprecated
    public XoRoShiRo128StarStarRandomGenerator getRandomizer() {
        return randomizer;
    }

    @NotNull
    public ChunkGenerator.ChunkData getChunkData() {
        return chunkData;
    }

    @NotNull
    public LayerBuilder newLayerBuilder(){
        return new LayerBuilder();
    }

    public long getSeed() {
        return seed;
    }

    public class LayerBuilder {
        private int x, z;
        private int sourceY, targetY;

        public LayerBuilder at(int x, int z){
            return at(x, 0, z);
        }

        public LayerBuilder at(int x, int y, int z){
            this.x = x;
            this.z = z;
            this.sourceY = y;
            this.targetY = y;
            return this;
        }

        public LayerBuilder to(int targetY){
            if(targetY >= sourceY) {
                this.targetY = targetY;
            }
            return this;
        }

        public LayerBuilder up(int targetDeltaY){
            if(targetY + targetDeltaY >= sourceY) {
                this.targetY += targetDeltaY;
            }
            return this;
        }

        public LayerBuilder is(Material material, double chance){
            if(randomizer.nextDouble() <= chance) is(material);
            return this;
        }

        public LayerBuilder is(Material material){
            for (int i = sourceY; i <= targetY; i++){
                chunkData.setBlock(x, i, z, material);
            }
            sourceY = targetY + 1;
            return this;
        }

        public LayerBuilder is(Material... material){
            for (int i = sourceY; i <= targetY; i++){
                chunkData.setBlock(x, i, z, material[randomizer.nextInt(material.length)]);
            }
            sourceY = targetY + 1;
            return this;
        }

        public LayerBuilder is(BlockData blockData, double chance){
            if(randomizer.nextDouble() <= chance) is(blockData);
            return this;
        }

        public LayerBuilder is(BlockData blockData){
            for (int i = sourceY; i <= targetY; i++){
                chunkData.setBlock(x, i, z, blockData);
            }
            sourceY = targetY + 1;
            return this;
        }

        public LayerBuilder is(BlockData... blockData){
            for (int i = sourceY; i <= targetY; i++){
                chunkData.setBlock(x, i, z, blockData[randomizer.nextInt(blockData.length)]);
            }
            sourceY = targetY + 1;
            return this;
        }
    }
}
