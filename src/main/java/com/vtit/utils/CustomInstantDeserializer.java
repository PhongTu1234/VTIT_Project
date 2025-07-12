package com.vtit.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")
            .withZone(ZoneId.of("GMT+7"));

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(text, FORMATTER);
            return localDateTime.atZone(ZoneId.of("GMT+7")).toInstant();
        } catch (DateTimeParseException e) {
            throw new IOException("Không thể parse birthday với format yyyy-MM-dd hh:mm:ss a", e);
        }
    }
}
