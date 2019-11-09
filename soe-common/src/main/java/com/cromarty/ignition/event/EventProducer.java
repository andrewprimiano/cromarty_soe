package com.cromarty.ignition.event;

import java.util.ArrayList;

public class EventProducer<G>
{
    private ArrayList<EventConsumer<G>> consumers = new ArrayList<>();

    public void addEventListener(EventConsumer<G> consumer) {
        this.consumers.add(consumer);
    }
    public void removeEventListener(EventConsumer<G> consumer) {
        this.consumers.remove(consumer);
    }

    public void notifyEvent(G payload)
    {
        for (EventConsumer consumer:consumers)
        {
            consumer.onEventReceived(payload);
        }
    }
}
