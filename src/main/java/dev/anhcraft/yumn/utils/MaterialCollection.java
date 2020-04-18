package dev.anhcraft.yumn.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

public enum MaterialCollection {
    ACACIA("ACACIA_*"),
    AIR(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR),
    ANDESITE("ANDESITE_*", Material.ANDESITE),
    ANVIL("*_ANVIL", Material.ANVIL),
    APPLE("*_APPLE", Material.APPLE),
    BALL(Material.CLAY_BALL, Material.SLIME_BALL, Material.SNOWBALL),
    BAMBOO("BAMBOO_*", Material.BAMBOO),
    BANNER("*_BANNER"),
    BANNER_PATTERN("*_BANNER_PATTERN"),
    BED("*_BED"),
    BIRCH("BIRCH_*"),
    BOAT("*_BOAT"),
    BONE("*_BONE", Material.BONE),
    BOOK("*_BOOK", Material.BOOK),
    BOOTS("*_BOOTS"),
    BOTTLE("_BOTTLE"),
    BRICK("BRICK_*", Material.BRICK, Material.BRICKS),
    BUCKET("*_BUCKET", Material.BUCKET),
    BUTTON("*_BUTTON"),
    CARPET("*_CARPET"),
    CHAINMAIL_ARMOR(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS),
    CHESTPLATE("*_CHESTPLATE"),
    CHISELED("CHISELED_*"),
    COBBLESTONE("COBBLESTONE_*", Material.COBBLESTONE),
    COMBAT("*_SWORD", Material.BOW, Material.CROSSBOW, Material.TRIDENT),
    CONCRETE("*_CONCRETE"),
    CONCRETE_POWDER("*_CONCRETE_POWDER"),
    COOKED_FOOD("COOKED_*", Material.BAKED_POTATO, Material.BREAD, Material.CAKE, Material.PUMPKIN_PIE),
    CORAL("^(?!DEAD).+_CORAL"),
    CORAL_BLOCK("^(?!DEAD).+_CORAL_BLOCK"),
    CORAL_FAN("^(?!DEAD).+_CORAL_FAN"),
    CORAL_WALL_FAN("^(?!DEAD).+_CORAL_WALL_FAN"),
    DARK_OAK("DARK_OAK_*"),
    DEAD_CORAL("DEAD_*_CORAL"),
    DEAD_CORAL_BLOCK("DEAD_*_CORAL_BLOCK"),
    DEAD_CORAL_FAN("DEAD_*_CORAL_FAN"),
    DEAD_CORAL_WALL_FAN("DEAD_*_CORAL_WALL_FAN"),
    DIAMOND("DIAMOND_*"),
    DIAMOND_ARMOR(Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS),
    DIAMOND_TOOLS(Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL),
    DIORITE("DIORITE_*", Material.DIORITE),
    DOOR("*_DOOR"),
    DYE("*_DYE"),
    EMERALD("EMERALD_*", Material.EMERALD),
    ENCHANTED("ENCHANTED_*"),
    END_STONE("END_STONE_*", Material.END_STONE),
    FENCE("*_FENCE"),
    FENCE_GATE("*_FENCE_GATE"),
    FLOWER("*_TULIP", Material.ALLIUM, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.CORNFLOWER, Material.DANDELION, Material.LILAC, Material.LILY_OF_THE_VALLEY, Material.OXEYE_DAISY, Material.PEONY, Material.POPPY, Material.ROSE_BUSH, Material.SUNFLOWER, Material.WITHER_ROSE),
    FURNACE(Material.BLAST_FURNACE, Material.FURNACE),
    GLAZED_TERRACOTTA("*_GLAZED_TERRACOTTA"),
    GOLDEN("GOLDEN_*"),
    GOLDEN_ARMOR(Material.GOLDEN_BOOTS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET, Material.GOLDEN_LEGGINGS),
    GOLDEN_TOOLS(Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL),
    GRANITE("GRANITE_*", Material.GRANITE),
    HEAD("*_HEAD"),
    HELMET("*_HELMET"),
    INFESTED("INFESTED_*"),
    IRON("IRON_*"),
    IRON_ARMOR(Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS),
    IRON_TOOLS(Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL),
    JUNGLE("JUNGLE_*"),
    LEATHER_ARMOR(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS),
    LEAVES("*_LEAVES"),
    LEGGINGS("*_LEGGINGS"),
    LOG("*_LOG"),
    MINECART("*_MINECART", Material.MINECART),
    MOSSY_COBBLESTONE("MOSSY_COBBLESTONE_*", Material.MOSSY_COBBLESTONE),
    MOSSY_STONE_BRICK("MOSSY_STONE_BRICK_*", Material.MOSSY_STONE_BRICKS),
    MUSIC_DISC("MUSIC_DISC_*"),
    NETHER_BRICK("NETHER_BRICK_*", Material.NETHER_BRICK, Material.NETHER_BRICKS),
    OAK("OAK_*"),
    ORE("*_ORE"),
    PLANKS("*_PLANKS"),
    POLISHED_ANDESITE("POLISHED_ANDESITE_*", Material.POLISHED_ANDESITE),
    POLISHED_DIORITE("POLISHED_DIORITE_*", Material.POLISHED_DIORITE),
    POLISHED_GRANITE("POLISHED_GRANITE_*", Material.POLISHED_GRANITE),
    POTATO("*_POTATO", Material.POTATO),
    POTION("*_POTION", Material.POTION),
    POTTED("POTTED_*"),
    PRESSURE_PLATE("*_PRESSURE_PLATE"),
    PUMPKIN(Material.CARVED_PUMPKIN, Material.PUMPKIN),
    RAIL("*_RAIL", Material.RAIL),
    RAW_FOOD("*_APPLE", "*_POTATO", Material.APPLE, Material.APPLE, Material.BEEF, Material.BEETROOT, Material.CARROT, Material.CHICKEN, Material.COD, Material.DRIED_KELP, Material.HONEY_BOTTLE, Material.MELON_SLICE, Material.MUTTON, Material.PORKCHOP, Material.POTATO, Material.PUFFERFISH, Material.RABBIT, Material.ROTTEN_FLESH, Material.SALMON, Material.SPIDER_EYE, Material.SWEET_BERRIES, Material.TROPICAL_FISH),
    REDSTONE("REDSTONE_*", Material.REDSTONE),
    SAPLING("*_SAPLING"),
    SEEDS("*_SEEDS"),
    SHULKER_BOX("*_SHULKER_BOX"),
    SIGN("*_SIGN"),
    SKULL("*_SKULL"),
    SLAB("*_SLAB"),
    SMOOTH("SMOOTH_*"),
    SOUP(Material.BEETROOT_SOUP),
    SPAWN_EGG("*_SPAWN_EGG"),
    SPRUCE("SPRUCE_*"),
    STAINED_GLASS("*_STAINED_GLASS"),
    STAINED_GLASS_PANE("*_STAINED_GLASS_PANE"),
    STAIRS("*_STAIRS"),
    STEW("*_STEW"),
    STONE("STONE_*", Material.STONE),
    STONE_BRICK("STONE_BRICK_*", Material.STONE_BRICKS),
    STONE_TOOLS(Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL),
    STORAGE("*_CHEST", Material.BARREL, Material.CHEST),
    STRIPPED("STRIPPED_*"),
    SWEET_BERRY(Material.SWEET_BERRIES, Material.SWEET_BERRY_BUSH),
    SWORD("*_SWORD"),
    TERRACOTTA("*_TERRACOTTA"),
    THROWABLE(Material.EGG, Material.FIRE_CHARGE, Material.SNOWBALL, Material.SPLASH_POTION, Material.TRIDENT),
    TRAPDOOR("*_TRAPDOOR"),
    TULIP("*_TULIP"),
    WALL("*_WALL"),
    WALL_BANNER("*_WALL_BANNER"),
    WALL_SIGN("*_WALL_SIGN"),
    WITHER("WITHER_*"),
    WOOD("*_WOOD"),
    WOODEN("WOODEN_*"),
    WOODEN_TOOLS(Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL),
    WOOL("*_WOOL");

    private static Set<Material> join(MaterialCollection... collections){
        return Stream.of(collections).flatMap(MaterialCollection::stream).collect(ImmutableSet.toImmutableSet());
    }

    public static final Set<Material> ARMOR = join(LEATHER_ARMOR, CHAINMAIL_ARMOR, IRON_ARMOR, GOLDEN_ARMOR, DIAMOND_ARMOR);
    public static final Set<Material> FOOD = join(RAW_FOOD, COOKED_FOOD);
    public static final Set<Material> MOSSY = join(MOSSY_COBBLESTONE, MOSSY_STONE_BRICK);
    public static final Set<Material> TOOLS = join(WOODEN_TOOLS, STONE_TOOLS, IRON_TOOLS, GOLDEN_TOOLS, DIAMOND_TOOLS);

    private final Set<Material> materialSet = EnumSet.noneOf(Material.class);
    private final List<Material> materialList = new ArrayList<>();
    //public List<String> stringObjects = new ArrayList<>();

    MaterialCollection(Object... objects) {
        for (Object obj : objects) {
            if(obj instanceof Material) {
                //stringObjects.add("Material."+((Material) obj).name());
                materialSet.add((Material) obj);
            } else {
                //stringObjects.add("\""+obj.toString()+"\"");
                Pattern pattern = Pattern.compile(obj.toString().replace("*", "(.+)"));
                for (Material mt : Material.values()){
                    if(mt.isLegacy()) continue;
                    if(pattern.matcher(mt.name()).matches()){
                        materialSet.add(mt);
                    }
                }
            }
        }
        materialList.addAll(materialSet);
    }

    @NotNull
    public Stream<Material> stream(){
        return materialSet.stream();
    }

    @NotNull
    public <R, A> R collect(Collector<Material, A, R> collector){
        return materialSet.stream().collect(collector);
    }

    public boolean contains(@NotNull Material material){
        return materialSet.contains(material);
    }

    @NotNull
    public Material randomPick(@NotNull XoRoShiRo128StarStarRandomGenerator randomizer){
        return RandomUtil.rand(randomizer, materialList);
    }

    @NotNull
    public List<Material> asList(){
        return ImmutableList.copyOf(materialList);
    }

    @NotNull
    public Set<Material> asSet(){
        return ImmutableSet.copyOf(materialSet);
    }

    public void forEach(Consumer<Material> consumer) {
        materialList.forEach(consumer);
    }
}
