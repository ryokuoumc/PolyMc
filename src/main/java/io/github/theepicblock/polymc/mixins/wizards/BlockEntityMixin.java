package io.github.theepicblock.polymc.mixins.wizards;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Shadow @Nullable public abstract World getWorld();

    @Shadow public abstract void setWorld(World world);

    @Shadow public abstract BlockPos getPos();

    public BlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    }

    @Inject(method = "markRemoved", at = @At("HEAD"))
    protected void onRemove(CallbackInfo ci) {

    }
}
