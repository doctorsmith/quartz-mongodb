package org.doctorsmith.quartz.mongodb;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.OperableTrigger;

import java.text.ParseException;
import java.util.TimeZone;

public class CronTriggerPersistenceHelper implements TriggerPersistenceHelper {
    private static final String TRIGGER_CRON_EXPRESSION = "cronExpression";
    private static final String TRIGGER_TIMEZONE = "timezone";

    @Override
    public boolean canHandleTriggerType(OperableTrigger trigger) {
        return ((trigger instanceof CronTriggerImpl) && !((CronTriggerImpl) trigger).hasAdditionalProperties());
    }

    @Override
    public DBObject injectExtraPropertiesForInsert(OperableTrigger trigger, DBObject original) {
        CronTrigger t = (CronTrigger) trigger;

        return BasicDBObjectBuilder.start(original.toMap()).
                append(TRIGGER_CRON_EXPRESSION, t.getCronExpression()).
                append(TRIGGER_TIMEZONE, t.getTimeZone().getID()).
                get();
    }

    @Override
    public OperableTrigger setExtraPropertiesAfterInstantiation(OperableTrigger trigger, DBObject stored) {
        CronTriggerImpl t = (CronTriggerImpl) trigger;

        Object expression = stored.get(TRIGGER_CRON_EXPRESSION);
        if (expression != null) {
            try {
                t.setCronExpression(new CronExpression((String) expression));
            } catch (ParseException e) {
                // no good handling strategy and
                // checked exceptions route sucks just as much.
            }
        }
        Object tz = stored.get(TRIGGER_TIMEZONE);
        if (tz != null) {
            t.setTimeZone(TimeZone.getTimeZone((String) tz));
        }

        return t;
    }
}
