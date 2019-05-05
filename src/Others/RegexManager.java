package Others;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexManager {

    public static String convertPathToName(String path){
        Pattern pat = Pattern.compile("^.+[/]");
        Matcher mat = pat.matcher(path);
        return mat.replaceAll("");
    }

    public static String convertNameToReadable(String name){
        Pattern pat = Pattern.compile("[.][^.]+$");
        Matcher mat = pat.matcher(name);
        return mat.replaceAll("");
    }

    public static String getExtension(String name){
        Pattern pat = Pattern.compile("^.+[.]");
        Matcher mat = pat.matcher(name);
        if(!mat.find())
            return "";
        return mat.replaceAll("");
    }

    public static String convertFullPathToShort(String name){
        return name.substring(name.indexOf('/')+1);
    }

    public static String categoryToPath(String category){
        return "categories/" + category + "/";
    }

    public static String getCategory(File file){
        String path = file.getPath();
        Pattern pat = Pattern.compile("categories/");
        Matcher mat = pat.matcher(path);
        path = mat.replaceFirst("");
        Pattern pat2 = Pattern.compile("[/].+$");
        Matcher mat2 = pat2.matcher(path);
        return mat2.replaceAll("");
    }

    public static String getCategoryPath(File file){
        String category = getCategory(file);
        return categoryToPath(category);
    }
}
