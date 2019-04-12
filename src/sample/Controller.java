package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends TreeItem<File> implements Initializable{
    private Launcher launcher;
    private Stage stage;
    @FXML
    TreeView<File> FilesView; // FilesTreeView
    @FXML
    Button newFileButton;
    @FXML
    VBox rootBox;
    @FXML
    private void clickNewFileButton(ActionEvent action) throws IOException { //change the scene to CategoryChooser scene
        //TODO
    }
    @Override //run on start
    public void initialize(URL url, ResourceBundle resourceBundle){
        FilesTreeView  filesTreeViewClass = new FilesTreeView(); //call FilesTreeView constructor
        TreeItem<File> root = filesTreeViewClass.createNode(new File("categories/a")); //get files from a
        TreeItem<File> root2 = filesTreeViewClass.createNode(new File("categories/b")); //get files from b
        TreeItem<File> root3 = filesTreeViewClass.createNode(new File("categories/c")); //get files from c
        TreeItem<File> connectRoots = new TreeItem<>(null);
        connectRoots.getChildren().addAll(root,root2,root3);
        FilesView.setRoot(connectRoots);
        FilesView.setShowRoot(false);
    }

}