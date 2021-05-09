package dev.anhcraft.yumn;

import dev.anhcraft.yumn.nms.StructureManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public enum Structure {
    OAK_TREE_1(-4, -3, -4, 2),
    OAK_TREE_2(-4, -3, -4, 2),
    OAK_TREE_3(-3, -3, -4, 2),
    BIRCH_TREE_1(-6, -3, -6, 3),
    SWAMP_OAK_TREE_1(-3, 0, -3, 3),
    JUNGLE_TREE_1(-2, -3, -2, 5),
    FALLEN_OAK_LOG_1(0, 0, 0, 3),
    FALLEN_OAK_LOG_2(0, 0, 0, 3),
    FALLEN_BIRCH_LOG_1(0, 0, 0, 3),
    FALLEN_BIRCH_LOG_2(0, 0, 0, 3),
    LARGE_ROCK_1(0, -3, 0, 3),
    LARGE_ROCK_2(0, -3, 0, 3),
    ABANDONED_CAMPFIRE(0, 0, 0, 1);

    private final String id;
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;
    private final int radiusCheck;

    Structure(int offsetX, int offsetY, int offsetZ, int radiusCheck) {
        this.id = name().toLowerCase();
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.radiusCheck = radiusCheck;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean spawn(@NotNull Location location, boolean fast) {
        int vertDir = offsetY < 0 ? -1 : 1;
        Location center = location.clone().add(offsetX, 0, offsetZ);
        int y = Math.abs(offsetY);
        outer:
        for (int k = 0; k <= y; k++) {
            for (int i = -radiusCheck; i <= radiusCheck; i++) {
                for (int j = -radiusCheck; j <= radiusCheck; j++) {
                    center.add(i, vertDir * k, j);
                    Block b = center.getBlock();
                    if (!b.isEmpty() && !b.isLiquid() && !b.isPassable()) {
                        center.add(0, k * vertDir, 0);
                        continue outer;
                    }
                    center.subtract(i, vertDir * k, j);
                }
            }
            StructureManager.pasteStructure(center.add(0, (y - k) * vertDir, 0), id, fast);
            return true;
        }
        return false;
    }
}
