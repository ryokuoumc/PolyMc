package io.github.theepicblock.polymc.mixins.wizards.block;

import io.github.theepicblock.polymc.impl.misc.WatchListener;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ChunkFilter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class WatchProviderMixin {
    @Shadow protected abstract ChunkHolder getChunkHolder(long pos);

    @Inject(method = "sendWatchPackets(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"))
    private void onSendUnloadPacket(ServerPlayerEntity player, CallbackInfo ci) {
        var chunkHolder = this.getChunkHolder(player.getChunkPos().toLong());
        if (chunkHolder == null) return;

        var chunk = chunkHolder.getWorldChunk();
        if (chunk == null) return;

        ((WatchListener)chunk).polymc$removePlayer(player);
    }
}
