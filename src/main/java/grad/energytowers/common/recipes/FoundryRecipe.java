package grad.energytowers.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FoundryRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int cookingTime;

    public FoundryRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems, int cookingTime) {
        this.id = id;
        this.output = output;
        this.inputItems = inputItems;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        if (inputItems.size() > container.getContainerSize()) {
            return false;
        }

        for (int i = 0; i < inputItems.size(); i++) {
            if (!inputItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FOUNDRY_RECIPE.get();
    }

    // Getters for custom functionality
    public ItemStack getOutput() {
        return output.copy();
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public static class Serializer implements RecipeSerializer<FoundryRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("energytowers", "foundry");

        @Override
        public FoundryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);

            return new FoundryRecipe(recipeId, output, inputs, cookingTime);
        }

        @Override
        public @Nullable FoundryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            int cookingTime = buffer.readVarInt();

            return new FoundryRecipe(recipeId, output, inputs, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FoundryRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.getResultItem(null));
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}