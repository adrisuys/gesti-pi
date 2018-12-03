package be.he2b.esi.moblg5.g43320.gestipi.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String message;
    private String dateCreated;
    private User sender;
    private String urlImage;

    public Message(){

    }

    public Message(String message, User user){
        this.message = message;
        this.sender = user;
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.dateCreated = df.format(now);
    }

    public Message(String message, String urlImage, User user){
        this.message = message;
        this.urlImage = urlImage;
        this.sender = user;
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.dateCreated = df.format(now);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
