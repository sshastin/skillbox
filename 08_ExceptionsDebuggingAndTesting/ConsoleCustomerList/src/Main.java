
import Exceptions.PhoneNumberNotValidException;
import Exceptions.WrongCountryPhoneCodeException;
import Exceptions.ZeroStorageLengthException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static String addCommand = "add Василий Петров " +
            "vasily.petrov@gmail.com +79215637722";
    private static String commandExamples = "\t" + addCommand + "\n" +
            "\tlist\n\tcount\n\tremove Василий Петров";
    private static String commandError = "Wrong command! Available command examples: \n" +
            commandExamples;
    private static String helpText = "Command examples:\n" + commandExamples;

    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(System.in);
        CustomerStorage executor = new CustomerStorage();
        for (; ; ) {
            String command = scanner.nextLine();
            String[] tokens = command.split("\\s+", 2);

            if (tokens[0].equals("add")) {
                try {
                    executor.addCustomer(tokens[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Правильный синтаксис: " + addCommand);
                } catch (WrongCountryPhoneCodeException e) {
                    System.out.println(e.getMessage());
                } catch (PhoneNumberNotValidException e) {
                    System.out.println(e.getMessage());
                }
            } else if (tokens[0].equals("list")) {
                try {
                    executor.listCustomers();
                } catch (ZeroStorageLengthException e) {
                    System.out.println(e.getMessage());
                }
            } else if (tokens[0].equals("remove")) {
                try {
                    executor.removeCustomer(tokens[1]);
                } catch (NoSuchElementException e) {
                    System.out.println(e.getMessage());
                }
            } else if (tokens[0].equals("count")) {
                System.out.println("There are " + executor.getCount() + " customers");
            } else if (tokens[0].equals("help")) {
                System.out.println(helpText);
            } else if (tokens[0].equals("exit")) {
                return;
            } else {
                System.out.println(commandError);
            }
        }
    }
}
