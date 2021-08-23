import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static String path;

    static {
        path = getDirectory("Введите директорию: ");

        while (!Files.exists(Paths.get(path))) {
            path = getDirectory("Директория " + path + " не существует. Пожалуйста, проверьте и введи заново: ");
        }
    }

    public static void main(String[] args) {
        FileWalker fileWalker = new FileWalker();
        fileWalker.walk(path);
        System.out.printf("Size of all files in the directory %s is %s\n", path, fileWalker.getSum());
    }

    private static String getDirectory(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}
