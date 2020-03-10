package project.news;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity=null;
    private Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(NewsDetailsActivity.class.getName(),null, false);


    @Before
    public void setUp() throws Exception {
        mActivity=mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void test_onLaunch(){
        FrameLayout mContainer= mActivity.findViewById(R.id.main_container);
        assertNotNull(mContainer);
        MainPage mainPage= new MainPage();

        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),mainPage).commitAllowingStateLoss();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        View tablayout = mainPage.getView().findViewById(R.id.mainPage_tabLayout);
        assertNotNull(tablayout);

        View pager = mainPage.getView().findViewById(R.id.mainPage_pager);
        assertNotNull(pager);
    }


    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}