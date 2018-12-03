package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.io.Serializable;

public class GroupMoney implements Serializable {

    private String nameOfGroup;
    private String amount;
    private String mId;

    public GroupMoney(String mId, String nameOfGroup, String amount) {
        this.nameOfGroup = nameOfGroup;
        this.amount = amount;
        this.mId = mId;
    }

    public String getNameOfGroup() {
        return nameOfGroup;
    }

    public void setNameOfGroup(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
