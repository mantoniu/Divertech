package Si3.divertech.users;

import androidx.annotation.NonNull;

public class User {
    private String id;
    private String email;
    private String name;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String language;

    private UserType userType;

    public User() {
    }

    public User(String id, String email, String name, String lastName, String address, String phoneNumber, String language, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.userType = isAdmin ? UserType.ADMIN : UserType.NORMAL;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
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

    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }

    public UserType getUserType() {
        return userType;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", language='" + language + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
