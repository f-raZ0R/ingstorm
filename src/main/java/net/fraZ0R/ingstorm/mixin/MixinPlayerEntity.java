package net.fraZ0R.ingstorm.mixin;

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
import net.minecraft.block.Blocks;
import net.fraZ0R.ingstorm.common.BlockProximity;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	@Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	public int counter = 0;
	@Inject(method = "tick", at = @At("HEAD"))
	private void doNetherDamage(CallbackInfo ci) {
		if (world.getRegistryKey().getValue().toString().equals("minecraft:the_nether") && !BlockProximity.isSafe(this.getBlockPos(), world, Blocks.NETHER_PORTAL, 2) && !BlockProximity.isSafe(this.getBlockPos(),world, Blocks.NETHER_QUARTZ_ORE, 2)) {
			applyDamage(DamageSource.OUT_OF_WORLD, 0.05f);
			counter +=1;
			if(counter == 10) {
				playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
				counter = 0;
			}
		}
	}
}
