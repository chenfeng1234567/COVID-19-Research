import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotSpot {
    public static void main(String[] args) {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("ACE2/HumanACE2.txt"));
            StringBuilder sb1 = new StringBuilder();
            String str1 = GlobalAlignment.getString(reader1, sb1);
            // Create a pattern to be searched
            System.out.println(str1.charAt(352));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}