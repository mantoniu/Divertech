package Si3.divertech.parking;

public enum ReservationStatus {
    ACCEPTED("ACCEPTED"),

    WAITING("WAITING"),

    REFUSE("REFUSE");

    private final String text;

    ReservationStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
