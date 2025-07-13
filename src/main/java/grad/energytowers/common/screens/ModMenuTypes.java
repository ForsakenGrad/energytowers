package grad.energytowers.common.screens;

import grad.energytowers.Energytowers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final RegistryObject<MenuType<FoundryMenu>> FOUNDRY_MENU =
            Energytowers.MENU_TYPES.register("foundry_menu", () ->
                    IForgeMenuType.create(FoundryMenu::new));
}