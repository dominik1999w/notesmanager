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
import javafx.scene.layout.AnchorPane;
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
    TreeView<File> treeView;
    @FXML
    TextField fileTitleArea;
    @FXML
    TextArea textAreaFullScreen; //visibility changeable
    @FXML
    TextArea textAreaHalfScreen;

    @FXML
    ToggleButton rename;
    @FXML
    ToggleButton edit;
    @FXML
    ToggleButton fullSize;
    @FXML
    Button close;

    @FXML
    Button save;
    @FXML
    Button remove;
    @FXML
    Button natively;

    @FXML
    GridPane gridFilesFactory2;
    @FXML
    GridPane gridFilesFactory4;
    @FXML
    GridPane gridFiles2;
    @FXML
    GridPane gridFiles4;
    @FXML
    ScrollPane scrollPane;
    @FXML
    ScrollPane scrollPaneFull; //visibility changeable
    @FXML
    SplitPane splitPaneEditMode; //visibility changeable

    @FXML
    AnchorPane textPane;
    @FXML
    AnchorPane smallGridPane;
    @FXML
    AnchorPane treePane;


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
        treeView.setRoot(connectRoots);
        treeView.setShowRoot(false);
        treeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() { //replace path with file name
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

        scrollPaneFull.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneFull.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        smallGridPane.setMinWidth(510);
        smallGridPane.setMaxWidth(511);

        treePane.setMinWidth(360);
        treePane.setMaxWidth(380);

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
            File file = treeView.getSelectionModel().getSelectedItem().getValue();
            Controller.stageMaster.loadNewScene(new ControllerAreYouSure("/Scenes/AreYouSure.fxml", this,"remove",file));
        } catch (NullPointerException e){
            System.out.println("You can't remove nothing. ;)");
        }
    }

// DISPLAY FILE OPTIONS ------------------------------------------------------------

    public void rename(){
        if(selectedFile != null) {
            fileTitleArea.setEditable(!fileTitleArea.isEditable());
            fileTitleArea.setDisable(!fileTitleArea.isDisabled());
            System.out.println("SET title editable to: " + fileTitleArea.isEditable());
            if (fileTitleArea.isDisabled())
                displayTitle(selectedFile.getName());
            if (rename.isSelected() != fileTitleArea.isEditable())
                rename.setSelected(!rename.isSelected());
        }
    }

    public void submitRename(){
        fileTitleArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String category = RegexManager.getCategory(selectedFile);
                String pathToCategory = RegexManager.categoryToPath(category);
                String newPath = pathToCategory.concat(String.valueOf(fileTitleArea.getCharacters()));
                String extension = RegexManager.getExtension(selectedFile.getName());
                if(extension.length() > 0)
                    newPath = newPath.concat(".").concat(extension);
                try {
                    Path path = Files.move((Paths.get(selectedFile.getPath())), Paths.get(newPath));
                    File newFile = new File(path.toString());
                    selectedFile = newFile;
                    System.out.println("RENAMED to: " + selectedFile.getName());
                    rename();
                    stageMaster.refresh(this);
                    returnBeforeRefresh();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("FAILED to rename: " + selectedFile.getName());
                }
            }
        });
    }

    private void displayTitle(String name){
        fileTitleArea.setText(RegexManager.convertNameToReadable(name));
    }

    public void edit(){
        textAreaFullScreen.setEditable(!textAreaFullScreen.isEditable());
        textAreaHalfScreen.setEditable(!textAreaHalfScreen.isEditable());
        System.out.println("SET editable to: " + textAreaFullScreen.isEditable());
    }

    public void save(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
            if(textAreaFullScreen.isVisible()){
                fileOutputStream.write(textAreaFullScreen.getText().getBytes());
            } else {
                fileOutputStream.write(textAreaHalfScreen.getText().getBytes());
            }
            System.out.println("SAVED to: " + selectedFile.getName());
        } catch (FileNotFoundException e) {
            System.out.println("FAILED to find: " + selectedFile.getName());
        } catch (IOException e) {
            System.out.println("PROBLEM with saving");
        }
    }

    public void close(){
        endWork();
        textAreaFullScreen.setVisible(false);
        splitPaneEditMode.setVisible(false);
    }

// DISPLAY FILE --------------------------------------------------------------------


    private void displayGridFilesView(File dir){
        endWork();
        textPane.setVisible(true);
        selectedDir = new File(dir.getPath());
        gridManager = new GridManager(selectedDir, gridFilesFactory4, gridFiles4, scrollPaneFull);
        gridManager.adjustGridFilesView(dir,4); //globalne
    }

    public void openFileTree(){
            TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
            if(item != null && item.getValue().isDirectory()){
                selectedDir = item.getValue();
                displayGridFilesView(item.getValue());
            }
            if(item != null && item.getValue().isFile()) { //if clicked on file display content
                selectedFile = item.getValue();
                selectedDir = new File(RegexManager.getCategoryPath(selectedFile));
                System.out.println(selectedFile.getPath());
                displayGridFilesView(selectedDir);
                displayFile();
            }
            if(item != null)
                System.out.println(item.getValue());
    }

    public void openFileInEditMode(MouseEvent event) throws IOException {
        startWork();
        Node clicked = event.getPickResult().getIntersectedNode();
        if(clicked == null) return ;
        if(GridPane.getColumnIndex(clicked) != null && GridPane.getRowIndex(clicked) != null){
            selectedFile = new File("categories/" + selectedDir.getName() + "/" + clicked.getId());
            displayFile();
            System.out.println(selectedFile);
        }
    }

    private void displayFile() {
        startWork();

        splitPaneEditMode.setVisible(true);
        gridManager.setGridFilesFactory(gridFilesFactory2);
        gridManager.setGridPane(gridFiles2);
        gridManager.setScrollPane(scrollPane);
        gridManager.adjustGridFilesView(selectedDir,2); //globalne

        System.out.println(selectedFile.getPath());

        List<String> lines = new ArrayList<>();
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        displayTitle(selectedFile.getName());
        textAreaHalfScreen.setVisible(true);
        textAreaHalfScreen.setText("");
        for(String tmp: lines){
            textAreaHalfScreen.appendText(tmp + "\n");
        }
    }


    public void MakeTextAreaFullSize(){
        if(selectedFile != null){
            gridFiles2.setVisible(!gridFiles2.isVisible());
            scrollPane.setVisible(!scrollPane.isVisible());
            if (textAreaFullScreen.isVisible()) {
                textAreaFullScreen.setVisible(false);
                splitPaneEditMode.setVisible(true);
                prepareSmallScreen();
            } else {
                scrollPaneFull.setVisible(false);
                splitPaneEditMode.setVisible(false);
                prepareFullScreen();
            }
        }
    }

    private void prepareFullScreen(){
        textAreaFullScreen.setVisible(true);
        textAreaFullScreen.setText(textAreaHalfScreen.getText());
    }

    private void prepareSmallScreen(){
        if(textAreaFullScreen.getText() != ""){
            textAreaHalfScreen.setText(textAreaFullScreen.getText());
        }
    }


    public void endWork(){ //called when changing category
        fileTitleArea.setText("");
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

        textAreaFullScreen.setVisible(false);
        textAreaFullScreen.clear();
        scrollPaneFull.setVisible(false);

        splitPaneEditMode.setVisible(false);
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

        textAreaFullScreen.setEditable(false);
        fileTitleArea.setEditable(false);
        fileTitleArea.setDisable(true);
    }

    private void returnBeforeRefresh(){
        if(selectedDir != null){
            displayGridFilesView(selectedDir);
            if(selectedFile != null)
                displayFile();
        }
    }

}