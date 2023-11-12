package io.github.theepicblock.polymc.mixins.tag;

import io.github.theepicblock.polymc.impl.Util;
import net.minecraft.network.packet.s2c.common.SynchronizeTagsS2CPacket;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagPacketSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;
import java.util.Map;

@Mixin(SynchronizeTagsS2CPacket.class)
public class SynchronizeTagsMixin {
    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeMap(Ljava/util/Map;Lnet/minecraft/network/PacketByteBuf$PacketWriter;Lnet/minecraft/network/PacketByteBuf$PacketWriter;)V"))
    public Map<RegistryKey<? extends Registry<?>>, TagPacketSerializer.Serialized> editTagMap(Map<RegistryKey<? extends Registry<?>>, TagPacketSerializer.Serialized> in) {
        ServerPlayerEntity player = PacketContext.get().getPlayer();
        if (Util.isPolyMapVanillaLike(player)) {
            // Vanilla doesn't like it if it receives tags for registries that don't exist
            var newMap = new HashMap<RegistryKey<? extends Registry<?>>, TagPacketSerializer.Serialized>();
            in.forEach((key, tags) -> {
                if (Util.isVanilla(key.getValue())) {
                    newMap.put(key, tags);
                }
            });
            return newMap;
        } else {
            return in;
        }
    }
}
