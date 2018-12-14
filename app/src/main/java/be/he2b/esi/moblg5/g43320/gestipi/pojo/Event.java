package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a event
 */
public class Event {

    private String mId;
    private String mTitle;
    private String mLocation;
    private String mStartDate;
    private String mStartTime;
    private String mEndDate;
    private String mEndTime;
    private String mDescription;
    private Type mType;

    public Event(){}

    /**
     * Creates an event
     * @param mId the id
     * @param mTitle the name
     * @param mLocation the location
     * @param mStartDate the starting date
     * @param mStartTime the starting time
     * @param mEndDate the end date
     * @param mEndTime the end time
     * @param mDescription the description
     * @param mType the type
     */
    public Event(String mId, String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mStartDate = mStartDate;
        this.mStartTime = mStartTime;
        this.mEndDate = mEndDate;
        this.mEndTime = mEndTime;
        this.mDescription = mDescription;
        this.mType = mType;
    }

    /**
     * Creates an event without id
     * @param mTitle the name
     * @param mLocation the location
     * @param mStartDate the starting date
     * @param mStartTime the starting time
     * @param mEndDate the end date
     * @param mEndTime the end time
     * @param mDescription the description
     * @param mType the type
     */
    public Event(String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType) {
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mStartDate = mStartDate;
        this.mStartTime = mStartTime;
        this.mEndDate = mEndDate;
        this.mEndTime = mEndTime;
        this.mDescription = mDescription;
        this.mType = mType;
    }

    /**
     * Returns the id of the event
     * @return the id of the event
     */
    public String getmId() {
        return mId;
    }

    /**
     * Returns the name of the event
     * @return the name of the event
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * Returns the location of the event
     * @return the location of the event
     */
    public String getmLocation() {
        return mLocation;
    }

    /**
     * Returns the starting date of the event
     * @return the starting of the event as a String
     */
    public String getmStartDate() {
        return mStartDate;
    }

    /**
     * Returns the starting time of the event
     * @return the starting time of the event
     */
    public String getmStartTime() {
        return mStartTime;
    }

    /**
     * Returns the ending date of the event
     * @return the ending date of the event
     */
    public String getmEndDate() {
        return mEndDate;
    }

    /**
     * Returns the ending time of the event
     * @return the ending time of the event
     */
    public String getmEndTime() {
        return mEndTime;
    }

    /**
     * Returns the description of the event
     * @return the description of the event
     */
    public String getmDescription() {
        return mDescription;
    }

    /**
     * Returns the type of the event
     * @return the type of the event
     */
    public Type getmType() {
        return mType;
    }

    private Date getDateFromString(String s) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Returns the starting date of the event
     * @return the starting date of the event as a Date
     */
    public Date getStartingDate() {
        return getDateFromString(mStartDate);
    }

}
