package ru.nsu.ccfit.chirikhin.chat.client;

import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.util.Optional;

public class EnterUsernameView {
    private static final Logger logger = Logger.getLogger(EnterUsernameView.class.getName());

    public String show() {
        TextInputDialog dialog = new TextInputDialog("Wind");
        dialog.setTitle("Windogram");
        dialog.setHeaderText("Windogram");
        dialog.setContentText("Please, enter your nickname:");

        dialog.setResultConverter((dialogButton) -> {
            if (dialogButton == ButtonType.OK) {
                return dialog.getEditor().getText();
            } else {
                return null;
            }
        });

        Optional<String> str = dialog.showAndWait();

        if (!str.isPresent()) {
            return null;
        }

        return str.get();
    }
}
