package dev.anhcraft.yumn.heightmaps;

import dev.anhcraft.yumn.utils.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HeightMapManager {
    private final static Logger LOGGER = new Logger("HeightMapManager");
    private final Map<UUID, WorldHeightMap> data = new ConcurrentHashMap<>();

    public WorldHeightMap request(UUID worldId){
        return data.compute(worldId, (uuid, m) -> {
            if(m == null) {
                LOGGER.log("Heightmap for #%s not found! Creating new one...", worldId.toString());
                return new WorldHeightMap();
            } else {
                return m;
            }
        });
    }

    /**
     * @deprecated Internal uses only!
     */
    @Deprecated
    public void clean(){
        data.values().forEach(WorldHeightMap::clean);
    }
}
