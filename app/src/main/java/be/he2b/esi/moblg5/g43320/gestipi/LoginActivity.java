package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ActivityLoginBinding;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.LoginViewModel;

/**
 * Handles the activity that logs the user in the app
 */
public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        binding.setViewModel(loginViewModel);
        if (this.isCurrentUserLogged()) {
            loginViewModel.changingScreen();
        }
        loginViewModel.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginViewModel.handleResponseAfterSignIn(requestCode, resultCode, data);
    }



}
