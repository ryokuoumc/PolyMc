package io.github.theepicblock.polymc.mixins.compat;

import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistrySyncManager.class)
public class FabricRegistrySyncDisabler {
    @Inject(method = "configureClient", at = @At("HEAD"), cancellable = true)
    private static void configureClient(ServerConfigurationNetworkHandler handler, MinecraftServer server, CallbackInfo ci) {
        ci.cancel();
    }
}
