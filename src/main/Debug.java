package main;

/**
 * @author Jaroslaw Pawlak
 */
public class Debug {
    private static boolean on = true;
    private Debug() {}
    public static void print(Object o) {
        if (on) {
            System.out.println(o);
        }
    }
}