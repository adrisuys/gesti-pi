package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import be.he2b.esi.moblg5.g43320.gestipi.db_access.ChatHelper;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.ChatFragment;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

import static android.app.Activity.RESULT_OK;

/**
 * The business class of the screen that contains the chat group
 */
public class ChatViewModel extends BaseObservable implements ViewModel {

    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    public final ObservableField<String> content = new ObservableField<>();
    private final ObservableBoolean mImageSelected = new ObservableBoolean();
    private Uri uriImageSelected;
    private final User currentUser;
    private final ChatFragment activity;
    private final ImageView imageView;

    /**
     * Creates an instance of the class
     * @param currentUser the user
     * @param activity the activity the class depends on
     * @param imageView the imageview that stocks a preview of the image selected by the user
     */
    public ChatViewModel(User currentUser, ChatFragment activity, ImageView imageView) {
        this.currentUser = currentUser;
        this.activity = activity;
        this.imageView = imageView;
    }

    /**
     * Send a message and save it in the DB
     */
    public void handleSendButton() {
        try{
            InputMethodManager imm = (InputMethodManager) activity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(content.get())) {
            if (!mImageSelected.get() || uriImageSelected == null) {
                ChatHelper.createMessageForChat(content.get(), currentUser);
                Toast.makeText(activity.getContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
                content.set("");
            } else {
                //uploadImageInFirebaseAndSend();
                content.set("");
                uriImageSelected = null;
            }
            mImageSelected.set(false);
        } else {
            Toast.makeText(activity.getContext(), "Vous devez entrer du texte !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Allows the user to select a picture in the phone gallery
     */
    public void handleAddFileButton() {
        chooseImageFromPhone();
    }

    /**
     * Handles the response after the user has selected an image
     * @param requestCode the requestCode
     * @param resultCode the resultCode
     * @param data the data
     */
    public void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                mImageSelected.set(true);
                Glide.with(activity)
                     .load(uriImageSelected)
                     .apply(RequestOptions.circleCropTransform())
                     .into(imageView);
                Toast.makeText(activity.getContext(), "Image sélectionnée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity.getContext(), "Aucune image n'a été choisie", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseImageFromPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity.getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, RC_IMAGE_PERMS);
            }
        } else {
            activity.startActivityForResult(intent, RC_CHOOSE_PHOTO);
        }
    }

    private void uploadImageInFirebaseAndSend() {
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(this.uriImageSelected)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String pathImageSavedInFirebase = taskSnapshot.getMetadata().getDownloadUrl().toString();
                        String message = content.get();
                        ChatHelper.createImageMessageForChat(message, currentUser, pathImageSavedInFirebase);
                    }
                });
        Toast.makeText(activity.getContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
