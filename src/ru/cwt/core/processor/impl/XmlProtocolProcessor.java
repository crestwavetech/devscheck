package ru.cwt.core.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cwt.core.processor.ProtocolProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

public class XmlProtocolProcessor implements ProtocolProcessor {
    private final static Logger log = LoggerFactory.getLogger(XmlProtocolProcessor.class);

    private JAXBContext ctx;
    private boolean fragment;

    private static JAXBContext initContext(String jaxb) {
        try {
            log.info("context initialized");
            return JAXBContext.newInstance(jaxb);
        } catch (JAXBException e) {
            log.error("can't create context", e);
            return null;
        }
    }

    public XmlProtocolProcessor(String jaxb) {
        log.info("use jaxb context: {}", jaxb);
        setCtx(initContext(jaxb));
    }

    @Override
    public <T> T get(Reader reader, Class<T> clazz) throws ParseException {
        try {
            Unmarshaller unmarshaller = getCtx().createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            log.error("jaxb read exception", e);
            throw new ParseException(e.getMessage(), 0);
        }
    }

    @Override
    public <T> void write(Writer writer, T object) throws ParseException {
        if (getCtx() == null) {
            log.error("jaxb context was't initialised.");
            return;
        }

        try {
            Marshaller marshaller = getCtx().createMarshaller();

            if(fragment)
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(object, writer);
        } catch (JAXBException e) {
            log.error("jaxb write expcetion", e);
            throw new ParseException(e.getMessage(), 0);
        }

    }

    public JAXBContext getCtx() {
        return ctx;
    }

    public void setCtx(JAXBContext ctx) {
        this.ctx = ctx;
    }

    public boolean isFragment() {
        return fragment;
    }

    public void setFragment(boolean fragment) {
        this.fragment = fragment;
    }
}
