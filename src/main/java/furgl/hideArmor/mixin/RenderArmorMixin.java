package furgl.hideArmor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import furgl.hideArmor.HideArmor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorFeatureRenderer.class)
public abstract class RenderArmorMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

	@Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
	private void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, 
			EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
		if (HideArmor.shouldHide(entity, armorSlot))
			ci.cancel();
	}

}