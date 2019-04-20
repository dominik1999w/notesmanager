package sample;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ControllerAreYouSure extends Controller {

    public ControllerAreYouSure(String name, Controller previousController, String command, File file) {
        super(name,previousController);
        this.command = command;
        this.file = file;
        this.category = Controller.regexManager.getCategory(this.file);
    }

    private File file;
    private String category;
    private String command;

    @FXML
    TextField question;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       question.setText("Are you sure you want to remove " + file.getName() + " from  category " + category + "?" );
    }

    public void yes() throws IOException {
        if(command.equals("remove")){
            remove();
            super.goBack();
        } else {
            System.out.println("FATAL ERROR - no such command found");
        }
    }

    public void no() throws IOException {
        super.goBack();
    }

    private void remove(){
        String path = file.getPath();
        try {
            Files.delete(Paths.get(path));
            System.out.println("REMOVED: " + path);
        } catch (IOException e) {
            System.out.println("FAILED to remove: " + path);
        }
    }

}
