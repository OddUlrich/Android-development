package project.news;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class AllSiteFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity = mainActivityActivityTestRule.getActivity();
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        AllSiteFragment allSiteFragment = new AllSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(), allSiteFragment).commitAllowingStateLoss();

    }
    @Test
    public void test_FollowedScroll(){
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(70));
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}