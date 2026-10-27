import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.io.FileWriter;
//string LogFilePath ="C:/Users/Public/Documents/FRC/Log Files/DSLogs /2026_09_23 19_56_36 Wed";


public class RioLogParser{
    public static final String logTime="2026_09_23 19_56_36 Wed";
    public static final Path LogFilePath = Path.of("C:/Users/Public/Documents/FRC/Log Files/DSLogs/"+logTime+".dsevents");
    public static final String newLogFilePath = "C:/Users/Public/Documents/FRC/Log Files/DSLogs/"+logTime+".txt";
    

    public static void main(String[] args){
        System.err.println(SelectFile());
        String finishedText="";
        String text =readFile();
        int currentIndex =0;
        HashMap<String,String> logSection= new HashMap<>();
       /* File newLogFile = new File(newLogFilePath);
        try{
            if (newLogFile.createNewFile()) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("File already exists.");
            }
        }catch (IOException e){
            e.printStackTrace();
        }*/
        LogPositionData data;
        while((data = readNext(currentIndex, text)) != null){

            if(data.insideText.equals("TagVersion")){
                finishedText=finishedText.concat(addData(finishedText, logSection));
                logSection.clear();
            }
            currentIndex=data.endPosition;
            logSection.put(data.insideText,data.outsideText);
        }
                    
        finishedText=finishedText.concat(addData(finishedText, logSection));
        writeFile(finishedText, newLogFilePath);
    }

    public static String SelectFile(){
        /*File startFile = new File("C:\\Users\\Public\\Documents\\FRC");
        try{
            System.out.println(startFile.getAbsolutePath());
            System.out.println(startFile.exists());
            ProcessBuilder pb = new ProcessBuilder("explorer.exe","/open,"+startFile.getAbsolutePath());
        pb.start();
        
        
        }catch(Exception e){
            e.printStackTrace();
        }*/
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select file");

        File startFile=new File("C:\\Users\\Public\\Documents\\FRC\\Log Files\\DSLogs");
        fileChooser.setCurrentDirectory(startFile);

        int result =fileChooser.showOpenDialog(null);
        
        if(result==JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            String filePath=selectedFile.getAbsolutePath();
            return filePath;
            
        }else{
            System.err.println("File selection canceled");
            return "There was an error";
        }

        
    }

    public static String addData(String finishedText,HashMap<String,String> data){
        String lineToAdd="";
        if(data.containsKey("time")){
            lineToAdd= lineToAdd.concat(data.get("time"));
        }
        if(data.containsKey("message")){
            lineToAdd=lineToAdd.concat(" <message> "+data.get("message"));
        }
        if(data.containsKey("details")){
            lineToAdd=lineToAdd.concat(" <details> "+data.get("details"));
        }

        lineToAdd=lineToAdd.concat("\n");
        //System.out.println(lineToAdd);
        return lineToAdd;
    }

    public static void writeFile(String text, String filepath){
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(text);
            writer.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static LogPositionData readNext(int startPosition,String text){
        int leftBracketIndex =text.indexOf("<",startPosition);
        if(leftBracketIndex<0){
            return null;
        }
        int rightBracketIndex = text.indexOf(">",leftBracketIndex);
        if(rightBracketIndex<0){
            return null;
        }
        int endPosition = text.indexOf("<",rightBracketIndex)-1;
        if(endPosition<0){
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