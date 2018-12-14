package be.he2b.esi.moblg5.g43320.gestipi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ActivityUpdateFinanceBinding;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.FinanceUpdateViewModel;

/**
 * The screen on which the member can see all the info related to the budget of the organization
 */
public class UpdateFinanceActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar topBar = getSupportActionBar();
        if (topBar != null) topBar.setTitle("Mise à jour des finances");
        this.configureToolbar();
        ActivityUpdateFinanceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_update_finance);
        FinanceUpdateViewModel financeViewModel = new FinanceUpdateViewModel(this);
        binding.setViewModel(financeViewModel);
        financeViewModel.onCreate();
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
