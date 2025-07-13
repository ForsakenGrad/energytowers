package grad.energytowers.common.blockentities;

import grad.energytowers.Energytowers;
import grad.energytowers.common.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<FoundryBlockEntity>> FOUNDRY =
            Energytowers.BLOCK_ENTITIES.register("foundry", () ->
                    BlockEntityType.Builder.of(FoundryBlockEntity::new,
                            ModBlocks.FOUNDRY.get()).build(null));
}