package grad.energytowers.common.blocks;

import grad.energytowers.common.blocks.lattice.CubicLatticeBlock;
import grad.energytowers.common.enums.LatticeShape;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

import static grad.energytowers.Energytowers.BLOCKS;
import static grad.energytowers.Energytowers.ITEMS;

public class ModBlocks {
    // Ores
    public static final RegistryObject<Block> BAUXITE_ORE = BLOCKS.register("bauxite_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DEEPSLATE_BAUXITE_ORE = BLOCKS.register("deepslate_bauxite_ore",
            () -> new Block(BlockBehaviour.Properties.copy(BAUXITE_ORE.get())
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5f)));

    // Machines
    public static final RegistryObject<Block> FOUNDRY = BLOCKS.register("foundry",
            () -> new FoundryBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f)
                    .sound(SoundType.METAL)));

    public static final RegistryObject<Block> REFINERY = BLOCKS.register("refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f)
                    .sound(SoundType.METAL)));

    // Lattice Blocks
    public static final RegistryObject<Block> CUBIC_LATTICE_1X1 = BLOCKS.register("cubic_lattice_1x1",
            () -> new CubicLatticeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f)
                    .sound(SoundType.METAL), LatticeShape.CUBIC_1X1));

    // Add more lattice blocks here...

    // Anchor Point
    public static final RegistryObject<Block> ANCHOR_POINT_BLOCK = BLOCKS.register("anchor_point_block",
            () -> new AnchorPointBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0f)
                    .sound(SoundType.METAL)));

    // Register block items
    public static final RegistryObject<Item> BAUXITE_ORE_ITEM = ITEMS.register("bauxite_ore",
            () -> new BlockItem(BAUXITE_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> FOUNDRY_ITEM = ITEMS.register("foundry",
            () -> new BlockItem(FOUNDRY.get(), new Item.Properties()));

    // Add more block items here...
}
