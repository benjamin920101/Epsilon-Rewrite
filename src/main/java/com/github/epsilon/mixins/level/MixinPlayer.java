package com.github.epsilon.mixins.level;

import com.github.epsilon.events.TravelEvent;
import com.github.epsilon.modules.impl.player.AutoSprint;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
    private void hookTravel(Vec3 input, CallbackInfo ci) {
        TravelEvent travelEvent = NeoForge.EVENT_BUS.post(new TravelEvent(input));
        if (travelEvent.isCanceled()) {
            move(MoverType.SELF, getDeltaMovement());
            ci.cancel();
        }
    }

    @Inject(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V", shift = At.Shift.AFTER))
    private void hookCauseExtraKnockback(CallbackInfo callbackInfo) {
        if (AutoSprint.INSTANCE.isEnabled() && AutoSprint.INSTANCE.keepSprint.getValue()) {
            Minecraft mc = Minecraft.getInstance();
            float multiplier = 0.6f + 0.4f * AutoSprint.INSTANCE.motion.getValue().floatValue();
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x / 0.6 * multiplier, mc.player.getDeltaMovement().y, mc.player.getDeltaMovement().z / 0.6 * multiplier);
            mc.player.setSprinting(true);
        }
    }

}
