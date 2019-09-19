package mooc.vandy.java4android.birthdayprob.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mooc.vandy.java4android.birthdayprob.ui.OutputInterface;

/**
 * This is where the logic of this App is centralized for this assignment.
 * <p>
 * The assignments are designed this way to simplify your early Android interactions.
 * Designing the assignments this way allows you to first learn key 'Java' features without
 * having to beforehand learn the complexities of Android.
 *
 */
public class Logic 
       implements LogicInterface {
    /**
     * This is a String to be used in Logging (if/when you decide you
     * need it for debugging).
     */
    public static final String TAG =
        Logic.class.getName();

    /**
     * This is the variable that stores our OutputInterface instance.
     * <p>
     * This is how we will interact with the User Interface
     * [MainActivity.java].
     * <p>
     * It is called 'mOut' because it is where we 'out-put' our
     * results. (It is also the 'in-put' from where we get values
     * from, but it only needs 1 name, and 'mOut' is good enough).
    */
    OutputInterface mOut;

    /**
     * This is the constructor of this class.
     * <p>
     * It assigns the passed in [MainActivity] instance
     * (which implements [OutputInterface]) to 'out'
     */
    public Logic(OutputInterface out){
        mOut = out;
    }

    /**
     * This is the method that will (eventually) get called when the
     * on-screen button labelled 'Process...' is pressed.
     */
    public void process() {
        int groupSize = mOut.getSize();
        int simulationCount = mOut.getCount();

        if (groupSize < 2 || groupSize > 365) {
            mOut.makeAlertToast("Group Size must be in the range 2-365.");
            return;
        }
        if (simulationCount <= 0) {
            mOut.makeAlertToast("Simulation Count must be positive.");
            return;
        }

        double percent = calculate(groupSize, simulationCount);

        // report results
        mOut.println("For a group of " + groupSize + " people, the percentage");
        mOut.println("of times that two people share the same birthday is");
        mOut.println(String.format("%.2f%% of the time.", percent));

    }

    /**
     * This is the method that actually does the calculations.
     * <p>
     * We provide you this method that way we can test it with unit testing.
     */
    public double calculate(int size, int count) {
        // TODO -- add your code here

        // Only 365 days in a year (from 0 to 364).
        // As soon as you determine that two people have the same birthday you can stop the current simulation and start the next simulation
        // Seed the Random object with the number of the run you are simulating (count);
        //  return the percentage in the range 0.0 – 100.0

        List<Integer> birthdayList = new ArrayList<>();
        double sameCnt = 0;


        for (int cnt = 0; cnt < count; cnt++) {
            Random rand = new Random();
            rand.setSeed(cnt);

            birthdayList.clear();

            for (int personNo = 0; personNo < size; personNo++) {
                int day = rand.nextInt(365);
                if (birthdayList.contains(day)) {
                    sameCnt++;
                    birthdayList.clear();
                    break;
                } else {
                    birthdayList.add(day);
                }
            }
        }

        return sameCnt / count * 100;
    }
    // TODO - add your code here
}
