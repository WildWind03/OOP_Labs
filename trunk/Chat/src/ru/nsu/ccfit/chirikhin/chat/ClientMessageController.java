package ru.nsu.ccfit.chirikhin.chat;

import org.apache.log4j.Logger;
import ru.nsu.ccfit.chirikhin.chat.client.Client;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;


public class ClientMessageController extends Observable implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientMessageController.class.getName());

    private final Client client;
    private final BlockingQueue<Message> messages;

    public ClientMessageController(BlockingQueue<Message> messages, Client client) {
        this.client = client;
        this.messages = messages;
    }

    public void handleTextMessage(ServerTextMessage serverMessage) {
        setChanged();
        notifyObservers(serverMessage);
    }

    public void handleNewClientServerMessage(NewClientServerMessage newClientServerMessage) {
        setChanged();
        notifyObservers(newClientServerMessage);
    }

    public void handleErrorServerMessage(ServerErrorAnswer serverErrorAnswer) {
        setChanged();
        notifyObservers(serverErrorAnswer);
    }

    public void handleServerClientListMessage(ServerClientListMessage serverClientListMessage) {
        setChanged();
        notifyObservers(serverClientListMessage);
    }

    public void handleSuccessLoginServerMessage(ServerSuccessLoginAnswer serverSuccessMessage) {
        client.setSessionId(serverSuccessMessage.getSessionId());
        setChanged();
        notifyObservers(serverSuccessMessage);
    }

    public void handleSuccessServerAnswer(ServerSuccessAnswer serverSuccessAnswer) {
        setChanged();
        notifyObservers(serverSuccessAnswer);
    }

    public void handleUserLogoutMessage(ClientLogoutServerMessage message) {
        setChanged();
        notifyObservers(message);
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Message message = messages.take();

                logger.info("New message has been received!");

                if (!(message instanceof ServerMessage)) {
                    throw new ClassCastException("Invalid message");
                }

                ServerMessage serverMessage = (ServerMessage) message;

                serverMessage.process(this);

            }
        } catch (InterruptedException e) {
            logger.error("Interrupt");
        }
    }
}
