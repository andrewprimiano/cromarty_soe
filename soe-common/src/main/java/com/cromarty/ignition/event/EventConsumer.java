package com.cromarty.ignition.event;

public interface EventConsumer<G>
{
    void onEventReceived(G EventData);
}
