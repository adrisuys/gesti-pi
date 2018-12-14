package be.he2b.esi.moblg5.g43320.gestipi.db_access;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * Handles all the methods to interact with the collection CHATS of Firebase
 */
public class ChatHelper {

    private static final String COLLECTION_NAME = "/chats";

    /**
     * Get the collection corresponding to the string COLLECTION_NAME
     * @return the collection corresponding to the string COLLECTION_NAME
     */
    public static CollectionReference getChatCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**
     * Create a SMS message and insert it in the DB
     * @param textMessage the content of the message
     * @param userSender the sender of the message
     * @return a Task<DocumentReference>
     */
    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender) {
        Message message = new Message(textMessage, userSender);
        return ChatHelper.getChatCollection().add(message);
    }

    /**
     * Create a MMS message and insert it in the DB
     * @param textMessage the content of the message
     * @param userSender the sender of the message
     * @param url the url of the picture in the message
     * @return a Task<DocumentReference>
     */
    public static Task<DocumentReference> createImageMessageForChat(String textMessage, User userSender, String url) {
        Message message = new Message(textMessage, url, userSender);
        return ChatHelper.getChatCollection().add(message);
    }
}
