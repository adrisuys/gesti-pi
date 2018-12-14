package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import be.he2b.esi.moblg5.g43320.gestipi.db_access.ChatHelper;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.ChatFragment;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
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

    /**
     * Creates an instance of the class
     * @param currentUser the user
     * @param activity the activity the class depends on
     */
    public ChatViewModel(User currentUser, ChatFragment activity) {
        this.currentUser = currentUser;
        this.activity = activity;
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
            ChatHelper.createMessageForChat(content.get(), currentUser);
            Toast.makeText(activity.getContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
            content.set("");
            uriImageSelected = null;
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
                uploadImage();
                Toast.makeText(activity.getContext(), "Image sélectionnée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity.getContext(), "Aucune image n'a été choisie", Toast.LENGTH_SHORT).show();
            }
            activity.setUri(uriImageSelected);
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

    private void uploadImage() {
        if (uriImageSelected != null) {
            final ProgressDialog progressDialog = new ProgressDialog(activity.getContext());
            progressDialog.setTitle("Chargement...");
            progressDialog.show();
            FirebaseStorage mStorageRef = FirebaseStorage.getInstance();
            StorageReference storage = mStorageRef.getReference();
            final StorageReference ref = storage.child(System.currentTimeMillis() + "." + getFileExtension(uriImageSelected));
            ref.putFile(uriImageSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            }, 500);
                            ChatHelper.createImageMessageForChat(content.get(), currentUser, uri.toString());
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        } else {
            Toast.makeText(activity.getContext(), "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = activity.getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}
