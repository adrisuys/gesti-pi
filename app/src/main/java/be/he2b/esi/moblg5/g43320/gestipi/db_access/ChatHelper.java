package be.he2b.esi.moblg5.g43320.gestipi.db_access;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

public class ChatHelper {

    private static final String COLLECTION_NAME = "/chats";

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender){
        Message message = new Message(textMessage, userSender);
        return ChatHelper.getChatCollection().add(message);
    }

    public static Task<DocumentReference> createImageMessageForChat(String textMessage, User userSender, String url){
        Message message = new Message(textMessage, url, userSender);
        return ChatHelper.getChatCollection().add(message);
    }
}
