package be.he2b.esi.moblg5.g43320.gestipi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;

public class ProfileActivity extends BaseActivity {

    TextInputEditText name_tf;
    TextInputEditText firstName_tf;
    TextInputEditText totem_tf;
    TextInputEditText email_tf;
    TextInputEditText phone_tf;
    Spinner group_spinner;
    Button updateUser_btn;
    ActionBar bar;
    CheckBox checkbox;

    private static final int UPDATE_INFO = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_tf = (TextInputEditText) findViewById(R.id.profile_name_edit);
        firstName_tf = (TextInputEditText) findViewById(R.id.profile_firstname_edit);
        totem_tf = (TextInputEditText) findViewById(R.id.profile_totem_edit);
        email_tf = (TextInputEditText) findViewById(R.id.profile_email_edit);
        phone_tf = (TextInputEditText) findViewById(R.id.profile_phone_edit);
        group_spinner = (Spinner) findViewById(R.id.profile_group_edit);
        updateUser_btn = (Button) findViewById(R.id.profile_btn_update);
        bar = getSupportActionBar();
        bar.setTitle("Modifier votre profil");
        checkbox = (CheckBox) findViewById(R.id.profile_is_chief_edit);

        this.configureToolbar();
        this.updateUIWhenCreating();
    }

    private void updateUIWhenCreating() {
        if (this.getCurrentUser() != null){
            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();

            //Update views with data
            this.email_tf.setText(email);

            // 5 - Get additional data from Firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    updateUser(currentUser);
                }
            });
        }
    }

    private void updateUser(User currentUser) {
        // name
        String username = TextUtils.isEmpty(currentUser.getmName()) ? getString(R.string.info_no_username_found) : currentUser.getmName();
        name_tf.setText(username);
        //first name
        String userFirstname = TextUtils.isEmpty(currentUser.getmFirstname()) ? getString(R.string.info_no_firstname_found) : currentUser.getmFirstname();
        firstName_tf.setText(userFirstname);
        // totem
        String totem = TextUtils.isEmpty(currentUser.getmTotem()) ? getString(R.string.info_no_totem_found) : currentUser.getmTotem();
        totem_tf.setText(totem);
        // phone no
        String phone = TextUtils.isEmpty(currentUser.getmPhoneNumber()) ? getString(R.string.info_no_phone_number_found) : currentUser.getmPhoneNumber();
        phone_tf.setText(phone);
        // email
        String email = TextUtils.isEmpty(currentUser.getmEmail()) ? getString(R.string.info_no_email_found) : currentUser.getmEmail();
        email_tf.setText(email);
        // group
        String group = TextUtils.isEmpty(currentUser.getmGroup()) ? getString(R.string.info_no_group_found) : currentUser.getmGroup();
        if (!group.equals("Aucun groupe")){
            int pos = getIndex(group_spinner, group);
            if (pos != -1) group_spinner.setSelection(pos);
            System.out.println("Il y a un groupe donc je l'affiche");
        }
        checkbox.setChecked(currentUser.isChief());
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    public void onClickUpdateButton(View v) {
        this.updateInFirebase();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateInFirebase() {
        String username = this.name_tf.getText().toString();
        String userFirstname = this.firstName_tf.getText().toString();
        String totem = this.totem_tf.getText().toString();
        String email = this.email_tf.getText().toString();
        String phone = this.phone_tf.getText().toString();
        String group = this.group_spinner.getSelectedItem().toString();

        if (this.getCurrentUser() != null){
            if (!username.isEmpty() && !username.equals(getString(R.string.info_no_username_found))){
                UserHelper.updateUserData(username, this.getCurrentUser().getUid(), "mName");
            }
            if (!userFirstname.isEmpty() && !userFirstname.equals(getString(R.string.info_no_firstname_found))){
                UserHelper.updateUserData(userFirstname, this.getCurrentUser().getUid(), "mFirstname");
            }
            if (!totem.isEmpty() && !totem.equals(getString(R.string.info_no_totem_found))){
                UserHelper.updateUserData(totem, this.getCurrentUser().getUid(), "mTotem");
            }
            if (!email.isEmpty() && !email.equals(getString(R.string.info_no_email_found))){
                UserHelper.updateUserData(email, this.getCurrentUser().getUid(), "mEmail");
            }
            if (!phone.isEmpty() && !phone.equals(getString(R.string.info_no_phone_number_found))){
                UserHelper.updateUserData(phone, this.getCurrentUser().getUid(), "mPhoneNumber");
            }
            if (!group.isEmpty() && !group.equals(getString(R.string.info_no_group_found))){
                UserHelper.updateUserData(group, this.getCurrentUser().getUid(), "mGroup");
            }
            UserHelper.updateUserChief(this.getCurrentUser().getUid(), checkbox.isChecked());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                //getActivity().onBackPressed();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
