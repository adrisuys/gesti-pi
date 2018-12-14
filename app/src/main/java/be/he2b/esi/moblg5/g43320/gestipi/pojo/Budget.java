package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.io.Serializable;

/**
 * Represents the overall budget of the scouts organization
 */
public class Budget implements Serializable {

    private final String mId;
    private final String amount;

    /**
     * Create a new Budget
     * @param mId the id of the organization
     * @param amount the budget
     */
    public Budget(String mId, String amount) {
        this.mId = mId;
        this.amount = amount;
    }

    /**
     * Returns the number of the organization
     * @return the number of the organization
     */
    public String getmId() {
        return mId;
    }

    /**
     * Returns the budget
     * @return the budget
     */
    public String getAmount() {
        return amount;
    }

}
