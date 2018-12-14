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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UpdateEventActivityTest {

    @Rule
    public ActivityTestRule<UpdateEventActivity> activityTestRule = new ActivityTestRule<>(UpdateEventActivity.class, true, false);

    @Test
    public void update(){
        initUpdate();
        onView(withId(R.id.event_update_btn)).perform(closeSoftKeyboard(), scrollTo(), click());
        String text = "L'événement a été mis à jour.";
        onView(withText(text)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void createSuccess(){
        Intent intent = new Intent();
        intent.putExtra("mode", "0");
        intent.putExtra("currentUser", getUser());
        activityTestRule.launchActivity(intent);
        onView(withId(R.id.event_title_edit)).perform(typeText("Barbar Ferres"));
        onView(withId(R.id.event_location_edit)).perform(typeText("Rebecq"));
        onView(withId(R.id.event_start_date_edit)).perform(typeText("08/12/2018"));
        onView(withId(R.id.event_start_time_edit)).perform(typeText("14:00"));
        onView(withId(R.id.event_end_date_edit)).perform(typeText("09/12/2018"));
        onView(withId(R.id.event_end_time_edit)).perform(typeText("18:00"));
        onView(withId(R.id.event_update_btn)).perform(closeSoftKeyboard(), scrollTo(), click());
        String text = "L'évènement a bien été crée.";
        onView(withText(text)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void createFail(){
        Intent intent = new Intent();
        intent.putExtra("mode", "0");
        User user = getUser();
        intent.putExtra("currentUser", user);
        activityTestRule.launchActivity(intent);
        onView(withId(R.id.event_update_btn)).perform(closeSoftKeyboard(), scrollTo(), click());
        onView(withText("Champs obligatoire")).check(matches(anything()));
    }

    @Test
    public void getBack(){
        initUpdate();
        onView(withId(R.id.event_update_btn)).perform(closeSoftKeyboard(), scrollTo(), click());
        onView(withText("Les événements")).check(matches(anything()));
    }

    private void initUpdate(){
        String eventId = "TXkCG7XflpiL0tLE0Ceg";
        Intent intent = new Intent();
        User user = getUser();
        intent.putExtra("event_id", eventId);
        intent.putExtra("mode", "1");
        intent.putExtra("currentUser", user);
        activityTestRule.launchActivity(intent);
    }

    private User getUser(){
        User user = new User("1", "Calao", "Suys", "Adrien", "a@b.c", "1234567890", "Cordée 1");
        user.setChief(true);
        return user;
    }

}