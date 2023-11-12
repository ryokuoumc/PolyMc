package io.github.theepicblock.polymc.api.resource.json;

import io.github.theepicblock.polymc.api.resource.PolyMcAsset;
import io.github.theepicblock.polymc.impl.Util;
import io.github.theepicblock.polymc.impl.resource.json.JBlockStateImpl;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public interface JBlockState extends PolyMcAsset {
    void setVariant(String propertyString, JBlockStateVariant[] variants);

    JBlockStateVariant[] getVariants(String variantString);

    Set<String> getPropertyStrings();

    default @Nullable JBlockStateVariant[] getVariantsBestMatching(BlockState state) {
        var variantBestMatching = this.getVariantId(state);
        return variantBestMatching == null ? null : getVariants(variantBestMatching);
    }

    /**
     * Returns the entry in {@link #getPropertyStrings()} that best matches the provided {@link BlockState}
     */
    @ApiStatus.Internal
    default @Nullable String getVariantId(BlockState state) {
        mainloop:
        for (var propertyString : getPropertyStrings()) {
            // propertyString will be a list of properties. Eg:
            // "facing=east,half=lower,hinge=left,open=false"

            for (var property : Util.splitBlockStateString(propertyString)) {
                // Split "facing=east" into "facing" and "east"
                var pair = property.split("=", 2);

                var blockProperty = state.getBlock().getStateManager().getProperty(pair[0]);
                if (blockProperty == null) continue mainloop;

                Optional<?> parsedValue = blockProperty.parse(pair[1]);
                if (parsedValue.isEmpty()) continue mainloop;
                if (!(parsedValue.get() == state.get(blockProperty))) {
                    continue mainloop;
                }
            }

            return propertyString;
        }
        return null;
    }

    static JBlockState create() {
        return new JBlockStateImpl();
    }
}
