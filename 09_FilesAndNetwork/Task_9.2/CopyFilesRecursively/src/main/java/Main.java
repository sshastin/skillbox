import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static String from;
    private static String to;


    static {
        from = getDirectory("Введите директорию для копирования: ");

        while (!Files.exists(Paths.get(from))) {
            from = getDirectory("Директория " + from + " не существует. Пожалуйста, проверьте и введи заново: ");
        }

        to = getDirectory("Введите директорию назначения: ");
    }

    public static void main(String[] args) {

        try {
            FileCopyWalker.copyRecursively(from, to);
            System.out.printf("Директория %s скопирована в директорию %s успешно.\n", from, to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getDirectory(String message) {
        System.out.println(message);
        String string = scanner.nextLine();
        return string;
    }
}
