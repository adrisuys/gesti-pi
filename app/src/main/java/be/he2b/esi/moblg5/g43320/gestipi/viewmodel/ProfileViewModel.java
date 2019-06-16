package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class of the screen on which the user can update its profile
 */
public class ProfileViewModel extends BaseObservable implements ViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> firstname = new ObservableField<>();
    public final ObservableField<String> totem = new ObservableField<>();
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableBoolean isChief = new ObservableBoolean();
    public final ObservableInt indexGroup = new ObservableInt();
    public final ObservableField<String> group = new ObservableField<>();
    private final User user;
    private AppCompatActivity app;

    /**
     * Creates an instance of the class
     * @param user the current user
     */
    public ProfileViewModel(User user) {
        this.user = user;
        setObservableFields();
        setGroupIndex();
    }

    private void setObservableFields() {
        name.set(user.getmName());
        firstname.set(user.getmFirstname());
        phone.set(user.getmPhoneNumber());
        email.set(user.getmEmail());
        isChief.set(user.isChief());
        totem.set(user.getmTotem());
    }

    private void setGroupIndex() {
        switch (user.getmGroup()) {
            case "Cordée 1":
                indexGroup.set(0);
                break;
            case "Cordée 2":
                indexGroup.set(1);
                break;
            case "Cordée 3":
                indexGroup.set(2);
                break;
            case "Cordée 4":
                indexGroup.set(3);
                break;
            default:
                indexGroup.set(0);
        }
    }

    @Override
    public void onCreate() {
        Log.d("ProfileViewModel", "onCreate");
    }

    @Override
    public void onResume() {
        Log.d("ProfileViewModel", "onResume");
    }

    /**
     * Update the profile in the firebase and return to the previous screen
     */
    public void onClickUpdateButton() {
        // remove keyboard from screen
        try{
            InputMethodManager imm = (InputMethodManager) app.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(app.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        this.updateInFirebase();
        changeActivity();
    }

    private void updateInFirebase() {
        String username = this.name.get();
        String userFirstname = this.firstname.get();
        String totem = this.totem.get();
        String email = this.email.get();
        String phone = this.phone.get();
        String group = this.group.get();
        if (this.user != null) {
            if (!username.isEmpty()) {
                user.setmName(username);
                UserHelper.updateUserData(username, user.getmId(), "mName");
            }
            if (!userFirstname.isEmpty()) {
                user.setmFirstname(userFirstname);
                UserHelper.updateUserData(userFirstname, user.getmId(), "mFirstname");
            }
            if (!totem.isEmpty()) {
                user.setmTotem(totem);
                UserHelper.updateUserData(totem, user.getmId(), "mTotem");
            }
            if (!email.isEmpty()) {
                user.setmEmail(email);
                UserHelper.updateUserData(email, user.getmId(), "mEmail");
            }
            if (!phone.isEmpty()) {
                user.setmPhoneNumber(phone);
                UserHelper.updateUserData(phone, user.getmId(), "mPhoneNumber");
            }
            if (user.getmGroup() != null) {
                user.setmGroup(group);
                UserHelper.updateUserData(user.getmGroup(), user.getmId(), "mGroup");
            }
            user.setChief(isChief.get());
            UserHelper.updateUserChief(user.getmId(), isChief.get());
        }
        displayToast("Le profile a été mis à jour.");
    }

    /**
     * When the checkbox 'isChief' is selected, changes the value of the user's field 'isChief'
     * @param view the view that has the checkbox in it
     */
    public void onCheckboxChange(View view) {
        this.isChief.set(((CheckBox) view).isChecked());
    }

    /**
     * Returns the current user
     * @return the current user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the activity
     * @param app the new activity
     */
    public void setApp(AppCompatActivity app) {
        this.app = app;
        if (user == null) {
            displayToast("Impossible d'afficher le profile.");
        }
    }

    private void changeActivity() {
        Intent intent = new Intent(app, MainActivity.class);
        intent.putExtra("currentUser", user);
        app.startActivity(intent);
    }

    private void displayToast(String message) {
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the change on the radio group
     * When a different radio button is selected, the group is changed.
     * @param rg the radio group
     * @param checkedId the value of the radio button
     */
    public void onCheckedChange(RadioGroup rg, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_cordee1:
                group.set("Cordée 1");
                indexGroup.set(0);
                break;
            case R.id.radio_button_cordee2:
                group.set("Cordée 2");
                indexGroup.set(1);
                break;
            case R.id.radio_button_cordee3:
                group.set("Cordée 3");
                indexGroup.set(2);
                break;
            case R.id.radio_button_cordee4:
                group.set("Cordée 4");
                indexGroup.set(3);
                break;
        }
    }

}
