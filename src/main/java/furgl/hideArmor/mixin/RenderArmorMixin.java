package furgl.hideArmor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import furgl.hideArmor.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ArmorFeatureRenderer.class)
public abstract class RenderArmorMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

	@Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
	private void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, 
			EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
		// if rendering player's armor
		if (entity instanceof PlayerEntity) {
			boolean isClientPlayer = entity.getUuid().equals(MinecraftClient.getInstance().player.getUuid());
			// cancel if rendering client player's armor and in hideYourArmor
			// or if rendering other player's armor and in hideOtherPlayerArmor
			if ((isClientPlayer && Config.hideYourArmor.get(armorSlot).booleanValue()) ||
					(!isClientPlayer && Config.hideOtherPlayerArmor.get(armorSlot).booleanValue()))
				ci.cancel();
		}
	}

}