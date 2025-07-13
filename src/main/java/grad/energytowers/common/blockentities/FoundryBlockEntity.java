package grad.energytowers.common.blockentities;

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
import net.minecraft.world.item.ItemStack;
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

import java.util.List;

public class FoundryBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // Recipe selection system
    private FoundryRecipe selectedRecipe = null;
    private boolean showRecipeSelection = true;
    private int progress = 0;
    private int maxProgress = 72; // 3.6 seconds at 20 ticks/second

    public FoundryBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FOUNDRY.get(), pos, blockState);
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.energytowers.foundry");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FoundryMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putBoolean("showRecipeSelection", showRecipeSelection);
        if (selectedRecipe != null) {
            tag.putString("selectedRecipe", selectedRecipe.getId().toString());
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        showRecipeSelection = tag.getBoolean("showRecipeSelection");
        // TODO: Load selected recipe from ID
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    // Recipe selection methods
    public List<FoundryRecipe> getAvailableRecipes() {
        if (level == null) return List.of();
        return level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FOUNDRY_RECIPE.get());
    }

    public void selectRecipe(FoundryRecipe recipe) {
        this.selectedRecipe = recipe;
        this.showRecipeSelection = false;
        this.progress = 0;
        setChanged();
    }

    public void showRecipeSelection() {
        this.showRecipeSelection = true;
        this.selectedRecipe = null;
        this.progress = 0;
        setChanged();
    }

    public boolean isShowingRecipeSelection() {
        return showRecipeSelection;
    }

    public FoundryRecipe getSelectedRecipe() {
        return selectedRecipe;
    }

    // Fixed tick method signature for 1.20.1
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        if (selectedRecipe != null && !showRecipeSelection) {
            if (hasValidInputs()) {
                progress++;
                if (progress >= maxProgress) {
                    craftItem();
                    progress = 0;
                }
            } else {
                progress = 0;
            }
        }
        setChanged();
    }

    private boolean hasValidInputs() {
        if (selectedRecipe == null) return false;

        // Check if we have the required ingredients
        // This is a simplified check - you'll need to implement proper recipe matching
        return !itemHandler.getStackInSlot(0).isEmpty();
    }

    private void craftItem() {
        if (selectedRecipe == null) return;

        // Consume inputs and produce output
        // This is simplified - implement proper recipe logic
        ItemStack result = selectedRecipe.getResultItem(level.registryAccess());

        // Place result in output slot (slot 8 for example)
        itemHandler.setStackInSlot(8, result.copy());

        // Consume input (slot 0 for example)
        itemHandler.extractItem(0, 1, false);
    }

    // Getters for GUI
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }
}