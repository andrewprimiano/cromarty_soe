package com.cromarty.ignition.designer;

import com.cromarty.ignition.SOE_Script;
import com.inductiveautomation.ignition.common.project.resource.ProjectResource;
import com.inductiveautomation.ignition.common.project.resource.ProjectResourceId;
import com.inductiveautomation.ignition.common.project.resource.ResourcePath;
import com.inductiveautomation.ignition.common.project.resource.ResourceType;
import com.inductiveautomation.ignition.common.xmlserialization.SerializationException;
import com.inductiveautomation.ignition.common.xmlserialization.serialization.XMLSerializer;
import com.inductiveautomation.ignition.designer.gui.IconUtil;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.designer.project.DesignableProject;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceDescriptor;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceEditor;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceFolderNode;
import com.inductiveautomation.ignition.designer.tabbedworkspace.TabbedResourceWorkspace;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

class SOE_TabbedResourceWorkspace extends TabbedResourceWorkspace implements ActionListener {
    ResourceBundle designerStrings = ResourceBundle.getBundle("SOE_DesignerStrings");

    protected SOE_TabbedResourceWorkspace(@Nonnull DesignerContext context, @Nonnull ResourceDescriptor resourceDescriptor)
    {
        super(context, resourceDescriptor);
    }

    @Override
    protected ResourceEditor newResourceEditor(ResourcePath resourcePath) {
        final SOE_ScriptEditor soe_scriptEditor = new SOE_ScriptEditor(this, resourcePath);


        return soe_scriptEditor;

    }

    @Override
    public void addNewResourceActions(ResourceFolderNode resourceFolderNode, JPopupMenu jPopupMenu)
    {
        JMenuItem newMenuItem = new JMenuItem("Create New Script");
        newMenuItem.setIcon(IconUtil.getIcon("document"));
        newMenuItem.addActionListener(this);

        jPopupMenu.add(newMenuItem);

    }

    @Override
    public String getKey()
    {
        return "SOE Workspace";
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        DesignableProject project = context.getProject();

        String path = "Event handler " + Integer.toString(getUniqueResourcePathIndex("SOE Module/SOE Script","Event handler",1));

        ProjectResourceId resID = new ProjectResourceId(project.getName(),new ResourcePath(new ResourceType("SOE Module", "SOE Script"), path));

        SOE_Script script = new SOE_Script("New Script", designerStrings.getString("HeaderComment"));

        XMLSerializer serializer = context.createSerializer();
        serializer.addObject(script);

        Map<String, byte[]> data = new HashMap<>();
        try {
            data.put(ProjectResource.DEFAULT_DATA_KEY, serializer.serializeBinary(true));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        ProjectResource resource = ProjectResource
                .newBuilder()
                .setResourceId(resID)
                .setData(data)
                .build();
        project.createOrModify(resource);
        SOE_ScriptEditor editor = new SOE_ScriptEditor(this,resource.getResourcePath());
    }

    private int getUniqueResourcePathIndex(String folderPath, String path, int counter)
    {

        for (ProjectResource res:this.context.getProject().getResources()){
            if (res.getResourcePath().toString().equals(folderPath + "/" + path + " " + Integer.toString(counter)))
            {
                counter = getUniqueResourcePathIndex(folderPath, path, counter+1);
            }
        }
        return counter;
    }
}
