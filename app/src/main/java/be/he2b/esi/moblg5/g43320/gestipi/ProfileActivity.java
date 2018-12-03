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
import android.widget.Toast;

import be.he2b.esi.moblg5.g43320.gestipi.db_access.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

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
    User currentUser;

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
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        this.configureToolbar();
        this.updateUIWhenCreating();
    }

    private void updateUIWhenCreating() {
        if (this.getCurrentUserFirebase() != null){
            String email = TextUtils.isEmpty(this.getCurrentUserFirebase().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUserFirebase().getEmail();
            this.email_tf.setText(email);
            displayUser(currentUser);
        }
    }

    private void displayUser(User currentUser) {
        if (currentUser == null){
            displayToast("Impossible d'afficher le profil");
        } else {
            String username = TextUtils.isEmpty(currentUser.getmName()) ? getString(R.string.info_no_username_found) : currentUser.getmName();
            name_tf.setText(username);
            String userFirstname = TextUtils.isEmpty(currentUser.getmFirstname()) ? getString(R.string.info_no_firstname_found) : currentUser.getmFirstname();
            firstName_tf.setText(userFirstname);
            String totem = TextUtils.isEmpty(currentUser.getmTotem()) ? getString(R.string.info_no_totem_found) : currentUser.getmTotem();
            totem_tf.setText(totem);
            String phone = TextUtils.isEmpty(currentUser.getmPhoneNumber()) ? getString(R.string.info_no_phone_number_found) : currentUser.getmPhoneNumber();
            phone_tf.setText(phone);
            String email = TextUtils.isEmpty(currentUser.getmEmail()) ? getString(R.string.info_no_email_found) : currentUser.getmEmail();
            email_tf.setText(email);
            String group = TextUtils.isEmpty(currentUser.getmGroup()) ? getString(R.string.info_no_group_found) : currentUser.getmGroup();
            if (!group.equals("Aucun groupe")){
                int pos = getIndex(group_spinner, group);
                if (pos != -1) group_spinner.setSelection(pos);
            }
            checkbox.setChecked(currentUser.isChief());
        }
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
        changeActivity();
    }

    private void updateInFirebase() {
        String username = this.name_tf.getText().toString();
        String userFirstname = this.firstName_tf.getText().toString();
        String totem = this.totem_tf.getText().toString();
        String email = this.email_tf.getText().toString();
        String phone = this.phone_tf.getText().toString();
        String group = this.group_spinner.getSelectedItem().toString();
        if (this.getCurrentUserFirebase() != null){
            if (!username.isEmpty() && !username.equals(getString(R.string.info_no_username_found))){
                currentUser.setmName(username);
                UserHelper.updateUserData(username, this.getCurrentUserFirebase().getUid(), "mName");
            }
            if (!userFirstname.isEmpty() && !userFirstname.equals(getString(R.string.info_no_firstname_found))){
                currentUser.setmFirstname(userFirstname);
                UserHelper.updateUserData(userFirstname, this.getCurrentUserFirebase().getUid(), "mFirstname");
            }
            if (!totem.isEmpty() && !totem.equals(getString(R.string.info_no_totem_found))){
                currentUser.setmTotem(totem);
                UserHelper.updateUserData(totem, this.getCurrentUserFirebase().getUid(), "mTotem");
            }
            if (!email.isEmpty() && !email.equals(getString(R.string.info_no_email_found))){
                currentUser.setmEmail(email);
                UserHelper.updateUserData(email, this.getCurrentUserFirebase().getUid(), "mEmail");
            }
            if (!phone.isEmpty() && !phone.equals(getString(R.string.info_no_phone_number_found))){
                currentUser.setmPhoneNumber(phone);
                UserHelper.updateUserData(phone, this.getCurrentUserFirebase().getUid(), "mPhoneNumber");
            }
            if (!group.isEmpty() && !group.equals(getString(R.string.info_no_group_found))){
                currentUser.setmGroup(group);
                UserHelper.updateUserData(group, this.getCurrentUserFirebase().getUid(), "mGroup");
            }
            currentUser.setChief(checkbox.isChecked());
            UserHelper.updateUserChief(this.getCurrentUserFirebase().getUid(), checkbox.isChecked());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                changeActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    private void displayToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
