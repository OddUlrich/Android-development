package mine.com.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText editText = findViewById(R.id.note_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onAddNoteAction(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void onCancelNoteAction(View view) {
        EditText editText = findViewById(R.id.note_text);
        editText.setText("");
        finish();
    }

    public void onClearText(View view) {
        EditText editText = findViewById(R.id.note_text);
        editText.setText("");
    }

    public void onAddNoteAction(View view) {
        // Get the text from the EditText
        EditText editText = findViewById(R.id.note_text);
        String note = editText.getText().toString();

        // Pass back the note into an Intent and close this activity.
        Intent intent = new Intent();
        intent.putExtra("noteString", note);
        setResult(RESULT_OK, intent);
        finish();
    }
}
