import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
//string LogFilePath ="C:/Users/Public/Documents/FRC/Log Files/DSLogs /2026_09_23 19_56_36 Wed";


public class RioLogParser{

    public static final Path LogFilePath = Path.of("C:/Users/Public/Documents/FRC/Log Files/DSLogs/2026_09_23 19_56_36 Wed.dsevents");

    public static void main(String[] args){
        String text =readFile();
        int currentIndex =0;
        HashMap<String,String> logSection= new HashMap<>();

        while(true){
            LogPositionData data= readNext(currentIndex, text);
            if(data==null){
                System.out.println(logSection);
                break;
            }
            if(data.insideText.equals("TagVersion")){
                System.out.println(logSection);
                logSection.clear();
            }
            currentIndex=data.endPosition;
            logSection.put(data.insideText,data.outsideText);
        }
        
        
    }

    public static LogPositionData readNext(int startPosition,String text){
        int leftBracketIndex =text.indexOf("<",startPosition);
        if(leftBracketIndex==-1){
            return null;
        }
        int rightBracketIndex = text.indexOf(">",leftBracketIndex);
        if(rightBracketIndex==-1){
            return null;
        }
        int endPosition = text.indexOf("<",rightBracketIndex)-1;
        if(endPosition==-1){
            return null;
        }
        String insideText = text.substring(leftBracketIndex+1,rightBracketIndex);
        String outSideText = text.substring(rightBracketIndex+1,endPosition);
        LogPositionData data = new LogPositionData();
        data.leftBracketIndex=leftBracketIndex;
        data.rightBracketIndex=rightBracketIndex;
        data.endPosition=endPosition;
        data.insideText=insideText;
        data.outsideText=outSideText;
        return data;
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