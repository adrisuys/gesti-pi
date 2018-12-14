package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ActivityProfileBinding;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.ProfileViewModel;

/**
 * The screen on which the user can see and update its own infos
 */
public class ProfileActivity extends BaseActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setTitle("Modifier votre profil");
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        ProfileViewModel viewModel = new ProfileViewModel(currentUser);
        viewModel.setApp(this);
        binding.setViewModel(viewModel);
        viewModel.onCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
