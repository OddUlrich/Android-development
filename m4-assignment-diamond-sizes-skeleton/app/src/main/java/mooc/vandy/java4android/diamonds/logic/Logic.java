package mooc.vandy.java4android.diamonds.logic;

import android.util.Log;
import mooc.vandy.java4android.diamonds.ui.OutputInterface;

/**
 * This is where the logic of this App is centralized for this assignment.
 * <p>
 * The assignments are designed this way to simplify your early
 * Android interactions.  Designing the assignments this way allows
 * you to first learn key 'Java' features without having to beforehand
 * learn the complexities of Android.
 */
public class Logic
       implements LogicInterface {
    /**
     * This is a String to be used in Logging (if/when you decide you
     * need it for debugging).
     */
    public static final String TAG = Logic.class.getName();

    /**
     * This is the variable that stores our OutputInterface instance.
     * <p>
     * This is how we will interact with the User Interface [MainActivity.java].
     * <p>
     * It is called 'out' because it is where we 'out-put' our
     * results. (It is also the 'in-put' from where we get values
     * from, but it only needs 1 name, and 'out' is good enough).
     */
    private OutputInterface mOut;

    /**
     * This is the constructor of this class.
     * <p>
     * It assigns the passed in [MainActivity] instance (which
     * implements [OutputInterface]) to 'out'.
     */
    public Logic(OutputInterface out){
        mOut = out;
    }

    /**
     * This is the method that will (eventually) get called when the
     * on-screen button labeled 'Process...' is pressed.
     */
    public void process(int size) {

        // TODO -- add your code here

        String[] marks = {"-", "="};
        String mark;


        // First row.
        System.out.print("+");
        for (int i = 0; i < 2*size; i++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int row = 1; row < size*2; row++) {
            System.out.print("|");

            // Choose proper mark;
            mark = marks[row%2];


            // Upper half.
            if (row < size) {
                for (int i = 0; i < size - row; i++) {
                    System.out.print(" ");
                }

                System.out.print("/");

                for (int k = 0; k < (row-1)*2; k++) {
                    System.out.print(mark);
                }

                System.out.print("\\");

                for (int i = 0; i < size - row; i++) {
                    System.out.print(" ");
                }
            }

            // Lower half.
            if (row > size) {
                for (int i = 0; i < row - size; i++) {
                    System.out.print(" ");
                }

                System.out.print("\\");

                for (int k = 0; k < (2*size-row-1)*2; k++) {
                    System.out.print(mark);
                }

                System.out.print("/");

                for (int i = 0; i < row - size; i++) {
                    System.out.print(" ");
                }
            }


            // Central row.
            if (row == size) {
                System.out.print("<");

                for (int k = 0; k < (size-1)*2; k++) {
                    System.out.print(mark);
                }

                System.out.print(">");
            }

            System.out.println("|");
        }

        // Last row.
        System.out.print("+");
        for (int i = 0; i < 2*size; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }


}
