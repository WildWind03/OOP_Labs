package ru.nsu.ccfit.chirikhin.chat.client.model;

import org.apache.log4j.Logger;
import ru.nsu.ccfit.chirikhin.chat.client.view.ClientViewController;

public class ClientListFailedAnswer implements ServerEvent{
    private static final Logger logger = Logger.getLogger(ClientListFailedAnswer.class.getName());

    private final String reason;
    public ClientListFailedAnswer(String reason) {
        if (null == reason) {
            throw new NullPointerException("Reason can not be null");
        }
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void process(ClientViewController clientViewController) {
        if (null == clientViewController) {
            throw new NullPointerException("ClientViewController can not be null");
        }

        clientViewController.onClientListFailedAnswer(this);
    }
}
