package com.cromarty.ignition.designer;

import com.inductiveautomation.ignition.common.project.resource.ProjectResource;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceNode;
import com.inductiveautomation.ignition.designer.tabbedworkspace.TabbedResourceWorkspace;

class SOE_TreeNode extends ResourceNode {
    private String name;

    public SOE_TreeNode(String name, DesignerContext context, TabbedResourceWorkspace tws, ProjectResource res)
    {
        super(context, tws, res);
        this.name = name;
    }

    @Override
    public String getWorkspaceName()
    {
        return this.name;
    }

}
