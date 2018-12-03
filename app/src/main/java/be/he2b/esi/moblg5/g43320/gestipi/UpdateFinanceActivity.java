package be.he2b.esi.moblg5.g43320.gestipi;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Budget;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.GroupMoney;

public class UpdateFinanceActivity extends BaseActivity {
    TextInputEditText amount_group1;
    TextInputEditText amount_group2;
    TextInputEditText amount_group3;
    TextInputEditText amount_group4;
    List<GroupMoney> groups = new ArrayList<>();
    Budget overall;
    TextInputEditText amount_overall;
    ActionBar topBar;
    Button btnUpdate;
    Button btnCancel;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_finance);
        topBar = getSupportActionBar();
        topBar.setTitle("Mise à jour des finances");
        this.configureToolbar();
        amount_group1 = (TextInputEditText) findViewById(R.id.finance_group_1);
        amount_group2 = (TextInputEditText) findViewById(R.id.finance_group_2);
        amount_group3 = (TextInputEditText) findViewById(R.id.finance_group_3);
        amount_group4 = (TextInputEditText) findViewById(R.id.finance_group_4);
        amount_overall = (TextInputEditText) findViewById(R.id.finance_total);
        groups = getGroups();
        displayGroups(groups);
        overall = (Budget) getIntent().getSerializableExtra("overall");
        if (overall != null) amount_overall.setText(overall.getAmount());
        btnCancel = (Button) findViewById(R.id.finance_btn_cancel);
        btnUpdate = (Button) findViewById(R.id.finance_btn_update);
    }

    private void displayGroups(List<GroupMoney> groups) {
        if (groups != null){
            for (GroupMoney group : groups){
                switch (group.getNameOfGroup()){
                    case "1" : amount_group1.setText(group.getAmount()); break;
                    case "2" : amount_group2.setText(group.getAmount()); break;
                    case "3" : amount_group3.setText(group.getAmount()); break;
                    default : amount_group4.setText(group.getAmount());
                }
            }
        } else {
            displayToast("Impossible de mettre à jour les données financières");
        }
    }

    public void handleCancelClick(View v){
        this.onBackPressed();
    }

    public void handleUpdateClick(View v){
        Collections.sort(groups, new Comparator<GroupMoney>(){
            public int compare(GroupMoney e1, GroupMoney e2) {
                return e1.getNameOfGroup().compareTo(e2.getNameOfGroup());
            }
        });
        updateInDB("/groupMoney", groups.get(0).getmId(), "amount", amount_group1.getText().toString());
        updateInDB("/groupMoney", groups.get(1).getmId(), "amount", amount_group2.getText().toString());
        updateInDB("/groupMoney", groups.get(2).getmId(), "amount", amount_group3.getText().toString());
        updateInDB("/groupMoney", groups.get(3).getmId(), "amount", amount_group4.getText().toString());
        updateInDB("/overallMoney", overall.getmId(), "amount", amount_overall.getText().toString());
        this.onBackPressed();
    }

    private void updateInDB(String collection, String uid, String field, String value){
        FirebaseFirestore.getInstance().collection(collection).document(uid).update(field, value);
    }

    public List<GroupMoney> getGroups() {
        List<GroupMoney> groups = new ArrayList<>();
        GroupMoney groupA = (GroupMoney) getIntent().getSerializableExtra("groupA");
        groups.add(groupA);
        GroupMoney groupB = (GroupMoney) getIntent().getSerializableExtra("groupB");
        groups.add(groupB);
        GroupMoney groupC = (GroupMoney) getIntent().getSerializableExtra("groupC");
        groups.add(groupC);
        GroupMoney groupD = (GroupMoney) getIntent().getSerializableExtra("groupD");
        groups.add(groupD);
        return groups;
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

    private void displayToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
