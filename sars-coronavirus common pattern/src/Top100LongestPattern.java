import java.io.*;
import java.nio.channels.ScatteringByteChannel;
import java.util.Arrays;
import java.util.Comparator;

public class Top100LongestPattern {

    static final int TOP_NUM = 10;
    static int totalLength = 0;
    static BufferedWriter bufferedWriter;
    static BufferedWriter numericData;
    static {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("result_2.txt"));
            numericData = new BufferedWriter(new FileWriter("numeric_2.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String[] longestTop100 = new String[TOP_NUM];

    public static void main(String[] args) {
        try {
            BufferedReader covidReader = new BufferedReader(new FileReader("covid-19-origin.txt"));
            BufferedReader sarsReader = new BufferedReader(new FileReader("bat-RaTG13.txt"));
            StringBuilder covidSB = new StringBuilder();
            StringBuilder sarsSB = new StringBuilder();
            String covid = getString(covidReader, covidSB);
            String sars = getString(sarsReader, sarsSB);
            LCS(covid, sars, 0);
            System.out.println("total length: " + totalLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void LCS(String X, String Y, int count) throws IOException {
        if (count == TOP_NUM) {
            return;
        }
        int m = X.length();
        int n = Y.length();
        int maxLen = 0;            // stores the max length of LCS
        int endingIndexInX = m;        // stores the ending index of LCS in X
        int endingIndexInY = n;

        // lookup[i][j] stores the length of LCS of substring
        // X[0..i-1], Y[0..j-1]
        int[][] lookup = new int[m + 1][n + 1];

        // fill the lookup table in bottom-up manner
        for (int i = 1; i <= m; i++) {
            if (X.charAt(i - 1) == '0')
                continue;
            for (int j = 1; j <= n; j++) {
                if (Y.charAt(j - 1) == '0')
                    continue;
                // if current character of X and Y matches
                if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    lookup[i][j] = lookup[i - 1][j - 1] + 1;
                    // update the maximum length and ending index
                    if (lookup[i][j] > maxLen) {
                        maxLen = lookup[i][j];
                        endingIndexInX = i;
                        endingIndexInY = j;
                    }
                }
            }
        }
        longestTop100[count] = X.substring(endingIndexInX - maxLen, endingIndexInX);
        writeToFile(maxLen, endingIndexInX, endingIndexInY, numericData);
        totalLength += longestTop100[count].length();
        StringBuilder sb = new StringBuilder();
        int length = maxLen;
        while (length > 0) {
            sb.append(0);
            length--;
        }
        String marked = sb.toString();
        X = X.substring(0, endingIndexInX - maxLen).concat(marked).concat(X.substring(endingIndexInX));
        Y = Y.substring(0, endingIndexInY - maxLen).concat(marked).concat(Y.substring(endingIndexInY));
        LCS(X, Y, ++count);
    }

    private static void writeToFile(int maxLen, int endingIndexInX, int endingIndexInY, BufferedWriter numericData) throws IOException {
        numericData.write("" + (endingIndexInX - maxLen));
        numericData.write(" ");
        numericData.write("" + endingIndexInX);
        numericData.write(" ");
        numericData.write("" + (endingIndexInY - maxLen));
        numericData.write(" ");
        numericData.write("" + endingIndexInY);
        numericData.newLine();
        numericData.flush();
    }

    private static String getString(BufferedReader reader, StringBuilder sb) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            int idx = 0;
            while (Character.isDigit(line.charAt(idx))) {
                idx++;
            }
            line = line.substring(idx).replaceAll("\\s", "");
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }
}