package com.linnett.paint_and_inovate.common.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RoboCreeperEntity extends Creeper {

    public final AnimationState idleAnimation = new AnimationState();
    public final AnimationState walkAnimation = new AnimationState();
    public final AnimationState runAnimation = new AnimationState();
    public final AnimationState boomAnimation = new AnimationState();

    private boolean isRunning = false;

    public RoboCreeperEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new ChasePlayerGoal(this)); // Новый Goal для погони
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    private void setupAnimationStates() {
        if (this.isIgnited()) {
            if (!boomAnimation.isStarted()) {
                System.out.println("Boom animation started");
                boomAnimation.start(this.tickCount);
            }
        } else if (isRunning) {
            if (!runAnimation.isStarted()) {
                System.out.println("Run animation started");
                runAnimation.start(this.tickCount);
            }
            walkAnimation.stop();
            idleAnimation.stop();
            boomAnimation.stop();
        } else if (this.isMoving()) {
            if (!walkAnimation.isStarted()) {
                System.out.println("Walk animation started");
                walkAnimation.start(this.tickCount);
            }
            runAnimation.stop();
            idleAnimation.stop();
            boomAnimation.stop();
        } else {
            if (!idleAnimation.isStarted()) {
                System.out.println("Idle animation started");
                idleAnimation.start(this.tickCount);
            }
            walkAnimation.stop();
            runAnimation.stop();
            boomAnimation.stop();
        }
    }


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 0.001;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CREEPER_PRIMED;
    }

    // GOAL для погони за игроком
    static class ChasePlayerGoal extends Goal {
        private final RoboCreeperEntity creeper;
        private Player target;

        public ChasePlayerGoal(RoboCreeperEntity creeper) {
            this.creeper = creeper;
        }

        @Override
        public boolean canUse() {
            this.target = this.creeper.level().getNearestPlayer(this.creeper, 16.0);
            return this.target != null && !this.target.isCreative();
        }

        @Override
        public void start() {
            this.creeper.isRunning = true;
            this.creeper.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D); // Ускорение
            this.creeper.getNavigation().moveTo(this.target, 1.5D);
        }

        @Override
        public void stop() {
            this.creeper.isRunning = false;
            this.creeper.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D); // Обычная скорость
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target != null && this.creeper.distanceToSqr(this.target) < 9.0D) {
                this.creeper.ignite(); // Начинает взрыв
            } else {
                this.creeper.getNavigation().moveTo(this.target, 1.5D);
            }
        }
    }
}
