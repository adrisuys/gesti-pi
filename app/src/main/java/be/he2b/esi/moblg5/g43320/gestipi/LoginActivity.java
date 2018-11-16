package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    Button buttonLoginEmail;
    Button buttonLoginGoogle;
    ProgressBar progressBar;

    private static final int RC_SIGN_IN = 123;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLoginEmail = (Button) findViewById(R.id.login_activity_button_login);
        buttonLoginGoogle = (Button) findViewById(R.id.login_activity_button_login_google);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (this.isCurrentUserLogged()) changingScreen();
        buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLoginButton(false);
            }
        });
        buttonLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLoginButton(true);
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
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

    public void onClickLoginButton(boolean viaMail) {
        progressBar.setVisibility(View.VISIBLE);
        this.startSignInActivity(viaMail);
    }

    private void changingScreen(){
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    User currentUser = documentSnapshot.toObject(User.class);
                    if (currentUser.needToBeUpdate()){
                        // encore des données à rajouter
                        switchActivity(ProfileActivity.class);
                    } else {
                        // plus à modifier, on passe directement à l'activité principale
                        switchActivity(MainActivity.class);
                    }
                }
            }
        });
    }

    private List<AuthUI.IdpConfig> setProviderslist(boolean isMail){
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        if (isMail){
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
            );
        } else {
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
            );
        }
        return providers;
    }

    private void startSignInActivity(boolean isMail){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_pi)
                        .setAvailableProviders(setProviderslist(isMail))
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
                progressBar.setVisibility(View.GONE);
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
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> doc) {
                if(!doc.getResult().exists()){
                    String uid = getCurrentUser().getUid();
                    String totem = "";
                    String name = getCurrentUser().getDisplayName();
                    String firstname = "";
                    String email = getCurrentUser().getEmail();
                    String phone = "";
                    String group = "";
                    UserHelper.createUser(uid,totem,name,firstname,email,phone,group);
                }
            }
        });
    }

    private void updateUIWhenResuming(){
        // rien
    }
}
