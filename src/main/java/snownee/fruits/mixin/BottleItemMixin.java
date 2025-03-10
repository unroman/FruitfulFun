package snownee.fruits.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.BottleItem;
import snownee.fruits.FFCommonConfig;
import snownee.fruits.ritual.RitualModule;

@Mixin(BottleItem.class)
public class BottleItemMixin {

	@WrapOperation(method = {"method_7726", "lambda$use$0", "m_289173_"}, constant = @Constant(classValue = EnderDragon.class))
	private static boolean allowDummyDragonBreath(
			Object object,
			Operation<Boolean> original,
			@Local(argsOnly = true) AreaEffectCloud cloud) {
		return RitualModule.DUMMY_UUID.equals(cloud.ownerUUID) || original.call(object);
	}

	@Inject(
			method = "method_7726",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;isAlive()Z"),
			cancellable = true)
	private static void fixDragonBreathExploit(AreaEffectCloud cloud, CallbackInfoReturnable<Boolean> ci) {
		if (FFCommonConfig.fixDragonBreathExploit && cloud.getRadius() <= 0) {
			// fix MC-114618
			ci.setReturnValue(false);
		}
	}
}
