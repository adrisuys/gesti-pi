package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import android.databinding.ObservableField;
import java.io.Serializable;
import java.util.Observable;

/**
 * Represents a member of the organization
 */
public class User extends Observable implements Serializable {


    private final ObservableField<String> nickName = new ObservableField<>();
    private String mId;
    private String mTotem;
    private String mName;
    private String mFirstname;
    private String mEmail;
    private String mPhoneNumber;
    private String mGroup;
    private Boolean mIsChief;

    public User(){}

    /**
     * Creates an user
     * @param mId the id
     * @param mTotem the totem
     * @param mName the name
     * @param mFirstname the firstname
     * @param mEmail the email adress
     * @param mPhoneNumber the phone number
     * @param mGroup the group he belongs to
     */
    public User(String mId, String mTotem, String mName, String mFirstname, String mEmail, String mPhoneNumber, String mGroup) {
        this.mId = mId;
        this.mTotem = mTotem;
        this.mName = mName;
        this.mFirstname = mFirstname;
        this.mEmail = mEmail;
        this.mPhoneNumber = mPhoneNumber;
        this.mGroup = mGroup;
        this.mIsChief = false;
        setObservableField();
    }

    private void setObservableField() {
        if (!mTotem.equals("")) {
            nickName.set(mTotem);
        } else if (!mFirstname.equals("")) {
            nickName.set(mFirstname);
        } else {
            nickName.set(mName);
        }
    }

    /**
     * Returns the id
     * @return the id
     */
    public String getmId() {
        return mId;
    }

    /**
     * Returns the totem
     * @return the totem
     */
    public String getmTotem() {
        return mTotem;
    }

    /**
     * Set the totem
     * @param mTotem the new totem
     */
    public void setmTotem(String mTotem) {
        this.mTotem = mTotem;
    }

    /**
     * Returns the name
     * @return the name
     */
    public String getmName() {
        return mName;
    }

    /**
     * Set the name
     * @param mName the new name
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * Returns the firstname
     * @return the firstname
     */
    public String getmFirstname() {
        return mFirstname;
    }

    /**
     * Set the name
     * @param mFirstname the new firstname
     */
    public void setmFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    /**
     * Returns the email
     * @return the email
     */
    public String getmEmail() {
        return mEmail;
    }

    /**
     * Set the email
     * @param mEmail the new email
     */
    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    /**
     * Returns the phone no
     * @return the phone no
     */
    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    /**
     * Set the phone no
     * @param mPhoneNumber the new phone no
     */
    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    /**
     * Returns the group
     * @return the group
     */
    public String getmGroup() {
        return mGroup;
    }

    /**
     * Set the group
     * @param mGroup the new group
     */
    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    /**
     * Indicates if the user has all its info updated
     * @return true if the user needs to update its info, false otherwise
     */
    public boolean needToBeUpdate() {
        return mId.equals("") || mFirstname.equals("") || mGroup.equals("") || mTotem.equals("") || mPhoneNumber.equals("");
    }

    /**
     * Indicates if the user is a chief
     * @return true if the user is a chief, false otherwise
     */
    public Boolean isChief() {
        return mIsChief;
    }

    /**
     * Change the value of isChief
     * @param chief the new value
     */
    public void setChief(Boolean chief) {
        mIsChief = chief;
    }

    /**
     * Returns the pseudo of the user
     * @return the pseudo of the user
     */
    public String getPseudo(){
        if (!mTotem.equals("")) {
            return mTotem;
        } else if (!mFirstname.equals("")) {
            return mFirstname;
        } else {
            return mName;
        }
    }
}
