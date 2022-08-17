package net.fraZ0R.ingstorm.mixin;

import net.fraZ0R.ingstorm.Ingstorm;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	@Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void doNetherDamage(CallbackInfo ci) {
		if (world.getRegistryKey().getValue().toString().equals("minecraft:the_nether")) {
			applyDamage(DamageSource.OUT_OF_WORLD, 0.05f);
			Ingstorm.counter +=1;
			if(Ingstorm.counter == 50) {
				playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
				Ingstorm.counter = 0;
			}
		}
	}
}
