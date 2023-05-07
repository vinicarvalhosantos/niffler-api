package br.com.santos.vinicius.nifflerapi.util;


import java.util.regex.Pattern;

public class StringUtil {

    private static final int MAX_REPEATED_CHARACTERS = 1;
    private static final int MAX_REPEATED_KS = 1;

    public static double similarity(String longerString, String shorterString) {

        final int distance = getLevenshteinDistance(longerString, shorterString);

        return calculateSimilarity(distance, longerString.length());
    }

    private static int getLevenshteinDistance(String s, String t) {
        return org.apache.commons.lang3.StringUtils.getLevenshteinDistance(s, t);
    }

    private static double calculateSimilarity(int distance, int length) {
        return 1.0 - ((double) distance / (double) length);
    }

    public static boolean isSpam(String message) {
        int numRepeatedChars = countMatches("([a-zA-Z])\\1+", message);
        int numRepeatedKs = countMatches("((?i)k)\\1{3}", message);

        return numRepeatedChars > MAX_REPEATED_CHARACTERS || numRepeatedKs > MAX_REPEATED_KS;
    }

    private static int countMatches(String regex, String message) {
        return Pattern.compile(regex)
                .matcher(message)
                .results()
                .mapToInt(m -> 1)
                .sum();
    }

}
