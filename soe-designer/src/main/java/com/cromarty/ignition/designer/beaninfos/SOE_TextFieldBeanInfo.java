package com.cromarty.ignition.designer.beaninfos;

import com.inductiveautomation.factorypmi.designer.beaninfo.PMITextFieldBeanInfo;

import java.beans.IntrospectionException;


public class SOE_TextFieldBeanInfo extends PMITextFieldBeanInfo {
    public SOE_TextFieldBeanInfo()
    {
        super();

    }

    @Override
    protected void initProperties() throws IntrospectionException {
        // Adds common properties
        super.initProperties();
        this.beanClass = SOE_TextField.class;
        addBoundProp("message", "Event Message", "The text to attach to the event for description", "SOE", PREFERRED_MASK);
        addBoundProp("enableEvents", "Enable Events", "Enable events on textbox update", "SOE", PREFERRED_MASK);

    }

}
