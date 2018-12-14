package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.MembersFragment;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class of the screen on which the user can see the infos of all the members
 */
public class MembersItemViewModel extends BaseObservable implements ViewModel {

    private static final int REQUEST_CODE = 45;
    public final ObservableField<String> mUserName = new ObservableField<>();
    private Intent callIntent;
    private final User mUser;
    private final MembersFragment activity;

    /**
     * Creates an instance of the class
     * @param activity the activity on which the class depends
     * @param mUser the current user
     */
    public MembersItemViewModel(MembersFragment activity, User mUser) {
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
        if (ContextCompat.checkSelfPermission(activity.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (! ActivityCompat.shouldShowRequestPermissionRationale(activity.getActivity(), Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(activity.getActivity(), new String[]{
                        Manifest.permission.CALL_PHONE
                }, REQUEST_CODE);
            }
        } else {
            activity.startActivity(callIntent);
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //activity.getmMembersAdapter().getFilter().filter(s);
        activity.filter(s.toString());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
