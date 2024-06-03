package Si3.divertech.parking;

import androidx.annotation.NonNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import Si3.divertech.utils.DateUtils;

public class Reservations {
    private String id;
    private String eventId;
    private String licencePlate;

    private ZonedDateTime date;

    private String adress;

    private String phone;

    private String userCreatorId;

    private ReservationStatus status;


    public Reservations() {
    }

    public Reservations(String id, String eventId, String licencePlate, String phone, String address, String date, String userCreatorId) {
        this.id = id;
        this.eventId = eventId;
        this.licencePlate = licencePlate;
        this.phone = phone;
        this.adress = address;
        this.date = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        this.userCreatorId = userCreatorId;
        this.status = ReservationStatus.WAITING;
    }

    public Reservations(String id, String eventId, String licencePlate, String phone, String address, String date, String userCreatorId, ReservationStatus status) {
        this.id = id;
        this.eventId = eventId;
        this.licencePlate = licencePlate;
        this.phone = phone;
        this.adress = address;
        this.date = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        this.userCreatorId = userCreatorId;
        this.status = status;
    }

    public Reservations(String eventId, String licencePlate, String phone, String address, String date, String userCreatorId) {
        this.eventId = eventId;
        this.licencePlate = licencePlate;
        this.phone = phone;
        this.adress = address;
        this.date = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        this.userCreatorId = userCreatorId;
        this.status = ReservationStatus.WAITING;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getDate() {
        return DateUtils.formatZonedDateWithoutTime(date);
    }

    public String getAddress() {
        return adress;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getId() {
        return id;
    }

    public ReservationStatus getStatus() {
        return status;
    }


    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", licence plate='" + licencePlate + '\'' +
                ", date='" + date + '\'' +
                ", phone number='" + phone + '\'' +
                ", adress='" + adress + '\'' +
                ", user id='" + userCreatorId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
