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
package io.github.theepicblock.polymc.api;

import com.google.common.collect.ImmutableMap;
import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.block.BlockStateManager;
import io.github.theepicblock.polymc.api.entity.EntityPoly;
import io.github.theepicblock.polymc.api.gui.GuiPoly;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.item.ItemPoly;
import io.github.theepicblock.polymc.api.item.ItemTransformer;
import io.github.theepicblock.polymc.impl.PolyMapImpl;
import io.github.theepicblock.polymc.impl.poly.item.ArmorItemPoly;
import io.github.theepicblock.polymc.impl.poly.item.ArmorMaterialPoly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to register Polys.
 * Also contains helper stuff to help with the registration of Polys and help lower conflicts.
 * This eventually gets transformed to an {@link PolyMap}.
 */
public class PolyRegistry {
    private final CustomModelDataManager CMDManager = new CustomModelDataManager();
    private final BlockStateManager blockStateManager = new BlockStateManager(this);

    private final Map<Item,ItemPoly> itemPolys = new HashMap<>();
    private final List<ItemTransformer> globalItemPolys = new ArrayList<>();
    private final Map<Block,BlockPoly> blockPolys = new HashMap<>();
    private final Map<ScreenHandlerType<?>,GuiPoly> guiPolys = new HashMap<>();
    private final Map<EntityType<?>,EntityPoly<?>> entityPolys = new HashMap<>();
    private final Map<ArmorMaterial, ArmorMaterialPoly> armorPolys = new HashMap<>();

    /**
     * Register a poly for an item.
     * @param item item to associate poly with.
     * @param poly poly to register.
     */
    public void registerItemPoly(Item item, ItemPoly poly) {
        itemPolys.put(item, poly);
    }

    /**
     * Registers a global item poly. This {@link ItemPoly#getClientItem(ItemStack, io.github.theepicblock.polymc.api.item.ItemLocation)} shall be called for all items.
     * <p>
     * The order is dependant on the registration order. If it is registered earlier it'll be called earlier.
     * @param poly poly to register.
     */
    public void registerGlobalItemPoly(ItemTransformer poly) {
        globalItemPolys.add(poly);
    }

    /**
     * Register a poly for a block.
     * @param block block to associate poly with.
     * @param poly  poly to register.
     */
    public void registerBlockPoly(Block block, BlockPoly poly) {
        blockPolys.put(block, poly);
    }

    /**
     * Register a poly for a gui.
     * @param screenHandler screen handler to associate poly with.
     * @param poly          poly to register.
     */
    public void registerGuiPoly(ScreenHandlerType<?> screenHandler, GuiPoly poly) {
        guiPolys.put(screenHandler, poly);
    }

    /**
     * Register a poly for an entity.
     * @param entityType    entity type to associate poly with.
     * @param poly          poly to register.
     */
    public <T extends Entity> void registerEntityPoly(EntityType<T> entityType, EntityPoly<T> poly) {
        entityPolys.put(entityType, poly);
    }

    /**
     * Register a poly for an armor material.
     * @param material  material to associate poly with
     * @param itemPoly  The ArmorItemPoly to register for. An ArmorMaterialPoly will be created.
     */
    public ArmorMaterialPoly registerArmorMaterialPoly(ArmorMaterial material, ArmorItemPoly itemPoly) {

        ArmorMaterialPoly armorMaterialPoly;

        if (armorPolys.containsKey(material)) {
            armorMaterialPoly = armorPolys.get(material);
        } else {
            armorMaterialPoly = new ArmorMaterialPoly(material);
            this.registerArmorMaterialPoly(material, armorMaterialPoly);
        }

        return armorMaterialPoly;
    }

    /**
     * Register a poly for an armor material.
     * @param material           material to associate poly with
     * @param armorMaterialPoly  poly to register
     */
    public ArmorMaterialPoly registerArmorMaterialPoly(ArmorMaterial material, ArmorMaterialPoly armorMaterialPoly) {

        ArmorMaterialPoly existingPoly = armorPolys.get(material);
        Integer color = null;
        Integer number = null;

        // Add (or overwrite) the material's poly
        armorPolys.put(material, armorMaterialPoly);

        if (existingPoly == null) {
            number = armorPolys.size();
            color = 0xFFFFFF - number * 2;
        } else {
            number = existingPoly.getNumber();
            color = existingPoly.getColorId();
        }

        armorMaterialPoly.setNumber(number);
        armorMaterialPoly.setColorId(color);

        return armorMaterialPoly;
    }

    /**
     * Checks if the item has a registered {@link ItemPoly}.
     * @param item item to check.
     * @return True if a {@link ItemPoly} exists for the given item.
     */
    public boolean hasItemPoly(Item item) {
        return itemPolys.containsKey(item);
    }

    /**
     * Checks if the block has a registered {@link BlockPoly}.
     * @param block block to check.
     * @return True if an {@link BlockPoly} exists for the given block
     */
    public boolean hasBlockPoly(Block block) {
        return blockPolys.containsKey(block);
    }

    /**
     * Checks if the screen handler has a registered {@link GuiPoly}.
     * @param screenHandler screen handler to check.
     * @return True if a {@link GuiPoly} exists for the given screen handler.
     */
    public boolean hasGuiPoly(ScreenHandlerType<?> screenHandler) {
        return guiPolys.containsKey(screenHandler);
    }

    /**
     * Checks if the entity type has a registered {@link EntityPoly}.
     * @param entityType entity type to check.
     * @return True if a {@link EntityPoly} exists for the given screen handler.
     */
    public boolean hasEntityPoly(EntityType<?> entityType) {
        return entityPolys.containsKey(entityType);
    }

    /**
     * Checks if this armor material has a registered {@link ArmorItemPoly}.
     * @param material armor material type to check.
     * @return True if a {@link ArmorItemPoly} exists for the given material.
     */
    public boolean hasArmorMaterialPoly(ArmorMaterial material) {
        return armorPolys.containsKey(material);
    }

    /**
     * Gets the ArmorMaterialPoly for the given material.
     * @param material armor material type to check.
     * @return True if a {@link ArmorItemPoly} exists for the given material.
     */
    public ArmorMaterialPoly getArmorMaterialPoly(ArmorMaterial material) {
        return armorPolys.get(material);
    }

    /**
     * Gets the {@link CustomModelDataManager} allocated to assist during registration
     */
    public CustomModelDataManager getCMDManager() {
        return CMDManager;
    }

    /**
     * Gets the {@link BlockStateManager} allocated to assist during registration
     */
    public BlockStateManager getBlockStateManager() {
        return blockStateManager;
    }

    /**
     * Creates an immutable {@link PolyMap} containing all of the registered polys
     */
    public PolyMap build() {
        return new PolyMapImpl(
                ImmutableMap.copyOf(itemPolys),
                globalItemPolys.toArray(new ItemTransformer[0]),
                ImmutableMap.copyOf(blockPolys),
                ImmutableMap.copyOf(guiPolys),
                ImmutableMap.copyOf(entityPolys),
                ImmutableMap.copyOf(armorPolys));
    }
}
