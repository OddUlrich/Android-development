package project.news;

import android.content.Context;
import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Common utility for the whole app.
 *
 * @version 1.0
 */
public class Utility {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static final String API_KEY = "3498f3618263477a88f516f01aa24d00"; // API_KEY for the NEWS API

    // Localhost server address and port
    public static final String Server_BASE_URL = "http://10.0.2.2:5000";

    /**
     * Convert a time string get from News API using PrettyTime Library
     *
     * @author Dawen
     * @param jsonTime json time
     * @return a string indicating that the article published at like "3 hours ago".
     */
    public static final String ConvertTime(String jsonTime){

        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

        // Define the time format same as the json time string
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH);
        String timeAgo = null;

        try {
            // Convert string into a date to pass to PrettyTime.
            Date date = timeFormat.parse(jsonTime);

            // Convert Date object to a string indicating the article published at hours ago.
            timeAgo = prettyTime.format(date);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return timeAgo;
    }

    /**
     * Save data into a json file.
     *
     * @author Wyman
     * @param context context.
     * @param data data to be saved.
     * @param filename the json file name.
     */
    static void saveListToJson(Context context, String data, String filename) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();

        } catch (Exception e) {
            Log.e("Exception", "File write stream failed: " + filename);
            e.printStackTrace();
        }

    }

    /**
     * Load data from a json file.
     *
     * @author Wyman
     * @param context context.
     * @param filename the json file name.
     * @return jsonStr
     */
    static String LoadListFromJson(Context context, String filename) {
        String jsonStr = "";

        try {
            FileInputStream inputStream = context.openFileInput(filename);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            StringBuilder strBuffer = new StringBuilder();
            String line;

            // Read each line from the input stream and append them together.
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }

            jsonStr = strBuffer.toString();

        } catch (IOException e) {
            Log.e("Exception", "File read stream failed: " + filename);
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("Exception", "Null pointer exception when opening file: " + filename);
            e.printStackTrace();
        }

        return jsonStr;
    }
}
