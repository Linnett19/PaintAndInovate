package com.linnett.paint_and_inovate.common.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RoboCreeperEntity extends Animal {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();
    public final AnimationState boomAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private boolean isExploding = false;
    private int explosionCountdown = 0;

    private static final float EXPLOSION_RADIUS = 3.0F;
    private static final int EXPLOSION_DURATION = 60; // 3 секунды (60 тиков)

    public RoboCreeperEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5, false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D) // Быстрее обычного крипера
                .add(Attributes.FOLLOW_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D); // Не атакует, только взрывается
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving() && !this.isExploding) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isExploding) {
            if (!this.boomAnimationState.isStarted()) {
                this.boomAnimationState.start(this.tickCount);
            }
        } else {
            this.boomAnimationState.stop();
        }

        if (!this.isExploding) {
            if (this.isAggressive() && this.getTarget() != null) {
                this.runAnimationState.start(this.tickCount);
                this.walkAnimationState.stop();
            } else if (this.isMoving()) {
                this.walkAnimationState.start(this.tickCount);
                this.runAnimationState.stop();
            }
        }
    }


    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 0.001;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.3f);
    }

    @Override
    public void tick() {
        super.tick();

        // Логика взрыва
        if (this.isExploding) {
            if (this.explosionCountdown <= 0) {
                this.explode();
            } else {
                --this.explosionCountdown;
                // Бешеный бег на игрока
                LivingEntity target = this.getTarget();
                if (target != null) {
                    this.getNavigation().moveTo(target, 1.5);
                }
            }
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void customServerAiStep() {
        // В креативном режиме игнорирует игрока
        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            Player nearestPlayer = this.level().getNearestPlayer(this, 16.0);
            if (nearestPlayer != null && !nearestPlayer.isCreative()) {
                this.setTarget(nearestPlayer);
            }
        }
        super.customServerAiStep();
    }

    private void startExplosion() {
        if (!this.isExploding) {
            this.isExploding = true;
            this.explosionCountdown = EXPLOSION_DURATION;
            this.boomAnimationState.start(this.tickCount);
        }
    }

    private void explode() {
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CREEPER_PRIMED;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_DAMAGE;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    public void die(DamageSource pSource) {
        super.die(pSource);

        if (!this.level().isClientSide()) {
            int numPrismarine = this.random.nextInt(3) + 1;
            int numRedstone = this.random.nextInt(2) + 1;

            for (int i = 0; i < numPrismarine; i++) {
                ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(Items.PRISMARINE_CRYSTALS));
                this.level().addFreshEntity(item);
            }

            for (int i = 0; i < numRedstone; i++) {
                ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(Items.REDSTONE));
                this.level().addFreshEntity(item);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean damage = super.hurt(pSource, pAmount);
        if (damage && pSource.getEntity() instanceof Player) {
            Player attacker = (Player) pSource.getEntity();
            this.setTarget(attacker);

            // Начинает взрыв при низком здоровье
            if (this.getHealth() < 5.0F) {
                this.startExplosion();
            }
        }
        return damage;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Взрывается, если близко к игроку
        LivingEntity target = this.getTarget();
        if (target != null && this.distanceToSqr(target) < 9.0) {
            this.startExplosion();
        }
    }
}