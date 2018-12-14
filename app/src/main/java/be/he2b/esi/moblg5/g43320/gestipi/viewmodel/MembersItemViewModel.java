package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class of the screen on which the user can see the infos of all the members
 */
public class MembersItemViewModel extends BaseObservable implements ViewModel {

    private static final int REQUEST_CODE = 45;
    public final ObservableField<String> mUserName = new ObservableField<>();
    private Intent callIntent;
    private final User mUser;
    private final MainActivity activity;

    /**
     * Creates an instance of the class
     * @param activity the activity on which the class depends
     * @param mUser the current user
     */
    public MembersItemViewModel(MainActivity activity, User mUser) {
        this.activity = activity;
        this.mUser = mUser;
        setNickname();
    }

    private void setNickname() {
        if (!mUser.getmTotem().equals("")) {
            mUserName.set(mUser.getmTotem());
        } else if (!mUser.getmFirstname().equals("")) {
            mUserName.set(mUser.getmFirstname());
        } else {
            mUserName.set(mUser.getmName());
        }
    }

    /**
     * Launches a call to the selected user
     */
    public void onClickCallBtn() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.putExtra("call", "call");
        callIntent.setData(Uri.parse("tel:" + mUser.getmPhoneNumber()));
        performCall(callIntent);
    }

    /**
     * Launches a email client to send a mail to the selected user
     */
    public void onClickEmailBtn() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getmEmail()});
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        activity.startActivity(emailIntent);
    }

    private void performCall(Intent callIntent) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (! ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.CALL_PHONE
                }, REQUEST_CODE);
            }
        } else {
            activity.startActivity(callIntent);
        }
    }

    /**
     * Launches a call if the permissions are granted
     * @param requestCode the code requested
     * @param permissions the lists of permissions
     * @param grantResults the results granted
     * @param intent the intent
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activity.startActivity(callIntent);
                }  // permission denied, boo! Disable the // functionality that depends on this permission.
            }
        }
    }

    /*
    public void bind(User user) {
        if (user != null) {
            mUser = user;
            String pseudo = user.getmTotem().equals("") ? user.getmName() : user.getmTotem();
            mUserName.set(pseudo);
        } else {
            Toast.makeText(activity, "Impossible d'afficher les données de l'utilisateur", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
