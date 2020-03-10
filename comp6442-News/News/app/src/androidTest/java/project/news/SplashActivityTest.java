package project.news;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    private SplashActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    /**
     * test if the view of splash activity launch and MainActivity launches
     * @author Dawen
     */
    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.splash_layout);
        assertNotNull(view);

        Activity MainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(MainActivity);

        MainActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}