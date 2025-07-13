package grad.energytowers.common.blocks.machines;

import grad.energytowers.common.blockentities.FoundryBlockEntity;
import grad.energytowers.common.blockentities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class FoundryBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

    private static final VoxelShape SHAPE_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_TOP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FoundryBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(IS_TOP, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(IS_TOP) ? SHAPE_TOP : SHAPE_BOTTOM;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // Always interact with the bottom block
            BlockPos bottomPos = state.getValue(IS_TOP) ? pos.below() : pos;
            BlockEntity blockEntity = level.getBlockEntity(bottomPos);

            if (blockEntity instanceof FoundryBlockEntity foundryBE) {
                NetworkHooks.openScreen((ServerPlayer) player, foundryBE, bottomPos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // Check if we can place 2 blocks (bottom + top)
        if (pos.getY() < level.getMaxBuildHeight() - 1 &&
                level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(IS_TOP, false);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable Player player, net.minecraft.world.item.ItemStack stack) {
        if (!level.isClientSide()) {
            // Place the top block
            BlockPos topPos = pos.above();
            level.setBlock(topPos, this.defaultBlockState()
                    .setValue(FACING, state.getValue(FACING))
                    .setValue(IS_TOP, true), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // Drop items from block entity before removing
            if (!state.getValue(IS_TOP)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof FoundryBlockEntity foundryBE) {
                    foundryBE.drops();
                }
            }

            // Remove the other half when one half is broken
            if (state.getValue(IS_TOP)) {
                // This is the top block, remove the bottom
                BlockPos bottomPos = pos.below();
                if (level.getBlockState(bottomPos).getBlock() == this) {
                    level.removeBlock(bottomPos, false);
                }
            } else {
                // This is the bottom block, remove the top
                BlockPos topPos = pos.above();
                if (level.getBlockState(topPos).getBlock() == this) {
                    level.removeBlock(topPos, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    // REMOVED @Override - This method signature doesn't exist in 1.20.1
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_TOP);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Only the bottom block has a block entity
        return state.getValue(IS_TOP) ? null : new FoundryBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || state.getValue(IS_TOP)) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntities.FOUNDRY.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }
}