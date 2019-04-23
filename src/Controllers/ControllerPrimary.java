package Controllers;

import Management.StageMaster;
import Others.FilesTreeView;
import Others.GridManager;
import Others.RegexManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    TreeView<File> FilesView;
    @FXML
    TextArea DisplayFileText;
    @FXML
    TextField DisplayTitle;
    @FXML
    ToggleButton rename;
    @FXML
    ToggleButton edit;
    @FXML
    ToggleButton fullSize;
    @FXML
    Button save;
    @FXML
    Button remove;
    @FXML
    Button natively;
    @FXML
    Button close;
    @FXML
    GridPane gridFilesFactory;
    @FXML
    GridPane gridFiles;
    @FXML
    ScrollPane scrollPane;
    @FXML
    SplitPane splitPane;

    @Override
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
                        setText((file == null || empty) ? "" : file.getName());
                    }
                };
            }
        });

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinViewportHeight(1);
        scrollPane.setMinViewportWidth(1);
        endWork();
    }

// NAVIGATION ----------------------------------------------------------------------

    public void clickCategoryControllerButton() throws IOException {
        //Creating new ControllerFiles and loading it
        Controller.stageMaster.loadNewScene(new ControllerFiles("/Scenes/Files.fxml", this));
    }

    public void clickNewCategoryButton() throws IOException {
        Controller.stageMaster.loadNewScene(new ControllerCategories("/Scenes/Categories.fxml", this));
    }

// FILE OPTIONS --------------------------------------------------------------------

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

// DISPLAY FILE OPTIONS ------------------------------------------------------------

    public void rename(){
        if(selectedFile != null) {
            DisplayTitle.setEditable(!DisplayTitle.isEditable());
            DisplayTitle.setDisable(!DisplayTitle.isDisabled());
            System.out.println("SET title editable to: " + DisplayTitle.isEditable());
            if (DisplayTitle.isDisabled())
                displayTitle(selectedFile.getName());
            if (rename.isSelected() != DisplayTitle.isEditable())
                rename.setSelected(!rename.isSelected());
        }
    }

    public void submitRename(){
        DisplayTitle.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String category = RegexManager.getCategory(selectedFile);
                String pathToCategory = RegexManager.categoryToPath(category);
                String newPath = pathToCategory.concat(String.valueOf(DisplayTitle.getCharacters()));
                String extension = RegexManager.getExtension(selectedFile.getName());
                if(extension.length() > 0)
                    newPath = newPath.concat(".").concat(extension);
                try {
                    Path path = Files.move((Paths.get(selectedFile.getPath())), Paths.get(newPath));
                    File newFile = new File(path.toString());
                    selectedFile = newFile;
                    System.out.println("RENAMED to: " + selectedFile.getName());
                    rename();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("FAILED to rename: " + selectedFile.getName());
                }
            }
        });
    }

    private void displayTitle(String name){
        DisplayTitle.setText(RegexManager.convertNameToReadable(name));
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

    public void close(){
        endWork();
        gridFiles.setVisible(false);
        scrollPane.setVisible(false);
    }

// DISPLAY FILE --------------------------------------------------------------------


    private void displayGridFilesView(File dir){
        endWork();
        selectedDir = new File(dir.getPath());
        gridManager = new GridManager(selectedDir, gridFilesFactory, gridFiles, scrollPane);
        gridManager.adjustGridFilesView(dir,4); //globalne
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
            if(item!=null)
            System.out.println(item.getValue());
        }

    private void displayFile(File file) {
        startWork();
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

    public void openFileInEditMode(MouseEvent event) throws IOException {
        startWork();
        Node clicked = event.getPickResult().getIntersectedNode();
        if(clicked == null) return ;
        if(GridPane.getColumnIndex(clicked) != null && GridPane.getRowIndex(clicked) != null){
            selectedFile = new File("categories/" + selectedDir.getName() + "/" + clicked.getId());
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


    public void endWork(){ //called when changing category
        DisplayFileText.setVisible(false);
        DisplayTitle.setText("");
        rename.setSelected(false);
        rename.setDisable(true);
        edit.setSelected(false);
        edit.setDisable(true);
        fullSize.setDisable(true);
        fullSize.setSelected(false);
        save.setDisable(true);
        remove.setDisable(true);
        natively.setDisable(true);
        close.setDisable(true);
        close.setVisible(false);
        selectedFile = null;
    }

    private void startWork(){ //called when
        rename.setSelected(false);
        rename.setDisable(false);
        edit.setSelected(false);
        edit.setDisable(false);
        fullSize.setDisable(false);
        fullSize.setSelected(false);
        save.setDisable(false);
        remove.setDisable(false);
        natively.setDisable(false);
        close.setDisable(false);
        close.setVisible(true);
    }

}