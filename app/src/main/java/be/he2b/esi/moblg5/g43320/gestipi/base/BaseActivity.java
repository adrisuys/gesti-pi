package be.he2b.esi.moblg5.g43320.gestipi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import be.he2b.esi.moblg5.g43320.gestipi.R;

/**
 * The base Activity that launches the app
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
    }

    /**
     * Configure the toolbar
     */
    protected void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Get the current user logged in the Firebase DB
     * @return the user currently logged and using the app
     */
    @Nullable
    public FirebaseUser getCurrentUserFirebase() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Specifies if the current user is logged.
     * @return true if he is logged, false otherwise.
     */
    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUserFirebase() != null);
    }
}

