package com.cromarty.ignition.soe;

import com.inductiveautomation.ignition.common.alarming.evaluation.EventProperty;
import com.inductiveautomation.ignition.common.config.*;
import soe.EventProperties;

import javax.annotation.Nullable;

public class SOE_TagPropContributor implements PropertyModelContributor {

    private boolean exampleprop;

    public SOE_TagPropContributor()
    {

    }

    @Override
    public void configure(MutableConfigurationPropertyModel mutableConfigurationPropertyModel) {

        mutableConfigurationPropertyModel.addProperties(EventProperties.EnableSOETag);
    }

    public boolean isExampleprop() {
        return exampleprop;
    }

    public void setExampleprop(boolean exampleprop) {
        this.exampleprop = exampleprop;
    }
}

