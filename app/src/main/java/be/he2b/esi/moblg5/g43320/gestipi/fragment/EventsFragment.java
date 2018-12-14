package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.EventsFragmentBinding;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.EventsItemBinding;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.EventHelper;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Event;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Type;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.EventsViewModel;

/**
 * Represents the screen on which the users can see all the events.
 */
public class EventsFragment extends Fragment {

    private static final String UPDATE = "1";
    private static final String CREATE = "0";
    private RecyclerView mEventsRecyclerView;
    private EventsFragmentBinding binding;
    private EventsAdapter mEventsAdapter;
    private List<Event> events = new ArrayList<>();
    private User currentUser;
    private EventsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUser = ((MainActivity) getActivity()).getCurrentUser();
        binding = DataBindingUtil.inflate(inflater, R.layout.events_fragment, container, false);
        View view = binding.getRoot();
        mEventsRecyclerView = binding.eventsRecyclerView;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mEventsRecyclerView.setLayoutManager(layoutManager);
        mEventsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(4), true));
        mEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventsRecyclerView.setNestedScrollingEnabled(false);
        mEventsAdapter = new EventsAdapter(events, currentUser);
        mEventsRecyclerView.setAdapter(mEventsAdapter);
        viewModel = new EventsViewModel(currentUser, (MainActivity) getActivity(), events, mEventsRecyclerView, mEventsAdapter, null);
        binding.setViewModel(viewModel);
        updateOnEvent();
        return view;
    }

    private void updateOnEvent() {
        EventHelper.getEventsCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("EventsFragment", "Error " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        try {
                            Event event = getEventFromSnapshot(doc.getDocument());
                            events.add(event);
                            mEventsAdapter.notifyDataSetChanged();
                        } catch (NullPointerException ex) {
                            Toast.makeText(getContext(), "Impossible d'afficher les évènements", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    private Event getEventFromSnapshot(DocumentSnapshot document) {
        if (document == null) {
            throw new NullPointerException();
        } else {
            String id = (String) document.get("mId");
            String title = (String) document.get("mTitle");
            String location = (String) document.get("mLocation");
            String startDate = (String) document.get("mStartDate");
            String startTime = (String) document.get("mStartTime");
            String endDate = (String) document.get("mEndDate");
            String endTime = (String) document.get("mEndTime");
            String desc = (String) document.get("mDescription");
            String typeStr = (String) document.get("mType");
            String importance = (String) document.get("mImportance");
            Type type = Type.valueOf(typeStr);
            return new Event(id, title, location, startDate, startTime, endDate, endTime, desc, type);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    /**
     * A Class that handles the decoration of the GridSpacingItem
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        /**
         * Create a new GridSpacingItemDecoration
         * @param spanCount the number of span
         * @param spacing the value of the spacing
         * @param includeEdge a boolean indicating if the edge is included or not
         */
        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private class EventsHolder extends RecyclerView.ViewHolder {

        private EventsItemBinding binding;

        /**
         * Creates a new EventsHolder
         * @param binding the binding class linked to the holder
         */
        public EventsHolder(final EventsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Handles all the item that sits in the recyclerView (the differents events)
     */
    public class EventsAdapter extends RecyclerView.Adapter<EventsHolder> {

        private List<Event> mEvents;
        private User mUser;
        private EventsViewModel viewModel;

        /**
         * Creates a new EventsAdapter
         * @param events the list of events
         * @param user the current user
         */
        public EventsAdapter(List<Event> events, User user) {
            mEvents = events;
            mUser = user;
            Collections.sort(mEvents, new Comparator<Event>() {
                public int compare(Event e1, Event e2) {
                    return e1.getStartingDate().compareTo(e2.getStartingDate());
                }
            });
        }

        @Override
        public EventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            EventsItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.events_item, parent, false);
            return new EventsHolder(binding);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        @Override
        public void onBindViewHolder(EventsHolder holder, int position) {
            Button button = holder.binding.eventsIcon;
            viewModel = new EventsViewModel(mUser, (MainActivity) getActivity(), events, mEventsRecyclerView, mEventsAdapter, button);
            viewModel.setEvent(mEvents.get(position));
            holder.binding.setViewModel(viewModel);
        }

        /**
         * Set the new lists of events and sort it chronologically
         * @param events
         */
        public void setmEvents(List<Event> events) {
            mEvents = events;
            if (mEvents != null || !mEvents.isEmpty()) {
                Collections.sort(mEvents, new Comparator<Event>() {
                    public int compare(Event e1, Event e2) {
                        return e1.getStartingDate().compareTo(e2.getStartingDate());
                    }
                });
            }
        }

    }
}
