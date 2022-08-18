package net.fraZ0R.ingstorm.mixin;

import net.fraZ0R.ingstorm.Ingstorm;
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
//import net.minecraft.stat.Stat;
import net.fraZ0R.ingstorm.common.BlockProximity;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	@Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

	@Shadow public abstract double getHeightOffset();
	@Shadow public abstract void increaseStat(Identifier stat, int amount);

	@Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

	//todo: make this variable be taken from a config file instead of hardcoded to 0.05
	public float corDamage = 0.05f;

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


	@Inject(method = "tick", at = @At("HEAD"))
	private void doNetherDamage(CallbackInfo ci) {
		//Damage source type is temporarily starvation.
		if (canTakeDamage() && world.getBiome(getBlockPos()).isIn(CORROSIVE) && !(BlockProximity.isSafe(getBlockPos(), world) || BlockProximity.isSafe(getBlockPos().up((int)getStandingEyeHeight()), world))) {
			//semi-hacky way to avoid damage tilt by not even calling the damage function which makes the tilt in the first place.
			float realDamage = Math.max(corDamage-getAbsorptionAmount(), 0.0f);
			float oldAbs = getAbsorptionAmount();
			setAbsorptionAmount(Math.max(getAbsorptionAmount()-corDamage, 0.0f));
			increaseStat(Stats.DAMAGE_ABSORBED, Math.round((oldAbs-getAbsorptionAmount())*10));
			if(realDamage >= getHealth())
			{
				damage(DamageSource.STARVE, realDamage);
			}
			else
			{
				setHealth(getHealth() - realDamage);
				increaseStat(Stats.DAMAGE_TAKEN, Math.round(realDamage*10));
			}



			counter += 1;
			counter %= 10;
			if(counter == 0) {
				playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
			}
		}
	}
}
