package Si3.divertech;

import android.util.Log;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Si3.divertech.qr_reader.DataBaseListener;

public class ListEvent {
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private static final Map<String, Event> eventMap = new HashMap<>();
    private static BaseAdapter adapter;

    public static void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    public static Map<String, Event> getEventMap(){
        return eventMap;
    }

    public static Event getEvent(String eventId) {
        return eventMap.get(eventId);
    }

    public static boolean containsEvent(String eventId) {
        return eventMap.containsKey(eventId);
    }

    public static void eventExists(String eventId, DataBaseListener listener) {
        DatabaseReference eventsRef = rootRef.child("Events").child(eventId);

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event == null)
                        return;

                    event.setId(eventId);
                    ListEvent.addEvent(event);
                    listener.onDataBaseResponse(eventId, true);
                    Log.d("UNIQUE", "");
                } else {
                    listener.onDataBaseResponse(eventId, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });
    }

    public static void requestEvent(String eventId) {
        Log.d("AJOUT REG", "");
        DatabaseReference registrationsRef = rootRef.child("Registrations");
        DatabaseReference newRegistrationRef = registrationsRef.push();
        newRegistrationRef.child("eventId").setValue(eventId);
        newRegistrationRef.child("userId").setValue(UserData.getUserId());
    }

    public static void setAdapter(BaseAdapter adapter) {
        ListEvent.adapter = adapter;
    }

    public static void requestData() {
        String userId = UserData.getUserId();
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

                            ListEvent.addEvent(event);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("TAG", databaseError.getMessage());
                        }
                    });
                }
                NotificationList.requestData();
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });
    }
}
