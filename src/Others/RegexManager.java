package Others;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexManager {

    public String convertPathToName(String path){
        Pattern pat = Pattern.compile("^.+[/]");
        Matcher mat = pat.matcher(path);
        return mat.replaceAll("");
    }

    public String convertNameToReadable(String name){
        Pattern pat = Pattern.compile("[.].+$");
        Matcher mat = pat.matcher(name);
        return mat.replaceAll("");
    }

    public String categoryToPath(String category){
        return "categories/" + category + "/";
    }

    public String getCategory(File file){
        String path = file.getPath();
        Pattern pat = Pattern.compile("categories/");
        Matcher mat = pat.matcher(path);
        path = mat.replaceFirst("");
        Pattern pat2 = Pattern.compile("[/].+$");
        Matcher mat2 = pat2.matcher(path);
        return mat2.replaceAll("");
    }
}
