package com.Search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Asriel
 */
public class InvertedIndex {
    public static void main(String[] args) {
        String directory = "src/main/resources/hyatt-k";
        createInvertedIndex(directory);
        readInvertedIndex("src/main/resources/InvertedIndex.txt");
    }

    public static void createInvertedIndex(String directory){
        String[] fileNames = getFileNames(directory);
        HashMap<String, ArrayList<Object[]>> invertedIndex =  createIndex(fileNames);
        outputInvertedIndex(invertedIndex);
    }

    public static String[] getFileNames(String directory){
        ArrayList<String> filePath = new ArrayList<>();

        File dir = new File(directory);
        String[] list = dir.list();

        assert list != null;
        for (String fileName: list){
            File file = new File(directory + "\\" + fileName);
            if (file.isDirectory()){
                filePath.addAll(Arrays.asList(getFileNames(directory + "\\" + fileName)));
            } else {
                filePath.add(directory + "\\" + fileName);
            }
        }
        return filePath.toArray(new String[0]);
    }

    @SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
    public static HashMap<String, ArrayList<Object[]>> createIndex(String[] fileNames){
        HashMap<String, ArrayList<Object[]>> wordIndex = new HashMap<>();
        for (String file: fileNames){
            String[] words = getText(file);
            int wordPosition = 0;
            for (String word: words){
                wordPosition += 1;
                if (wordIndex.containsKey(word)){
                    int index = isContain(wordIndex.get(word), file);
                    if (index != -1){
                        wordIndex.get(word).get(index)[1] = (int)wordIndex.get(word).get(index)[1]+1;
                        ArrayList<Integer> pos = (ArrayList<Integer>)(wordIndex.get(word).get(index)[2]);
                        pos.add(wordPosition);
                    } else {
                        wordIndex.get(word).add(new Object[3]);
                        int len = wordIndex.get(word).size();
                        wordIndex.get(word).get(len-1)[0] = file;
                        wordIndex.get(word).get(len-1)[1] = 1;
                        ArrayList<Integer> pos = new ArrayList<>();
                        pos.add(wordPosition);
                        wordIndex.get(word).get(len-1)[2] = pos;
                    }
                } else {
                    ArrayList<Object[]> list = new ArrayList<>();
                    wordIndex.put(word, list);
                    list.add(new Object[3]);
                    list.get(0)[0] = file;
                    list.get(0)[1] = 1;
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(wordPosition);
                    list.get(0)[2] = pos;
                }
            }
        }
        return wordIndex;
    }

    public static String[] getText(String filePath){
        File file = new File(filePath);
        ArrayList<String> text = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(file);
            boolean isText = false;
            while (fileScanner.hasNextLine()){
                if ("".equals(fileScanner.nextLine())){
                    isText = true;
                }
                if (isText){
                    String next;
                    while (fileScanner.hasNext()) {
                        next = fileScanner.next().replaceAll("\\pP|\\pS|\\pC|\\pN|\\pZ", " ");
                        for (String word:next.split(" ")){
                            if (!" ".equals(word) && word.length() > 1) {
                                text.add(word);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return text.toArray(new String[0]);
    }

    public static int isContain(ArrayList<Object[]> list, String file){
        for (int i=0; i<list.size(); i++){
            if (list.get(i)[0].equals(file)) {
                return i;
            }
        }
        return -1;
    }

    public static void outputInvertedIndex(HashMap<String, ArrayList<Object[]>> invertedIndex){
        String file = "src/main/resources/InvertedIndex.txt";
        try {
            FileWriter writer = new FileWriter(file);

            for (String word: invertedIndex.keySet()){
                writer.write(word + " ");
                for (Object[] list: invertedIndex.get(word)){
                    ArrayList<Integer> pos = (ArrayList<Integer>)(list[2]);
                    writer.write("(" + list[0] + ";" + list[1] + ";");
                    for (int i=0; i<pos.size()-1; i++){
                        writer.write(pos.get(i) + "|");
                    }
                    writer.write(pos.get(pos.size()-1)+"");
                    writer.write("),");
                }
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<Object[]>> readInvertedIndex(String fileName){
        File file = new File(fileName);
        Scanner indexFileScanner = null;
        try {
            indexFileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, ArrayList<Object[]>> invertedIndex = new HashMap<>();

        String line;
        while (Objects.requireNonNull(indexFileScanner).hasNextLine()){
            line = indexFileScanner.nextLine();
            String word = line.split(" ")[0];
            ArrayList<Object[]> wordIndex = new ArrayList<>();

            String[] records = line.split(" ")[1].split(",");
            for (String record: records){
                Object[] index = new Object[3];
                String[] split = record.substring(1, record.length() - 1).split(";");
                index[0] = split[0];
                index[1] = Integer.parseInt(split[1]);
                ArrayList<Integer> position = new ArrayList<>();
                for (String pos: split[2].split("\\|")){
                    position.add(Integer.parseInt(pos));
                }
                index[2] = position;
                wordIndex.add(index);
            }
            invertedIndex.put(word, wordIndex);
        }
        return invertedIndex;
    }
}
