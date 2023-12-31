package com.inforetros.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class DateLongFormatTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if(value != null) out.value(value.getTime());
        else out.nullValue();
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        return new Date(in.nextLong());
    }

}