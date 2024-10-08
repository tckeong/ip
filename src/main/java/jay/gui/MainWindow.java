package jay.gui;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jay.Jay;


/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jay jay;
    private boolean isExit = false;

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/user.png")));
    private final Image jayImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/jayBot.png")));

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setJay(Jay jay) {
        this.jay = jay;
        dialogContainer.getChildren().add(DialogBox.getJayDialog(jay.greet(), jayImage, false));
        dialogContainer.getChildren().add(DialogBox.getJayDialog(jay.showHighPriorityTasks(), jayImage,
                false));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        if (isExit) {
            return;
        }

        String input = userInput.getText();

        if (input.equals("bye")) {
            isExit = true;
        }

        try {
            String response = jay.handleInput(input);
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getJayDialog(response, jayImage, false)
            );
        } catch (ResponseMessageException e) {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getJayDialog(e.getMessage(), jayImage, true)
            );
        }

        userInput.clear();
    }
}
