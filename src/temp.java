
import java.io.File;
import java.util.Scanner;

/**
 * @author Jarcionek
 */
public class temp {
    public static void main(String[] args) throws Exception {
        File file = new File("src/main");
        int bigCounter = 0;
        for (File e : file.listFiles()) {
            Scanner in = new Scanner(e);
            int counter = 0;
            while(in.hasNextLine()) {
                counter++;
                in.nextLine();
            }
            bigCounter += counter;
            System.out.println(e.getName() + "\t" + counter);
        }
        System.out.println("\nTogether:\t" + bigCounter);
    }
}
