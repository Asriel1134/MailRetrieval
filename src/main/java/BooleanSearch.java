import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Asriel
 */
public class BooleanSearch {

    public static void main(String[] args) {
        HashMap<String, ArrayList<Object[]>> invertedIndex = InvertedIndex.readInvertedIndex("src/main/resources/InvertedIndex.txt");
        System.out.println(Arrays.toString(singleSearch("Order", invertedIndex)));
        System.out.println(Arrays.toString(andSearch("Order", "accessories", invertedIndex)));
        System.out.println(Arrays.toString(binaryWordsSearch("Order", "accessories", invertedIndex)));
        System.out.println(Arrays.toString(orSearch("Order", "accessories", invertedIndex)));
        System.out.println(Arrays.toString(andNotSearch( "accessories","Order", invertedIndex)));
    }

    public static ArrayList<Object[]> getRecord(String word, HashMap<String, ArrayList<Object[]>> invertedIndex) {
        return invertedIndex.getOrDefault(word, null);
    }

    public static String[] singleSearch(String word, HashMap<String, ArrayList<Object[]>> invertedIndex){
        ArrayList<Object[]> records = getRecord(word, invertedIndex);
        ArrayList<String> result = new ArrayList<>();
        for (Object[] re: records){
            result.add((String) re[0]);
        }
        return result.toArray(new String[0]);
    }

    public static String[] andSearch(String wordA, String wordB, HashMap<String, ArrayList<Object[]>> invertedIndex){
        ArrayList<Object[]> recordsA = getRecord(wordA, invertedIndex);
        ArrayList<Object[]> recordsB = getRecord(wordB, invertedIndex);
        ArrayList<String> result = new ArrayList<>();
        for (Object[] reA: recordsA){
            for (Object[] reB: recordsB){
                    if (Objects.equals((String) reA[0], (String) reB[0])) {
                        result.add((String) reA[0]);
                    }
            }
        }
        return result.toArray(new String[0]);
    }

    public static String[] orSearch(String wordA, String wordB, HashMap<String, ArrayList<Object[]>> invertedIndex){
        ArrayList<Object[]> recordsA = getRecord(wordA, invertedIndex);
        ArrayList<Object[]> recordsB = getRecord(wordB, invertedIndex);
        ArrayList<String> result = new ArrayList<>();
        for (Object[] reA: recordsA){
            result.add((String) reA[0]);
        }
        for (Object[] reB: recordsB){
            if (!result.contains((String)reB[0])){
                result.add((String) reB[0]);
            }
        }
        return result.toArray(new String[0]);
    }

    public static String[] andNotSearch(String wordA, String wordB, HashMap<String, ArrayList<Object[]>> invertedIndex){
        ArrayList<Object[]> recordsA = getRecord(wordA, invertedIndex);
        ArrayList<Object[]> recordsB = getRecord(wordB, invertedIndex);
        ArrayList<String> result = new ArrayList<>();
        for (Object[] reA: recordsA){
            result.add((String) reA[0]);
        }
        for (Object[] reB: recordsB){
            result.remove((String) reB[0]);
        }
        return result.toArray(new String[0]);
    }

    public static String[] binaryWordsSearch(String wordA, String wordB, HashMap<String, ArrayList<Object[]>> invertedIndex){
        ArrayList<Object[]> recordsA = getRecord(wordA, invertedIndex);
        ArrayList<Object[]> recordsB = getRecord(wordB, invertedIndex);
        ArrayList<String> result = new ArrayList<>();

        for (Object[] reA: recordsA){
            for (Object[] reB: recordsB){
                if (Objects.equals((String) reA[0], (String) reB[0])) {
                    ArrayList<Integer> posA = (ArrayList<Integer>)reA[2];
                    ArrayList<Integer> posB = (ArrayList<Integer>)reB[2];
                    for (Integer position: posA){
                        if (posB.contains(position+1)){
                            result.add((String) reA[0]);
                        }
                    }
                }
            }
        }
        return result.toArray(new String[0]);
    }
}
