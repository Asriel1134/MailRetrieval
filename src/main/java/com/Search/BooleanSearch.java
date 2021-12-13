package com.Search;

import java.util.*;

/**
 * @author Asriel
 */
public class BooleanSearch {
    HashMap<String, ArrayList<Object[]>> invertedIndex;

    public BooleanSearch(){
        invertedIndex = InvertedIndex.readInvertedIndex("src/main/resources/InvertedIndex.txt");
    }

    public static void main(String[] args) {
        BooleanSearch booleanSearch = new BooleanSearch();
            System.out.print(Arrays.toString(booleanSearch.search("A B AND (C OR (D NOT E)) AND F")));
        System.out.print((booleanSearch.search("Outlook Today")).length);
    }

    public String[] search(String searchText){
        ArrayList<Object[]> searchResult = searchHandler(searchText);
        searchResult.sort(new searchResultComparator());
        ArrayList<String> result = new ArrayList<>();
        for (Object[] re: searchResult){
            result.add((String) re[0]);
        }
        return result.toArray(new String[0]);
    }

    public ArrayList<Object[]> searchHandler(String searchText){
        ArrayList<Object[]> result;
        // 分解词项
        Scanner scanner = new Scanner(searchText);
        ArrayList<String> searchTextSplit = new ArrayList<>();
        int index = 0;
        int braCount = 0;
        String nextWord;

        while (scanner.hasNext()){
            nextWord = scanner.next();

            if (nextWord.charAt(0)=='('){
                braCount += 1;
                String word;
                StringBuilder text = new StringBuilder(nextWord);
                while (braCount!=0){
                    word = scanner.next();
                    text.append(" ").append(word);
                    if (word.charAt(0)=='('){
                        String temp = word;
                        while (temp.charAt(0)=='(') {
                            braCount += 1;
                            temp = temp.substring(1);
                        }
                    }
                    if (word.charAt(word.length()-1)==')'){
                        String temp = word;
                        while (temp.charAt(temp.length()-1)==')') {
                            braCount -= 1;
                            temp = temp.substring(0, temp.length()-1);
                        }
                    }
                }
                searchTextSplit.add(text.substring(1, text.length()-1));
                nextWord = scanner.next();
            }

            if (!isKeyWord(nextWord)){
                if (searchTextSplit.size()<index+1){
                    searchTextSplit.add(nextWord);
                } else {
                    searchTextSplit.set(index, searchTextSplit.get(index) + " " + nextWord);
                }
            } else {
                index += 1;
                searchTextSplit.add(nextWord);
                index += 1;
            }
        }
        String[] words = searchTextSplit.toArray(new String[0]);

        if (searchTextSplit.size() == 1){
            String[] split = words[0].split(" ");
            if (split.length == 1){
                return singleSearch(split[0]);
            } else if (split.length == 2){
                return binaryWordsSearch(split[0], split[1]);
            }
        }

        int searchIndex = 0;
        result = searchHandler(words[0]);
        while (searchIndex < words.length-1){
            if ("AND".equals(words[searchIndex + 1])){
                result = and(result, searchHandler(words[searchIndex+2]));
                searchIndex += 2;
                continue;
            }
            if ("OR".equals(words[searchIndex + 1])){
                result = or(result, searchHandler(words[searchIndex+2]));
                searchIndex += 2;
                continue;
            }
            if ("NOT".equals(words[searchIndex + 1])){
                result = not(result, searchHandler(words[searchIndex+2]));
                searchIndex += 2;
            }
        }

        return result;
    }

    public boolean isKeyWord(String word){
        return ("AND".equals(word) || "OR".equals(word) || "NOT".equals(word));
    }

    public ArrayList<Object[]> searchHandler1(String searchText){
        ArrayList<Object[]> result = new ArrayList<>();
        boolean and = false;
        boolean or = false;
        boolean not = false;

        if (searchText.contains(" AND ")){
            and = true;
            String[] searchTextSplit = searchText.split(" AND ");
            result = searchHandler1(searchTextSplit[0]);
            for (int i=0; i<searchTextSplit.length-1; i++){
                result = and(result, searchHandler1(searchTextSplit[i+1]));
            }
        }
        if (searchText.contains(" OR ")){
            or = true;
            String[] searchTextSplit = searchText.split(" OR ");
            result = searchHandler1(searchTextSplit[0]);
            for (int i=0; i<searchTextSplit.length-1; i++){
                result = or(result, searchHandler1(searchTextSplit[i+1]));
            }
        }
        if (searchText.contains(" NOT ")){
            not = true;
            String[] searchTextSplit = searchText.split(" NOT ");
            result = searchHandler1(searchTextSplit[0]);
            for (int i=0; i<searchTextSplit.length-1; i++){
                result = not(result, searchHandler1(searchTextSplit[i+1]));
            }
        }
        if (!(and||or||not)){
            String[] searchTextSplit = searchText.split(" ");
            if (searchTextSplit.length==1){
                result = singleSearch(searchText);
            } else {
                result = binaryWordsSearch(searchTextSplit[0], searchTextSplit[1]);
            }
        }
        return result;
    }

    public ArrayList<Object[]> and(ArrayList<Object[]> resultA, ArrayList<Object[]> resultB){
        ArrayList<Object[]> result = new ArrayList<>();
        ArrayList<String> appeared = new ArrayList<>();

        for (Object[] reA: resultA){
            for (Object[] reB: resultB){
                if (Objects.equals(reA[0], reB[0]) && !appeared.contains((String)reA[0])) {
                    Object[] temp = new Object[2];
                    temp[0] = reA[0];
                    temp[1] = (int)reA[1] + (int)reB[1];
                    result.add(temp);
                    appeared.add((String)reA[0]);
                }
            }
        }
        return result;
    }

    public ArrayList<Object[]> or(ArrayList<Object[]> resultA, ArrayList<Object[]> resultB){
        ArrayList<Object[]> result = new ArrayList<>(resultA);
        ArrayList<String> appeared = new ArrayList<>();

        for (Object[] re: resultA){
            appeared.add((String)re[0]);
        }

        for (Object[] reB: resultB){
            if (!appeared.contains((String)reB[0])){
                result.add(reB);
            }
        }
        return result;
    }

    public ArrayList<Object[]> not(ArrayList<Object[]> resultA, ArrayList<Object[]> resultB){
        ArrayList<Object[]> result = new ArrayList<>();
        ArrayList<String> appeared = new ArrayList<>();

        for (Object[] re: resultB){
            appeared.add((String)re[0]);
        }

        for (Object[] reA: resultA){
            if (!appeared.contains((String)reA[0])){
                result.add(reA);
            }
        }
        return result;
    }

    public ArrayList<Object[]> getRecord(String word) {
        return invertedIndex.getOrDefault(word, null);
    }

    public ArrayList<Object[]> singleSearch(String word){
        ArrayList<Object[]> records = getRecord(word);
        return (records!=null)?records: new ArrayList<>();
    }

    public ArrayList<Object[]> binaryWordsSearch(String wordA, String wordB){
        ArrayList<Object[]> recordsA = getRecord(wordA);
        ArrayList<Object[]> recordsB = getRecord(wordB);
        ArrayList<Object[]> result = new ArrayList<>();

        if (recordsA == null || recordsB == null){
            return new ArrayList<>();
        }

        for (Object[] reA: recordsA){
            for (Object[] reB: recordsB){
                boolean flag = false;
                if (Objects.equals(reA[0], reB[0])) {
                    flag = true;
                    ArrayList<Integer> posA = (ArrayList<Integer>)reA[2];
                    ArrayList<Integer> posB = (ArrayList<Integer>)reB[2];
                    for (Integer position: posA){
                        if (posB.contains(position+1)){
                            Object[] temp = new Object[2];
                            temp[0] = reA[0];
                            temp[1] = (int)reA[1] + (int)reB[1];
                            result.add(temp);
                        }
                    }
                }
                if (flag){
                    break;
                }
            }
        }
        return result;
    }
}

class searchResultComparator implements Comparator<Object[]> {
    @Override
    public int compare(Object[] o1, Object[] o2) {
        return Integer.compare((int) o1[1], (int) o2[1]);
    }
}