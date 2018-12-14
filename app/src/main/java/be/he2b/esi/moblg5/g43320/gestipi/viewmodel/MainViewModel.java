package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.ChatFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.EventsFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.FinanceFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.MembersFragment;

/**
 * The business class from the main screen that displays the bottom nav bar
 */
public class MainViewModel {

    private final ActionBar toolbar;
    private final MainActivity activity;
    private int currentFragment;

    /**
     * Creates an instance of the class
     * @param activity the activity the class depends on
     * @param bottomNav the bottomNavigationBar that handles the navigation between screen
     */
    public MainViewModel(MainActivity activity, BottomNavigationView bottomNav) {
        this.activity = activity;
        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationClick(item);
            }
        };
        bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        toolbar = activity.getSupportActionBar();
        if (toolbar != null) toolbar.setTitle("Le Poste");
        loadFragment(new MembersFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment).commit();
    }

    private boolean onNavigationClick(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_listing:
                toolbar.setTitle("Le Poste");
                loadFragment(new MembersFragment());
                currentFragment = 0;
                return true;
            case R.id.navigation_event:
                toolbar.setTitle("Les événements");
                loadFragment(new EventsFragment());
                currentFragment = 1;
                return true;
            case R.id.navigation_money:
                toolbar.setTitle("Le budget camp");
                loadFragment(new FinanceFragment());
                currentFragment = 2;
                return true;
            case R.id.navigation_chat:
                toolbar.setTitle("Discussion");
                loadFragment(new ChatFragment());
                currentFragment = 3;
                return true;
        }
        return false;
    }

    /**
     * According to the current fragment index, the corresponding scrren is displayed when the activity is resumed.
     */
    public void onResume() {
        switch (currentFragment) {
            case 0:
                loadFragment(new MembersFragment());
                break;
            case 1:
                loadFragment(new EventsFragment());
                break;
            case 2:
                loadFragment(new FinanceFragment());
                break;
            default:
                loadFragment(new ChatFragment());
        }
    }
}
