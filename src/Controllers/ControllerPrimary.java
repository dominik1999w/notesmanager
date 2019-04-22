package Controllers;

import Management.StageMaster;
import Others.FilesTreeView;
import Others.GridManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private GridManager gridManager = new GridManager();
    private File selectedDir;
    private File selectedFile;
    @FXML
    TreeView<File> FilesView; // FilesTreeView
    @FXML
    TextArea DisplayFileText;
    @FXML
    TextField DisplayTitle;
    @FXML
    ToggleButton rename;
    @FXML
    ToggleButton fullSize;

    @Override //run on start
    public void initialize(URL url, ResourceBundle resourceBundle){
        FilesTreeView filesTreeViewClass = new FilesTreeView(); //call FilesTreeView constructor
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

        gridFiles.setHgap(25);
        gridFiles.setVgap(25);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinViewportHeight(1);
        scrollPane.setMinViewportWidth(1);
    }

    public void clickCategoryControllerButton() throws IOException {
        //Creating new ControllerFiles and loading it
        Controller.stageMaster.loadNewScene(new ControllerFiles("/Scenes/Files.fxml", this));
    }

    public void clickNewCategoryButton() throws IOException {
        Controller.stageMaster.loadNewScene(new ControllerCategories("/Scenes/Categories.fxml", this));
    }

    public void displayFile(){
            TreeItem<File> item = FilesView.getSelectionModel().getSelectedItem();
            if(item != null && item.getValue().isDirectory()){
                selectedDir = item.getValue();
                displayGridFilesView(item.getValue());
            }
            if(item != null && item.getValue().isFile()) { //if clicked on file display content
                selectedFile = item.getValue();
                System.out.println(selectedFile);
                displayFile(item.getValue());
            }
        }

    private void displayFile(File file) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        displayTitle(file.getName());
        DisplayFileText.setText("");
        for(String tmp: lines){
            DisplayFileText.appendText(tmp + "\n");
        }
    }

    ///////////////////////////////////////////////
    //grid Files stuff

    @FXML
    GridPane gridFilesFactory;
    @FXML
    GridPane gridFiles;
    @FXML
    ScrollPane scrollPane;

    private void displayGridFilesView(File dir){
        selectedDir = new File(dir.getPath());
        gridManager = new GridManager(selectedDir, gridFilesFactory, gridFiles, scrollPane);
        gridManager.adjustGridFilesView(dir,4); //globalne
    }

    public void openFileInEditMode(MouseEvent event) throws IOException {
        Node clicked = event.getPickResult().getIntersectedNode();
        if(GridPane.getColumnIndex(clicked) != null && GridPane.getRowIndex(clicked) != null){
            selectedFile = new File(clicked.getId());
            DisplayFileText.setPrefWidth(500); //globalne
            DisplayFileText.setVisible(true);
            gridManager.adjustGridFilesView(selectedDir,2); //globalne
            displayFile(new File(selectedDir.getPath() + "/" + clicked.getId()));
            System.out.println(clicked.getId());
        }
    }

    public void MakeTextAreaFullSize(){
        if(selectedFile != null){
            gridFiles.setVisible(!gridFiles.isVisible());
            scrollPane.setVisible(!scrollPane.isVisible());
            if (DisplayFileText.getPrefWidth() == 940) { //globalne
                DisplayFileText.setPrefWidth(500); //globalne
            } else {
                DisplayFileText.setPrefWidth(940); //globalne
            }
        }
    }

    /////////////////////////////////////////////////////////////

    private void displayTitle(String name){
        DisplayTitle.setText(regexManager.convertNameToReadable(name));
    }

    public void openFileNatively() throws InterruptedException {
        if(selectedFile == null)  return;
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
                System.out.println("FAILED to open file:");
            }
        }
    }

    public void removeFile() throws IOException {
        try{
            File file = FilesView.getSelectionModel().getSelectedItem().getValue();
            Controller.stageMaster.loadNewScene(new ControllerAreYouSure("/Scenes/AreYouSure.fxml", this,"remove",file));
        } catch (NullPointerException e){
            System.out.println("You can't remove nothing. ;)");
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

    public void submitRename(){
        DisplayTitle.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String category = regexManager.getCategory(selectedFile);
                String pathToCategory = regexManager.categoryToPath(category);
                String newPath = pathToCategory.concat(String.valueOf(DisplayTitle.getCharacters()));
                try {
                    Path path = Files.move((Paths.get(selectedFile.getPath())), Paths.get(newPath));
                    File newFile = new File(path.toString());
                    selectedFile = newFile;
                    System.out.println("RENAMED to: " + selectedFile.getName());
                    rename();
                } catch (IOException e) {
                    System.out.println("FAILED to rename: " + selectedFile.getName());
                }
            }
        });
    }

    public void rename(){
        DisplayTitle.setEditable(!DisplayTitle.isEditable());
        DisplayTitle.setDisable(!DisplayTitle.isDisabled());
        System.out.println("SET title editable to: " + DisplayTitle.isEditable());
        if(DisplayTitle.isDisabled())
            displayTitle(selectedFile.getName());
        if(rename.isSelected() != DisplayTitle.isEditable())
            rename.setSelected(!rename.isSelected());
    }

}