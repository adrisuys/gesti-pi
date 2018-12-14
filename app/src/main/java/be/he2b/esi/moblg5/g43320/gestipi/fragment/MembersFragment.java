package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.MemberInfosItemBinding;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.MembersFragmentBinding;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.MembersItemViewModel;

/**
 * Represent the sceen where the users can see all the information of the other members.
 */
public class MembersFragment extends Fragment {

    List<User> users = new ArrayList<>();
    private RecyclerView mMembersRecyclerView;
    private MembersAdapter mMembersAdapter;
    private MembersFragmentBinding membersBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        membersBinding = DataBindingUtil.inflate(inflater, R.layout.members_fragment, container, false);
        mMembersRecyclerView = membersBinding.membersRecyclerView;
        mMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMembersAdapter = new MembersAdapter(users);
        mMembersRecyclerView.setAdapter(mMembersAdapter);
        MembersItemViewModel viewModel = new MembersItemViewModel((MainActivity) getActivity(), ((MainActivity) getActivity()).getCurrentUser());
        membersBinding.setViewModel(viewModel);
        updateUI();
        return membersBinding.getRoot();
    }

    private void updateUI() {
        UserHelper.getUsersCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("MembersFragment", "Error " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc != null) {
                        User user = doc.getDocument().toObject(User.class);
                        if (doc.getType() == DocumentChange.Type.REMOVED) {
                            users.remove(user);
                        } else {
                            users.add(user);
                        }
                        mMembersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Impossible de récupérer les utilisateurs", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateUI();
    }

    public MembersAdapter getmMembersAdapter() {
        return mMembersAdapter;
    }

    private class MembersHolder extends RecyclerView.ViewHolder {

        private final MemberInfosItemBinding binding;

        /**
         * Creates a new MembersHolder
         * @param binding the binding class linked to the holder
         */
        public MembersHolder(final MemberInfosItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MembersAdapter extends RecyclerView.Adapter<MembersHolder> {

        private List<User> mUsers;
        private List<User> filtered;
        private MembersItemViewModel viewModel;

        /**
         * Create a new MembersAdapter
         * @param users the list of the members
         */
        public MembersAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public MembersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            MemberInfosItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.member_infos_item, parent, false);
            return new MembersHolder(binding);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        @Override
        public void onBindViewHolder(MembersHolder holder, int position) {
            viewModel = new MembersItemViewModel((MainActivity) getActivity(), users.get(position));
            holder.binding.setViewModel(viewModel);
        }

        public void filterList(List<User> filterdNames) {
            this.mUsers = filterdNames;
            notifyDataSetChanged();
        }
    }

    public void filter(String text){
        List<User> filterdNames = new ArrayList<>();
        for (User s : users) {
            if (s.getPseudo().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
                System.out.println(s.getPseudo());
            }
        }
        System.out.println(filterdNames.size());

        //calling a method of the adapter class and passing the filtered list
        mMembersAdapter.filterList(filterdNames);
    }

}
