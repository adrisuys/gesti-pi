package be.he2b.esi.moblg5.g43320.gestipi.db_access;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.Event;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Type;

/**
 * Handles all the methods to interact with the collection EVENTS of Firebase
 */
public class EventHelper {

    private static final String COLLECTION_NAME = "/events";
    private static List<DocumentSnapshot> events = new ArrayList<>();

    /**
     * Get the collection corresponding to the string COLLECTION_NAME
     * @return the collection corresponding to the string COLLECTION_NAME
     */
    public static CollectionReference getEventsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**
     * Creates an event and inserts it in the DB.
     * @param mTitle the name
     * @param mLocation the location
     * @param mStartDate the start date
     * @param mStartTime the start time
     * @param mEndDate the end date
     * @param mEndTime the end time
     * @param mDescription the description
     * @param mType the type
     * @param mImportance the importance
     * @return a Task<DocumentReference>
     */
    public static Task<DocumentReference> createEvent(/*String mId,*/ String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType) {
        Event eventToCreate = new Event(mTitle, mLocation, mStartDate, mStartTime, mEndDate, mEndTime, mDescription, mType);
        return EventHelper.getEventsCollection().add(eventToCreate);
    }

    /**
     * Get the event that has the id corresponding to the attribute.
     * @param uid the id of the event
     * @return a Task<DocumentSnapshot> containing the event
     */
    public static Task<DocumentSnapshot> getEvent(String uid) {
        return EventHelper.getEventsCollection().document(uid).get();
    }

    /**
     * Update an event
     * @param value the new value of the event field
     * @param uid the id of the event to be updated
     * @param field the field that will be updated
     * @return a Task<Void>
     */
    public static Task<Void> updateEventData(String value, String uid, String field) {
        return EventHelper.getEventsCollection().document(uid).update(field, value);
    }

    /**
     * Deletes an event
     * @param uid the id of the event to be deleted
     * @return a Task<Void>
     */
    public static Task<Void> deleteEvent(String uid) {
        return EventHelper.getEventsCollection().document(uid).delete();
    }


}
