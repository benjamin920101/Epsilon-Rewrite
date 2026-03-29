package com.github.epsilon.mixins.client;

import com.github.epsilon.modules.impl.player.UseCooldown;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    private int rightClickDelay;

    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"))
    private void onDoItemUseHand(CallbackInfo ci, @Local ItemStack itemStack) {
        UseCooldown useCooldown = UseCooldown.INSTANCE;
        if (useCooldown.isEnabled()) {
            rightClickDelay = useCooldown.cooldown.getValue();
        }
    }

}
