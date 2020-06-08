package io.github.apace100.panacea.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.panacea.Panacea;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WorkstationScreen extends ForgingScreen<WorkstationScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Panacea.MODID, "textures/gui/container/workstation.png");

    public WorkstationScreen(WorkstationScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
        this.field_25267 = 60;
        this.field_25268 = 18;
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
    }
}
