package com.cromarty.ignition.designer;

import com.inductiveautomation.ignition.common.project.resource.ProjectResource;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.designer.navtree.model.AbstractNavTreeNode;
import com.inductiveautomation.ignition.designer.navtree.model.FolderNode;

class SOE_TreeFolderNode extends FolderNode {

    protected SOE_TreeFolderNode(String name, DesignerContext context, ProjectResource res, int scope) {
        super(context, res, scope);
        this.setName(name);

    }

    @Override
    protected AbstractNavTreeNode createChildNode(ProjectResource projectResource) {
        return null;
    }
}
