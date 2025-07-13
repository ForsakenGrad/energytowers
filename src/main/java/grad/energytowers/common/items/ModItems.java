package grad.energytowers.common.items;

import grad.energytowers.Energytowers;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Raw Materials
    public static final RegistryObject<Item> RAW_BAUXITE =
            Energytowers.ITEMS.register("raw_bauxite", () -> new Item(new Item.Properties()));

    // Ingots
    public static final RegistryObject<Item> STEEL_INGOT =
            Energytowers.ITEMS.register("steel_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALUMINUM_INGOT =
            Energytowers.ITEMS.register("aluminum_ingot", () -> new Item(new Item.Properties()));

    // Components
    public static final RegistryObject<Item> CERAMIC_DISC =
            Energytowers.ITEMS.register("ceramic_disc", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_WIRE =
            Energytowers.ITEMS.register("copper_wire", () -> new Item(new Item.Properties()));

    // Tools
    public static final RegistryObject<Item> WRENCH =
            Energytowers.ITEMS.register("wrench", () -> new Item(new Item.Properties().stacksTo(1)));

    // Keep the test item for now - we'll remove it later
    public static final RegistryObject<Item> TEST_ITEM =
            Energytowers.ITEMS.register("test_item", () -> new Item(new Item.Properties()));
}