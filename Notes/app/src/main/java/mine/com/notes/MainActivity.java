package mine.com.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int NOTE_ACTIVITY_REQUEST_CODE = 1;
    private ArrayAdapter adapter;
    ArrayList<String> noteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList.add("Test 01.");
        noteList.add("Note number 01.");

        adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, noteList);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }

    public void onClickNewButton(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
//        startActivity(intent);
        startActivityForResult(intent, NOTE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the NoteActivity with an OK result
        if (requestCode == NOTE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get note string from Intent
                String returnNote = data.getStringExtra("noteString");

                // Add new note string into list and update the list view.
                noteList.add(returnNote);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
