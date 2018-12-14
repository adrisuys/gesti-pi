package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.io.Serializable;

/**
 * Represents the amount of money a group of scouts has collected
 */
public class GroupMoney implements Serializable {

    private String nameOfGroup;
    private String amount;
    private String mId;

    public GroupMoney(){}

    /**
     * Creates a GroupMoney
     * @param mId the id
     * @param nameOfGroup the name of the group
     * @param amount the amount collected so far
     */
    public GroupMoney(String mId, String nameOfGroup, String amount) {
        this.nameOfGroup = nameOfGroup;
        this.amount = amount;
        this.mId = mId;
    }

    /**
     * Returns the name of the group
     * @return the name of the group
     */
    public String getNameOfGroup() {
        return nameOfGroup;
    }

    /**
     * Returns the amount collected
     * @return the amount collected
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the id
     * @return the id
     */
    public String getmId() {
        return mId;
    }
}
