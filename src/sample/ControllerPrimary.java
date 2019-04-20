package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPrimary extends Controller implements Initializable{

    public ControllerPrimary(String name, Stage stage) {
        this.name = name;
        this.previousController = this;
        Controller.stageMaster = new StageMaster(stage); //One and only stageMaster
    }

    private File selectedFile;
    @FXML
    TreeView<File> FilesView; // FilesTreeView
    @FXML
    TextArea DisplayFileText;
    @FXML
    TextArea DisplayTitle;

    @Override //run on start
    public void initialize(URL url, ResourceBundle resourceBundle){
        FilesTreeView  filesTreeViewClass = new FilesTreeView(); //call FilesTreeView constructor
        List<String> categories = getCategories(true);
        List<TreeItem<File>> roots = new LinkedList<>();

        for(String categoryName : categories){
            roots.add(filesTreeViewClass.createNode(new File(categoryName)));
        }

        TreeItem<File> connectRoots = new TreeItem<>(null);
        connectRoots.getChildren().addAll(roots);
        FilesView.setRoot(connectRoots);
        FilesView.setShowRoot(false);
        FilesView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() { //replace path with file name
            @Override
            public TreeCell<File> call(TreeView<File> fileTreeView) {
                return new TreeCell<File>(){
                    @Override
                    public void updateItem(File file, boolean empty){
                        super.updateItem(file,empty);
                        setText((file==null||empty) ? "" : file.getName());
                    }
                };
            }
        });
    }


    public void clickCategoryControllerButton() throws IOException {
        //Creating new ControllerFiles and loading it
        Controller.stageMaster.loadNewScene(new ControllerFiles("Files.fxml", this));
    }

    public void clickNewCategoryButton() throws IOException {
        Controller.stageMaster.loadNewScene(new ControllerCategories("Categories.fxml", this));
    }

    public void displayFile(){
        FilesView.setOnMouseClicked(mouseEvent -> {
            TreeItem<File> item = FilesView.getSelectionModel().getSelectedItem();
            if(item != null && item.getValue().isFile()){ //if clicked on file display content
                selectedFile = item.getValue();
                System.out.println(selectedFile);
                List<String> lines = new ArrayList<>();
                String line;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(item.getValue()));
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                displayTitle(item.getValue().getName());
                DisplayFileText.setText("");
                for(String tmp: lines){
                    DisplayFileText.appendText(tmp + "\n");
                }
            }
        });
    }

    public void displayTitle(String name){
        DisplayTitle.setText(regexManager.convertNameToReadable(name));
    }

    public void openFileNatively() throws InterruptedException {
        if(selectedFile==null)  return;
        Thread cur=Thread.currentThread();
        ExternalThread tr = new ExternalThread();
        if(Desktop.isDesktopSupported()){
            tr.start();
        }

    }

    class ExternalThread extends Thread{
        @Override
        public void run(){
            try {
                Desktop.getDesktop().open(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeFile(){
        TreeItem<File> item = FilesView.getSelectionModel().getSelectedItem();
        if(item != null){
            String path = item.getValue().getPath();
            try {
                Files.delete(Paths.get(path)); Controller.stageMaster.refresh(this);
                System.out.println("REMOVED: " + path);
            } catch (IOException e) {
                System.out.println("FAILED to remove: " + path);
            }
        }
    }

    public void edit(){
        DisplayFileText.setEditable(!DisplayFileText.isEditable());
        System.out.println("SET editable to: " + DisplayFileText.isEditable());
    }

    public void save(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
            fileOutputStream.write(DisplayFileText.getText().getBytes());
            System.out.println("SAVED to: " + selectedFile.getName());
        } catch (FileNotFoundException e) {
            System.out.println("FAILED to find: " + selectedFile.getName());
        } catch (IOException e) {
            System.out.println("PROBLEM with saving");
        }
    }

}