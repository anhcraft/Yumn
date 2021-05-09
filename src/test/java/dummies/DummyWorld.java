package dummies;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class DummyWorld implements World {
    private static final UUID id = UUID.randomUUID();
    private static final long SEED = 1511624184305249414L;

    @Override
    public @NotNull Block getBlockAt(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Block getBlockAt(@NotNull Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location) {
        return 0;
    }

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return 0;
    }

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
        return null;
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return null;
    }

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Block block) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isChunkLoaded(@NotNull Chunk chunk) {
        return false;
    }

    @NotNull
    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(@NotNull Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return false;
    }

    @Override
    public void loadChunk(int x, int z) {

    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        return false;
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {

    }

    @Override
    public @NotNull Collection<Chunk> getForceLoadedChunks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        return false;
    }

    @Override
    public void removePluginChunkTickets(@NotNull Plugin plugin) {

    }

    @Override
    public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
        return false;
    }

    @Override
    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {
        return false;
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull LightningStrike strikeLightning(@NotNull Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull LightningStrike strikeLightningEffect(@NotNull Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Entity> getEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Player> getPlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, @Nullable Predicate<Entity> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<Entity> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<Entity> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull UUID getUID() {
        return id;
    }

    @Override
    public @NotNull Location getSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull boolean setSpawnLocation(@NotNull Location location) {
        return false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return false;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public void setTime(long time) {

    }

    @Override
    public long getFullTime() {
        return 0;
    }

    @Override
    public void setFullTime(long time) {

    }

    @Override
    public boolean hasStorm() {
        return false;
    }

    @Override
    public void setStorm(boolean hasStorm) {

    }

    @Override
    public int getWeatherDuration() {
        return 0;
    }

    @Override
    public void setWeatherDuration(int duration) {

    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public void setThundering(boolean thundering) {

    }

    @Override
    public int getThunderDuration() {
        return 0;
    }

    @Override
    public void setThunderDuration(int duration) {

    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
        return false;
    }

    @Override
    public @NotNull Environment getEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSeed() {
        return SEED;
    }

    @Override
    public boolean getPVP() {
        return false;
    }

    @Override
    public void setPVP(boolean pvp) {

    }

    @Override
    public @Nullable ChunkGenerator getGenerator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save() {

    }

    @Override
    public @NotNull List<BlockPopulator> getPopulators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {

    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {

    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {

    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {

    }

    @Override
    public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {

    }

    @Override
    public boolean getAllowAnimals() {
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        return false;
    }

    @Override
    public @NotNull Biome getBiome(int x, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {

    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {

    }

    @Override
    public double getTemperature(int x, int z) {
        return 0;
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        return 0;
    }

    @Override
    public double getHumidity(int x, int z) {
        return 0;
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {

    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void setAutoSave(boolean value) {

    }

    @Override
    public void setDifficulty(@NotNull Difficulty difficulty) {

    }

    @Override
    public @NotNull Difficulty getDifficulty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull File getWorldFolder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable WorldType getWorldType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canGenerateStructures() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public void setHardcore(boolean hardcore) {

    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {

    }

    @Override
    public long getTicksPerWaterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerWaterSpawns(int i) {

    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int i) {

    }

    @Override
    public long getTicksPerAmbientSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAmbientSpawns(int i) {

    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {

    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAmbientSpawnLimit(int i) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {

    }

    @NotNull
    @Override
    public String[] getGameRules() {
        return new String[0];
    }

    @Override
    public @Nullable String getGameRuleValue(@Nullable String rule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
        return false;
    }

    @Override
    public boolean isGameRule(@NotNull String rule) {
        return false;
    }

    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
        return false;
    }

    @Override
    public @NotNull WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

    }

    @Override
    public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getViewDistance() {
        return 0;
    }

    @Override
    public @NotNull Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Raid locateNearestRaid(@NotNull Location location, int radius) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Raid> getRaids() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable DragonBattle getEnderDragonBattle() {
        return null;
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {

    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {

    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {

    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw new UnsupportedOperationException();
    }
}
