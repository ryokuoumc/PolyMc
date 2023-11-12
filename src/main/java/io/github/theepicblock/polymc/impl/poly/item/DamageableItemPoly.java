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
package io.github.theepicblock.polymc.impl.poly.item;

import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.item.ItemLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class DamageableItemPoly extends CustomModelDataPoly {
    private static final Item[] GENERIC_DAMAGEABLE = new Item[]{Items.WOODEN_PICKAXE, Items.STONE_PICKAXE};

    public DamageableItemPoly(CustomModelDataManager registerManager, Item base) {
        this(registerManager, base, GENERIC_DAMAGEABLE);
    }

    public DamageableItemPoly(CustomModelDataManager registerManager, Item base, Item target) {
        this(registerManager, base, new Item[]{target});
    }

    public DamageableItemPoly(CustomModelDataManager registerManager, Item base, Item[] targets) {
        super(registerManager, base, targets);
    }

    @Override
    public ItemStack getClientItem(ItemStack input, @Nullable ServerPlayerEntity player, @Nullable ItemLocation location) {
        ItemStack sup = super.getClientItem(input, player, location);
        int inputDamage = input.getDamage();
        int damage = (int)(((float)inputDamage / input.getMaxDamage()) * sup.getMaxDamage()); //convert serverside damage to clientside damage
        if (damage == 0 && inputDamage > 0) damage = 1; //If the item is damaged in any way it should show up
        sup.setDamage(damage);

        return sup;
    }
}
