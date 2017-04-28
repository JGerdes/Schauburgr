package com.jonasgerdes.schauburgr.model.tmdb;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.04.2017
 */

public class DateDeserializer implements JsonDeserializer<Date> {

    private static final SimpleDateFormat FORMAT
            = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);

    @Override
    public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String date = element.getAsString();
        try {
            return FORMAT.parse(date);
        } catch (ParseException exp) {
            return null;
        }
    }
}
