package Others;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpellingChecker {
    @FXML
    ListView<String> suggestionsList;
    @FXML
    Text SuggestionsText;
    private TextArea textAreaToCheck;
    private HashMap<String,String> misspelledWords;
    private List<String> matchStringList;
    private  List<RuleMatch> matchList;
    public SpellingChecker(TextArea textAreaToCheck){
        this.textAreaToCheck=textAreaToCheck;
        misspelledWords= new HashMap<>();
        matchStringList=new ArrayList<>();
        suggestionsList=new ListView<>();
        prepareSuggestions();
        setUpSpellingScreen();
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
                        misspelledWords.put(tmp,a.getSuggestedReplacements().get(0));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MISSPELLED WORDS:");
        for(String x: misspelledWords.keySet()){
            String t=x+" -> "+misspelledWords.get(x);
            System.out.println(t);
            matchStringList.add(t);
        }
        suggestionsList.getItems().add("a");
//        suggestionsList.setItems(FXCollections.observableList(matchStringList));
    }
    private void setUpSpellingScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Scenes/SpellingWindow.fxml"));
            fxmlLoader.setController(this);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
             stage.setScene(new Scene(root1));
            stage.show();
        }catch (Exception ignored){}
    }
}
