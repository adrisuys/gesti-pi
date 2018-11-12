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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.MembersFragment;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;

public class MainActivity extends BaseActivity {

    private ActionBar toolbar;
    private static final int SIGN_OUT_TASK = 10;
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Le Poste");
        setMembersFragment();
        //loadFragment(new MembersFragment());
    }

    private void setMembersFragment(){
        //List<User> users = new ArrayList<>();
        /*List<DocumentSnapshot> dss = UserHelper.getAllUsers();
        System.out.println("nb of users : " + dss.size());
        for (DocumentSnapshot ds : dss){
            users.add(ds.toObject(User.class));
        }*/
        users.add(new User("1", "Axis", "Doe", "John", "jd@dr.be", "0456789342", "Cordée 1", true));
        MembersFragment fragment = new MembersFragment();
        fragment.setUsers(users);
        loadFragment(fragment);
    }

    private void loadFragment(Fragment Fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, Fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_listing:
                    toolbar.setTitle("Le Poste");
                    //loadFragment(new MembersFragment());
                    setMembersFragment();
                    return true;
                case R.id.navigation_event:
                    toolbar.setTitle("Les évènements");
                    return true;
                case R.id.navigation_money:
                    toolbar.setTitle("Le budget camp");
                    return true;
                case R.id.navigation_chat:
                    toolbar.setTitle("Discussion");
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
                startActivity(new Intent(this, ProfileActivity.class));
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
}
