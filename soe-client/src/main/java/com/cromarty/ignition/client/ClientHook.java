package com.cromarty.ignition.client;

import com.cromarty.ignition.EventRPC;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.inductiveautomation.ignition.client.model.ClientContext;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.modules.ModuleInfo;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.vision.api.client.AbstractClientModuleHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHook extends AbstractClientModuleHook {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void startup(ClientContext context, LicenseState activationState) throws Exception {
        super.startup(context, activationState);
    }

    @Override
    public void initializeScriptManager(ScriptManager scriptManager)
    {
        scriptManager.addScriptModule("system.soe", ModuleRPCFactory.create("com.cromarty.ignition.soe", EventRPC.class));
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

}
