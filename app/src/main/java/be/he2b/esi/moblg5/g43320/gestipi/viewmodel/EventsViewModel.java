package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.UpdateEventActivity;
import be.he2b.esi.moblg5.g43320.gestipi.db_access.EventHelper;
import be.he2b.esi.moblg5.g43320.gestipi.fragment.EventsFragment;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.Event;
import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

/**
 * The business class of the class that has all the events displayed in grid
 */
public class EventsViewModel extends BaseObservable implements ViewModel {

    private static final String CREATE = "0";
    private static final String UPDATE = "1";
    public final ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> type = new ObservableField<>();
    public final ObservableBoolean isChief = new ObservableBoolean();
    private final User user;
    private final MainActivity activity;
    private final List<Event> events;
    private final RecyclerView mEventsRecyclerView;
    private final EventsFragment.EventsAdapter mEventsAdapter;
    private Event event;
    private final Button eventIcon;

    /**
     * Creates an instance of the class
     * @param user the current user
     * @param activity the activity the class depends on
     * @param events the list of events
     * @param mEventsRecyclerView the recycler view
     * @param mEventsAdapter the adapter for the recycler view
     * @param eventIcon the icon of the event
     */
    public EventsViewModel(User user, MainActivity activity, List<Event> events, RecyclerView mEventsRecyclerView, EventsFragment.EventsAdapter mEventsAdapter, Button eventIcon) {
        this.activity = activity;
        this.user = user;
        this.events = events;
        this.mEventsRecyclerView = mEventsRecyclerView;
        this.mEventsAdapter = mEventsAdapter;
        isChief.set(user.isChief());
        this.eventIcon = eventIcon;
    }

    /**
     * Change the screen to the one the user can enter the infos of the new event
     */
    public void onClickAddBtn() {
        Intent intent = new Intent(activity, UpdateEventActivity.class);
        intent.putExtra("currentUser", user);
        intent.putExtra("mode", CREATE);
        activity.startActivity(intent);
    }

    /**
     * Change the screen to the one the user can update/see the infos of an event
     */
    public void onClickViewBtn() {
        Intent intent = new Intent(activity, UpdateEventActivity.class);
        intent.putExtra("mode", UPDATE);
        intent.putExtra("event_id", event.getmId());
        intent.putExtra("currentUser", user);
        activity.startActivity(intent);
    }


    /**
     * Update the the screen when resuming the application
     */
    public void onResume() {
        if (events.size() != 0) {
            updateUI();
        }
    }

    private void updateUI() {
        events.clear();
        EventHelper.getEventsCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        events.add(document.toObject(Event.class));
                    }
                } else {
                    Toast.makeText(activity, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteEventsIfPast();
        mEventsAdapter.setmEvents(events);
        mEventsRecyclerView.setAdapter(mEventsAdapter);
        mEventsAdapter.notifyDataSetChanged();
    }

    private void deleteEventsIfPast() {
        if (!events.isEmpty()) {
            for (Event e : events) {
                try {
                    Date d = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(e.getmEndDate());
                    if (d.compareTo(new Date()) < 0) {
                        events.remove(e);
                        EventHelper.deleteEvent(e.getmId());
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Link the event with the view by upgrading its title and icon
     * @param event the event
     */
    public void setEvent(Event event) {
        this.event = event;
        title.set(event.getmTitle());
        setBackgroundResource();
    }

    private void setBackgroundResource() {
        switch (event.getmType()) {
            case REUNION:
                eventIcon.setBackgroundResource(R.drawable.event_play_type);
                break;
            case HIKE:
            case CAMP:
            case REUNION_CAMP:
                eventIcon.setBackgroundResource(R.drawable.event_camp_type);
                break;
            case BAR_PI:
            case BAR_PI_SPECIAL:
                eventIcon.setBackgroundResource(R.drawable.event_drink_type);
                break;
            case OPE_BOUFFE:
                eventIcon.setBackgroundResource(R.drawable.event_cook_type);
                break;
            case SOIREE:
            case BAL_PI:
                eventIcon.setBackgroundResource(R.drawable.event_dance_type);
                break;
            default:
                eventIcon.setBackgroundResource(R.drawable.event_else_type);
        }
    }

    @Override
    public void onCreate() {

    }
}
