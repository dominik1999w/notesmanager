package Controllers;

import Management.StageMaster;
import Others.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerPrimary extends Controller implements Initializable{

    public ControllerPrimary(String name, Stage stage) {
        buttons = new Buttons();
        this.name = name;
        this.previousController = this;
        Controller.stageMaster = new StageMaster(stage); //One and only stageMaster
    }

    private Buttons buttons;
    private GridManager gridManager = new GridManager();
    private File selectedDir;
    private File selectedFile;
    private List<String> autoPaths = new LinkedList<>();

    @FXML
    private TreeView<File> treeView;
    @FXML
    private TextField fileTitleArea;
    @FXML
    private TextArea textAreaFullScreen; //visibility changeable
    @FXML
    private TextArea textAreaHalfScreen;
    @FXML
    private ToggleButton rename;
    @FXML
    private TextField searchText;
    @FXML
    private ToggleButton edit;
    @FXML
    private ToggleButton fullSize;
    @FXML
    private Button close;
    @FXML
    private Button save;
    @FXML
    private Button remove;
    @FXML
    private Button natively;
    @FXML
    private GridPane gridFilesFactory2;
    @FXML
    private GridPane gridFilesFactory4;
    @FXML
    private GridPane gridFiles2;
    @FXML
    private GridPane gridFiles4;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane scrollPaneFull; //visibility changeable
    @FXML
    private SplitPane splitPaneEditMode; //visibility changeable
    @FXML
    private AnchorPane textPane;
    @FXML
    private AnchorPane smallGridPane;
    @FXML
    private AnchorPane treePane;
    @FXML
    private TextField autoFillText;
    @FXML
    private Text findCounter;
    @FXML
    private AnchorPane optionsBarAnchor;
    @FXML
    private BorderPane optionsBarBorder;
    @FXML
    private Button newFileButton;
    @FXML
    private Button newCategoryButton;
    @FXML
    private Text titleText;
    @FXML
    private Button spellingCheckerButton;
    @FXML
    private AnchorPane rootBox;


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

        save.setGraphic(buttons.setCustomImage("save"));
        edit.setGraphic(buttons.setCustomImage("edit"));
        rename.setGraphic(buttons.setCustomImage("rename"));
        newFileButton.setGraphic(buttons.setCustomImage("newFile"));
        remove.setGraphic(buttons.setCustomImage("remove"));
        newCategoryButton.setGraphic(buttons.setCustomImage("newCategory"));
        natively.setGraphic(buttons.setCustomImage("external"));
        state.setGraphic(buttons.setCustomImage("state"));
        Tooltip a = new Tooltip("Spelling Checker!");
        setTooltipTimer(a);
        spellingCheckerButton.setTooltip(a);
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

        prepareAutoTextField();
        prepareLearningStates();
        endWork();
    }

    private void setTooltipTimer(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);
            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(200)));
        } catch (Exception e) {//e.printStackTrace();
            System.out.println("NIE WAŻNY WYJĄTEK ;))))))))))))"); }
    }

// NAVIGATION ----------------------------------------------------------------------

    @FXML
    public void clickCategoryControllerButton() throws IOException {
        //Creating new ControllerFiles and loading it
        Controller.stageMaster.loadNewScene(new ControllerFiles("/Scenes/Files.fxml", this));
    }

    @FXML
    public void clickNewCategoryButton() throws IOException {
        Controller.stageMaster.loadNewScene(new ControllerCategories("/Scenes/Categories.fxml", this));
    }

// FILE OPTIONS --------------------------------------------------------------------

    @FXML
    public void openFileNatively() {
        if(selectedFile == null)  return;
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

    @FXML
    public void removeFile() throws IOException {
        try{
            autoPaths.clear();
            Controller.stageMaster.loadNewScene(new ControllerAreYouSure("/Scenes/AreYouSure.fxml", this,"remove",selectedFile));
            prepareAutoTextField();
        } catch (NullPointerException e){
            System.out.println("You can't remove nothing. ;)");
        }
    }

// DISPLAY FILE OPTIONS ------------------------------------------------------------

    @FXML
    public void rename(){
        if(selectedFile != null) {
            autoPaths.clear();
            fileTitleArea.setEditable(!fileTitleArea.isEditable());
            fileTitleArea.setDisable(!fileTitleArea.isDisabled());
            fileTitleArea.setVisible(!fileTitleArea.isVisible());
            titleText.setVisible(!titleText.isVisible());
            System.out.println("SET title editable to: " + fileTitleArea.isEditable());
            if (fileTitleArea.isDisabled())
                displayTitle(selectedFile.getName());
            if (rename.isSelected() != fileTitleArea.isEditable())
                rename.setSelected(!rename.isSelected());
            prepareAutoTextField();
        }
    }

    @FXML
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
                    selectedFile = new File(path.toString());
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
        titleText.setText(RegexManager.convertNameToReadable(name));
    }

    @FXML
    public void edit(){
        textAreaFullScreen.setEditable(!textAreaFullScreen.isEditable());
        textAreaHalfScreen.setEditable(!textAreaHalfScreen.isEditable());
        System.out.println("SET editable to: " + textAreaFullScreen.isEditable());
    }

    @FXML
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

    @FXML
    public void close(){
        endWork();
        textAreaFullScreen.setVisible(false);
        splitPaneEditMode.setVisible(false);
    }

// DISPLAY FILE --------------------------------------------------------------------


    private void displayGridFilesView(File dir){
        endWork();
        startAnything();
        textPane.setVisible(true);
        selectedDir = new File(dir.getPath());
        gridManager = new GridManager(gridFilesFactory4, gridFiles4, scrollPaneFull,this);
        gridManager.adjustGridFilesView(dir,4); //globalne
    }

    @FXML
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

    @FXML
    public void openFileInEditMode(MouseEvent event) {
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

        displayState(selectedFile);

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
        textAreaHalfScreen.selectRange(0,0);
    }

    @FXML
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
        if(!textAreaFullScreen.getText().equals("")){
            textAreaHalfScreen.setText(textAreaFullScreen.getText());
        }
    }


    private void endWork(){ //called when changing category //some text
        fileTitleArea.setText("");
        titleText.setText("");
        spellingCheckerButton.setDisable(true);
        spellingCheckerButton.setVisible(false);
        rename.setSelected(false);
        rename.setDisable(true);
        edit.setSelected(false);
        edit.setDisable(true);
        fullSize.setDisable(true);
        fullSize.setSelected(false);
        fullSize.setVisible(false);
        save.setDisable(true);
        remove.setDisable(true);
        natively.setDisable(true);
        close.setDisable(true);
        close.setVisible(false);

        textAreaFullScreen.setVisible(false);
        textAreaFullScreen.clear();
        scrollPaneFull.setVisible(false);
        splitPaneEditMode.setVisible(false);

        searchText.setDisable(true);
        searchText.setVisible(false);
        findCounter.setText("");
        searchText.setText("");
        rememberedWord = "";

        statePane.setVisible(false);
        state.setDisable(true);
        state.setSelected(false);
        stateDisplay.setVisible(false);

    }

    private void startWork(){ //some text
        rename.setSelected(false);
        rename.setDisable(false);
        spellingCheckerButton.setDisable(false);
        spellingCheckerButton.setVisible(true);
        edit.setSelected(false);
        edit.setDisable(false);
        fullSize.setDisable(false);
        fullSize.setSelected(false);
        fullSize.setVisible(true);
        save.setDisable(false);
        remove.setDisable(false);
        natively.setDisable(false);
        close.setDisable(false);
        close.setVisible(true);

        textAreaFullScreen.setEditable(false);
        fileTitleArea.setEditable(false);
        fileTitleArea.setDisable(true);
        fileTitleArea.setVisible(false);
        titleText.setVisible(true);

        searchText.setDisable(false);
        searchText.setVisible(true);
        statePane.setVisible(false);
        state.setDisable(false);
        state.setSelected(false);
    }

    private void startAnything(){
        close.setVisible(true);
        close.setDisable(false);
    }

    private void returnBeforeRefresh(){
        if(selectedDir != null){
            displayGridFilesView(selectedDir);
            if(selectedFile != null)
                displayFile();
        }
    }

//CONFIGURE SEARCH BAR AND SEARCH BUTTON

    private void prepareAutoTextField(){
        for(int i = 0; i < treeView.getRoot().getChildren().size(); i++) {
            for(int j = 0; j < treeView.getTreeItem(i).getChildren().size(); j++){
                String s = String.valueOf(treeView.getTreeItem(i).getChildren().get(j).getValue());
                if(!autoPaths.contains(RegexManager.convertFullPathToShort(s)))
                    autoPaths.add(RegexManager.convertFullPathToShort(s));
            }
        }
        TextFields.bindAutoCompletion(autoFillText, autoPaths);
    }

    @FXML
    public void autoOpenFile(){
        autoFillText.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                String s = String.valueOf(autoFillText.getCharacters());
                if(!autoPaths.contains(s)){
                    return; //invalid file name
                }
                selectedFile = new File("categories/" + s);
                File dir = new File(RegexManager.getCategoryPath(selectedFile));
                displayGridFilesView(dir);
                displayFile();
                autoFillText.setText("");
            }
        });
    }


// CONFIGURE FIND OPTIONS-------------------------------------------------------------

    private int counter = 0;
    private String rememberedWord = "";
    private List<Pair<Integer,Integer>> positions;

    @FXML
    public void findText() {
        searchText.setOnKeyReleased(event -> {
            if(searchText.getCharacters().length() == 0){
                textAreaHalfScreen.selectRange(0,0);
                findCounter.setText("");
            }
            else if(searchText.getCharacters().length() > 0){
                if (!rememberedWord.equals(String.valueOf(searchText.getCharacters()))) { //if a new word
                    counter = 0;
                    rememberedWord = String.valueOf(searchText.getCharacters());
                    positions = new LinkedList<>();
                    Pattern pattern = Pattern.compile(String.valueOf(searchText.getCharacters()));
                    Matcher matcher = pattern.matcher(textAreaHalfScreen.getText());
                    while (matcher.find()) {
                        positions.add(new Pair(matcher.start(), matcher.end()));
                    }
                    findCounter.setText(counter + "/" + positions.size());
                }
                if (event.getCode() == KeyCode.ENTER) { //go to the next found pattern
                    counter++;
                }
                if (counter == positions.size()) { //if the last one is reached
                    counter = 0;
                }if(textAreaFullScreen.isVisible()){
                    textAreaFullScreen.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick;");
                    if (positions.size() > 0) {
                        findCounter.setText(counter + 1 + "/" + positions.size());
                        textAreaFullScreen.selectRange(positions.get(counter).getKey(), positions.get(counter).getValue());
                    }
                }else{
                    textAreaHalfScreen.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick;");
                    if (positions.size() > 0) {
                        findCounter.setText(counter + 1 + "/" + positions.size());
                        textAreaHalfScreen.selectRange(positions.get(counter).getKey(), positions.get(counter).getValue());
                    }
                }
            }
        });

    }

// CONFIGURE SPELLING CHECKER OPTIONS-------------------------------------------------

    @FXML
    public void invokeSpellingChecker(){
        Controller controllerSpellingChecker=null;
        Stage s=new Stage();
        if(textAreaFullScreen.isVisible()) {
            controllerSpellingChecker = new ControllerSpellingChecker("/Scenes/SpellingWindow.fxml", this, textAreaFullScreen,this,s);
        }
        if(textAreaHalfScreen.isVisible()){
            controllerSpellingChecker = new ControllerSpellingChecker("/Scenes/SpellingWindow.fxml", this, textAreaHalfScreen,this,s);
        }
        StageMaster stageMaster = new StageMaster(s);
        stageMaster.setResizable(false);
        stageMaster.setName("Spelling App!");
        try {
            stageMaster.loadNewScene(controllerSpellingChecker);
        } catch (IOException e) {
            System.out.println("LOADING SPELLING CHECKER FAILED...");
        }
    }

    TextArea getTextAreaFullScreen() {
        return textAreaFullScreen;
    }

    TextArea getTextAreaHalfScreen() {
        return textAreaHalfScreen;
    }

    // LEARNING - STATE------------------------------------------------------------------

    @FXML
    Pane statePane;
    @FXML
    ToggleButton state;
    @FXML
    ImageView stateDisplay;
    @FXML
    RadioButton state0;
    @FXML
    RadioButton state1;
    @FXML
    RadioButton state2;
    @FXML
    private RadioButton state3;
    @FXML
    RadioButton state4;
    @FXML
    RadioButton state5;
    @FXML
    RadioButton state6;
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private HashMap<String,Integer> states = new HashMap<>(); //Strings are paths to files (short form)
    private int quantity = 7;
    private int defaultValue = 5;
    private String pathToStates = "src/main/resources/States/states";

    private void getStates(){

        try {
            FileInputStream fileInputStream = new FileInputStream(pathToStates);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            states = (HashMap<String, Integer>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            System.out.println("GETTING states OK");
        } catch(Exception e){
            System.out.println("GETTING states FAILED");
        }

        for(String s : autoPaths)
            if(!states.keySet().contains(s))
                states.put(s,defaultValue);

        //for(String path : states.keySet())
        //    System.out.println(path + " " + states.get(path) );
    }

    private void updateStates(String path, int state){
        getStates();

        if(states.keySet().contains(path))
            states.replace(path,state);
        else
            states.put(path,state);

        for(String s : autoPaths)
            if(!states.keySet().contains(s))
                states.put(s,defaultValue);

        commitUpdate();
    }

    private void commitUpdate(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathToStates);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(states);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("UPDATING states OK");
        } catch(Exception e){
            System.out.println("UPDATING states FAILED");
        }
    }

    private void prepareLearningStates(){
        radioButtons.add(state0);
        radioButtons.add(new RadioButton());
        radioButtons.add(state2);
        radioButtons.add(new RadioButton());
        radioButtons.add(state4);
        radioButtons.add(state5);
        radioButtons.add(state6);

        getStates();

        for(int i = 0; i < quantity; i++){
            final int id = i;
            radioButtons.get(i).setOnAction(event -> stateRadio(id,true));
        }
    }

    @FXML
    public void statePaneActivation(){
        System.out.println("LEARNING ACTIVATION!");
        stateRadio(states.get(RegexManager.convertFullPathToShort(selectedFile.getPath())),false);
        statePane.setVisible(!statePane.isVisible());
    }

    @FXML
    public void stateRadio(int id, boolean ifClose){
        System.out.println("stateRadio for: " + id);

        RadioButton radioButton = radioButtons.get(id);
        for(int i = 0; i < quantity; i++){
            if(i != id)
                radioButtons.get(i).setSelected(false);
        }
        radioButton.setSelected(true);
        updateStates(RegexManager.convertFullPathToShort(selectedFile.getPath()),id);
        if(ifClose){
            statePane.setVisible(false);
            displayState(selectedFile);
            displayGridFilesView(selectedDir);
            displayFile();
        }
    }

    private void displayState(File file){
        int state = states.get(RegexManager.convertFullPathToShort(file.getPath()));
        stateDisplay.setImage(new Image(getClass().getResourceAsStream("../States/" + state + ".png")));
        stateDisplay.setVisible(true);
    }

    public HashMap<String,Integer> getStatesMap(){
        return this.states;
    }

}