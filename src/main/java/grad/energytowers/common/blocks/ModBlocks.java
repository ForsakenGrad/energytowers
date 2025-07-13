package grad.energytowers.common.blocks;

import grad.energytowers.Energytowers;
import grad.energytowers.common.blocks.machines.FoundryBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    // Ores
    public static final RegistryObject<Block> BAUXITE_ORE =
            Energytowers.BLOCKS.register("bauxite_ore", () ->
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)));

    public static final RegistryObject<Block> DEEPSLATE_BAUXITE_ORE =
            Energytowers.BLOCKS.register("deepslate_bauxite_ore", () ->
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE)));

    // Machines - NEW Multi-block Foundry
    public static final RegistryObject<Block> FOUNDRY =
            Energytowers.BLOCKS.register("foundry", () ->
                    new FoundryBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
                            .noOcclusion())); // Important for custom models

    // Block Items
    public static final RegistryObject<Item> BAUXITE_ORE_ITEM =
            Energytowers.ITEMS.register("bauxite_ore", () ->
                    new BlockItem(BAUXITE_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DEEPSLATE_BAUXITE_ORE_ITEM =
            Energytowers.ITEMS.register("deepslate_bauxite_ore", () ->
                    new BlockItem(DEEPSLATE_BAUXITE_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> FOUNDRY_ITEM =
            Energytowers.ITEMS.register("foundry", () ->
                    new BlockItem(FOUNDRY.get(), new Item.Properties()));

    // Keep test blocks for now
    public static final RegistryObject<Block> TEST_BLOCK =
            Energytowers.BLOCKS.register("test_block", () ->
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.0f)
                            .sound(SoundType.STONE)));

    public static final RegistryObject<Item> TEST_BLOCK_ITEM =
            Energytowers.ITEMS.register("test_block", () ->
                    new BlockItem(TEST_BLOCK.get(), new Item.Properties()));
}