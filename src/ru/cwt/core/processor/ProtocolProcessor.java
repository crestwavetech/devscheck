package ru.cwt.core.processor;

import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

public interface ProtocolProcessor {

    public abstract <T> T get(Reader reader, Class<T> clazz) throws ParseException;

    public abstract <T> void write(Writer writer, T object) throws ParseException;

}
