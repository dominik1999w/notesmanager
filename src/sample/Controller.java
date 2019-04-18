package sample;

import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Controller implements Initializable {

    static StageMaster stageMaster;

    String name;
    Controller previousController;


    public String getName() {
        return this.name;
    }

    public Controller getPreviousController(){
        return this.previousController;
    }

    public void setPreviousScene(Controller controller){
        this.previousController = controller;
    }


    public Controller() { }

    public Controller(String name) {
        this.name = name;
    }

    public Controller(String name, Controller previousController) {
        this.name = name;
        this.previousController = previousController;
    }


    public List<String> getCategories(boolean pathOrName){ // true for paths, false for just names
        List<String> categories = new LinkedList<>();
        List<String> names = new LinkedList<>();
        try {
            categories =  Files.find(Paths.get("categories/"), 1,
                    (path, basicFileAttributes) -> {
                        if(path.toString().equals("categories")) return false;
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
            Pattern pat = Pattern.compile("^.+[/]");
            Matcher mat;
            for(String s : categories){
                mat = pat.matcher(s);
                names.add(mat.replaceAll(""));
            }
        }
        names.sort(String::compareTo);
        return names;
    }


    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

}
