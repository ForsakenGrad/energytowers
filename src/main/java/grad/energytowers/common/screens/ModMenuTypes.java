package grad.energytowers.common.screens;

import grad.energytowers.Energytowers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Energytowers.MODID);

    public static final RegistryObject<MenuType<FoundryMenu>> FOUNDRY_MENU =
            MENUS.register("foundry_menu", () ->
                    IForgeMenuType.create(FoundryMenu::new));

    public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
