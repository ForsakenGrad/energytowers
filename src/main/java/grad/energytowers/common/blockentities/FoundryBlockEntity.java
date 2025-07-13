package grad.energytowers.common.blockentities;

import grad.energytowers.common.blocks.FoundryBlock;
import grad.energytowers.common.recipes.FoundryRecipe;
import grad.energytowers.common.recipes.ModRecipeTypes;
import grad.energytowers.common.screens.FoundryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FoundryBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true; // Input slots - accept any item for now
                case 2 -> false; // Output slot - no direct insertion
                case 3 -> isFuel(stack); // Fuel slot
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78; // Same as vanilla furnace
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public FoundryBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FOUNDRY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FoundryBlockEntity.this.progress;
                    case 1 -> FoundryBlockEntity.this.maxProgress;
                    case 2 -> FoundryBlockEntity.this.fuelTime;
                    case 3 -> FoundryBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FoundryBlockEntity.this.progress = value;
                    case 1 -> FoundryBlockEntity.this.maxProgress = value;
                    case 2 -> FoundryBlockEntity.this.fuelTime = value;
                    case 3 -> FoundryBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.energytowers.foundry");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FoundryMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("foundry.progress", progress);
        tag.putInt("foundry.fuelTime", fuelTime);
        tag.putInt("foundry.maxFuelTime", maxFuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("foundry.progress");
        fuelTime = tag.getInt("foundry.fuelTime");
        maxFuelTime = tag.getInt("foundry.maxFuelTime");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        if (isConsumingFuel()) {
            fuelTime--;
        }

        if (hasRecipe()) {
            if (hasFuelInFuelSlot() && !isConsumingFuel()) {
                consumeFuel();
            }
            if (isConsumingFuel()) {
                level.setBlock(pos, state.setValue(FoundryBlock.LIT, true), 3);
                progress++;
                setChanged(level, pos, state);
                if (progress > maxProgress) {
                    craftItem();
                }
            }
        } else {
            level.setBlock(pos, state.setValue(FoundryBlock.LIT, false), 3);
            resetProgress();
        }
    }

    private void consumeFuel() {
        ItemStack fuel = this.itemHandler.getStackInSlot(3);
        this.maxFuelTime = this.fuelTime = getFuelTime(fuel);
        fuel.shrink(1);
        this.itemHandler.setStackInSlot(3, fuel);
    }

    private int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            if (item == Items.COAL || item == Items.CHARCOAL) return 1600;
            if (item == Items.COAL_BLOCK) return 16000;
            if (item == Items.LAVA_BUCKET) return 20000;
            // Add more fuel types as needed
            return 0;
        }
    }

    private boolean isFuel(ItemStack stack) {
        return getFuelTime(stack) > 0;
    }

    private boolean isConsumingFuel() {
        return this.fuelTime > 0;
    }

    private boolean hasFuelInFuelSlot() {
        return !this.itemHandler.getStackInSlot(3).isEmpty();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<FoundryRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem(null);
            this.itemHandler.extractItem(0, 1, false);
            this.itemHandler.extractItem(1, 1, false);
            this.itemHandler.setStackInSlot(2, new ItemStack(result.getItem(),
                    this.itemHandler.getStackInSlot(2).getCount() + result.getCount()));
            resetProgress();
        }
    }

    private boolean hasRecipe() {
        Optional<FoundryRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<FoundryRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.FOUNDRY_RECIPE.get(), inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(2).isEmpty() || this.itemHandler.getStackInSlot(2).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(2).getCount() + count <= this.itemHandler.getStackInSlot(2).getMaxStackSize();
    }
}
