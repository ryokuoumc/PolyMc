package io.github.theepicblock.polymc.impl.poly.entity;

import io.github.theepicblock.polymc.api.entity.EntityPoly;
import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.api.wizard.VItem;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import io.github.theepicblock.polymc.impl.poly.wizard.EntityUtil;
import io.github.theepicblock.polymc.mixins.wizards.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Optional;

public class MissingEntityPoly<T extends Entity> implements EntityPoly<T> {
    @Override
    public Wizard createWizard(WizardInfo info, T entity) {
        return new MissingEntityWizard<>(info, entity);
    }

    public static class MissingEntityWizard<T extends Entity> extends EntityWizard<T> {
        private static final ItemStack ITEM = new ItemStack(Items.RED_STAINED_GLASS_PANE);
        private final VItem item;

        public MissingEntityWizard(WizardInfo info, T entity) {
            super(info, entity);
            item = new VItem();
        }

        @Override
        public void onMove(PacketConsumer players) {
            item.move(players, this.getPosition(), (byte)0, (byte)0, true);
        }

        @Override
        public void addPlayer(PacketConsumer player) {
            item.spawn(player, this.getPosition());
            item.setNoGravity(player, true);
            item.sendItem(player, ITEM);

            player.sendPacket(EntityUtil.createDataTrackerUpdate(
                    this.item.getId(),
                    List.of(
                            new DataTracker.Entry<>(EntityAccessor.getCustomName(), Optional.of(this.getEntity().getName())),
                            new DataTracker.Entry<>(EntityAccessor.getNameVisible(), true))
                    )
            );
        }

        @Override
        public void removePlayer(PacketConsumer player) {
            item.remove(player);
        }
    }
}
