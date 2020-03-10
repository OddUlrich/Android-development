package project.news;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    Fragment mainPage = new MainPage();
    Fragment followingPage = new FollowingPage();
    Fragment favouritePage = new FavouritePage();

    FragmentManager fm = getSupportFragmentManager();
    Fragment selectedFragment = mainPage;

    /**
     * Initialize bottom navigation bar, and use FragmentManager to hide and show selected fragment
     * to avoid recreating each fragment when it's clicked.
     *
     * @param savedInstanceState instance state
     * @author Dawen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Paper.init(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // Set default fragment as navigation_news,
        // and application will start with news fragment page.
        bottomNav.setSelectedItemId(R.id.navigation_news);

        // Add three fragments into fragment manager but shows mainPage as the default selected fragment.
        fm.beginTransaction().add(R.id.main_container,favouritePage,"favourite").hide(favouritePage).commit();
        fm.beginTransaction().add(R.id.main_container,followingPage,"following").hide(followingPage).commit();
        fm.beginTransaction().add(R.id.main_container,mainPage,"mainpage").commit();
    }

    // Hide and show fragments based on the id of each item on the navigation bar.
    //reference stackoverflow answer https://stackoverflow.com/a/50939140
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    // Hide current selectedFragment and show mainPage fragment
                    fm.beginTransaction().hide(selectedFragment).show(mainPage).commit();
                    // Set current selectedFragment as mainPage fragment.
                    selectedFragment = mainPage;
                    return true;

                case R.id.navigation_following:
                    fm.beginTransaction().hide(selectedFragment).show(followingPage).commit();
                    selectedFragment = followingPage;
                    return true;

                case R.id.navigation_favourite:
                    fm.beginTransaction().hide(selectedFragment).show(favouritePage).commit();
                    selectedFragment = favouritePage;
                    ((FavouritePage) favouritePage).updateState();
                    return true;
            }

            return false;
        }
    };

}
