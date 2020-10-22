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
package io.github.theepicblock.polymc.generator;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.Util;
import io.github.theepicblock.polymc.api.OutOfBoundsException;
import io.github.theepicblock.polymc.api.block.*;
import io.github.theepicblock.polymc.api.register.PolyRegistry;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

/**
 * Class to automatically generate BlockPolys for Blocks
 */
public class BlockPolyGenerator {

    /**
     * Automatically generates all {@link BlockPoly}s that are missing in the specified builder
     * @param builder builder to add the {@link BlockPoly}s to
     */
    public static void generateMissing(PolyRegistry builder) {
        for (Block block : getBlockRegistry()) {
            if (builder.hasBlockPoly(block)) continue;
            Identifier id = getBlockRegistry().getId(block);
            if (!Util.isVanilla(id)) {
                //this is a modded block and should have a Poly
                addBlockToBuilder(block, builder);
            }
        }
    }

    /**
     * Generates the most suitable BlockPoly for a given block
     */
    public static BlockPoly generatePoly(Block block, PolyRegistry builder) {
        BlockState state = block.getDefaultState();
        VoxelShape collisionShape = null;
        try {
            collisionShape = state.getCollisionShape(null, null);
        } catch (Exception e) {
            PolyMc.LOGGER.warn("Failed to get collision shape for " + block.getTranslationKey() + ": " + e.getMessage());
            return new SimpleReplacementPoly(Blocks.STONE);
        }

        //Handle fluids
        if (block instanceof FluidBlock) {
            return new PropertyRetainingReplacementPoly(Blocks.WATER);
        }
        //Handle leaves
        if (block instanceof LeavesBlock) {
            try {
                return new SingleUnusedBlockStatePoly(builder, BlockStateProfile.LEAVES_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Handle doors/trapdoors
        if (block instanceof DoorBlock) {
            try {
                return new PoweredStateBlockPoly(builder, BlockStateProfile.DOOR_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        if (block instanceof TrapdoorBlock) {
            try {
                return new PoweredStateBlockPoly(builder, BlockStateProfile.TRAPDOOR_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Handle full blocks
        if (Block.isShapeFullCube(collisionShape)) {
            try {
                return new UnusedBlockStatePoly(block, builder, BlockStateProfile.NOTEBLOCK_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Handle blocks without collision
        if (collisionShape.isEmpty()) {
            if (block instanceof SaplingBlock) {
                try {
                    return new SingleUnusedBlockStatePoly(builder, BlockStateProfile.NO_COLLISION_PROFILE);
                } catch (OutOfBoundsException ignored) {}
            }
            try {
                return new UnusedBlockStatePoly(block, builder, BlockStateProfile.NO_COLLISION_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Handle slabs
        if (block instanceof SlabBlock) {
            try {
                return new UnusedBlockStatePoly(block, builder, BlockStateProfile.PETRIFIED_OAK_SLAB_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Handle blocks with same collision as farmland
        if (collisionShape == Blocks.FARMLAND.getOutlineShape(null,null,null,null)) {
            try {
                return new UnusedBlockStatePoly(block, builder, BlockStateProfile.FARMLAND_PROFILE);
            } catch (OutOfBoundsException ignored) {}
        }
        //Odd cases
        return new SimpleReplacementPoly(Blocks.STONE); //TODO better implementation
    }

    /**
     * Generates the most suitable BlockPoly and directly adds it to the {@link PolyRegistry}
     * @see #generatePoly(Block, PolyRegistry)
     */
    private static void addBlockToBuilder(Block block, PolyRegistry builder) {
        builder.registerBlockPoly(block, generatePoly(block, builder));
    }

    /**
     * @return the minecraft item registry
     */
    private static DefaultedRegistry<Block> getBlockRegistry() {
        return Registry.BLOCK;
    }
}
