package com.cromarty.ignition;

import com.inductiveautomation.ignition.common.QualifiedPath;
import com.inductiveautomation.ignition.common.alarming.AlarmEvent;
import com.inductiveautomation.ignition.common.alarming.AlarmStateTransition;
import com.inductiveautomation.ignition.common.alarming.config.BasicAlarmConfiguration;
import com.inductiveautomation.ignition.common.alarming.config.BasicAlarmProperty;
import com.inductiveautomation.ignition.common.alarming.config.CommonAlarmProperties;
import com.inductiveautomation.ignition.common.config.Property;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import java.io.IOException;

public class SOE_Event {
    private SOE_EventType eventType;
    private DateTime timestamp;
    private String name;
    private String userName;
    private String description;
    private Object userData;

    public SOE_Event(String name, String description, String userName, Object userData, DateTime customTimestamp) {
        if(customTimestamp==null)
            this.setTimestamp(DateTime.now());
        else
            this.setTimestamp(customTimestamp);

        this.setName(name);
        this.setDescription(description);
        this.setUserName(userName);
        this.setUserData(userData);


    }



    public SOE_Event(AlarmEvent event) {

        switch(event.getLastEventState())
        {
            case Ack:
                this.setEventType(SOE_EventType.AlarmAck);
                this.setTimestamp(new DateTime(event.getAckData().getTimestamp()));

                String prop = event.get(CommonAlarmProperties.AckUser).toString();
                QualifiedPath userPath = null;
                try {
                    userPath = QualifiedPath.parse(prop);
                    this.userName = userPath.getLastPathComponent();
                }
                catch(Exception e)
                {
                    this.userName = "None";
                }


                break;
            case Active:
                this.setEventType(SOE_EventType.AlarmActive);
                this.setTimestamp(new DateTime(event.getActiveData().getTimestamp()));
                break;
            case Clear:
                this.setEventType(SOE_EventType.AlarmClear);
                this.setTimestamp(new DateTime(event.getClearedData().getTimestamp()));
                break;
            default:
                this.setEventType(null);
        }


        this.setName(event.getName());
        this.setDescription(event.getDisplayPath().toString());

        this.setUserData(userData);


    }

    @Override
    public String toString()
    {
        return String.format("SOE_Event { timestamp: %s, name: %s, description: %s, userName: %s, userData: %s }",this.timestamp.toString(),this.name,this.description,this.userName,this.userData);
    }

    public SOE_EventType getEventType() {
        return eventType;
    }

    public void setEventType(SOE_EventType eventType) {
        this.eventType = eventType;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}

