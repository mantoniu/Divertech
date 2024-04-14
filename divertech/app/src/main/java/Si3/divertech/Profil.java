package Si3.divertech;

public class Profil {
    String id;
    String name;
    String lastName;
    String profilPicture;
    String email;
    String phoneNumber;
    String language;

    public Profil(String id, String name, String lastName, String profilPicture,String email, String phoneNumber,String language) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.profilPicture = profilPicture;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilPicture() {
        return profilPicture;
    }
}
