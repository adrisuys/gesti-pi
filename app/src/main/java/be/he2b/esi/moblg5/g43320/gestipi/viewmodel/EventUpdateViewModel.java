package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.EventHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Event;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Type;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business classes of the screen on which a user can see/update an event
 */
public class EventUpdateViewModel extends BaseObservable implements ViewModel {

    private static final String UPDATE = "1";
    private static final String CREATE = "0";
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> location = new ObservableField<>();
    public final ObservableField<String> startDate = new ObservableField<>();
    public final ObservableField<String> startTime = new ObservableField<>();
    public final ObservableField<String> endDate = new ObservableField<>();
    public final ObservableField<String> endTime = new ObservableField<>();
    public final ObservableField<String> desc = new ObservableField<>();
    public final ObservableField<String> type = new ObservableField<>("REUNION");
    public final ObservableBoolean isRed = new ObservableBoolean();
    public final ObservableBoolean isChief = new ObservableBoolean();
    private final AppCompatActivity app;
    private final String editionMode;

    /**
     * Creates an instance of the class
     * @param app the activity the class depends on
     * @param user the current user
     * @param editionMode the edition mode (creating or updating)
     */
    public EventUpdateViewModel(AppCompatActivity app, User user, String editionMode) {
        isChief.set(user.isChief());
        this.editionMode = editionMode;
        this.app = app;
        initScreen();
    }

    private void initScreen() {
        if (editionMode.equals(UPDATE)) {
            getCurrentEvent();
        } else {
            name.set("");
            location.set("");
            startDate.set("");
            startTime.set("");
            endDate.set("");
            endTime.set("");
            desc.set("");
            type.set("REUNION");
        }
    }

    private void getCurrentEvent() {
        String eventID = (String) app.getIntent().getSerializableExtra("event_id");
        EventHelper.getEvent(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = documentSnapshot.toObject(Event.class);
                displayEvent(event);
            }
        });
    }

    private void displayEvent(Event event) {
        name.set(event.getmTitle());
        location.set(event.getmLocation());
        startDate.set(event.getmStartDate());
        startTime.set(event.getmStartTime());
        endDate.set(event.getmEndDate());
        endTime.set(event.getmEndTime());
        desc.set(event.getmDescription());
        type.set(event.getmType().toString());
    }

    private boolean areValidInfos() {
        boolean valid = true;
        if (name.get().equals("")) {
            name.set("Champs obligatoire");
            valid = false;
        }
        if (location.get().equals("")) {
            location.set("Champs obligatoire");
            valid = false;
        }
        if (startDate.get().equals("")) {
            startDate.set("Champs obligatoire");
            valid = false;
        }
        if (startTime.get().equals("")) {
            startTime.set("Champs obligatoire");
            valid = false;
        }
        if (endDate.get().equals("")) {
            endDate.set("Champs obligatoire");
            valid = false;
        }
        if (endTime.get().equals("")) {
            endTime.set("Champs obligatoire");
            valid = false;
        }
        if (!valid){
            isRed.set(true);
        }
        return valid;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onResume() {
    }

    /**
     * Return to the previous screen
     */
    public void handleClickCancel() {
        closeKeyBoard();
        app.onBackPressed();
    }

    /**
     * Update the event with the new infos and returns to the previous screen
     */
    public void handleClickUpdate() {
        closeKeyBoard();
        if (writeInFirebase()) {
            showSnackBar("L'événement a été enregistré.");
            app.onBackPressed();
        }
    }

    private void closeKeyBoard(){
        try{
            InputMethodManager imm = (InputMethodManager) app.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(app.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean writeInFirebase() {
        String name = this.name.get();
        String location = this.location.get();
        String startingDate = this.startDate.get();
        String endingDate = this.endDate.get();
        String startingTime = this.startTime.get();
        String endingTime = this.endTime.get();
        String typeStr = this.type.get();
        String desc = this.desc.get();
        // write in DB
        if (areValidInfos()) {
            Type type = getTypeFromInput(typeStr);
            if (editionMode.equals(CREATE)) {
                createInDatabase(name, location, startingDate, endingDate, startingTime, endingTime, type, desc);
            } else {
                typeStr = type.name();
                updateInDatabase(name, location, startingDate, endingDate, startingTime, endingTime, typeStr, desc);
            }
            return true;
        }
        return false;
    }

    private Type getTypeFromInput(String typeStr) {
        return Type.getTypeByLabel(typeStr);
    }

    private void createInDatabase(String name, String location, String startingDate, String endingDate, String startingTime, String endingTime, Type type, String desc) {
        Task<DocumentReference> task = EventHelper.createEvent(name, location, startingDate, startingTime, endingDate, endingTime, desc, type);
        task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                EventHelper.updateEventData(id, id, "mId");
                showSnackBar("L'évènement a bien été crée.");
            }
        });
    }

    private void showSnackBar(String message) {
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
    }

    private void updateInDatabase(String name, String location, String startingDate, String endingDate, String startingTime, String endingTime, String type, String desc) {
        String eventID = (String) app.getIntent().getSerializableExtra("event_id");
        EventHelper.updateEventData(name, eventID, "mTitle");
        EventHelper.updateEventData(location, eventID, "mLocation");
        EventHelper.updateEventData(startingDate, eventID, "mStartDate");
        EventHelper.updateEventData(endingDate, eventID, "mEndDate");
        EventHelper.updateEventData(startingTime, eventID, "mStartTime");
        EventHelper.updateEventData(endingTime, eventID, "mEndTime");
        EventHelper.updateEventData(type, eventID, "mType");
        EventHelper.updateEventData(desc, eventID, "mDescription");
        showSnackBar("L'événement a été mis à jour.");
    }

    /**
     * Handles the change of option on the radio button
     * The user selects the type of the event
     * @param rg the radio group
     * @param checkedId the value of the radio button selected
     */
    public void onCheckedChange(RadioGroup rg, int checkedId) {
        Type type = null;
        switch (checkedId) {
            case R.id.radio_button_type_reunion:
                type = Type.REUNION;
                break;
            case R.id.radio_button_type_hike:
                type = Type.HIKE;
                break;
            case R.id.radio_button_type_barpi:
                type = Type.BAR_PI;
                break;
            case R.id.radio_button_type_barpi_special:
                type = Type.BAR_PI_SPECIAL;
                break;
            case R.id.radio_button_type_ope_bouffe:
                type = Type.OPE_BOUFFE;
                break;
            case R.id.radio_button_type_balpi:
                type = Type.BAL_PI;
                break;
            case R.id.radio_button_type_soiree:
                type = Type.SOIREE;
                break;
            case R.id.radio_button_type_reunion_camp:
                type = Type.REUNION_CAMP;
                break;
            case R.id.radio_button_type_camp:
                type = Type.CAMP;
                break;
            case R.id.radio_button_type_other:
                type = Type.AUTRES;
                break;
            default: /* do nothing */
        }
        this.type.set(type.toString());
    }

    /**
     * Open the date picker and handles the choice of the user
     * @param isStarting a boolean indicating if it is the starting date that is being updated, false if it is the end date
     */
    public void openDatePicker(final boolean isStarting){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                if (isStarting){
                    startDate.set(sdf.format(myCalendar.getTime()));
                } else {
                    endDate.set(sdf.format(myCalendar.getTime()));
                }
            }
        };
        new DatePickerDialog(app, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void openTimePicker(final boolean isStarting){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(app, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (isStarting){
                    startTime.set(selectedHour + ":" + selectedMinute);
                } else {
                    endTime.set(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Choisissez une heure");
        mTimePicker.show();
    }


}
