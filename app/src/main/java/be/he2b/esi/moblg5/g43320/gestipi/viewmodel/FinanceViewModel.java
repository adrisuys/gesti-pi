package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.UpdateFinanceActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Budget;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.GroupMoney;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business classes of the screen on which all the infos related to the budget are displayed
 */
public class FinanceViewModel extends BaseObservable implements ViewModel {

    public final ObservableField<String> group1Name = new ObservableField<>();
    public final ObservableField<String> group2Name = new ObservableField<>();
    public final ObservableField<String> group3Name = new ObservableField<>();
    public final ObservableField<String> group4Name = new ObservableField<>();
    public final ObservableField<String> group1 = new ObservableField<>();
    public final ObservableField<String> group2 = new ObservableField<>();
    public final ObservableField<String> group3 = new ObservableField<>();
    public final ObservableField<String> group4 = new ObservableField<>();
    public final ObservableField<String> overall = new ObservableField<>();
    public final ObservableBoolean isChief = new ObservableBoolean();
    private final MainActivity activity;
    private final List<GroupMoney> groupsCollect = new ArrayList<>();
    private final List<Budget> currentBankAccount = new ArrayList<>();
    private final User user;

    /**
     * Creates an instance of the class
     * @param activity the main activity which the viewModel depends on
     */
    public FinanceViewModel(MainActivity activity, User user) {
        this.activity = activity;
        getCollectedMoneyFromGroups();
        getTotalAmountOnBankAccount();
        this.user = user;
        isChief.set(user.isChief());
    }

    private void getCollectedMoneyFromGroups() {
        groupsCollect.clear();
        FirebaseFirestore.getInstance().collection("/groupMoney").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                GroupMoney amount = getGroupMoneyFromSnapchot(ds);
                                groupsCollect.add(amount);
                            }
                        }
                        displayGroups();
                    }
                });
    }

    private void displayGroups() {
        Collections.sort(groupsCollect, new Comparator<GroupMoney>() {
            public int compare(GroupMoney e1, GroupMoney e2) {
                return Double.compare(Double.parseDouble(e2.getAmount()), Double.parseDouble(e1.getAmount()));
            }
        });
        group1Name.set("Cordée " + groupsCollect.get(0).getNameOfGroup() + " : ");
        group2Name.set("Cordée " + groupsCollect.get(1).getNameOfGroup() + " : ");
        group3Name.set("Cordée " + groupsCollect.get(2).getNameOfGroup() + " : ");
        group4Name.set("Cordée " + groupsCollect.get(3).getNameOfGroup() + " : ");
        group1.set(groupsCollect.get(0).getAmount() + " €");
        group2.set(groupsCollect.get(1).getAmount() + " €");
        group3.set(groupsCollect.get(2).getAmount() + " €");
        group4.set(groupsCollect.get(3).getAmount() + " €");
    }

    private GroupMoney getGroupMoneyFromSnapchot(DocumentSnapshot document) {
        if (document != null) {
            String id = (String) document.get("mId");
            String amount = (String) document.get("amount");
            String nameOfGroup = (String) document.get("nameOfGroup");
            return new GroupMoney(id, nameOfGroup, amount);
        } else {
            throw new NullPointerException();
        }
    }

    private Budget getAmountAccountFromSnapchot(DocumentSnapshot document) {
        if (document != null) {
            String id = (String) document.get("mId");
            String amount = (String) document.get("amount");
            return new Budget(id, amount);
        } else {
            throw new NullPointerException();
        }
    }

    private void getTotalAmountOnBankAccount() {
        FirebaseFirestore.getInstance().collection("/overallMoney").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("OverallMoney", "Error " + e.getMessage());
                }
                currentBankAccount.clear();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    try {
                        Budget actualSum = getAmountAccountFromSnapchot(doc.getDocument());
                        currentBankAccount.add(actualSum);
                    } catch (NullPointerException ex) {
                        Toast.makeText(activity, "Impossible d'afficher le budget total du poste", Toast.LENGTH_SHORT).show();
                    }

                }
                overall.set(currentBankAccount.get(0).getAmount() + " €");
            }
        });
    }

    /**
     * Launch a new screen on which the admin can update these infos
     */
    public void handleClickOnUpdate() {
        try{
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(activity, UpdateFinanceActivity.class);
        intent.putExtra("groupA", groupsCollect.get(0));
        intent.putExtra("groupB", groupsCollect.get(1));
        intent.putExtra("groupC", groupsCollect.get(2));
        intent.putExtra("groupD", groupsCollect.get(3));
        intent.putExtra("overall", currentBankAccount.get(0));
        activity.startActivity(intent);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
