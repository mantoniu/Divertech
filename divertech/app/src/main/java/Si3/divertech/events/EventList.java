package Si3.divertech.events;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import Si3.divertech.DataBaseListener;
import Si3.divertech.DataBaseResponses;
import Si3.divertech.users.UserData;

public class EventList extends Observable {
    private static EventList instance;
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private final Map<String, Event> eventMap = new HashMap<>();
    private final Map<String, ValueEventListener> listenerMap = new HashMap<>();
    private static boolean initialized = false;

    private EventList() {
        requestData();
        registrationEventsListener(UserData.getInstance().getUserId());
    }

    public static EventList getInstance() {
        if (instance == null)
            instance = new EventList();
        return instance;
    }

    private void addEvent(Event event) {
        if (event == null)
            return;

        if (eventMap.containsKey(event.getId()))
            eventMap.replace(event.getId(), event);
        eventMap.put(event.getId(), event);
    }

    public boolean isInitialized() {
        return initialized;
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
                    if (containsEvent(eventId))
                        return;
                    addEvent(getEventBySnapshot(eventId, snapshot));
                    listener.onDataBaseResponse(eventId, DataBaseResponses.SUCCESS);
                    registerUserToEvent(eventId);
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

    public void registerUserToEvent(String eventId) {
        DatabaseReference registrationsRef = rootRef.child("Registrations");
        DatabaseReference newRegistrationRef = registrationsRef.push();
        newRegistrationRef.child("eventId").setValue(eventId);
        newRegistrationRef.child("userId").setValue(UserData.getInstance().getUserId());
    }

    private void requestData() {
        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        DatabaseReference userRegistrationsRef = rootRef
                .child("Registrations");

        userRegistrationsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    initialized = true;
                    setChanged();
                    notifyObservers();
                }
                for (DataSnapshot registrationSnapshot : dataSnapshot.getChildren()) {
                    String eventId = registrationSnapshot.child("eventId").getValue(String.class);
                    requestEvent(eventId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("REGISTRATIONS REQUEST ERROR : ", databaseError.getMessage());
            }
        });
    }

    private void registrationEventsListener(String userId) {
        rootRef.child("Registrations").orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!snapshot.exists())
                    return;

                String eventId = snapshot.child("eventId").getValue(String.class);
                requestEvent(eventId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!snapshot.exists())
                    return;

                String eventId = snapshot.child("eventId").getValue(String.class);
                requestEvent(eventId);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;

                String eventId = snapshot.child("eventId").getValue(String.class);
                removeEvent(eventId);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEventByDataSnapshot(String eventId, DataSnapshot dataSnapshot) {
        addEvent(getEventBySnapshot(eventId, dataSnapshot));
        initialized = true;
        Log.d("EVENT_MAP", eventMap.keySet().toString());
    }

    private Event getEventBySnapshot(String eventId, DataSnapshot snapshot) {
        if (!snapshot.exists())
            return null;

        Event event = snapshot.getValue(Event.class);
        if (event == null)
            return null;

        event.setId(eventId);
        return event;
    }

    private void requestEvent(String eventId) {
        if (eventId == null)
            return;

        DatabaseReference eventRef = rootRef
                .child("Events").child(eventId);

        if (!listenerMap.containsKey(eventId))
            eventRef.addValueEventListener(createEventListener(eventId));
    }

    private ValueEventListener createEventListener(String eventId) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addEventByDataSnapshot(eventId, dataSnapshot);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("EVENT REQUEST ERROR : ", databaseError.getMessage());
            }
        };
        listenerMap.put(eventId, listener);

        return listener;
    }

    private void removeEvent(String eventId) {
        ValueEventListener listener = listenerMap.get(eventId);

        if (listener == null)
            return;

        rootRef.child("Events").child(eventId).removeEventListener(listener);
        eventMap.remove(eventId);
        listenerMap.remove(eventId);
    }

    public void writeEvent(String eventId, String title, String pictureUrl, String shortDescription, String address, String postalCode, String city, String description, String instagramURL, String date) {
        DatabaseReference eventsRef = rootRef.child("Events");
        DatabaseReference eventRef = (eventId != null) ? eventsRef.child(eventId) : eventsRef.push();

        Log.d("antoniu => ", date);

        eventRef.child("title").setValue(title);
        if (pictureUrl != null) {
            if (getEvent(eventId).getPictureUrl() != null && !getEvent(eventId).getPictureUrl().equals(pictureUrl)) {
                FirebaseStorage.getInstance().getReferenceFromUrl(getEvent(eventId).getPictureUrl()).delete().addOnSuccessListener(task ->
                        eventRef.child("pictureUrl").setValue(pictureUrl)
                );
            }
            eventRef.child("pictureUrl").setValue(pictureUrl);
        }
        eventRef.child("shortDescription").setValue(shortDescription);
        eventRef.child("address").setValue(address);
        eventRef.child("postalCode").setValue(postalCode);
        eventRef.child("city").setValue(city);
        eventRef.child("description").setValue(description);
        eventRef.child("instagramURL").setValue(instagramURL);
        eventRef.child("date").setValue(date);

        if (eventId == null)
            registerUserToEvent(eventRef.getKey());
    }

    public void deleteAdminEvent(String eventId) {
        if (!UserData.getInstance().getConnectedUser().isAdmin())
            return;

        deleteNotificationsForEvent(eventId);
        deleteRegistrationsWithEvent(eventId);
        rootRef.child("Events").child(eventId).removeValue();
    }

    public void deleteUserEvent(String eventId) {
        //DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
        //        .child("Users").child(UserData.getInstance().getUserId()).child("Notifications").;
    }

    private void deleteUserNotificationsForEvent(String eventId) {
        //TODO
        // rootRef.child("Users").orderByChild()
    }

    private void deleteRegistrationsWithEvent(String eventId) {
        rootRef.child("Registrations").orderByChild("eventId").equalTo(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot registrationRef : snapshot.getChildren()) {
                    registrationRef.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR DELETING REGISTRATIONS WITH EVENT_ID :", eventId, error.toException());
            }
        });
    }

    private void deleteNotificationsForEvent(String eventId) {
        rootRef.child("Notifications").orderByChild("eventId").equalTo(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                            String notificationId = notificationSnapshot.getKey();
                            removeNotificationForUsers(notificationId);
                            notificationSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("ERROR DELETING NOTIFICATIONS WITH EVENT_ID :", eventId, error.toException());
                    }
                });
    }

    private void removeNotificationForUsers(String notificationId) {
        rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = snapshot.getKey();
                if (userId == null)
                    return;
                rootRef.child("Users").child(userId).child("notifications").child(notificationId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR DELETING USER NOTIFICATION : ", notificationId, error.toException());
            }
        });
    }

    public void reset() {
        instance = null;
    }
}
