package io.github.theepicblock.polymc.mixins.block.implementations.forceintcontrol;

import io.github.theepicblock.polymc.impl.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.IndexedIterable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.nucleoid.packettweaker.PacketContext;

@Mixin(PacketByteBuf.class)
public class WriteRegistryValueImplementation {
    @Redirect(method = "writeRegistryValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/IndexedIterable;getRawId(Ljava/lang/Object;)I"))
    private <T> int redirectBlock(IndexedIterable<T> instance, T t) {
        if (instance == Block.STATE_IDS) {
            var player = PacketContext.get().getTarget();
            var polymap = Util.tryGetPolyMap(player);
            return polymap.getClientStateRawId((BlockState)t, player);
        }
        return instance.getRawId(t);
    }
}
