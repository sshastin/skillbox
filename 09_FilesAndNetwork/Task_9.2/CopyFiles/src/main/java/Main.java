import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Path sourcePath = Paths.get(getDirectory("Введите директорию для копирования: "));

        while (!Files.exists(sourcePath)) {
            sourcePath = Paths.get(getDirectory("Директория " + sourcePath.toString() + " не существует. Пожалуйста, проверьте и введи заново: "));
        }

        Path destinationPath = Paths.get(getDirectory("Введите директорию назначения: "));

        MyFileVisitor myFileVisitor = new MyFileVisitor(sourcePath, destinationPath);

        try {
            Files.walkFileTree(sourcePath, myFileVisitor);
            System.out.printf("Директория %s скопирована в директорию %s успешно.\n", sourcePath.toString(), destinationPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getDirectory(String message) {
        System.out.println(message);
        String string = scanner.nextLine();
        return string;
    }
}
