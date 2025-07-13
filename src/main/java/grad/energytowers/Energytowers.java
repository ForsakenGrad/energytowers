package grad.energytowers;

import com.mojang.logging.LogUtils;
import grad.energytowers.common.blockentities.ModBlockEntities;
import grad.energytowers.common.blocks.ModBlocks;
import grad.energytowers.common.items.ModItems;
import grad.energytowers.common.recipes.ModRecipeSerializers;
import grad.energytowers.common.recipes.ModRecipeTypes;
import grad.energytowers.common.screens.ModMenuTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
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

    // Creative Tab
    public static final RegistryObject<CreativeModeTab> ENERGY_TOWERS_TAB = CREATIVE_MODE_TABS.register("energy_towers_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(ModItems.STEEL_INGOT.get()))
            .title(Component.translatable("creativetab.energytowers.energy_towers_tab"))
            .displayItems((parameters, output) -> {
                // Add your items to the creative tab here
                output.accept(ModItems.RAW_BAUXITE.get());
                output.accept(ModItems.STEEL_INGOT.get());
                output.accept(ModItems.ALUMINUM_INGOT.get());
                output.accept(ModItems.CERAMIC_DISC.get());
                output.accept(ModItems.COPPER_WIRE.get());
                output.accept(ModItems.WRENCH.get());

                // Insulators
                output.accept(ModItems.SMALL_INSULATOR.get());
                output.accept(ModItems.MEDIUM_INSULATOR.get());
                output.accept(ModItems.LONG_INSULATOR.get());

                // Blocks
                output.accept(ModBlocks.BAUXITE_ORE.get());
                output.accept(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());
                output.accept(ModBlocks.FOUNDRY.get());
                // Add more items as you create them
            }).build());

    public Energytowers() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        // Register all our deferred registers
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);

        // Register menu types
        ModMenuTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        // Comment out or remove this line until we create the Config class
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Energy Towers mod loading...");
        // Comment out ore generation until we create that class
        // event.enqueueWork(() -> {
        //     ModOreGeneration.registerOreGeneration();
        // });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Add blocks to vanilla building blocks tab if desired
            event.accept(ModBlocks.FOUNDRY.get());
        }
    }
}