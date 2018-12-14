package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mActivityRule = new ActivityTestRule<>(ProfileActivity.class, true, false);

    @Test
    public void updateProfile(){
        User user = new User("1", "Calao", "Suys", "Adrien", "a@b.c", "1234567890", "Cordée 1");
        Intent intent = new Intent();
        intent.putExtra("currentUser", user);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.profile_btn_update)).perform(scrollTo(), closeSoftKeyboard(), click());
        String text = "Le profile a été mis à jour.";
        onView(withText(text)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

}