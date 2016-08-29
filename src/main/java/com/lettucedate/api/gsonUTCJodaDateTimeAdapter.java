package com.lettucedate.api;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Dave on 6/5/2016.
 */

public class gsonUTCJodaDateTimeAdapter implements JsonSerializer<DateTime>,JsonDeserializer<DateTime> {



    @Override
    public synchronized JsonElement serialize(DateTime date, Type type, JsonSerializationContext jsonSerializationContext) {
        final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        return new JsonPrimitive(fmt.print(date.toDateTime(DateTimeZone.UTC)));
    }

    @Override
    public synchronized DateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        // Do not try to deserialize null or empty values
        if (jsonElement.getAsString() == null || jsonElement.getAsString().isEmpty())
        {
            return null;
        }

        final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String dateTimeStr = jsonElement.getAsString();
        DateTime finalTime;

        try{
            finalTime = fmt.parseDateTime(dateTimeStr);
        } catch (Exception exp) {
            if (!dateTimeStr.contains("Z"))
                dateTimeStr = dateTimeStr.concat("Z");
            finalTime = fmt.parseDateTime(dateTimeStr);
        }

        return finalTime;
    }
}