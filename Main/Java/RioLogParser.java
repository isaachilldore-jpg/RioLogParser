import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
//string LogFilePath ="C:/Users/Public/Documents/FRC/Log Files/DSLogs /2026_09_23 19_56_36 Wed";
import java.util.Dictionary;

public class RioLogParser{

    public static final Path LogFilePath = Path.of("C:/Users/Public/Documents/FRC/Log Files/DSLogs/2026_09_23 19_56_36 Wed.dsevents");

    public static void main(String[] args){
        String text =readFile();
        int currentIndex =0;
        Dictionary[String,String] logSection;

        int leftBracketIndex =text.indexOf("<",currentIndex);
        int rightBracketIndex = text.indexOf(">",leftBracketIndex);

        

    }


    public static String readFile(){
        try {
            String content = Files.readString(LogFilePath,Charset.forName("ISO-8859-1")); // Reads entire file as String (UTF-8 by default)
            return content;
            } catch (IOException error) {
        error.printStackTrace();
        return "error";
    } 
}
}