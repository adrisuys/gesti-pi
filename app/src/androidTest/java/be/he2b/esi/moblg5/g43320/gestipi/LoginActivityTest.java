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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void googleConnexion(){
        onView(withId(R.id.login_activity_button_login_google)).perform(closeSoftKeyboard(), click());
        onView(withText("Le Poste")).check(matches(anything()));
    }

    @Test
    public void emailConnexion(){
        onView(withId(R.id.login_activity_button_login)).perform(closeSoftKeyboard(),click());
        onView(withText("Le Poste")).check(matches(anything()));
    }

}