package grad.energytowers.common.items;

import grad.energytowers.common.enums.VoltageType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static grad.energytowers.Energytowers.ITEMS;

public class ModItems {
    // Raw Materials
    public static final RegistryObject<Item> RAW_BAUXITE = ITEMS.register("raw_bauxite",
            () -> new Item(new Item.Properties()));

    // Ingots
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot",
            () -> new Item(new Item.Properties()));

    // Components
    public static final RegistryObject<Item> CERAMIC_DISC = ITEMS.register("ceramic_disc",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire",
            () -> new CopperWireItem(new Item.Properties()));

    // Tools
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
            () -> new WrenchItem(new Item.Properties().stacksTo(1)));

    // Insulators
    public static final RegistryObject<Item> SMALL_INSULATOR = ITEMS.register("small_insulator",
            () -> new InsulatorItem(new Item.Properties(), VoltageType.LOW));

    public static final RegistryObject<Item> MEDIUM_INSULATOR = ITEMS.register("medium_insulator",
            () -> new InsulatorItem(new Item.Properties(), VoltageType.MEDIUM));

    public static final RegistryObject<Item> LONG_INSULATOR = ITEMS.register("long_insulator",
            () -> new InsulatorItem(new Item.Properties(), VoltageType.HIGH));

    // Anchor Points
    public static final RegistryObject<Item> ANCHOR_POINT = ITEMS.register("anchor_point",
            () -> new AnchorPointItem(new Item.Properties()));
}
