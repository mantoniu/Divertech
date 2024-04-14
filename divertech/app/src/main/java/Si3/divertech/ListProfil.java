package Si3.divertech;

import java.util.HashMap;
import java.util.Map;



public class ListProfil {

    private static final Profil profil0 = new Profil("1", "Hubert", "Nico", String.valueOf(R.drawable.image_default), "voitremmeyeive-3633@yopmail.com", "+33168310344", "Français");
    private static final Profil profil1 = new Profil("1", "Jean Eude", "Martel", String.valueOf(R.drawable.image_default), "jauddeuprolepoi-8785@yopmail.com", "+33540386690", "Anglais");
    private static final Profil profil2 = new Profil("2", "Nicole", "Jeanette", String.valueOf(R.drawable.image_default), "xacaleikoiya-1260@yopmail.com", "+33636833453", "Français");
    private static final Map<String, Profil> profilMap = new HashMap<String, Profil>() {{
        put("0", profil0);
        put("1", profil1);
        put("2", profil2);
    }};

    public static Map<String, Profil> getProfilMap() {
        return profilMap;
    }

    public static void addProfil(Profil profil) {
        profilMap.put(profil.getId(), profil);
    }

    public static void addProfil(String id, String name, String lastName, String profilPicture,String email, String phoneNumber,String language) {
        Profil profil = new Profil( id, name, lastName,  profilPicture, email, phoneNumber,language);
        addProfil(profil);
    }

    public static String generateId() {
        return String.valueOf(profilMap.keySet().stream().map(Integer::parseInt).max(Integer::compare).get() + 1);
    }
}