package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.UpdateFinanceActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Budget;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.GroupMoney;

public class FinanceFragment extends Fragment {

    private Button updateBtn;
    private TextView overall;
    private TextView group1;
    private TextView group2;
    private TextView group3;
    private TextView group4;
    private TextView group1Name;
    private TextView group2Name;
    private TextView group3Name;
    private TextView group4Name;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private List<GroupMoney> groupsCollect = new ArrayList<>();
    private List<Budget> currentBankAccount = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.finance_fragment, container, false);
        updateBtn = (Button) view.findViewById(R.id.money_btn_update);
        overall = (TextView) view.findViewById(R.id.money_overall);
        group1 = (TextView) view.findViewById(R.id.money_group1);
        group2 = (TextView) view.findViewById(R.id.money_group2);
        group3 = (TextView) view.findViewById(R.id.money_group3);
        group4 = (TextView) view.findViewById(R.id.money_group4);
        group1Name = (TextView) view.findViewById(R.id.money_group1_name);
        group2Name = (TextView) view.findViewById(R.id.money_group2_name);
        group3Name = (TextView) view.findViewById(R.id.money_group3_name);
        group4Name = (TextView) view.findViewById(R.id.money_group4_name);
        getCollectedMoneyFromGroups();
        getTotalAmountOnBankAccount();
        handleClickOnUpdate();
        return view;
    }

    public void onResume(){
        super.onResume();
    }

    private void getCollectedMoneyFromGroups(){
        FirebaseFirestore.getInstance().collection("/groupMoney").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("GroupMoney", "Error "+e.getMessage());
                }
                groupsCollect.clear();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    try {
                        GroupMoney amount = getGroupMoneyFromSnapchot(doc.getDocument());
                        groupsCollect.add(amount);
                    } catch (NullPointerException ex){
                        Toast.makeText(getContext(), "Impossible d'afficher la somme de chaque groupe", Toast.LENGTH_SHORT).show();
                    }
                }
                displayGroups();
            }
        });
    }

    private void displayGroups(){
        Collections.sort(groupsCollect, new Comparator<GroupMoney>(){
            public int compare(GroupMoney e1, GroupMoney e2) {
                if (Double.parseDouble(e1.getAmount()) < Double.parseDouble(e2.getAmount())){
                    return 1;
                } else if (Double.parseDouble(e1.getAmount()) == Double.parseDouble(e2.getAmount())){
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        group1Name.setText("Cordée " + groupsCollect.get(0).getNameOfGroup() + " : ");
        group2Name.setText("Cordée " + groupsCollect.get(1).getNameOfGroup() + " : ");
        group3Name.setText("Cordée " + groupsCollect.get(2).getNameOfGroup() + " : ");
        group4Name.setText("Cordée " + groupsCollect.get(3).getNameOfGroup() + " : ");
        group1.setText(groupsCollect.get(0).getAmount() + " €");
        group2.setText(groupsCollect.get(1).getAmount() + " €");
        group3.setText(groupsCollect.get(2).getAmount() + " €");
        group4.setText(groupsCollect.get(3).getAmount() + " €");
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
        if (document != null){
            String id = (String) document.get("mId");
            String amount = (String) document.get("amount");
            return new Budget(id, amount);
        } else {
            throw new NullPointerException();
        }
    }

    private void getTotalAmountOnBankAccount(){
        FirebaseFirestore.getInstance().collection("/overallMoney").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("OverallMoney", "Error "+e.getMessage());
                }
                currentBankAccount.clear();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    try {
                        Budget actualSum = getAmountAccountFromSnapchot(doc.getDocument());
                        currentBankAccount.add(actualSum);
                    } catch (NullPointerException ex){
                        Toast.makeText(getContext(), "Impossible d'afficher le budget total du poste", Toast.LENGTH_SHORT).show();
                    }

                }
                overall.setText(currentBankAccount.get(0).getAmount() + " €");
            }
        });
    }

    public void handleClickOnUpdate(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateFinanceActivity.class);
                intent.putExtra("groupA", groupsCollect.get(0));
                intent.putExtra("groupB", groupsCollect.get(1));
                intent.putExtra("groupC", groupsCollect.get(2));
                intent.putExtra("groupD", groupsCollect.get(3));
                intent.putExtra("overall", currentBankAccount.get(0));
                startActivity(intent);
            }
        });
    }
}
