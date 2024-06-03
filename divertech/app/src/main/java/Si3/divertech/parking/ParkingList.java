package Si3.divertech.parking;

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
import java.util.Objects;
import java.util.Observable;
import java.util.Optional;

import Si3.divertech.events.EventList;
import Si3.divertech.users.UserData;

public class ParkingList extends Observable {
    private static ParkingList instance;
    private final Map<String, Reservations> ReservationsMap = new HashMap<>();

    private ParkingList() {
        requestData();
    }

    public static ParkingList getInstance() {
        if (instance == null)
            instance = new ParkingList();
        return instance;
    }

    public Reservations getReservations(String ReservationsId) {
        return ReservationsMap.get(ReservationsId);
    }

    public List<Reservations> getReservations() {
        return new ArrayList<>(ReservationsMap.values());
    }

    private void addReservations(Reservations Reservations) {
        ReservationsMap.put(Reservations.getId(), Reservations);
    }

    public void requestData() {
        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        DatabaseReference userReservationssRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId).child("Reservations");


        userReservationssRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReservationsMap.clear();
                if (!snapshot.exists()) {
                    setChanged();
                    notifyObservers();
                }
                Log.d("testok", snapshot.toString());
                for (DataSnapshot registrationSnapshot : snapshot.getChildren()) {
                    String ReservationsId = registrationSnapshot.getValue(String.class);
                    if (ReservationsId == null)
                        return;

                    DatabaseReference ReservationsRef = FirebaseDatabase.getInstance().getReference()
                            .child("Reservations").child(ReservationsId);
                    Log.d("testok", ReservationsRef.toString());
                    ReservationsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String eventId = snapshot.child("eventId").getValue(String.class);
                            String licencePlate = snapshot.child("licencePlate").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);
                            String phone = snapshot.child("phone").getValue(String.class);
                            String date = snapshot.child("date").getValue(String.class);
                            String userCreatorId = snapshot.child("userCreatorId").getValue(String.class);
                            Log.d("test", snapshot.toString());
                            ReservationStatus status = ReservationStatus.valueOf(snapshot.child("status").getValue(String.class));

                            if (UserData.getInstance().getConnectedUser().isAdmin() && status == ReservationStatus.WAITING) {
                                Reservations Reservations = new Reservations(ReservationsId, eventId, licencePlate, phone, address, date, userCreatorId);
                                Log.d("Reservations", Reservations.toString());
                                addReservations(Reservations);
                                setChanged();
                                notifyObservers();
                            } else if (!UserData.getInstance().getConnectedUser().isAdmin()) {
                                Reservations Reservations = new Reservations(ReservationsId, eventId, licencePlate, phone, address, date, userCreatorId, status);
                                Log.d("Reservations", Reservations.toString());
                                addReservations(Reservations);
                                setChanged();
                                notifyObservers();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("TAG", error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });
    }

    public void acceptReservation(String id) {
        FirebaseDatabase.getInstance().getReference().child("Reservations/" + id + "/status").setValue(ReservationStatus.ACCEPTED.getText());
        ParkingList.getInstance().ReservationsMap.remove(id);
        setChanged();
        notifyObservers();
    }

    public void refuseReservation(String id) {
        FirebaseDatabase.getInstance().getReference().child("Reservations/" + id + "/status").setValue(ReservationStatus.REFUSE.getText());
        ParkingList.getInstance().ReservationsMap.remove(id);
        setChanged();
        notifyObservers();
    }

    public void addReservation(String eventId, String licencePlate, String phoneNumber, String address, String date, String userId) {
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference().child("Reservations").push();
        reservationRef.child("eventId").setValue(eventId);
        reservationRef.child("address").setValue(address);
        reservationRef.child("date").setValue(date);
        reservationRef.child("phone").setValue(phoneNumber);
        reservationRef.child("licencePlate").setValue(licencePlate);
        reservationRef.child("userCreatorId").setValue(userId);
        reservationRef.child("status").setValue("WAITING");
        DatabaseReference userReservationRef = FirebaseDatabase.getInstance().getReference().child("Users/" + userId).child("Reservations").push();
        userReservationRef.setValue(reservationRef.getKey());
        DatabaseReference userCreatorReservationRef = FirebaseDatabase.getInstance().getReference().child("Users/" + EventList.getInstance().getEvent(eventId).getOrganizer()).child("Reservations").push();
        userCreatorReservationRef.setValue(reservationRef.getKey());
        requestData();
        Log.d("test", ReservationsMap.toString());
    }

    public Optional<Reservations> existReservation(String id) {
        Log.d("test", ReservationsMap.toString());
        return ReservationsMap.values().stream().filter(reservation -> Objects.equals(reservation.getEventId(), id)).findFirst();
    }

    public void reset() {
        instance = null;
    }
}
