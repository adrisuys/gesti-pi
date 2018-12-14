package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Collections;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.LoginActivity;
import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.ProfileActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class that handles the login screen
 */
public class LoginViewModel extends BaseObservable implements ViewModel {

    private static final int RC_SIGN_IN = 123;
    private final LoginActivity app;
    public final ObservableBoolean isCharging = new ObservableBoolean(false);

    /**
     * Creates an instance of the class
     * @param app the activity the app depends on
     */
    public LoginViewModel(LoginActivity app) {
        this.app = app;
    }

    /**
     * Log the user to the app
     * @param viaMail a boolean indicating if the user wants to be logged via mail or not
     *                else he will be logged via google
     */
    public void onClickLoginButton(boolean viaMail) {
        isCharging.set(true);
        startSignInActivity(viaMail);
    }

    private void startSignInActivity(boolean isMail) {
        app.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_pi)
                        .setAvailableProviders(setProviderslist(isMail))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private List<AuthUI.IdpConfig> setProviderslist(boolean isMail) {
        List<AuthUI.IdpConfig> providers;
        if (isMail) {
            providers = Collections.singletonList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
            );
        } else {
            providers = Collections.singletonList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
            );
        }
        return providers;
    }

    /**
     * Handle the callback from Firebase Auth by changing the screen
     * @param requestCode the requested code
     * @param resultCode the result code
     * @param data the data
     */
    public void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(app.getString(R.string.connection_succeed));
                isCharging.set(false);
                changingScreen();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(app.getString(R.string.error_authentication_canceled));
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(app.getString(R.string.error_no_internet));
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(app.getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void createUserInFirestore() {
        UserHelper.getUser(app.getCurrentUserFirebase().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> doc) {
                if (!doc.getResult().exists()) {
                    String uid = app.getCurrentUserFirebase().getUid();
                    String totem = "";
                    String name = app.getCurrentUserFirebase().getDisplayName();
                    String firstname = "";
                    String email = app.getCurrentUserFirebase().getEmail();
                    String phone = "";
                    String group = "";
                    UserHelper.createUser(uid, totem, name, firstname, email, phone, group);
                }
            }
        });
    }

    /**
     * Change the screen, if the user has entered all its info, he is taken to the main screen
     * Else he is taken to the screen on which he can update its profile info
     */
    public void changingScreen() {
        UserHelper.getUser(app.getCurrentUserFirebase().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    if (currentUser.needToBeUpdate()) {
                        // encore des données à rajouter
                        switchActivity(ProfileActivity.class, currentUser);
                    } else {
                        // plus à modifier, on passe directement à l'activité principale
                        switchActivity(MainActivity.class, currentUser);
                    }
                }
            }
        });
    }

    private void switchActivity(Class e, User user) {
        Intent intent = new Intent(app, e);
        intent.putExtra("currentUser", user);
        app.startActivity(intent);
    }

    private void showSnackBar(String message) {
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }

}
