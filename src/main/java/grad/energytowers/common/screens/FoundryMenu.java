package grad.energytowers.common.screens;

import grad.energytowers.common.blockentities.FoundryBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FoundryMenu extends AbstractContainerMenu {
    public final FoundryBlockEntity blockEntity;
    private final Level level;

    public FoundryMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public FoundryMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.FOUNDRY_MENU.get(), containerId);
        checkContainerSize(inv, 9);
        blockEntity = ((FoundryBlockEntity) entity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            // Input slot
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 11));
            // Output slot
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 80, 59));
        });
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // Implement shift-click logic
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    // Getters for GUI
    public FoundryBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isShowingRecipeSelection() {
        return blockEntity.isShowingRecipeSelection();
    }
}