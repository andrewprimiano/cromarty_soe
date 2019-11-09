package com.cromarty.ignition.designer;

import com.cromarty.ignition.EventRPC;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.inductiveautomation.ignition.common.licensing.LicenseState;

import com.inductiveautomation.ignition.common.project.resource.*;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.xmlserialization.serialization.XMLSerializer;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;
import com.inductiveautomation.ignition.designer.WorkspaceManager;
import com.inductiveautomation.ignition.designer.gui.IconUtil;
import com.inductiveautomation.ignition.designer.navtree.model.AbstractNavTreeNode;
import com.inductiveautomation.ignition.designer.navtree.model.FolderNode;
import com.inductiveautomation.ignition.designer.project.DesignableProject;
import com.inductiveautomation.ignition.designer.resourcedoceditor.DocEditorPanel;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceDescriptor;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceEditor;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceFolderNode;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceNode;
import com.inductiveautomation.ignition.designer.tabbedworkspace.TabbedResourceWorkspace;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DesignerHook extends AbstractDesignerModuleHook {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ProjectResource resource;

    @Override
    public void startup(DesignerContext context, LicenseState activationState) throws Exception {
        super.startup(context, activationState);
        logger.debug("SOE Designer Hook startup() executing");

        logger.debug("Getting the project");
//        DesignableProject project = context.getProject();
//        logger.debug("Retrieved Project: " + project.toString());
//
//        logger.debug("Creating Resource ID");
//        ProjectResourceId resID = new ProjectResourceId(project.getName(),new ResourcePath(new ResourceType("SOE Module", "SOE Script"),"Event Script"));
//        logger.debug("Created Resource ID: " + resID.toString());
//
//        logger.debug("Creating new script");
//        SOE_Script script = new SOE_Script("New Script", "#Add your script here");
//        logger.debug("Create script");
//
//        logger.debug("Creating Serializer for resource object");
//        XMLSerializer serializer = context.createSerializer();
//        serializer.addObject(script);
//        logger.debug("Serializer created: " + serializer.serializeXML());
//
//        logger.debug("Building resource");
//        Map<String, byte[]> data = new HashMap<>();
//        data.put(ProjectResource.DEFAULT_DATA_KEY, serializer.serializeBinary(true));
//        ProjectResource resource = ProjectResource
//                .newBuilder()
//                .setResourceId(resID)
//                .setData(data)
//                .build();
//        logger.debug("Resource built: " + resource.toString());
//        logger.debug("Adding resource to project: " + resource.toString());
//        project.createOrModify(resource);
//        logger.debug("Project resource: " + resource.toString());
        Icon folderIcon = IconUtil.getIcon("main folder");
        Icon scriptIcon = IconUtil.getIcon("document");
        SOE_TabbedResourceWorkspace workspace = new SOE_TabbedResourceWorkspace(context, ResourceDescriptor.builder().resourceType(new ResourceType("SOE Module", "SOE Script")/*resource.getResourceType()*/).rootFolderText("Event Scripts").rootIcon(folderIcon).icon(scriptIcon).build());

        WorkspaceManager wsManager = ((IgnitionDesigner)context.getRootPaneContainer()).getWorkspace();


        wsManager.setSelectedWorkspace(workspace.getKey());
        context.registerResourceWorkspace(workspace);

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
