package ru.nsu.ccfit.chirikhin.chat.service;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class MessageSenderFactory {
    private static final Logger logger = Logger.getLogger(MessageSenderFactory.class.getName());

    private MessageSenderFactory() {

    }

    public static MessageSender createMessageSender(ProtocolName protocolName, OutputStream outputStream) throws IOException {
        if (null == protocolName || null == outputStream) {
            throw new NullPointerException("Null reference in constructor");
        }

        MessageSender messageSender = null;

        switch (protocolName) {
            case XML:
                messageSender = new XMLMessageSender(outputStream);
                break;
            case SERIALIZE:
                messageSender = new ObjectMessageSender(outputStream);
                break;
        }

        return messageSender;
    }


}
