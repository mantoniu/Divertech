package Si3.divertech;

import androidx.annotation.NonNull;

public class User {
    private String id;
    private String email;
    private String name;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String language;

    public User() {
    }

    public User(String id, String email, String name, String lastName, String address, String phoneNumber, String language) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.language = language;
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
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }
}
