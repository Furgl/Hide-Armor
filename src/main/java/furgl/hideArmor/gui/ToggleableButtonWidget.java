package furgl.hideArmor.gui;


import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import furgl.hideArmor.config.Config;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToggleableButtonWidget extends TexturedButtonWidget {

	public static ArrayList<ToggleableButtonWidget> hideYourArmorButtons;
	public static ArrayList<ToggleableButtonWidget> hideOtherPlayersArmorButtons;

	private boolean toggled;
	private Identifier texture;
	private int u;
	private int v;
	private int toggledVOffset;
	private final int textureWidth;
	private final int textureHeight;
	private Tooltip toggledTooltip;
	private Tooltip untoggledTooltip;

	public ToggleableButtonWidget(int x, int y, int width, int height, int u, int v, int toggledVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, Tooltip toggledTooltip, Tooltip untoggledTooltip, Text text) {
		super(x, y, width, height, u, v, toggledVOffset, texture, textureWidth, textureHeight, pressAction, text);
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.toggledVOffset = toggledVOffset;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.toggledTooltip = toggledTooltip;
		this.untoggledTooltip = untoggledTooltip;
		this.setTooltip(untoggledTooltip);
	}

	public void toggle() {
		this.toggled = !this.toggled;
		this.setTooltip(this.toggled ? toggledTooltip : untoggledTooltip);
	}

	public boolean isToggled() {
		return this.toggled;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, this.texture);
		int i = this.v;
		if (this.isToggled()) 
			i += this.toggledVOffset;

		RenderSystem.enableDepthTest();
		// background
		drawTexture(matrices, this.getX()-1, this.getY()-1, 108f, this.isHovered() ? 20 : 0, this.width+2, this.height+2, this.textureWidth, this.textureHeight);
		// icon
		drawTexture(matrices, this.getX(), this.getY(), (float)this.u, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
	}

	public static void toggleExpandedGui() {
		Config.expandedGui = !Config.expandedGui;
		Config.writeToFile();
		// hide/show buttons
		if (Config.expandedGui) {
			for (ToggleableButtonWidget button2 : ToggleableButtonWidget.hideYourArmorButtons)
				button2.visible = true;
			for (ToggleableButtonWidget button2 : ToggleableButtonWidget.hideOtherPlayersArmorButtons)
				button2.visible = true;
		}
		else {
			for (ToggleableButtonWidget button2 : ToggleableButtonWidget.hideYourArmorButtons)
				button2.visible = false;
			for (ToggleableButtonWidget button2 : ToggleableButtonWidget.hideOtherPlayersArmorButtons)
				button2.visible = false;
		}
	}

}