package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String mImportance;

    public Event(){
    }

    public Event(String mId, String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType, String mImportance) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mStartDate = mStartDate;
        this.mStartTime = mStartTime;
        this.mEndDate = mEndDate;
        this.mEndTime = mEndTime;
        this.mDescription = mDescription;
        this.mType = mType;
        this.mImportance = (mType == Type.AUTRES ? mImportance : mType.getImportance());
    }

    public Event(String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType, String mImportance) {
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mStartDate = mStartDate;
        this.mStartTime = mStartTime;
        this.mEndDate = mEndDate;
        this.mEndTime = mEndTime;
        this.mDescription = mDescription;
        this.mType = mType;
        this.mImportance = (mType == Type.AUTRES ? mImportance : mType.getImportance());
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Type getmType() {
        return mType;
    }

    public void setmType(Type mType) {
        this.mType = mType;
    }

    public String getmImportance() {
        return mImportance;
    }

    public void setmImportance(String mImportance) {
        this.mImportance = mImportance;
    }

    private Date getDateFromString(String s){
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getStartingDate(){
        return getDateFromString(mStartDate);
    }

    public Date getEndingDate(){
        return getDateFromString(mEndDate);
    }
}
