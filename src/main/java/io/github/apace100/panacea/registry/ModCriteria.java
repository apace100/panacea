package io.github.apace100.panacea.registry;

import io.github.apace100.panacea.advancement.CodeTriggerCriterion;
import io.github.apace100.panacea.mixin.CriteriaRegistryInvoker;

public class ModCriteria {

    public static void register() {
        CriteriaRegistryInvoker.callRegister(CodeTriggerCriterion.INSTANCE);
    }
}
