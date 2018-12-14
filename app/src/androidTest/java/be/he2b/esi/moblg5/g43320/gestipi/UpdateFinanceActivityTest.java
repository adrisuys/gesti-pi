package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.Budget;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.GroupMoney;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UpdateFinanceActivityTest {

    @Rule
    public ActivityTestRule<UpdateFinanceActivity> activityTestRule = new ActivityTestRule<>(UpdateFinanceActivity.class, true, false);

    @Test
    public void update(){
        /*
        * Ne fonctionne pas car 90% de l'écran n'est pas visible à cause du clavier...
        * */
        init();
        onView(withId(R.id.finance_update_btn)).perform(closeSoftKeyboard(), click());
        String text = "Mis à jour";
        onView(withText(text)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void getBack(){
        init();
        onView(withId(R.id.finance_cancel_btn)).perform(closeSoftKeyboard(), click());
        onView(withText("Le budget camp")).check(matches(anything()));
    }

    private void init(){
        GroupMoney g1 = new GroupMoney("1","1", "230");
        GroupMoney g2 = new GroupMoney("2","2", "330");
        GroupMoney g3 = new GroupMoney("3","3", "430");
        GroupMoney g4 = new GroupMoney("4","4", "530");
        Budget overall = new Budget("1", "10000");

        Intent intent = new Intent();
        intent.putExtra("groupA", g1);
        intent.putExtra("groupB", g2);
        intent.putExtra("groupC", g3);
        intent.putExtra("groupD", g4);
        intent.putExtra("overall", overall);

        activityTestRule.launchActivity(intent);
    }

}