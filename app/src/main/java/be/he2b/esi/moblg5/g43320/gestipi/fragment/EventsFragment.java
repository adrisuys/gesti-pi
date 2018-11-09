package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.api.UserHelper;
import be.he2b.esi.moblg5.g43320.gestipi.model.Event;
import be.he2b.esi.moblg5.g43320.gestipi.model.User;

public class EventsFragment extends Fragment {

    private RecyclerView mEventsRecyclerView;
    private EventsAdapter mEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.events_fragment, container, false);
        mEventsRecyclerView = (RecyclerView) view.findViewById(R.id.members_recycler_view);
        mEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        //
    }

    private class EventsHolder extends RecyclerView.ViewHolder {

        private Event mEvent;
        private TextView mEventTitle;
        private Button mEventIcon;

        public EventsHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.member_infos_item, parent, false));
            mEventTitle = (TextView) itemView.findViewById(R.id.events_title);
            mEventIcon = (Button) itemView.findViewById(R.id.events_icon);
            mEventIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // do some
                }
            });
        }

        public void bind(Event event){
            mEvent = event;
            mEventTitle.setText(event.getmTitle());
            setButtonIcons();
        }

        private void setButtonIcons(){
            switch (mEvent.getmType()){
                case REUNION:
                    mEventIcon.setBackgroundResource(R.drawable.event_play_type);
                    break;
                case HIKE: case CAMP: case REUNION_CAMP:
                    mEventIcon.setBackgroundResource(R.drawable.event_camp_type);
                    break;
                case BAR_PI: case BAR_PI_SPECIAL:
                    mEventIcon.setBackgroundResource(R.drawable.event_drink_type);
                    break;
                case OPE_BOUFFE:
                    mEventIcon.setBackgroundResource(R.drawable.event_cook_type);
                    break;
                case SOIREE: case BAL_PI:
                    mEventIcon.setBackgroundResource(R.drawable.event_dance_type);
                    break;
                default:
                    mEventIcon.setBackgroundResource(R.drawable.event_else_type);
            }
        }

    }

    private class EventsAdapter extends RecyclerView.Adapter<EventsHolder>{

        private List<Event> mEvents;

        public EventsAdapter(List<Event> events){
            mEvents = events;
            Collections.sort(mEvents, new Comparator<Event>(){
                public int compare(Event e1, Event e2) {
                    return e1.getStartingDate().compareTo(e2.getStartingDate());
                }
            });
        }

        @Override
        public EventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventsHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        @Override
        public void onBindViewHolder(EventsHolder holder, int position) {
            Event event = mEvents.get(position);
            holder.bind(event);
        }

        public List<Event> getmEvents() {
            return mEvents;
        }

        public void setEvents(List<Event> events) {
            this.mEvents = events;
            Collections.sort(mEvents, new Comparator<Event>(){
                public int compare(Event e1, Event e2) {
                    return e1.getStartingDate().compareTo(e2.getStartingDate());
                }
            });
        }
    }
}
