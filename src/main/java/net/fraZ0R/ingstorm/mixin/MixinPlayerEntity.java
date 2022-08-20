package net.fraZ0R.ingstorm.mixin;

import net.fraZ0R.ingstorm.Ingstorm;
import net.fraZ0R.ingstorm.common.IngstormConfig;
import net.minecraft.block.Block;
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

	public float biomeDamage = IngstormConfig.biomeDamage;
	public float blockDamage = IngstormConfig.blockDamage;

	@Unique int counter = 0;
	/**
	 * {@link TagKey} for extremely hot biomes
	 *
	 * @author Oliver-makes-code
	 * */
	@Unique TagKey<Biome> CORROSIVE = TagKey.of(Registry.BIOME_KEY, new Identifier(Ingstorm.modid, "corrosive"));

	@Unique TagKey<Block> RANGE_0 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_0"));
	@Unique TagKey<Block> RANGE_2 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_2"));
	@Unique TagKey<Block> RANGE_4 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_4"));
	@Unique TagKey<Block> RANGE_8 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_8"));
	@Unique TagKey<Block> UNSAFE_RANGE_0 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "unsaferange_0"));
	@Unique TagKey<Block> UNSAFE_RANGE_2 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "unsaferange_2"));
	@Unique TagKey<Block> UNSAFE_RANGE_4 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "unsaferange_4"));
	@Unique TagKey<Block> UNSAFE_RANGE_8 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "unsaferange_8"));


	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private static final DamageSource CORROSION = new DamageSource("ingstorm.corrosion") {{
		setBypassesArmor();
		setUnblockable();
	}};


	private void doDamage(float amount) {
		counter += 1;
		counter %= 20;
		if(counter%10 == 0) {
			playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5F, 1F);
		}
		if(counter == 0) {
			float realDamage = Math.max((20* amount)-getAbsorptionAmount(), 0.0f);
			float oldAbs = getAbsorptionAmount();
			setAbsorptionAmount(Math.max(getAbsorptionAmount()-(20* amount), 0.0f));
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



	@Inject(method = "tick", at = @At("HEAD"))
	private void damageCheck(CallbackInfo ci) {
		//Why did I think this if statement was a good idea
		if (canTakeDamage() && world.getBiome(getBlockPos()).isIn(CORROSIVE) &&
				!( BlockProximity.isInRange(getBlockPos(), world, RANGE_0, 0) || BlockProximity.isInRange(getBlockPos(), world, RANGE_2, 2) || BlockProximity.isInRange(getBlockPos(), world, RANGE_4, 4) || BlockProximity.isInRange(getBlockPos(), world, RANGE_8, 8) ||
				BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, RANGE_0, 0) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, RANGE_2, 2) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, RANGE_4, 4) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, RANGE_8, 8))) {

			doDamage(biomeDamage);
		}

		//overly long monster of an if statement... 2!
		if (canTakeDamage() &&
				( (BlockProximity.isInRange(getBlockPos(), world, UNSAFE_RANGE_0, 0)) || (BlockProximity.isInRange(getBlockPos(), world, UNSAFE_RANGE_2, 2)) || BlockProximity.isInRange(getBlockPos(), world, UNSAFE_RANGE_4, 4) || BlockProximity.isInRange(getBlockPos(), world, UNSAFE_RANGE_8, 8) ||
						BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, UNSAFE_RANGE_0, 0) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, UNSAFE_RANGE_2, 2) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, UNSAFE_RANGE_4, 4) || BlockProximity.isInRange(getBlockPos().up((int)getStandingEyeHeight()), world, UNSAFE_RANGE_8, 8))) {

			doDamage(blockDamage);
		}
	}
}
