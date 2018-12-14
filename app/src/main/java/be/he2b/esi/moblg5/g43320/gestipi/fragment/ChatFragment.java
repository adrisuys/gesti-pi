package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ChatFragmentBinding;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.ChatItemBinding;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.ChatHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Message;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.ChatItemViewModel;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.ChatViewModel;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * The screen of the group chat
 */
public class ChatFragment extends Fragment {

    private ChatAdapter mChatAdapter;
    private final List<Message> messages = new ArrayList<>();
    private ChatViewModel viewModel;
    private Uri myUri;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);
        View view = binding.getRoot();
        RecyclerView mChatRecyclerView = binding.chatRecyclerView;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        mChatRecyclerView.setLayoutManager(llm);
        User currentUser = (User) getActivity().getIntent().getSerializableExtra("currentUser");
        mChatAdapter = new ChatAdapter(messages, currentUser, Glide.with(view));
        mChatRecyclerView.setAdapter(mChatAdapter);
        viewModel = new ChatViewModel(currentUser, this);
        binding.setViewModel(viewModel);
        updateUI();
        return view;
    }

    private void updateUI() {
        ChatHelper.getChatCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("ChatFragment", "Error " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    Message message = doc.getDocument().toObject(Message.class);
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        messages.add(message);
                    }
                    sortMessages();
                    mChatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sortMessages() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                try {
                    Date d1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).parse(message.getDateCreated());
                    Date d2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).parse(t1.getDateCreated());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        Collections.reverse(messages);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.handleResponse(requestCode, resultCode, data);
    }

    public void setUri(Uri myUri){
        this.myUri = myUri;
    }

    public Uri getUri(){
        return myUri;
    }

    private class ChatHolder extends RecyclerView.ViewHolder {

        private final ChatItemBinding binding;

        ChatHolder(ChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

        private final List<Message> messages;
        private final User user;
        private final RequestManager glide;
        private ChatItemViewModel viewModel;

        /**
         * Creates an instance of the ChatAdapter
         * @param messages the lists of all the messages
         * @param user the current user
         * @param glide the Glide to handle the picture
         */
        ChatAdapter(List<Message> messages, User user, RequestManager glide) {
            this.user = user;
            this.messages = messages;
            this.glide = glide;
        }

        @NonNull
        @Override
        public ChatFragment.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            ChatItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.chat_item, parent, false);
            return new ChatHolder(binding);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ChatFragment.ChatHolder holder, int position) {
            ImageView imageView = holder.binding.chatItemImageSent;
            RelativeLayout rootContainer = holder.binding.chatItemRoot;
            LinearLayout messageContainer = holder.binding.chatItemContainer;
            viewModel = new ChatItemViewModel(messages.get(position), glide, user, (MainActivity) getActivity(), imageView, rootContainer, messageContainer);
            holder.binding.setViewModel(viewModel);
        }

    }

}
