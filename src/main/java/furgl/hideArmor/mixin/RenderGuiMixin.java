package furgl.hideArmor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import furgl.hideArmor.HideArmor;
import furgl.hideArmor.config.Config;
import furgl.hideArmor.gui.ToggleableButtonWidget;
import furgl.hideArmor.utils.Utils;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Mixin(InventoryScreen.class)
public abstract class RenderGuiMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {

	@Shadow
	RecipeBookWidget recipeBook;

	@Unique
	Identifier BUTTONS = new Identifier(HideArmor.MODID, "textures/gui/buttons.png");
	@Unique
	Identifier BACKGROUND = new Identifier(HideArmor.MODID, "textures/gui/background.png");
	@Unique
	TexturedButtonWidget button;

	public RenderGuiMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		// buttons
		ToggleableButtonWidget.hideYourArmorButtons = Lists.newArrayList();
		ToggleableButtonWidget.hideOtherPlayersArmorButtons = Lists.newArrayList();
		for (int i=0; i<Utils.ARMOR_SLOTS.size(); ++i) {
			EquipmentSlot slot = Utils.ARMOR_SLOTS.get(i);
			// hide your armor
			ToggleableButtonWidget toggle = new ToggleableButtonWidget(0, 0, 18, 18, i*18, 0, 18, BUTTONS, 128, 128, (button) -> {
				((ToggleableButtonWidget) button).toggle();
				Config.hideYourArmor.put(slot, ((ToggleableButtonWidget) button).isToggled());
				Config.writeToFile();
			}, (button, matrices, mouseX, mouseY) -> {
				this.renderTooltip(matrices, 
						(((ToggleableButtonWidget) button).isToggled() ?
								Text.translatable("gui.hidearmor.hiding").formatted(Formatting.RED) :
									Text.translatable("gui.hidearmor.showing").formatted(Formatting.GREEN))
						.append(" ")
						.append(Text.translatable("gui.hidearmor.your", Utils.ARMOR_SLOT_INFO.get(slot).nameSingular).formatted(Formatting.WHITE)),
						mouseX, mouseY);
			}, Text.empty());
			ToggleableButtonWidget.hideYourArmorButtons.add(toggle);
			toggle.visible = Config.expandedGui;
			if (Config.hideYourArmor.get(slot).booleanValue())
				toggle.toggle();
			this.addDrawableChild(toggle);
			// hide other player's armor
			toggle = new ToggleableButtonWidget(0, 0, 18, 18, i*18, 36, 18, BUTTONS, 128, 128, (button) -> {
				((ToggleableButtonWidget) button).toggle();
				Config.hideOtherPlayerArmor.put(slot, ((ToggleableButtonWidget) button).isToggled());
				Config.writeToFile();
			}, (button, matrices, mouseX, mouseY) -> {
				this.renderTooltip(matrices, 
						(((ToggleableButtonWidget) button).isToggled() ?
								Text.translatable("gui.hidearmor.hiding").formatted(Formatting.RED) :
								Text.translatable("gui.hidearmor.showing").formatted(Formatting.GREEN))
						.append(" ")
						.append(Text.translatable("gui.hidearmor.others", Utils.ARMOR_SLOT_INFO.get(slot).namePlural).formatted(Formatting.WHITE)),
						mouseX, mouseY);
			}, Text.empty());
			ToggleableButtonWidget.hideOtherPlayersArmorButtons.add(toggle);
			toggle.visible = Config.expandedGui;
			if (Config.hideOtherPlayerArmor.get(slot).booleanValue())
				toggle.toggle();
			this.addDrawableChild(toggle);
		}
		// button to expand
		this.button = new TexturedButtonWidget(0, 0, 18, 18, 72, 0, 18, BUTTONS, 128, 128, (button) -> {
			ToggleableButtonWidget.toggleExpandedGui();
		});
		this.button.visible = Config.showGuiButton;
		this.addDrawableChild(this.button);
		// reset button positions
		this.resetButtonPositions();
	}

	@Inject(method = "drawForeground", at = @At("HEAD"))
	private void onDrawForeground(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
		if (Config.expandedGui) 
			// draw text
			drawCenteredText(matrices, textRenderer, Text.translatable("hidearmor.name").formatted(Formatting.WHITE), this.backgroundWidth+40, 12, 16777215);
	}

	@Inject(method = "drawBackground", at = @At("HEAD"))
	private void onDrawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if (Config.expandedGui) {
			// draw background
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.BACKGROUND);
			this.drawTexture(matrices, x+this.backgroundWidth+2, this.y, 0, 0, 75, 117);
		}
	}

	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable ci) {
		resetButtonPositions();
	}

	private void resetButtonPositions() {
		this.button.setPos(this.x + 143 + Config.guiButtonXOffset, this.height / 2 - 22 + Config.guiButtonYOffset);
		for (int i=0; i<ToggleableButtonWidget.hideYourArmorButtons.size(); ++i) {
			int x = this.x+this.backgroundWidth+17;
			int y = this.y+i*21+24;
			ToggleableButtonWidget.hideYourArmorButtons.get(i).setPos(x, y);
			ToggleableButtonWidget.hideOtherPlayersArmorButtons.get(i).setPos(x+25, y);
		}
	}
}