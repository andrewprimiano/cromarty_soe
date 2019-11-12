package soe;

import com.inductiveautomation.ignition.common.alarming.config.AlarmProperty;
import com.inductiveautomation.ignition.common.alarming.config.BasicAlarmProperty;
import com.inductiveautomation.ignition.common.config.BasicConfigurationProperty;

public class EventProperties {

    /**
     * EXTENDED CONFIG - These are different than the properties above, they are registered for each alarm through the
     * extended config system
     **/
    public static AlarmProperty<Boolean> EnableSOE = new BasicAlarmProperty<Boolean>("Enable SOE Recording",
            Boolean.class, false,
            "EventProperties.Properties.ExtendedConfig.Enable",
            "EventProperties.Properties.ExtendedConfig.Category",
            "EventProperties.Properties.ExtendedConfig.Desc",true,false);

    public static BasicConfigurationProperty<Boolean> EnableSOETag = new BasicConfigurationProperty<Boolean>("Enable Events",
            "EventProperties.Properties.TagConfig.Enable",
            "EventProperties.Properties.TagConfig.Category",
            "EventProperties.Properties.TagConfig.Desc",Boolean.class,false);


    static {
        //        MESSAGE.setExpressionSource(true);
        //        MESSAGE.setDefaultValue(i18n("SlackNotification." + "Properties.Message.DefaultValue"));
        //
        //        THROTTLED_MESSAGE.setExpressionSource(true);
        //        THROTTLED_MESSAGE.setDefaultValue(i18n("SlackNotification." + "Properties.ThrottledMessage.DefaultValue"));
        //
        //        TIME_BETWEEN_NOTIFICATIONS.setExpressionSource(true);
        //        TIME_BETWEEN_NOTIFICATIONS.setDefaultValue(i18n("SlackNotification."
        //                + "Properties.TimeBetweenNotifications.DefaultValue"));
        //
        //        TEST_MODE.setDefaultValue(false);
        //        List<ConfigurationProperty.Option<Boolean>> options = new ArrayList<>();
        //        options.add(new ConfigurationProperty.Option<>(true, new LocalizedString("words.yes")));
        //        options.add(new ConfigurationProperty.Option<>(false, new LocalizedString("words.no")));
        //        TEST_MODE.setOptions(options);
    }

}
