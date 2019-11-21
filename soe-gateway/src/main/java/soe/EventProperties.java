package soe;

import com.inductiveautomation.ignition.common.alarming.config.AlarmProperty;
import com.inductiveautomation.ignition.common.alarming.config.BasicAlarmProperty;
import com.inductiveautomation.ignition.common.config.BasicConfigurationProperty;

public class EventProperties {

    public static AlarmProperty<Boolean> EnableSOE = new BasicAlarmProperty<Boolean>("Enable SOE Recording",
            Boolean.class, false,
            "EventProperties.Properties.ExtendedConfig.Enable",
            "EventProperties.Properties.ExtendedConfig.Category",
            "EventProperties.Properties.ExtendedConfig.Desc",true,false);

    public static BasicConfigurationProperty<Boolean> EnableSOETag = new BasicConfigurationProperty<Boolean>("Enable Event on Change",
            "EventProperties.Properties.TagConfig.Enable",
            "EventProperties.Properties.TagConfig.Category",
            "EventProperties.Properties.TagConfig.Desc",Boolean.class,false);

}
