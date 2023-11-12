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
package io.github.theepicblock.polymc.mixins.item;

import io.github.theepicblock.polymc.impl.Util;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.stream.Collectors;

/**
 * Minecraft syncs the entire recipe library when the client joins.
 * With mods, this recipe list can contain modded recipe types.
 * This fix prevents those from being sent
 */
@Mixin(SynchronizeRecipesS2CPacket.class)
public class CustomRecipeFix {
    /**
     * Modifies the recipes to remove custom serializers (which will crash vanilla clients).
     */
    @Inject(method = "writeRecipe", at = @At("HEAD"), cancellable = true)
    private static void writeRecipe(PacketByteBuf buf, RecipeEntry<?> recipe, CallbackInfo ci) {
        if (!Util.isPolyMapVanillaLike(PacketContext.get().getPlayer())) {
            return;
        }
        if (!Util.isVanilla(Registries.RECIPE_SERIALIZER.getId(recipe.value().getSerializer()))) {
            ci.cancel();
        }
    }
}
