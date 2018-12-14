package be.he2b.esi.moblg5.g43320.gestipi.db_access;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * Handles all the methods to interact with the collection USER of Firebase
 */
public class UserHelper {

    private static final String COLLECTION_NAME = "/users";
    private static List<DocumentSnapshot> users = new ArrayList<>();

    /**
     * Get the collection corresponding to the string COLLECTION_NAME
     * @return the collection corresponding to the string COLLECTION_NAME
     */
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**
     * Creates a user and inserts it in the db
     * @param uid the id of the user
     * @param uTotem the totem of the user
     * @param username the name of the user
     * @param userFirstName the firstname of the user
     * @param userEmail the email of the user
     * @param userPhone the phone number of the user
     * @param userGroup the group the user belongs to
     * @return a Task<Void>
     */
    public static Task<Void> createUser(String uid, String uTotem, String username, String userFirstName, String userEmail, String userPhone, String userGroup) {
        User userToCreate = new User(uid, uTotem, username, userFirstName, userEmail, userPhone, userGroup);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    /**
     *
     * @param uid
     * @return
     */
    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    /**
     * Update an user
     * @param value the new value to give to the user
     * @param uid the id of the user to be updated
     * @param field the field of the user to be updated
     * @return a Task<Void>
     */
    public static Task<Void> updateUserData(String value, String uid, String field) {
        return UserHelper.getUsersCollection().document(uid).update(field, value);
    }

    /**
     * Change the parameter indicating if the user is a chief
     * @param uid the id of the user
     * @param isChecked a boolean indicating if the user is a chief
     */
    public static void updateUserChief(String uid, boolean isChecked) {
        UserHelper.getUsersCollection().document(uid).update("chief", isChecked);
    }
}
