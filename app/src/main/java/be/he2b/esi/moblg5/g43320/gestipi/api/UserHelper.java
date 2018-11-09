package be.he2b.esi.moblg5.g43320.gestipi.api;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.model.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
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

    // --- GET ALL USER ---

    public static List<DocumentSnapshot> getAllUsers(){
        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users = task.getResult().getDocuments();
                        }
                    }
                });
        return users;
    }

    /*private static User getUserFromDocument(DocumentSnapshot doc) {
        String id = (String) doc.get("mId");
        String totem = (String) doc.get("mTotem");
        String name = (String) doc.get("mName");
        String firstname = (String) doc.get("mFirstname");
        String email = (String) doc.get("mEmail");
        String phone = (String) doc.get("mPhoneNumber");
        String group = (String) doc.get("mGroup");
        boolean isChief = (boolean) doc.get("chief");
        return new User(id, totem, name, firstname, email, phone, group, isChief);
    }*/

    /*public static List<User> getUserFromGroup(final String group){
        getUsersCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful())){
                            for (DocumentSnapshot document : task.getResult()){
                                if (getUserFromDocument(document).getmGroup().equals(group)){
                                    users.add(getUserFromDocument(document));
                                }
                            }
                        }
                    }
                });
        return users;
    }*/

    public static void updateUserChief(String uid, boolean isChecked) {
        UserHelper.getUsersCollection().document(uid).update("chief", isChecked);
    }
}
