package net.fraZ0R.ingstorm.mixin;

import net.fraZ0R.ingstorm.Ingstorm;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
	@Unique int counter = 0;
	@Unique TagKey<Biome> HOT = TagKey.of(Registry.BIOME_KEY, new Identifier(Ingstorm.modid, "hot"));

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}


	@Inject(method = "tick", at = @At("HEAD"))
	private void doNetherDamage(CallbackInfo ci) {
		if (world.getBiome(getBlockPos()).isIn(HOT) && !BlockProximity.isSafe(getBlockPos(), world)) {
			applyDamage(DamageSource.OUT_OF_WORLD, 0.05f);
			counter += 1;
			counter %= 10;
			if(counter == 0) {
				playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
			}
		}
	}
}
