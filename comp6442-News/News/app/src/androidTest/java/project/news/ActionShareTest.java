package project.news;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ActionShareTest {

    @Rule
    public ActivityTestRule<NewsDetailsActivity> mActivityTestRule = new ActivityTestRule<NewsDetailsActivity>(NewsDetailsActivity.class);

    private NewsDetailsActivity mActivity = null;

    @Test
    public void test_action_share() {
        onView(withId(R.id.newsdetail_webview))
                .perform(click())
                .check(matches(isDisplayed()));
        View view3 = mActivity.findViewById(R.id.action_share);
        assertNotNull(view3);
        onView(withId(R.id.action_share))
                .perform(click());
        mActivity.isDestroyed();
    }

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}