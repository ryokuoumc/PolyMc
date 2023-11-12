package io.github.theepicblock.polymc.mixins;

import com.mojang.authlib.GameProfile;
import io.github.theepicblock.polymc.impl.Util;
import net.fabricmc.fabric.mixin.message.PlayerManagerMixin;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class DisableCustomParticles implements PlayerAssociatedNetworkHandler {

    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    public void sendPacketInject(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if (packet instanceof ParticleS2CPacket particlePacket && Util.isPolyMapVanillaLike(this.getPlayer())) {
            var effect = particlePacket.getParameters();
            if (!Util.isVanilla(Registries.PARTICLE_TYPE.getId(effect.getType()))) {
                ci.cancel();
            }
        }
    }
}
