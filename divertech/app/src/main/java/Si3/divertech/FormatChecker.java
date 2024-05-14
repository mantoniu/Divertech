package Si3.divertech;

import android.widget.EditText;

public class FormatChecker {

    public static boolean globalCheck(EditText field){
        return field.getText() != null && !field.getText().toString().isEmpty();
    }

    public static boolean checkMail(EditText field){
        return field.getText().toString().contains("@") && field.getText().toString().contains(".");
    }

    public static boolean checkPasswordLength(EditText field){
        return field.getText().toString().length() >= 6;
    }

    public static boolean checkPasswordDigits(EditText field){
        return field.getText().toString().matches(".*\\d.*");
    }

    public static boolean checkPhone(EditText field){
        return field.getText().toString().length() > 8;
    }
}
