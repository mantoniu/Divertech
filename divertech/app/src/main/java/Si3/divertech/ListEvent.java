package Si3.divertech;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ListEvent extends Observable {
    private static ListEvent instance;
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private static final Map<String, Event> eventMap = new HashMap<>();

    private ListEvent() {
        requestData();
    }

    public static ListEvent getInstance() {
        if (instance == null)
            instance = new ListEvent();
        return instance;
    }

    private void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(eventMap.values());
    }

    public Event getEvent(String eventId) {
        return eventMap.get(eventId);
    }

    public boolean containsEvent(String eventId) {
        return eventMap.containsKey(eventId);
    }

    public void eventExists(String eventId, DataBaseListener listener) {
        DatabaseReference eventsRef = rootRef.child("Events").child(eventId);

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event == null)
                        return;

                    event.setId(eventId);
                    addEvent(event);
                    listener.onDataBaseResponse(eventId, DataBaseResponses.SUCCESS);
                } else
                    listener.onDataBaseResponse(eventId, DataBaseResponses.EVENT_DOES_NOT_EXIST);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EVENT EXISTS REQUEST ERROR : ", error.getMessage());
                listener.onDataBaseResponse(eventId, DataBaseResponses.ERROR);
            }
        });
    }

    public void requestEvent(String eventId) {
        DatabaseReference registrationsRef = rootRef.child("Registrations");
        DatabaseReference newRegistrationRef = registrationsRef.push();
        newRegistrationRef.child("eventId").setValue(eventId);
        newRegistrationRef.child("userId").setValue(UserData.getInstance().getUserId());
    }

    public void requestData() {
        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        DatabaseReference userRegistrationsRef = rootRef
                .child("Registrations");

        userRegistrationsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventMap.clear();
                for (DataSnapshot registrationSnapshot : dataSnapshot.getChildren()) {
                    String eventId = registrationSnapshot.child("eventId").getValue(String.class);
                    if (eventId == null)
                        return;

                    DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference()
                            .child("Events").child(eventId);

                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Event event = dataSnapshot.getValue(Event.class);
                            if (event == null)
                                return;

                            event.setId(eventId);
                            Log.d("EVENT", event.toString());

                            addEvent(event);
                            setChanged();
                            notifyObservers();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("EVENT REQUEST ERROR : ", databaseError.getMessage());
                        }
                    });
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("REGISTRATIONS REQUEST ERROR : ", databaseError.getMessage());
            }
        });
    }
}
