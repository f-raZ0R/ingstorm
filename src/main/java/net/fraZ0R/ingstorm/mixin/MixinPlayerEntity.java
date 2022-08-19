package net.fraZ0R.ingstorm.mixin;

import net.fraZ0R.ingstorm.Ingstorm;
import net.fraZ0R.ingstorm.common.IngstormConfig;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
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
import net.fraZ0R.ingstorm.common.BlockProximity;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	@Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

	@Shadow public abstract double getHeightOffset();
	@Shadow public abstract void increaseStat(Identifier stat, int amount);

	@Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

	public float corDamage = IngstormConfig.biomeDamage;

	@Unique int counter = 0;
	/**
	 * {@link TagKey} for extremely hot biomes
	 *
	 * @author Oliver-makes-code
	 * */
	@Unique TagKey<Biome> CORROSIVE = TagKey.of(Registry.BIOME_KEY, new Identifier(Ingstorm.modid, "corrosive"));

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private static final DamageSource CORROSION = new DamageSource("ingstorm.corrosion") {{
		setBypassesArmor();
		setUnblockable();
	}};



	@Inject(method = "tick", at = @At("HEAD"))
	private void doNetherDamage(CallbackInfo ci) {
		//Damage source type is temporarily out_of_world, to ignore iFrames.
		if (canTakeDamage() && world.getBiome(getBlockPos()).isIn(CORROSIVE) && !(BlockProximity.isSafe(getBlockPos(), world) || BlockProximity.isSafe(getBlockPos().up((int)getStandingEyeHeight()), world))) {




			counter += 1;
			counter %= 20;
			if(counter%10 == 0) {
				playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
			}
			if(counter == 0) {
				//ok screw it im keeping damage tilt for now but im damaging the player less often now.
				float realDamage = Math.max((20*corDamage)-getAbsorptionAmount(), 0.0f);
				float oldAbs = getAbsorptionAmount();
				setAbsorptionAmount(Math.max(getAbsorptionAmount()-(20*corDamage), 0.0f));
				increaseStat(Stats.DAMAGE_ABSORBED, Math.round(oldAbs-getAbsorptionAmount()));
				if(realDamage >= getHealth())
				{
					damage(CORROSION, realDamage);
				}
				else
				{
					setHealth(getHealth() - realDamage);
					increaseStat(Stats.DAMAGE_TAKEN, Math.round(realDamage));
				}
			}
		}
	}
}
