package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;

public class MembersFragment extends Fragment {

    private RecyclerView mMembersRecyclerView;
    private MembersAdapter mMembersAdapter;
    Intent callIntent;
    Intent emailIntent;
    private static final int REQUEST_CODE = 45;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.members_fragment, container, false);
        mMembersRecyclerView = (RecyclerView) view.findViewById(R.id.members_recycler_view);
        mMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    public void onResume(){
        super.onResume();
        //updateUI();
    }

    private void updateUI(){
        List<User> users = new ArrayList<>();
        List<DocumentSnapshot> dss = UserHelper.getAllUsers();
        for (DocumentSnapshot ds : dss){
            System.out.println("onCreateView - boucle");
            users.add(ds.toObject(User.class));
        }
        System.out.println("onCreateView");
        if (mMembersAdapter == null){
            mMembersAdapter = new MembersAdapter(users);
            mMembersRecyclerView.setAdapter(mMembersAdapter);
        } else {
            mMembersAdapter.setUsers(users);
            mMembersAdapter.notifyDataSetChanged();
        };
    }

    private class MembersHolder extends RecyclerView.ViewHolder {

        private User mUser;
        private TextView mUserName;
        private Button mCallBtn;
        private Button mEmailBtn;

        public MembersHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.member_infos_item, parent, false));
            mUserName = (TextView) itemView.findViewById(R.id.members_info_name);
            mCallBtn = (Button) itemView.findViewById(R.id.members_call);
            mEmailBtn = (Button) itemView.findViewById(R.id.members_mail);
            mCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mUser.getmPhoneNumber()));
                    performCall();
                }
            });
            mEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getmEmail()});
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("message/rfc822");
                    startActivity(emailIntent);
                }
            });
        }

        private void performCall(){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)){

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CALL_PHONE
                    }, REQUEST_CODE);
                }
            } else {
                startActivity(callIntent);
            }
        }

        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults, Intent intent) {
            switch (requestCode) {
                case REQUEST_CODE: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }
            }
        }

        public void bind(User user){
            mUser = user;
            String pseudo = user.getmTotem().equals("") ? user.getmName() : user.getmTotem();
            mUserName.setText(pseudo);
        }

    }

    private class MembersAdapter extends RecyclerView.Adapter<MembersHolder> {

        private List<User> mUsers;

        public MembersAdapter(List<User> users){
            mUsers = users;
        }

        @Override
        public MembersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MembersHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        @Override
        public void onBindViewHolder(MembersHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bind(user);
        }

        public List<User> getmUsers() {
            return mUsers;
        }

        public void setUsers(List<User> users) {
            this.mUsers = users;
        }

    }

}
