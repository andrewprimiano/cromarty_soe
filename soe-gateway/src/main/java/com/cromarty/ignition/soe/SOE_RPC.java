package com.cromarty.ignition.soe;

import com.cromarty.ignition.EventRPC;
import com.cromarty.ignition.SOE_Event;
import com.cromarty.ignition.event.EventProducer;
import org.joda.time.DateTime;

public class SOE_RPC implements EventRPC {

    private EventProducer<SOE_Event> eventProducer = new EventProducer<SOE_Event>();

    public SOE_RPC() {

    }

    @Override
    public void dispatchEvent(String name, String description, String userName, Object userData, DateTime customTimestamp) {
        getEventProducer().notifyEvent(new SOE_Event(name, description, userName, userData, customTimestamp));
    }

    public EventProducer<SOE_Event> getEventProducer() {
        return eventProducer;
    }
}
