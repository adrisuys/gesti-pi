package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.ChatFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.EventsFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.FinanceFragment;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.MembersFragment;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

public class MainActivity extends BaseActivity {

    private ActionBar toolbar;
    private static final int SIGN_OUT_TASK = 10;
    private List<User> users = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        toolbar.setTitle("Le Poste");
        loadFragment(new MembersFragment());
    }

    protected void onResume(){
        super.onResume();
    }

    private void loadFragment(Fragment Fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, Fragment);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_listing:
                    toolbar.setTitle("Le Poste");
                    loadFragment(new MembersFragment());
                    return true;
                case R.id.navigation_event:
                    toolbar.setTitle("Les évènements");
                    loadFragment(new EventsFragment());
                    return true;
                case R.id.navigation_money:
                    toolbar.setTitle("Le budget camp");
                    loadFragment(new FinanceFragment());
                    return true;
                case R.id.navigation_chat:
                    toolbar.setTitle("Discussion");
                    loadFragment(new ChatFragment());
                    return true;
            }
            return false;
        }
    };

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

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        };
    }

    public User getCurrentUser(){
        return currentUser;
    }
}
