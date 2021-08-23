import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor extends SimpleFileVisitor {
    private Double sum = 0.0;

    public String getSum() {
        String result;
        if (sum < Math.pow(2, 10)) {
            result = sum + " byte";
        }else if (sum < Math.pow(2, 20)) {
            result = sum / Math.pow(2, 10) + " KiB";
        } else if (sum < Math.pow(2, 30)) {
            result = sum / Math.pow(2, 20) + " MiB";
        } else {
            result = sum / Math.pow(2, 30) + " GiB";
        }

        return result;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) file;
        if (Files.isRegularFile(path)) {
            sum += Files.size(path);
        }
        return FileVisitResult.CONTINUE;
    }
}
