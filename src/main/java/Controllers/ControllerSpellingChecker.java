package Controllers;

import Others.Buttons;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerSpellingChecker extends Controller {

    private final ControllerPrimary controllerPrimary;
    private final Stage stage;
    private Buttons buttons;
    @FXML
    ListView<String> suggestionsList1;
    @FXML
    ListView<String> suggestionsList2;
    @FXML
    AnchorPane spellingPane;
    @FXML
    Button acceptSuggestion;
    class tuple{
        int l;
        int r;
        List<String> suggestedWords;

        public tuple(int l, int r, List<String> suggestedReplacements) {
            this.l=l;
            this.r=r;
            this.suggestedWords=suggestedReplacements;
        }
    }
    private TextArea textAreaToCheck;
    private HashMap<String, tuple> misspelledWords;
    private List<String> matchStringList1;
    private List<String> matchStringList2;
    private  List<RuleMatch> matchList;

    public ControllerSpellingChecker(String name, Controller previousController, TextArea textAreaToCheck, ControllerPrimary controllerPrimary, Stage stage) {
        this.stage=stage;
        this.controllerPrimary=controllerPrimary;
        this.name = name;
        this.previousController = previousController;
        this.textAreaToCheck=textAreaToCheck;
        misspelledWords= new HashMap<>();
        matchStringList1=new ArrayList<>();
        matchStringList2=new ArrayList<>();
        buttons=new Buttons();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("../Images/arrow.png"));
        ImageView arrow=new ImageView(image);
        arrow.setImage(image);
        arrow.setPreserveRatio(true);
        arrow.setFitHeight(50);
        arrow.setFitWidth(70);
        arrow.setY(160);
        arrow.setX(225);
        spellingPane.getChildren().add(arrow);
        suggestionsList1.setOnMouseClicked(mouseEvent->{
            String tmp=suggestionsList1.getSelectionModel().getSelectedItems().get(0);
            if(suggestionsList1.getSelectionModel().getSelectedItems().get(0)!=null){
                matchStringList2.clear();
                int a=Math.min(10,misspelledWords.get(tmp).suggestedWords.size());
                for(int i=0;i<a;i++){
                    matchStringList2.add(misspelledWords.get(tmp).suggestedWords.get(i));
                }
                suggestionsList2.setItems(FXCollections.observableList(matchStringList2));
            }
        });
        prepareSuggestions();
    }
    private void prepareSuggestions(){
        JLanguageTool lTool=new JLanguageTool(new AmericanEnglish());
        try {
            matchList=lTool.check(textAreaToCheck.getText());
            matchList.forEach(a-> {
                int l=a.getFromPos();
                int r=a.getToPos();
                if(r-l>2) {
                    String tmp = textAreaToCheck.getText().substring(l, r);
                    if (!misspelledWords.containsKey(tmp)) {
                        misspelledWords.put(tmp, new tuple(l,r,a.getSuggestedReplacements()));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String x: misspelledWords.keySet()){
            matchStringList1.add(x);
        }
        suggestionsList1.setItems(FXCollections.observableList(matchStringList1));
    }
    public void importSuggestion(){
        String d=textAreaToCheck.getText();
        String original=suggestionsList1.getSelectionModel().getSelectedItems().get(0);
        String tmp=suggestionsList2.getSelectionModel().getSelectedItems().get(0);
        if(controllerPrimary.textAreaFullScreen.isVisible())
            controllerPrimary.textAreaFullScreen.setText(d.replace(original,tmp));
        else
            controllerPrimary.textAreaHalfScreen.setText(d.replace(original,tmp));
        matchStringList1.remove(original);
        matchStringList2.clear();
        suggestionsList2.setItems(FXCollections.observableList(matchStringList2));
        suggestionsList1.setItems(FXCollections.observableList(matchStringList1));
        if(matchStringList1.isEmpty())
            stage.close();
    }
}
