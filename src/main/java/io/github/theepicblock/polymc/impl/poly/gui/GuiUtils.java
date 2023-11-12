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
package io.github.theepicblock.polymc.impl.poly.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GuiUtils {
    public static List<Slot> removePlayerSlots(List<Slot> base) {
        return base.stream().filter(
                (slot) -> !(slot.inventory instanceof PlayerInventory)
        ).collect(Collectors.toList());
    }

    public static void resyncPlayerInventory(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            resyncPlayerInventory((ServerPlayerEntity)player);
        }
    }

    public static void resyncPlayerInventory(ServerPlayerEntity player) {
        player.currentScreenHandler.syncState();
    }
}
