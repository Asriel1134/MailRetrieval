package com.Search;

import java.util.Arrays;
import java.util.Random;

public class Levenshtein {
    public static void main(String[] args) {
        String strA = buildString(150);
        String strB = buildString(150);
        int[][] result = levenshtein(strA, strB);
        for (int i = 0; i <= strA.length(); i++) {
            System.out.print(Arrays.toString(result[i]));
            System.out.print("\n");
        }
    }

    public static int[][] levenshtein(String strA, String strB){
        int[][] result = new int[strA.length()+1][strB.length()+1];
        for (int i=0; i<=strA.length(); i++){
            for (int j=0; j<=strB.length(); j++){
                if (Math.min(i, j) == 0){
                    result[i][j] = Math.max(i, j);
                } else {
                    int a3;
                    if (strA.charAt(i-1) == strB.charAt(j-1)){
                        a3 = 0;
                    } else {
                        a3 = 1;
                    }
                    result[i][j] = Math.min(result[i-1][j]+1, result[i][j-1]+1);
                    result[i][j] = Math.min(result[i][j], result[i-1][j-1] + a3);
                }
            }
        }
        int levenshtein = result[strA.length()][strB.length()];
        return result;
    }

    public static String buildString(int len){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int num;
        for (int i=0; i<len; i++){
            num = Math.abs(random.nextInt()) % 48;
            num += 65;
            if (num>90){
                num+=6;
            }
            stringBuilder.append((char)num);
        }
        return stringBuilder.toString();
    }
}
