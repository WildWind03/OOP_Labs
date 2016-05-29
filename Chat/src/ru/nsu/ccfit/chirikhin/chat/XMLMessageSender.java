package ru.nsu.ccfit.chirikhin.chat;

import org.apache.log4j.Logger;

import java.io.*;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class XMLMessageSender implements MessageSender {
    private static final Logger logger = Logger.getLogger(XMLMessageSender.class.getName());
    private final XMLMessageParser xmlMessageParser = new XMLMessageParser();
    private final OutputStreamWriter outputStreamWriter;
    private final OutputStream outputStream;

    public XMLMessageSender(OutputStream outputStream) {
        if (null == outputStream) {
            throw new NullPointerException("Null in constructor");
        }

        outputStreamWriter = new OutputStreamWriter(outputStream);
        this.outputStream = outputStream;
    }

    @Override
    public void send(Message message) {
        if (null == message) {
            throw new NullPointerException("Null instead of message");
        }

        String xml = xmlMessageParser.createXMLFromMessage(message);
        byte[] xmlBytes = xml.getBytes(Charset.forName("UTF-8"));
        int size = xmlBytes.length;
        try {
            outputStream.write(size);
            outputStream.write(xmlBytes);

        } catch (IOException e) {
            logger.error("Error while sending");
        }
    }

    @Override
    public void close() throws IOException {

    }
}