package com.cromarty.ignition;

public class SOE_Script {
    private String userScript;
    private String name;
    private boolean enabled = false;

    public SOE_Script()
    {

    }

    public SOE_Script(String name, String userScript)
    {
        this.name = name;
        this.userScript = userScript;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public String getUserScript()
    {
        return userScript;
    }

    public String getName()
    {
        return name;
    }

    public void setUserScript(String userScript)
    {
        this.userScript = userScript;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}


