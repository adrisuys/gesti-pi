package be.he2b.esi.moblg5.g43320.gestipi.db_access;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "/users";
    private static List<DocumentSnapshot> users = new ArrayList<>();

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String uTotem, String username, String userFirstName, String userEmail, String userPhone, String userGroup) {
        User userToCreate = new User(uid, uTotem, username, userFirstName, userEmail, userPhone, userGroup);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserData(String value, String uid, String field) {
        return UserHelper.getUsersCollection().document(uid).update(field, value);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    public static void updateUserChief(String uid, boolean isChecked) {
        UserHelper.getUsersCollection().document(uid).update("chief", isChecked);
    }
}
