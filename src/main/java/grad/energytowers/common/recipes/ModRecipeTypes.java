package grad.energytowers.common.recipes;

import grad.energytowers.Energytowers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final RegistryObject<RecipeType<FoundryRecipe>> FOUNDRY_RECIPE =
            Energytowers.RECIPE_TYPES.register("foundry", () -> RecipeType.simple(ResourceLocation.parse(Energytowers.MODID + ":foundry")));
}