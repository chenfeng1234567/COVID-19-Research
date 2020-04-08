import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GlobalAlignment {
    // function to find out
    // the minimum penalty
    private static void getMinimumPenalty(String x, String y, int pxy, int pgap) {

        int m = x.length(); // length of gene1
        int n = y.length(); // length of gene2

        // table for storing optimal
        // substructure answers
        int[][] dp = new int[n + m + 1][n + m + 1];

        // initializing the table
        for (int i = 0; i <= (n + m); i++) {
            dp[i][0] = i * pgap;
            dp[0][i] = i * pgap;
        }

        // calculating the
        // minimum penalty
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pxy,
                            dp[i - 1][j] + pgap),
                            dp[i][j - 1] + pgap);
                }
            }
        }

        // Reconstructing the solution
        int l = n + m; // maximum possible length

        int i = m;
        int j = n;

        int xpos = l;
        int ypos = l;

        // Final answers for
        // the respective strings
        int xans[] = new int[l + 1];
        int yans[] = new int[l + 1];

        while (!(i == 0 || j == 0)) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j - 1] + pxy == dp[i][j]) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j] + pgap == dp[i][j]) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = '_';
                i--;
            } else if (dp[i][j - 1] + pgap == dp[i][j]) {
                xans[xpos--] = '_';
                yans[ypos--] = y.charAt(j - 1);
                j--;
            }
        }
        while (xpos > 0) {
            if (i > 0) xans[xpos--] = x.charAt(--i);
            else xans[xpos--] = '_';
        }
        while (ypos > 0) {
            if (j > 0) yans[ypos--] = y.charAt(--j);
            else yans[ypos--] = '_';
        }

        // Since we have assumed the
        // answer to be n+m long,
        // we need to remove the extra
        // gaps in the starting id
        // represents the index from
        // which the arrays xans,
        // yans are useful
        int id = 1;
        for (i = l; i >= 1; i--) {
            if ((char) yans[i] == '_' &&
                    (char) xans[i] == '_') {
                id = i + 1;
                break;
            }
        }

        // Printing the final answer
//        System.out.println("Minimum Penalty in aligning the genes = " + dp[m][n]);
        System.out.println("Similarity: " + ((x.length() - dp[m][n]) * 1.0 / x.length()));
//        System.out.println("The aligned genes are:");
        String original = printEachElement(l, xans, yans, id);
//        System.out.println();
        System.out.println(original);
    }

    private static String printEachElement(int l, int[] original, int[] aligned, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        int gap = 0;
        char[] ori = new char[5];
        char[] ali = new char[5];
        for (int i = id; i <= l; i++) {
//            System.out.print((char) original[i]);
            if ((char) original[i] == '_') gap++;
            if (i == (30 + id + gap) || i == (34 + id + gap) || i == (37 + id + gap) || i == (81 + id + gap) || i == (352 + id + gap)) {
                if (i == 30 + id + gap) {
                    ori[0] = (char) original[i];
                    ali[0] = (char) aligned[i];
                }
                if (i == 34 + id + gap) {
                    ori[1] = (char) original[i];
                    ali[1] = (char) aligned[i];
                }
                if (i == 37 + id + gap) {
                    ori[2] = (char) original[i];
                    ali[2] = (char) aligned[i];
                }
                if (i == 81 + id + gap) {
                    ori[3] = (char) original[i];
                    ali[3] = (char) aligned[i];
                }
                if (i == 352 + id + gap) {
                    ori[4] = (char) original[i];
                    ali[4] = (char) aligned[i];
                }
                stringBuilder.append("Amino Acid in hotspot ").append((i - (id + gap - 1))).append(" is: ").append((char) original[i]).append(" -- ").append((char) aligned[i]);
                stringBuilder.append(original[i] == aligned[i] ? " match!" : " no match").append("\n");
            }
        }
        if (ori[0] == ali[0] && ori[1] == ali[1]) {
            stringBuilder.append("Hotspot pair 31-35 is matched").append("\n");
        }
        if (ori[2] == ali[2] && ori[4] == ali[4]) {
            stringBuilder.append("Hotspot pair 38-353 is matched").append("\n");
        }
        if (ori[3] == ali[3]) {
            stringBuilder.append("Hotspot pair 82 is matched").append("\n");
        }
        System.out.println();
//        for (int i = id; i <= l; i++) {
//            System.out.print((char) aligned[i]);
//        }
        return stringBuilder.toString();
    }

    static String getString(BufferedReader reader, StringBuilder sb) throws IOException {
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

    // Driver code
    public static void main(String[] args) {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("ACE2/HumanACE2.txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader("ACE2/仓鼠.txt"));
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            String str1 = getString(reader1, sb1).toUpperCase();
            String str2 = getString(reader2, sb2).toUpperCase();
            // initializing penalties of different types
            int misMatchPenalty = 1;
            int gapPenalty = 1;
            getMinimumPenalty(str1, str2, misMatchPenalty, gapPenalty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}