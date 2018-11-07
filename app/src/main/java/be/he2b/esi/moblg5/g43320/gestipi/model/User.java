package be.he2b.esi.moblg5.g43320.gestipi.model;

import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;

import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;

public class User {

    private String mId;
    @Nullable private String mTotem;
    private String mName;
    private String mFirstname;
    private String mEmail;
    private String mPhoneNumber;
    private String mGroup;
    private Boolean mIsChief;

    public User(){}

    public User(String mId, String mTotem, String mName, String mFirstname, String mEmail, String mPhoneNumber, String mGroup) {
        this.mId = mId;
        this.mTotem = mTotem;
        this.mName = mName;
        this.mFirstname = mFirstname;
        this.mEmail = mEmail;
        this.mPhoneNumber = mPhoneNumber;
        this.mGroup = mGroup;
        this.mIsChief = false;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Nullable
    public String getmTotem() {
        return mTotem;
    }

    public void setmTotem(@Nullable String mTotem) {
        this.mTotem = mTotem;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public void setmFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmGroup() {
        return mGroup;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    public boolean needToBeUpdate(){
        return mId.equals("") || mFirstname.equals("") || mGroup.equals("") || mTotem.equals("") || mPhoneNumber.equals("");
    }

    public Boolean isChief() {
        return mIsChief;
    }

    public void setChief(Boolean chief) {
        mIsChief = chief;
    }
}
