/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 * @author Francesc Perez
 * @version 1.1.008
 */
public class DataValidation {

    public static boolean isNumber(char c) {
        return (48 <= c && c <= 57);
    }

    public static boolean isLetter(char c) {
        //The name can contain uppercase and lowercase letters, whitespace, 
        //hyphens and code control
//        return (97 <= c && c <= 122) || (65 <= c && c <= 90) || (c == 32) || (c == 45);
        return Character.isLetter(c) || c == 32 || c == 45;
    }

    public static String calculateNifLetter(String nifNoLetter) {
        String[] letter = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B",
            "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};
        return nifNoLetter + letter[Integer.parseInt(nifNoLetter) % 23];
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

public static boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber != null && phoneNumber.matches("^\\+?[0-9]{9,15}$");
}


    public static boolean isValidPostalCode(String postalCode) {
        String postalCodeRegex = "^(\\d{5})(?:[-\\s]?\\d{4})?$";
        return postalCode != null && postalCode.matches(postalCodeRegex);
    }

}
