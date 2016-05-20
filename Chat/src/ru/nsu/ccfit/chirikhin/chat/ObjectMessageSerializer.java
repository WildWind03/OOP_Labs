package ru.nsu.ccfit.chirikhin.chat;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectMessageSerializer implements MessageSerializer {
    private static final Logger logger = Logger.getLogger(ObjectMessageSerializer.class.getName());
    private final ObjectInputStream objectInputStream;

    public ObjectMessageSerializer(InputStream inputStream) throws IOException {
        if (null == inputStream) {
            throw new NullPointerException("Input stream is null");
        }

        objectInputStream = new ObjectInputStream(inputStream);
    }

    @Override
    public void close() throws IOException {
        try {
            objectInputStream.close();
        } catch (IOException e) {
            logger.error("Closing connection");
        }
    }

    @Override
    public Message serialize() throws ClassNotFoundException, SAXException, IOException {
        Object object = null;
        try {
            object = objectInputStream.readObject();
        } catch (IOException e) {
            logger.error("IO while reading object");
            throw e;

        }

        logger.info("Read message");

        if (!(object instanceof Message)) {
            throw new ClassCastException("Can not read! Object is not a message!");
        }

        return (Message) object;
    }
}
