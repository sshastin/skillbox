import Exceptions.EmailNotValidException;
import Exceptions.PhoneNumberNotValidException;
import Exceptions.WrongCountryPhoneCodeException;
import Exceptions.ZeroStorageLengthException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CustomerStorage {
    private HashMap<String, Customer> storage;

    EmailValidator emailValidator = EmailValidator.getInstance(true, true);
    Scanner scanner = new Scanner(System.in);

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) throws EmailNotValidException, WrongCountryPhoneCodeException, PhoneNumberNotValidException {
        String[] components = data.split("\\s+");
        if (components.length != 4) {
            throw new ArrayIndexOutOfBoundsException("Команда add была введена неправильно.");
        }

        if (!emailValidator.isValid(components[2])) {
            throw new EmailNotValidException("Email " + components[2] + "not valid. Please check the email and try again.");
        }

        if (components[3].charAt(0) != '+' && Character.isDigit(components[3].charAt(0))) {
            System.out.print("Пожалуйста, уточните код страны: ");
            Integer countryPhoneCode = 0;

            try {
                countryPhoneCode = scanner.nextInt();
            } catch (InputMismatchException e) {
                throw new WrongCountryPhoneCodeException("Вы ввели неправильный код страны. Проверьте номер и попробуйте снова.");
            }
            components[3] = components[3].replaceFirst("\\d", countryPhoneCode.toString());
        } else {
            components[3] = components[3].replaceFirst(".", "");
        }

        try {
            Long.parseLong(components[3]);
        } catch (NumberFormatException e) {
            throw new PhoneNumberNotValidException("Вы ввели неправильный номер. Проверьте номер и попробуйте снова.");
        }

        String name = components[0] + " " + components[1];
        storage.put(name, new Customer(name, components[3], components[2]));
    }

    public void listCustomers() throws ZeroStorageLengthException {
        if (storage.size() == 0) {
            throw new ZeroStorageLengthException("Список пуст.");
        }
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) throws NoSuchElementException {
        if (!storage.containsKey(name)) {
            throw new NoSuchElementException("Такой записи не существует.");
        }
        storage.remove(name);
    }

    public int getCount() {
        return storage.size();
    }
}