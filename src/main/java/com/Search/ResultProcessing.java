package com.Search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ResultProcessing {
    public static void main(String[] args) {
        System.out.println(" a".matches("\\s.*"));
    }

    public static HashMap<String, Integer> initFileInfo(){
        HashMap<String, Integer> infoMap = new HashMap<>(17);
        infoMap.put("Message-ID:", 0);
        infoMap.put("Date:", 1);
        infoMap.put("From:", 2);
        infoMap.put("To:", 3);
        infoMap.put("Subject:", 4);
        infoMap.put("Mime-Version:", 5);
        infoMap.put("Content-Type:", 6);
        infoMap.put("Content-Transfer-Encoding:", 7);
        infoMap.put("X-From:", 8);
        infoMap.put("X-To:", 9);
        infoMap.put("X-cc:", 10);
        infoMap.put("X-bcc:", 11);
        infoMap.put("X-Folder:", 12);
        infoMap.put("X-Origin:", 13);
        infoMap.put("X-FileName:", 14);
        infoMap.put("Cc:", 15);
        infoMap.put("Bcc:", 16);
        return infoMap;
    }

    public static String[] getFileInfo(String fileName){
        HashMap<String, Integer> infoMap = initFileInfo();
        String[] result = new String[]{"","","","","","","","","","","","","","","","","","",""};
        File file = new File(fileName);
        int index = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if ("".equals(line)){
                    break;
                }
                String[] lineSplit = line.split(" ");
                if (lineSplit.length>1 && infoMap.containsKey(lineSplit[0])) {
                    index = infoMap.get(lineSplit[0]);
                    result[index] = line.replace(lineSplit[0]+" ", "");
                } else {
                    result[index] = result[index] + line;
                }
            }

            while (scanner.hasNextLine()){
                String text = scanner.nextLine();
                if (("").equals(result[17])) {
                    if (!("").equals(text)) {
                        result[17] = text;
                    }
                } else {
                    result[17] = result[17] + "\n" + text;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (result[4].matches("\\s.*")){
            result[4] = result[4].substring(1);
        }
        result[3] = result[3].replace(" ", "");
        result[3] = result[3].replace("\t", "");
        return result;
    }
}
