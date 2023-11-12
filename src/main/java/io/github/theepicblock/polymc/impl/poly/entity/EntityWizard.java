package io.github.theepicblock.polymc.impl.poly.entity;

import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import io.github.theepicblock.polymc.impl.poly.wizard.ThreadedWizardUpdater;
import net.minecraft.entity.Entity;

public abstract class EntityWizard<T extends Entity> extends Wizard {
    private final T entity;

    public EntityWizard(WizardInfo info, T entity) {
        super(info);
        this.entity = entity;
    }

    @ThreadedWizardUpdater.KindaSafe
    public T getEntity() {
        return entity;
    }
}
