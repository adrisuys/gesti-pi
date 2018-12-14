package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents a message a user can send on the chat
 */
public class Message {

    private String message;
    private String dateCreated;
    private User sender;
    private String urlImage;

    public Message(){}

    /**
     * Creates a message without image
     * @param message the content of the message
     * @param user the sender
     */
    public Message(String message, User user) {
        this.message = message;
        this.sender = user;
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        this.dateCreated = df.format(now);
    }

    /**
     * Creates a message with an image
     * @param message the content of the message
     * @param urlImage the url of the image
     * @param user the sender
     */
    public Message(String message, String urlImage, User user) {
        this.message = message;
        this.urlImage = urlImage;
        this.sender = user;
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        this.dateCreated = df.format(now);
    }

    /**
     * Returns the content
     * @return the content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the date the message was send at
     * @return the date the message was send at
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Returns the sender
     * @return the sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Returns the url of the image
     * @return the url of the image
     */
    public String getUrlImage() {
        return urlImage;
    }


}
