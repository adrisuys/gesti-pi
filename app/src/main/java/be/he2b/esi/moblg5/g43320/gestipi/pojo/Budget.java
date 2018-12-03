package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.io.Serializable;

public class Budget implements Serializable {

    private String mId;
    private String amount;

    public Budget(String mId, String amount) {
        this.mId = mId;
        this.amount = amount;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
