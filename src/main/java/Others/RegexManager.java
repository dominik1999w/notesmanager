package Others;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        return name.substring(name.indexOf('/') + 1);
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

    static int isState(File file){
        String path = file.getPath();
        String firstDir = path.substring(0, path.indexOf('/'));
        if(firstDir.equals("catHelp")){
            return Integer.valueOf(path.substring(path.indexOf('/') + 1));
        } else if(firstDir.equals("search")) {
            return -2; //from search option
        } else return -1; //regular category
    }

    public static ArrayList<File> searchForPatternInFiles(String pattern, List<String> files){
        ArrayList<File> result = new ArrayList<>();
        Pattern pat = Pattern.compile(pattern);
        BufferedReader bufferedReader = null;
        String line;

        for(String path : files){

            try {
                bufferedReader = new BufferedReader(new FileReader(new File("categories/" + path)));
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher mat = pat.matcher(line);
                    if (mat.find()) {
                        result.add(new File("categories/" + path));
                        break;
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                try {
                    assert bufferedReader != null;
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
