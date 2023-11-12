/*
 * PolyMc
 * Copyright (C) 2020-2020 TheEpicBlock_TEB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package io.github.theepicblock.polymc.mixins;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.impl.Util;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class CustomPacketDisabler implements PlayerAssociatedNetworkHandler {


    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void sendPacketInject(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CustomPayloadS2CPacket && Util.isPolyMapVanillaLike(this.getPlayer())) {
            Identifier channel = ((CustomPayloadS2CPacket) packet).payload().id();
            PolyMc.LOGGER.info("C: " + channel + ", P: " + this.getPlayer().getName().getString());
            if (!Util.isVanilla(channel)) {
                ci.cancel();
            }
        }
    }
}
