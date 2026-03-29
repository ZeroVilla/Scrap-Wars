package net.zerostudios.scrapwars.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.item.ItemStack;
import net.zerostudios.scrapwars.common.blockentity.ImprovisedWorkbenchBlockEntity;
import net.zerostudios.scrapwars.common.recipe.improvised.ImprovisedWorkbenchRecipe;
import net.zerostudios.scrapwars.common.workbench.WorkbenchRecipeHelper;
import net.zerostudios.scrapwars.setup.ModRecipeTypes;

import java.util.Optional;
import java.util.function.Supplier;

public class CraftSelectedRecipeC2SPacket {
    private final BlockPos blockPos;
    private final ResourceLocation recipeId;

    public CraftSelectedRecipeC2SPacket(BlockPos blockPos, ResourceLocation recipeId) {
        this.blockPos = blockPos;
        this.recipeId = recipeId;
    }

    public static void encode(CraftSelectedRecipeC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeResourceLocation(msg.recipeId);
    }

    public static CraftSelectedRecipeC2SPacket decode(FriendlyByteBuf buf) {
        return new CraftSelectedRecipeC2SPacket(buf.readBlockPos(), buf.readResourceLocation());
    }

    public static void handle(CraftSelectedRecipeC2SPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) {
                return;
            }

            if (!(player.level().getBlockEntity(msg.blockPos) instanceof ImprovisedWorkbenchBlockEntity)) {
                return;
            }

            Optional<ImprovisedWorkbenchRecipe> recipeOptional = Optional.empty();

            for (ImprovisedWorkbenchRecipe recipe : player.level()
                    .getRecipeManager()
                    .getAllRecipesFor(ModRecipeTypes.IMPROVISED_WORKBENCH_RECIPE.get())) {
                if (recipe.getId().equals(msg.recipeId)) {
                    recipeOptional = Optional.of(recipe);
                    break;
                }
            }

            if (recipeOptional.isEmpty()) {
                return;
            }

            ImprovisedWorkbenchRecipe recipe = recipeOptional.get();

            if (!WorkbenchRecipeHelper.hasMaterials(player, recipe)) {
                return;
            }

            WorkbenchRecipeHelper.consumeMaterials(player, recipe);

            ItemStack result = recipe.getResultItem(player.level().registryAccess()).copy();
            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }

            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.ANVIL_USE,
                    SoundSource.BLOCKS,
                    0.4F,
                    1.2F
            );
        });
        ctx.setPacketHandled(true);
    }
}