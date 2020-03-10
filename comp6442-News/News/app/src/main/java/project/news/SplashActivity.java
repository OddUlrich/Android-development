package project.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * SplashActivity
 *
 * Opening display for the app.
 *
 * @author Dawen
 * @version 1.0
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView logoImage;

    /**
     * Create the splash view.
     *
     * @author Dawen
     * @param savedInstanceState instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoImage = findViewById(R.id.logo_img);
        final Animation transition = AnimationUtils.loadAnimation(this,R.anim.splashtransition);
        logoImage.startAnimation(transition);

        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}
