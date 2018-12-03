package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.ChatHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import pub.devrel.easypermissions.EasyPermissions;
import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private RecyclerView mChatRecyclerView;
    private ChatAdapter mChatAdapter;
    private List<Message> messages = new ArrayList<>();
    private Button addFile;
    private Button takePhoto;
    private Button sendBtn;
    private TextInputEditText content;
    private User currentUser;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_TAKE_PHOTO_PERMS = 101;
    private Uri uriImageSelected;
    private static final int RC_CHOOSE_PHOTO = 200;
    private static final int RC_TAKE_PHOTO = 201;
    private ImageView imageViewPreview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        mChatRecyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentUser = (User) getActivity().getIntent().getSerializableExtra("currentUser");
        mChatAdapter = new ChatFragment.ChatAdapter(messages, currentUser, Glide.with(view));
        mChatRecyclerView.setAdapter(mChatAdapter);
        addFile = (Button) view.findViewById(R.id.chat_add_file_btn);
        takePhoto = (Button) view.findViewById(R.id.chat_take_photo_btn);
        sendBtn = (Button) view.findViewById(R.id.chat_send_btn);
        content = (TextInputEditText) view.findViewById(R.id.chat_input_message);
        imageViewPreview = (ImageView) view.findViewById(R.id.chat_image_preview);
        imageViewPreview.setVisibility(View.GONE);
        handleSendButton();
        handleAddFileButton();
        handleTakePhotosButton();
        updateUI();
        return view;
    }

    private void handleSendButton(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(content.getText())){
                    if (imageViewPreview.getDrawable() == null || uriImageSelected == null){
                        ChatHelper.createMessageForChat(content.getText().toString(), currentUser);
                        content.setText("");
                    } else {
                        uploadImageInFirebaseAndSend();
                        content.setText("");
                        imageViewPreview.setVisibility(View.GONE);
                        uriImageSelected = null;
                    }
                }
            }
        });
    }

    private void updateUI(){
        ChatHelper.getChatCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("ChatFragment", "Error "+e.getMessage());
                }
                for (DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    Message message = doc.getDocument().toObject(Message.class);
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        messages.add(message);
                    }
                    sortMessages();
                    mChatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sortMessages(){
        if (messages != null || !messages.isEmpty()) {
            Collections.sort(messages, new Comparator<Message>() {
                @Override
                public int compare(Message message, Message t1) {
                    try {
                        Date d1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(message.getDateCreated());
                        Date d2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t1.getDateCreated());
                        return d1.compareTo(d2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }
    }

    public void onResume(){
        super.onResume();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void handleAddFileButton(){
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromPhone();
            }
        });
    }

    private void handleTakePhotosButton(){
        takePhoto.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               takePhoto();
           }
        });
    }

    private void chooseImageFromPhone(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, RC_IMAGE_PERMS);
            }
        } else {
            startActivityForResult(intent, RC_CHOOSE_PHOTO);
        }
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.CAMERA,
                }, RC_TAKE_PHOTO_PERMS);
            }
        } else {
            startActivityForResult(intent, RC_TAKE_PHOTO);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewPreview);
                imageViewPreview.setVisibility(View.VISIBLE);
            } else {
                System.out.println("Aucune image n'a été choisie");
            }
        } else if (requestCode == RC_TAKE_PHOTO){
            if (resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                this.uriImageSelected = getUri(getContext(), bmp);
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewPreview);
                imageViewPreview.setVisibility(View.VISIBLE);
            } else {
                System.out.println("Aucune photo n'a été prise");
            }
        }
    }

    private Uri getUri(Context ctx, Bitmap bmp){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bmp, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageInFirebaseAndSend(){
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(this.uriImageSelected)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String pathImageSavedInFirebase = taskSnapshot.getMetadata().getDownloadUrl().toString();
                        String message = content.getText().toString();
                        ChatHelper.createImageMessageForChat(message, currentUser, pathImageSavedInFirebase);
                    }
                });
    }

    private void handleKeyboard(){
        mChatRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mChatRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });
    }

    private class ChatHolder extends RecyclerView.ViewHolder {

        private TextView messageContent;
        private TextView timestamps;
        private User user;
        private Message message;
        private RelativeLayout rootContainer;
        private LinearLayout messageContainer;
        private ImageView imageViewSent;
        private TextView username;

        public ChatHolder(LayoutInflater inflater, ViewGroup parent, User user){
            super(inflater.inflate(R.layout.chat_item, parent, false));
            this.user = user;
            messageContent = (TextView) itemView.findViewById(R.id.chat_item_message);
            timestamps = (TextView) itemView.findViewById(R.id.chat_item_timestamp);
            rootContainer = (RelativeLayout) itemView.findViewById(R.id.chat_item_root);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.chat_item_container);
            imageViewSent = (ImageView) itemView.findViewById(R.id.chat_item_image_sent);
            username = (TextView) itemView.findViewById(R.id.chat_item_username);
        }

        public void bind(Message message, RequestManager glide){
            if (message != null){
                this.message = message;
                messageContent.setText(message.getMessage());
                timestamps.setText(message.getDateCreated());
                if (message.getSender().getmTotem().equals("")){
                    username.setText(message.getSender().getmFirstname());
                } else {
                    username.setText(message.getSender().getmTotem());
                }
                if (message.getUrlImage() != null) {
                    glide.load(message.getUrlImage()).into(imageViewSent);
                    imageViewSent.setVisibility(View.VISIBLE);
                } else {
                    imageViewSent.setVisibility(View.GONE);
                }
                boolean isSender = message.getSender().getmId().equals(user.getmId());
                updateDesign(isSender);
            } else {
                Toast.makeText(getContext(), "Impossible d'afficher les messages", Toast.LENGTH_SHORT).show();
            }
        }

        private void updateDesign(boolean isSender){
            if (!isSender){
                messageContent.setTextColor(Color.BLACK);
                timestamps.setTextColor(Color.BLACK);
                username.setTextColor(Color.BLACK);
                messageContainer.setBackground(getResources().getDrawable(R.drawable.button_radius_white_color));
                imageViewSent.setBackground(getResources().getDrawable(R.drawable.button_radius_white_color));
            } else {
                username.setVisibility(View.GONE);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
            this.messageContainer.setLayoutParams(params);
            this.rootContainer.requestLayout();
        }

    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder>{

        private List<Message> messages;
        private User user;
        private RequestManager glide;

        public ChatAdapter(List<Message> messages, User user, RequestManager glide){
            this.user = user;
            this.messages = messages;
            this.glide = glide;
        }

        @Override
        public ChatFragment.ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ChatFragment.ChatHolder(layoutInflater, parent, user);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public void onBindViewHolder(ChatFragment.ChatHolder holder, int position) {
            Message msg = messages.get(position);
            holder.bind(msg, glide);
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

    }

}
