import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void readFile(String fileName) {
        Scanner reader = null;
        try {
            reader = new Scanner(Paths.get(fileName));
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }
        readFile(args[0]);
    }
}

