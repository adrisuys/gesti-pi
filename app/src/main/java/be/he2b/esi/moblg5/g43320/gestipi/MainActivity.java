package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ActivityMainBinding;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.MainViewModel;

/**
 * Handles the main screen with the navigation bottom bar
 * There are 4 Fragments
 *   - a screen on which one can see the infos of the different members
 *   - a screen on which one can see the different events
 *   - a screen on which one can see the budget infos of the organization
 *   - a screen containing a group chat
 */
public class MainActivity extends BaseActivity {

    private User currentUser;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BottomNavigationView bottomNav = binding.navigation;
        viewModel = new MainViewModel(this, bottomNav);
        binding.setViewModel(viewModel);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnect:
                this.signOutUserFromFirebase();
                return true;
            case R.id.menu_update_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }

    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted() {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        };
    }

    /**
     * Returns the current user using the app
     * @return the current user using the app
     */
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onBackPressed(){
        // handles the back press on the main screen --> do nothing because it is impossible to go back further
    }
}
