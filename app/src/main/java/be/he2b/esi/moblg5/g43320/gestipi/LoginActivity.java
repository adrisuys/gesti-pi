package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    Button buttonLogin;

    private static final int RC_SIGN_IN = 123;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin = (Button) findViewById(R.id.login_activity_button_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIWhenResuming();
    }

    private void switchActivity(Class e){
        Intent intent = new Intent(this, e);
        startActivity(intent);
    }

    public void onClickLoginButton(View v) {
        if (this.isCurrentUserLogged()){
            changingScreen();
        } else {
            this.startSignInActivity();
        }
    }

    private void changingScreen(){
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (currentUser.needToBeUpdate()){
                    // encore des données à rajouter
                    switchActivity(ProfileActivity.class);
                } else {
                    // plus à modifier, on passe directement à l'activité principale
                    switchActivity(MainActivity.class);
                }
            }
        });
    }

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_pi)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                              new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void showSnackBar(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(getString(R.string.connection_succeed));
                changingScreen();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(getString(R.string.error_no_internet));
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void createUserInFirestore() {
        if (this.getCurrentUser() != null){
            String uid = this.getCurrentUser().getUid();
            String totem = "";
            String name = this.getCurrentUser().getDisplayName();
            String firstname = "";
            String email = this.getCurrentUser().getEmail();
            String phone = "";
            String group = "";
            UserHelper.createUser(uid,totem,name,firstname,email,phone,group);
        }
    }

    private void updateUIWhenResuming(){
        // rien
    }
}
