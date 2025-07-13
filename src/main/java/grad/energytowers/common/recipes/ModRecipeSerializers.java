package grad.energytowers.common.recipes;

import grad.energytowers.Energytowers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final RegistryObject<RecipeSerializer<FoundryRecipe>> FOUNDRY_SERIALIZER =
            Energytowers.RECIPE_SERIALIZERS.register("foundry", () -> FoundryRecipe.Serializer.INSTANCE);
}