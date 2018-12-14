package be.he2b.esi.moblg5.g43320.gestipi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ActivityViewEventBinding;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.EventUpdateViewModel;

/**
 * The screen on which the user can see/update the info of a particular event
 */
public class UpdateEventActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar topBar = getSupportActionBar();
        if (topBar != null) topBar.setTitle("Votre événement");
        this.configureToolbar();
        String editionMode = (String) this.getIntent().getSerializableExtra("mode");
        User currentUser = (User) this.getIntent().getSerializableExtra("currentUser");
        ActivityViewEventBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_event);
        EventUpdateViewModel viewModel = new EventUpdateViewModel(this, currentUser, editionMode);
        binding.setViewModel(viewModel);
        viewModel.onCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
