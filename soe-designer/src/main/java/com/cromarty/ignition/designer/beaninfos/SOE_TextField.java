package com.cromarty.ignition.designer.beaninfos;

import com.cromarty.ignition.EventRPC;
import com.inductiveautomation.factorypmi.application.components.PMITextField;
import com.inductiveautomation.factorypmi.application.script.builtin.SecurityUtilities;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SOE_TextField extends PMITextField implements ActionListener {

    private String message;
    private boolean enableEvents = true;

    private EventRPC RPCHandler;
    public SOE_TextField()
    {
        super();
        RPCHandler = ModuleRPCFactory.create("com.cromarty.ignition.soe", EventRPC.class);
        addActionListener(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(enableEvents) {
            RPCHandler.dispatchEvent(this.getName(), this.getMessage(), SecurityUtilities.getUsername(), null, null);
        }

    }

    public boolean isEnableEvents() {
        return enableEvents;
    }

    public void setEnableEvents(boolean enabled) {
        this.enableEvents = enabled;
    }
}
