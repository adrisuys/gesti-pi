package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.RequestManager;
import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class of a group chat bubble
 */
public class ChatItemViewModel extends BaseObservable implements ViewModel {


    public final ObservableField<String> messageContent = new ObservableField<>();
    public final ObservableField<String> timestamps = new ObservableField<>();
    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableBoolean hasImage = new ObservableBoolean();
    public final ObservableBoolean isSenderUser = new ObservableBoolean();
    private final User user;
    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final RelativeLayout rootContainer;
    private final LinearLayout messageContainer;

    /**
     * Creates an instance of the class
     * @param message the message
     * @param glide the glide that handles the picture
     * @param user the user
     * @param mainActivity the activity the class depends on
     * @param imageView the imageView that contains the image if there is a image
     * @param rootContainer the relative layout that contains the UI elements
     * @param messageContainer the linear layout that contains the different texts
     */
    public ChatItemViewModel(Message message, RequestManager glide, User user, MainActivity mainActivity, ImageView imageView, RelativeLayout rootContainer, LinearLayout messageContainer){
        this.user = user;
        this.mainActivity = mainActivity;
        this.imageView = imageView;
        this.rootContainer = rootContainer;
        this.messageContainer = messageContainer;
        bind(message, glide);
    }

    private void bind(Message message, RequestManager glide) {
        if (message != null) {
            messageContent.set(message.getMessage());
            timestamps.set(message.getDateCreated());
            if (message.getSender().getmTotem().equals("")) {
                username.set(message.getSender().getmFirstname());
            } else {
                username.set(message.getSender().getmTotem());
            }
            if (message.getUrlImage() != null) {
                hasImage.set(true);
                glide.load(message.getUrlImage()).into(imageView);
            } else {
                hasImage.set(false);
            }
            boolean isSender = message.getSender().getmId().equals(user.getmId());
            isSenderUser.set(isSender);
            updateUI(isSender);
        } else {
            Toast.makeText(mainActivity, "Impossible d'afficher les messages", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(boolean isSender){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        this.messageContainer.setLayoutParams(params);
        this.rootContainer.requestLayout();

    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
