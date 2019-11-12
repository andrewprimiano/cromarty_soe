package com.cromarty.ignition.designer;

import com.cromarty.ignition.EventRPC;
import com.cromarty.ignition.designer.beaninfos.SOE_TextField;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.inductiveautomation.ignition.common.licensing.LicenseState;

import com.inductiveautomation.ignition.common.project.resource.*;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;
import com.inductiveautomation.ignition.designer.WorkspaceManager;
import com.inductiveautomation.ignition.designer.gui.IconUtil;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceDescriptor;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.vision.api.designer.VisionDesignerInterface;
import com.inductiveautomation.vision.api.designer.palette.JavaBeanPaletteItem;
import com.inductiveautomation.vision.api.designer.palette.Palette;
import com.inductiveautomation.vision.api.designer.palette.PaletteItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class DesignerHook extends AbstractDesignerModuleHook {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ProjectResource resource;

    @Override
    public void startup(DesignerContext context, LicenseState activationState) throws Exception {
        super.startup(context, activationState);

        logger.debug("SOE Designer Hook startup() executing");

        logger.debug("Getting the project");
        Icon folderIcon = IconUtil.getIcon("main folder");
        Icon scriptIcon = IconUtil.getIcon("document");
        SOE_TabbedResourceWorkspace workspace = new SOE_TabbedResourceWorkspace(context, ResourceDescriptor.builder().resourceType(new ResourceType("SOE Module", "SOE Script")).rootFolderText("Event Scripts").rootIcon(folderIcon).icon(scriptIcon).build());

        WorkspaceManager wsManager = ((IgnitionDesigner)context.getRootPaneContainer()).getWorkspace();


        wsManager.setSelectedWorkspace(workspace.getKey());
        context.registerResourceWorkspace(workspace);

        context.addBeanInfoSearchPath("com.cromarty.ignition.designer.beaninfos");
        VisionDesignerInterface sdk = (VisionDesignerInterface) context.getModule(VisionDesignerInterface.VISION_MODULE_ID);
        if (sdk != null) {
            Palette palette = sdk.getPalette();
            PaletteItemGroup group = palette.addGroup("SOE");
            group.addPaletteItem(new JavaBeanPaletteItem(SOE_TextField.class));
        }


        logger.info("SOE Designer Hook startup() finished");

    }
    @Override
    public void initializeScriptManager(ScriptManager scriptManager)
    {
        scriptManager.addScriptModule("system.soe", ModuleRPCFactory.create("com.cromarty.ignition.soe", EventRPC.class));
    }

    @Override
    public void shutdown()
    {
        super.shutdown();

    }

}
