package sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexManager {

    String convertPathToName(String path){
        Pattern pat = Pattern.compile("^.+[/]");
        Matcher mat = pat.matcher(path);
        return mat.replaceAll("");
    }

    String convertNameToReadable(String name){
        Pattern pat = Pattern.compile("[.].+$");
        Matcher mat = pat.matcher(name);
        return mat.replaceAll("");
    }

    String categoryToPath(String category){
        return "categories/" + category + "/";
    }
}
