package io.github.theepicblock.polymc.mixins.wizards;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.impl.misc.PolyMapMap;
import io.github.theepicblock.polymc.impl.misc.WatchListener;
import io.github.theepicblock.polymc.impl.poly.wizard.FallingBlockWizardInfo;
import io.github.theepicblock.polymc.impl.poly.wizard.PolyMapFilteredPlayerView;
import io.github.theepicblock.polymc.impl.poly.wizard.SinglePlayerView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityTrackMixin {

    public EntityTrackMixin(EntityType<?> type, World world) {
    }

    @Shadow public abstract World getWorld();

    @Shadow public abstract ChunkPos getChunkPos();


    @Inject(method = "onStartedTrackingBy(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"))
    protected void onStartTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {

    }

    @Inject(method = "onStoppedTrackingBy(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"))
    protected void onStopTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {

    }

    @Inject(method = "setRemoved(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At("RETURN"))
    protected void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {

    }


}
