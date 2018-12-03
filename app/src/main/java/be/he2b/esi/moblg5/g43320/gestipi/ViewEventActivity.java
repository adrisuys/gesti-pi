package be.he2b.esi.moblg5.g43320.gestipi;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.EventHelper;
import be.he2b.esi.moblg5.g43320.gestipi.base.BaseActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Event;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Type;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

public class ViewEventActivity extends BaseActivity {

    TextInputEditText mName;
    TextInputEditText mLocation;
    TextInputEditText mStartingDateString;
    TextInputEditText mEndingDateString;
    TextInputEditText mStartingTimeString;
    TextInputEditText mEndingTimeString;
    Spinner mType;
    Spinner mElseImportance;
    EditText mDescription;
    User currentUser;
    Button updateBtn;
    Button cancelBtn;
    String editionMode;
    ActionBar topBar;
    Event event;
    private static final String UPDATE = "1";
    private static final String CREATE = "0";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        topBar = getSupportActionBar();
        topBar.setTitle("Votre événement");
        editionMode = (String) this.getIntent().getSerializableExtra("mode");
        currentUser = (User) this.getIntent().getSerializableExtra("currentUser");
        initAttributes();
        if (editionMode.equals(UPDATE)){
            getCurrentEvent();
        }
        disableFieldIfTypeIsElse();
        this.configureToolbar();
    }

    private void disableFieldIfTypeIsElse(){
        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mType.getSelectedItem().toString().equals("AUTRES")){
                    mElseImportance.setEnabled(true);
                } else {
                    mElseImportance.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mElseImportance.setEnabled(false);
            }
        });
    }

    private void getCurrentEvent(){
        String eventID = (String) this.getIntent().getSerializableExtra("event_id");
        EventHelper.getEvent(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = documentSnapshot.toObject(Event.class);
                displayEvent(event);
            }
        });
    }

    private boolean writeInFirebase(){
        String name = mName.getText().toString();
        String location = mLocation.getText().toString();
        String startingDate = mStartingDateString.getText().toString();
        String endingDate = mEndingDateString.getText().toString();
        String startingTime = mStartingTimeString.getText().toString();
        String endingTime = mEndingTimeString.getText().toString();
        String typeStr = "" + mType.getSelectedItem().toString();
        String desc = "" + mDescription.getText().toString();
        // write in DB
        if (areValidInfos(name, location, startingDate, endingDate, startingTime, endingTime)){
            Type type = getTypeFromInput(typeStr);
            String importance = getImportance(typeStr);
            if (editionMode.equals(CREATE)){
                createInDatabase(name, location, startingDate, endingDate, startingTime, endingTime, type, importance, desc);
            } else {
                typeStr = type.name();
                updateInDatabase(name, location, startingDate, endingDate, startingTime, endingTime, typeStr, importance, desc);
            }
            return true;
        }
        return false;
    }

    private boolean areValidInfos(String name, String location, String startingDate, String endingDate, String startingTime, String endingTime) {
        boolean valid = true;
        if (name.equals("")){
            mName.setError("Champs obligatoire");
            valid = false;
        }
        if (location.equals("")){
            mLocation.setError("Champs obligatoire");
            valid = false;
        }
        if (startingDate.equals("")){
            mStartingDateString.setError("Champs obligatoire");
            valid = false;
        }
        if (startingTime.equals("")){
            mStartingTimeString.setError("Champs obligatoire");
            valid = false;
        }
        if (endingDate.equals("")){
            mEndingDateString.setError("Champs obligatoire");
            valid = false;
        }
        if (endingTime.equals("")){
            mEndingTimeString.setError("Champs obligatoire");
            valid = false;
        }
        return valid;
    }

    private Type getTypeFromInput(String typeStr){
        if (typeStr == "AUTRES"){
            return Type.getTypeByLabel(typeStr);
        } else {
            return Type.getTypeByLabel(typeStr);
        }
    }

    private String getImportance(String typeStr){
        String importance;
        Type type;
        if (typeStr == "AUTRES"){
            importance = mElseImportance.getSelectedItem().toString();
            switch (importance){
                case "NORMAL" : importance = "1"; break;
                case "IMPORTANT" : importance = "2"; break;
                default : importance = "3";
            }
        } else {
            type = Type.getTypeByLabel(typeStr);
            importance = type.getImportance();
        }
        return importance;
    }

    private void createInDatabase(String name, String location, String startingDate, String endingDate, String startingTime, String endingTime, Type type, String importance, String desc){
        Task<DocumentReference> task = EventHelper.createEvent(name, location, startingDate, startingTime, endingDate, endingTime, desc, type, importance);
        task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                EventHelper.updateEventData(id, id, "mId");
            }
        });
    }

    public void updateInDatabase(String name, String location, String startingDate, String endingDate, String startingTime, String endingTime, String type, String importance, String desc){
        String eventID = (String) this.getIntent().getSerializableExtra("event_id");
        EventHelper.updateEventData(name, eventID, "mTitle");
        EventHelper.updateEventData(location, eventID, "mLocation");
        EventHelper.updateEventData(startingDate, eventID, "mStartDate");
        EventHelper.updateEventData(endingDate, eventID, "mEndDate");
        EventHelper.updateEventData(startingTime, eventID, "mStartTime");
        EventHelper.updateEventData(endingTime, eventID, "mEndTime");
        EventHelper.updateEventData(type, eventID, "mType");
        EventHelper.updateEventData(importance, eventID, "mImportance");
        EventHelper.updateEventData(desc, eventID, "mDescription");
    }

    public void handleClickCancel(View v){
        this.onBackPressed();
    }

    public void handleClickUpdate(View v){
        if (writeInFirebase()) {
            //startActivity(new Intent(this, MainActivity.class));
            this.onBackPressed();
        }
    }

    private void initAttributes(){
        mName = (TextInputEditText) findViewById(R.id.event_title_edit);
        mLocation = (TextInputEditText) findViewById(R.id.event_location_edit);
        mType = (Spinner) findViewById(R.id.event_type_edit);
        mElseImportance = (Spinner) findViewById(R.id.event_importance_edit);
        mDescription = (EditText) findViewById(R.id.event_desc_edit);
        mStartingDateString = (TextInputEditText) findViewById(R.id.event_starting_date_string);
        mEndingDateString = (TextInputEditText) findViewById(R.id.event_ending_date_string);
        mStartingTimeString = (TextInputEditText) findViewById(R.id.event_starting_time_string);
        mEndingTimeString = (TextInputEditText) findViewById(R.id.event_ending_time_string);
        updateBtn = (Button) findViewById(R.id.event_btn_update);
        cancelBtn = (Button) findViewById(R.id.event_btn_cancel);
    }

    private void displayEvent(Event event){
        if (event != null) {
            if (currentUser != null ) disableItemsAccordingToStatus(currentUser.isChief());
            String name = TextUtils.isEmpty(event.getmTitle()) ? "" : event.getmTitle();
            mName.setText(name);
            String location = TextUtils.isEmpty(event.getmLocation()) ? "" : event.getmLocation();
            mLocation.setText(location);
            String startingDate = TextUtils.isEmpty(event.getmStartDate()) ? "" : event.getmStartDate();
            mStartingDateString.setText(startingDate);
            String endingDate = TextUtils.isEmpty(event.getmEndDate()) ? "" : event.getmEndDate();
            mEndingDateString.setText(endingDate);
            String startingTime = TextUtils.isEmpty(event.getmStartTime()) ? "" : event.getmStartTime();
            mStartingTimeString.setText(startingTime);
            String endingTime = TextUtils.isEmpty(event.getmEndTime()) ? "" : event.getmEndTime();
            mEndingTimeString.setText(endingTime);
            String type = TextUtils.isEmpty(event.getmType().toString()) ? "" : event.getmType().toString();
            if (!type.equals("")){
                int pos = getIndex(mType, type);
                if (pos != -1) mType.setSelection(pos);
            }
            if (!type.equals("Autre")){
                mElseImportance.setEnabled(false);
            } else {
                mElseImportance.setEnabled(true);
                switch(event.getmImportance()){
                    case "1" : mElseImportance.setSelection(0); break;
                    case "2" : mElseImportance.setSelection(1); break;
                    default: mElseImportance.setSelection(2);
                }
            }
            String desc = TextUtils.isEmpty(event.getmDescription()) ? "" : event.getmDescription();
            mDescription.setText(desc);
        } else {
            displayToast("Impossible d'afficher l'évènement");
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    private void disableItemsAccordingToStatus(boolean isChief){
        if (!isChief){
            mName.setEnabled(false);
            mLocation.setEnabled(false);
            mStartingDateString.setEnabled(false);
            mEndingDateString.setEnabled(false);
            mStartingTimeString.setEnabled(false);
            mEndingTimeString.setEnabled(false);
            mType.setEnabled(false);
            mElseImportance.setEnabled(false);
            mDescription.setEnabled(false);
            cancelBtn.setVisibility(View.GONE);
            updateBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(this, MainActivity.class));
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
