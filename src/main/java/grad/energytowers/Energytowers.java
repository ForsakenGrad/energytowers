package grad.energytowers;

import com.mojang.logging.LogUtils;
import grad.energytowers.common.blocks.ModBlocks;
import grad.energytowers.common.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Energytowers.MODID)
public class Energytowers {
    public static final String MODID = "energytowers";
    private static final Logger LOGGER = LogUtils.getLogger();

    // Deferred Registers
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    // Custom Creative Tab
    public static final RegistryObject<CreativeModeTab> ENERGY_TOWERS_TAB = CREATIVE_MODE_TABS.register("energy_towers_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.STEEL_INGOT.get()))
            .title(Component.translatable("creativetab.energytowers.energy_towers_tab"))
            .displayItems((parameters, output) -> {
                // Raw Materials
                output.accept(ModItems.RAW_BAUXITE.get());

                // Ingots
                output.accept(ModItems.STEEL_INGOT.get());
                output.accept(ModItems.ALUMINUM_INGOT.get());

                // Components
                output.accept(ModItems.CERAMIC_DISC.get());
                output.accept(ModItems.COPPER_WIRE.get());

                // Tools
                output.accept(ModItems.WRENCH.get());

                // Ores
                output.accept(ModBlocks.BAUXITE_ORE_ITEM.get());
                output.accept(ModBlocks.DEEPSLATE_BAUXITE_ORE_ITEM.get());

                // Machines
                output.accept(ModBlocks.FOUNDRY_ITEM.get());

                // Keep test items for now
                output.accept(ModItems.TEST_ITEM.get());
                output.accept(ModBlocks.TEST_BLOCK_ITEM.get());

                LOGGER.info("Added all items to custom creative tab");
            }).build());

    @SuppressWarnings("removal")
    public Energytowers() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        // Register the registers FIRST
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus); // Register creative tabs

        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        MENU_TYPES.register(modEventBus);

        // FORCE early loading AFTER registering the registers
        LOGGER.info("Force loading ModItems...");
        try {
            Class.forName("grad.energytowers.common.items.ModItems");
            LOGGER.info("ModItems loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to load ModItems: " + e.getMessage());
        }

        LOGGER.info("Force loading ModBlocks...");
        try {
            Class.forName("grad.energytowers.common.blocks.ModBlocks");
            Class.forName("grad.energytowers.common.blockentities.ModBlockEntities");
            Class.forName("grad.energytowers.common.recipes.ModRecipeTypes");
            Class.forName("grad.energytowers.common.recipes.ModRecipeSerializers");
            Class.forName("grad.energytowers.common.screens.ModMenuTypes");
            LOGGER.info("ModBlocks loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to load ModBlocks: " + e.getMessage());
        }

        LOGGER.info("Energy Towers mod loaded WITH custom creative tab");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Energy Towers mod setup complete");

        // Log what got registered
        LOGGER.info("Registered items:");
        LOGGER.info("- test_item: " + ModItems.TEST_ITEM.getId());
        LOGGER.info("- test_block item: " + ModBlocks.TEST_BLOCK_ITEM.getId());

        LOGGER.info("Registered blocks:");
        LOGGER.info("- test_block: " + ModBlocks.TEST_BLOCK.getId());

        LOGGER.info("Custom creative tab registered: " + ENERGY_TOWERS_TAB.getId());
    }
}