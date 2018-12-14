package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.Budget;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.GroupMoney;

/**
 * The business class of the screen on which the user can update the infos related to the budget
 */
public class FinanceUpdateViewModel extends BaseObservable implements ViewModel {

    public final ObservableField<String> amount_group1 = new ObservableField<>();
    public final ObservableField<String> amount_group2 = new ObservableField<>();
    public final ObservableField<String> amount_group3 = new ObservableField<>();
    public final ObservableField<String> amount_group4 = new ObservableField<>();
    public final ObservableField<String> amount_overall = new ObservableField<>();
    private final List<GroupMoney> groups;
    private final Budget overall;
    private final AppCompatActivity app;

    /**
     * Creates an instance of the class
     * @param app the activity the class depends on
     */
    public FinanceUpdateViewModel(AppCompatActivity app) {
        this.app = app;
        groups = getGroups();
        displayGroups();
        overall = (Budget) app.getIntent().getSerializableExtra("overall");
        displayOverall();
    }

    private void displayGroups() {
        if (groups != null) {
            for (GroupMoney group : groups) {
                switch (group.getNameOfGroup()) {
                    case "1":
                        amount_group1.set(group.getAmount());
                        break;
                    case "2":
                        amount_group2.set(group.getAmount());
                        break;
                    case "3":
                        amount_group3.set(group.getAmount());
                        break;
                    default:
                        amount_group4.set(group.getAmount());
                }
            }
        } else {
            displayToast("Impossible de mettre à jour les données financières");
        }
    }

    private void displayOverall() {
        if (overall != null) {
            amount_overall.set(overall.getAmount());
        }
    }

    private void displayToast(String message) {
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
    }

    private List<GroupMoney> getGroups() {
        List<GroupMoney> groups = new ArrayList<>();
        GroupMoney groupA = (GroupMoney) app.getIntent().getSerializableExtra("groupA");
        groups.add(groupA);
        GroupMoney groupB = (GroupMoney) app.getIntent().getSerializableExtra("groupB");
        groups.add(groupB);
        GroupMoney groupC = (GroupMoney) app.getIntent().getSerializableExtra("groupC");
        groups.add(groupC);
        GroupMoney groupD = (GroupMoney) app.getIntent().getSerializableExtra("groupD");
        groups.add(groupD);
        return groups;
    }

    /**
     * Get to the previous screen
     */
    public void handleCancelClick() {
        closeKeyBoard();
        app.onBackPressed();
    }

    private void closeKeyBoard(){
        try{
            InputMethodManager imm = (InputMethodManager) app.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(app.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update the money infos with the new value
     */
    public void handleUpdateClick() {
        closeKeyBoard();
        Collections.sort(groups, new Comparator<GroupMoney>() {
            public int compare(GroupMoney e1, GroupMoney e2) {
                return e1.getNameOfGroup().compareTo(e2.getNameOfGroup());
            }
        });
        updateInDB("/groupMoney", groups.get(0).getmId(), amount_group1.get());
        updateInDB("/groupMoney", groups.get(1).getmId(), amount_group2.get());
        updateInDB("/groupMoney", groups.get(2).getmId(), amount_group3.get());
        updateInDB("/groupMoney", groups.get(3).getmId(), amount_group4.get());
        updateInDB("/overallMoney", overall.getmId(), amount_overall.get());
        Toast.makeText(app, "Mis à jour", Toast.LENGTH_SHORT).show();
        app.onBackPressed();
    }

    private void updateInDB(String collection, String uid, String value) {
        FirebaseFirestore.getInstance().collection(collection).document(uid).update("amount", value);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }

}
