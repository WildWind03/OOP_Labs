package ru.nsu.ccfit.chirikhin.chat.server;

import org.apache.log4j.Logger;

import java.io.OutputStream;

public class ObjectMessageSender implements MessageSender {
    private static final Logger logger = Logger.getLogger(ObjectMessageSender.class.getName());

    public ObjectMessageSender(OutputStream outputStream) {
        if (null == outputStream) {
            throw new NullPointerException("Null reference instead of OutputStream");
        }
    }

    @Override
    public void send(Message message) {
        if (null == message) {
            throw new NullPointerException("Null reference instead of message");
        }
    }
}
