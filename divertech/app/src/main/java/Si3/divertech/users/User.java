package Si3.divertech.users;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String phoneNumber;
    private String language;
    private String pictureUrl = "";
    private DatabaseReference userRef;
    private UserType userType;


    public User() {
    }

    public User(String userId, String firstName, String lastName, String mail, String phoneNumber, String language, String address, String postalCode, String city, String pictureUrl, boolean isAdmin) {
        this.id = userId;
        this.email = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.pictureUrl = pictureUrl;
        this.userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        this.userType = isAdmin ? UserType.ADMIN : UserType.NORMAL;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLanguage() {
        return language;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", language='" + language + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", userRef=" + userRef +
                ", userType=" + userType +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }
}
