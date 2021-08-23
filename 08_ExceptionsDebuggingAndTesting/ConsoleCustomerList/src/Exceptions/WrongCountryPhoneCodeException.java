package Exceptions;

public class WrongCountryPhoneCodeException extends Exception {
    public WrongCountryPhoneCodeException(String message) {
        super(message);
    }
}
