package com.cromarty.ignition.designer;

import com.cromarty.ignition.SOE_Script;
import com.inductiveautomation.ignition.common.project.resource.ResourcePath;
import com.inductiveautomation.ignition.designer.scripteditor.component.CodeEditorFactory;
import com.inductiveautomation.ignition.designer.tabbedworkspace.ResourceEditor;
import com.inductiveautomation.ignition.designer.tabbedworkspace.TabbedResourceWorkspace;
import com.jidesoft.editor.CodeEditor;

import javax.swing.*;
import java.awt.*;

class SOE_ScriptEditor extends ResourceEditor<SOE_Script>
{
    private SOE_Script model;
    private JCheckBox enabledCheckBox;
    private JPanel topPanel;
    private JPanel panel;
    private CodeEditor scriptArea;


    public SOE_ScriptEditor(TabbedResourceWorkspace workspace, ResourcePath path) {
        super(workspace, path);
    }


    @Override
    protected void init(SOE_Script soe_script) {

        model = soe_script;

        //init ui components
        this.setLayout(new BorderLayout());
        enabledCheckBox = new JCheckBox("Enabled");
        topPanel = new JPanel();
        scriptArea = CodeEditorFactory.newPythonEditor();

        //arrange layout
        topPanel.setLayout(new FlowLayout());
        topPanel.add(enabledCheckBox, BorderLayout.NORTH);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scriptArea, BorderLayout.CENTER);

        //load resource settings
        enabledCheckBox.setSelected(model.getEnabled());
        scriptArea.setText(model.getUserScript());

    }

    @Override
    protected SOE_Script getObjectForSave() throws Exception {
        model.setUserScript(this.scriptArea.getText());
        model.setEnabled(this.enabledCheckBox.isSelected());

        return model;
    }


}
