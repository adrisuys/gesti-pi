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

public class EventHelper {

    private static final String COLLECTION_NAME = "/events";
    private static List<DocumentSnapshot> events = new ArrayList<>();

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getEventsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createEvent(/*String mId,*/ String mTitle, String mLocation, String mStartDate, String mStartTime, String mEndDate, String mEndTime, String mDescription, Type mType, String mImportance) {
        Event eventToCreate = new Event(mTitle, mLocation, mStartDate, mStartTime, mEndDate, mEndTime, mDescription, mType, mImportance);
        return EventHelper.getEventsCollection().add(eventToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getEvent(String uid){
        return EventHelper.getEventsCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateEventData(String value, String uid, String field) {
        return EventHelper.getEventsCollection().document(uid).update(field, value);
    }

    // --- DELETE ---

    public static Task<Void> deleteEvent(String uid) {
        return EventHelper.getEventsCollection().document(uid).delete();
    }


}
