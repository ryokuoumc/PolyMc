package io.github.theepicblock.polymc.mixins;

import net.minecraft.server.network.ChunkFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkFilter.class)
public interface ChunkFilterAccessor {


    @Invoker
    static boolean callIsWithinDistanceExcludingEdge(int sourceChunkX, int sourceChunkZ, int playerChunkX, int playerChunkZ, int watchDistance) {
        throw new IllegalStateException();
    }

    @Invoker
    static boolean callIsWithinDistance(int centerX, int centerZ, int viewDistance, int x, int z, boolean includeEdge) {
        throw new IllegalStateException();
    }

}
