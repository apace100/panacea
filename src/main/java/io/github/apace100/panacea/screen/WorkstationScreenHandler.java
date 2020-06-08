package io.github.apace100.panacea.screen;

import io.github.apace100.panacea.misc.WorkstationRecipe;
import io.github.apace100.panacea.registry.ModBlocks;
import io.github.apace100.panacea.registry.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

import java.util.Optional;

public class WorkstationScreenHandler extends ForgingScreenHandler {

   public WorkstationScreenHandler(int syncId, PlayerInventory playerInventory) {
      this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
   }

   public WorkstationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
      super(null, syncId, playerInventory, context);
   }

   protected boolean canUse(BlockState state) {
      return state.isOf(ModBlocks.WORKSTATION);
   }

   protected boolean canTakeOutput(PlayerEntity player, boolean present) {
      return player.getEntityWorld().getRecipeManager().getFirstMatch(ModRecipes.Workstation.TYPE, (SimpleInventory)input, player.getEntityWorld()).isPresent();
   }

   protected ItemStack onTakeOutput(PlayerEntity player, ItemStack stack) {
      Optional<WorkstationRecipe> recipe = this.player.getEntityWorld().getRecipeManager().getFirstMatch(ModRecipes.Workstation.TYPE, (SimpleInventory)this.input, player.getEntityWorld());
      if(recipe.isPresent()) {
         recipe.get().craft((SimpleInventory)input);
         this.context.run((world, blockPos) -> {
            world.syncWorldEvent(1044, blockPos, 0);
         });
         updateResult();
         return stack;
      }
      return ItemStack.EMPTY;
   }

   public void updateResult() {
      Optional<WorkstationRecipe> recipe = this.player.getEntityWorld().getRecipeManager().getFirstMatch(ModRecipes.Workstation.TYPE, (SimpleInventory)this.input, player.getEntityWorld());
      if(recipe.isPresent()) {
         ItemStack out = recipe.get().getContextualOutput((SimpleInventory)input);
         output.setStack(0, out);
      } else {
         output.setStack(0, ItemStack.EMPTY);
      }
   }
}
