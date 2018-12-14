package be.he2b.esi.moblg5.g43320.gestipi;


import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.anything;
import static androidx.test.espresso.intent.Intents.intended;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void onMembersClicked(){
        init();
        onView(withId(R.id.navigation_listing)).perform(click());
        onView(withText("Le Poste")).check(matches(anything()));
    }

    @Test
    public void onEventClicked(){
        init();
        onView(withId(R.id.navigation_event)).perform(click());
        onView(withText("Les événements")).check(matches(anything()));
    }

    @Test
    public void onMoneyClicked(){
        init();
        onView(withId(R.id.navigation_money)).perform(click());
        onView(withText("Le budget camp")).check(matches(anything()));
    }

    @Test
    public void onChatClicked(){
        init();
        onView(withId(R.id.navigation_chat)).perform(click());
        onView(withText("Discussion")).check(matches(anything()));
    }

    private void init(){
        User user = new User("1", "Calao", "Suys", "Adrien", "a@b.c", "1234567890", "Cordée 1");
        Intent intent = new Intent();
        intent.putExtra("currentUser", user);
        activityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void call(){
        init();
        onView(withId(R.id.navigation_listing)).perform(click());
        onView(withId(R.id.members_recycler_view))
                .perform(RecyclerViewActions.
                        actionOnItemAtPosition(
                                4, MyViewAction.clickChildViewWithId(R.id.members_call)
                        )
                );
        intended(toPackage("be.he2b.esi.moblg5.g43320.gestipi"));
        intended(hasExtra("call", "call"));
    }

    @Test
    public void sendEmail(){
        init();
        onView(withId(R.id.navigation_listing)).perform(click());
        onView(withId(R.id.members_recycler_view))
                .perform(RecyclerViewActions.
                        actionOnItemAtPosition(
                                4, MyViewAction.clickChildViewWithId(R.id.members_mail)
                        )
                );
        intended(toPackage("be.he2b.esi.moblg5.g43320.gestipi"));
        intended(hasExtra(Intent.EXTRA_EMAIL, "43320@etu.he2b.be"));
    }

    @Test
    public void viewEvent(){
        init();
        onView(withId(R.id.navigation_event)).perform(click());
        onView(withId(R.id.events_recycler_view))
                .perform(RecyclerViewActions.
                        actionOnItemAtPosition(
                                1, MyViewAction.clickChildViewWithId(R.id.events_icon)
                        )
                );
        onData(withText("Votre événement")).check(matches(anything()));
        onData(withText("Bal Pi 2018")).check(matches(anything()));
        onData(withText("Salle paroissiale de Quenast")).check(matches(anything()));
    }

    @Test
    public void addEvent(){
        init();
        onView(withId(R.id.navigation_event)).perform(click());
        onData(withId(R.id.event_add_btn)).perform(click());
        onData(withText("Votre événement")).check(matches(anything()));
    }

    @Test
    public void updateMoney(){
        init();
        onView(withId(R.id.navigation_money)).perform(click());
        onData(withId(R.id.money_btn_update)).perform(click());
        onData(withText("Mise à jour des finances")).check(matches(anything()));
    }

    @Test
    public void sendSMS(){
        init();
        onView(withId(R.id.navigation_chat)).perform(click());
        onData(withId(R.id.chat_input_message)).perform(typeText("ESI"));
        onData(withId(R.id.chat_send_btn)).perform(click());
        onData(withText("ESI")).check(matches(anything()));
    }

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }

}