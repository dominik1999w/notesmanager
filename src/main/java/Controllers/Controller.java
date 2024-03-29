package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Others.RegexManager;
import Management.StageMaster;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public abstract class Controller implements Initializable {

    public static StageMaster stageMaster;
    public static String mainCategory = "./.categories";

    String name;
    Controller previousController;


    public String getName() {
        return this.name;
    }

    public Controller getPreviousController(){
        return this.previousController;
    }


    Controller() {}

    Controller(String name, Controller previousController) {
        this.name = name;
        this.previousController = previousController;
    }

    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

    @FXML
    void goBack() throws IOException {
        Controller.stageMaster.loadPreviousScene();
    }


    List<String> getCategories(boolean pathOrName){ // true for paths, false for just names
        List<String> categories = new LinkedList<>();
        List<String> names = new LinkedList<>();
        try {
            categories =  Files.find(Paths.get(mainCategory), 1,
                    (path, basicFileAttributes) -> {
                        if(path.toString().equals(mainCategory)) return false;
                        File file = path.toFile();
                        return file.isDirectory();
                    }).map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e + "Reading categories' directories failed.");
        }
        if(pathOrName){

            categories.sort(String::compareTo);
            return categories;
        } else {
            for(String path : categories){
                names.add(RegexManager.convertPathToName(path));
            }
        }
        names.sort(String::compareTo);
        return names;
    }




}
