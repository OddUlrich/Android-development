package mine.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import mine.com.Activity.MessageActivity;

public class MainActivity extends AppCompatActivity implements MessageFragment.OnMessageReadListener {

    public static FragmentManager fragmentManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dynamically add fragment at run time.
//        fragmentManager = getSupportFragmentManager();
//        if (findViewById(R.id.fragment_container) != null) {
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            HomeFragment homeFragment = new HomeFragment();
//
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.fragment_container, homeFragment, null);
//            fragmentTransaction.commit();
//        }

        if (findViewById(R.id.fragment_message_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            MessageFragment messageFragment = new MessageFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_message_container, messageFragment, null);
            fragmentTransaction.commit();
        }

    }

    // Passing message between activities.
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.user_message);
        String message = editText.getText().toString();

        Intent intent = new Intent(this, MessageActivity.class);

        intent.putExtra("EXTRA_MESSAGE", message);

        startActivity(intent);
    }

    @Override
    public void onMessageRead(String message) {
        textView = findViewById(R.id.text_display_message);
        textView.setText(message);
    }
}
