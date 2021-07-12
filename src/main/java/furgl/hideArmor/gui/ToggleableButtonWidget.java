package furgl.hideArmor.gui;


import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToggleableButtonWidget extends TexturedButtonWidget {

	private boolean toggled;
	private Identifier texture;
	private int u;
	private int v;
	private int toggledVOffset;
	private final int textureWidth;
	private final int textureHeight;

	public ToggleableButtonWidget(int x, int y, int width, int height, int u, int v, int toggledVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, ButtonWidget.TooltipSupplier tooltipSupplier, Text text) {
		super(x, y, width, height, u, v, toggledVOffset, texture, textureWidth, textureHeight, pressAction, tooltipSupplier, text);
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.toggledVOffset = toggledVOffset;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	public void toggle() {
		this.toggled = !this.toggled;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, this.texture);
		int i = this.v;
		if (this.isToggled()) 
			i += this.toggledVOffset;

		RenderSystem.enableDepthTest();
		// background
		drawTexture(matrices, this.x-1, this.y-1, 90f, this.isHovered() ? 20 : 0, this.width+2, this.height+2, this.textureWidth, this.textureHeight);
		// icon
		drawTexture(matrices, this.x, this.y, (float)this.u, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
		if (this.isHovered()) 
			this.renderTooltip(matrices, mouseX, mouseY);
	}

}
