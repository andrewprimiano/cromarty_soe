package com.cromarty.ignition;

import org.joda.time.DateTime;

public interface EventRPC
{
    void dispatchEvent(String name, String description, String userName, Object userData, DateTime customTimestamp);

}
