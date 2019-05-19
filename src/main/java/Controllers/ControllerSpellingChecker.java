package Controllers;

import Others.Buttons;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
    private Buttons buttons;
    @FXML
    ListView<String> suggestionsList1;
    @FXML
    ListView<String> suggestionsList2;
    @FXML
    Text SuggestionsText;
    @FXML
    AnchorPane spellingPane;
    private TextArea textAreaToCheck;
    private HashMap<String, List<String>> misspelledWords;
    private List<String> matchStringList1;
    private List<String> matchStringList2;
    private  List<RuleMatch> matchList;

    public ControllerSpellingChecker(String name, Controller previousController, TextArea textAreaToCheck) {
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
                int a=Math.min(10,misspelledWords.get(tmp).size());
                for(int i=0;i<a;i++){
                    matchStringList2.add(misspelledWords.get(tmp).get(i));
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
                    String tmp=textAreaToCheck.getText().substring(l,r);
                    if (!misspelledWords.containsKey(tmp))
                        misspelledWords.put(tmp,a.getSuggestedReplacements());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MISSPELLED WORDS:");
        for(String x: misspelledWords.keySet()){
            matchStringList1.add(x);
        }
        suggestionsList1.setItems(FXCollections.observableList(matchStringList1));
    }

}
