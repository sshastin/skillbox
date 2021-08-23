import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor extends SimpleFileVisitor {
    private Path source;
    private Path destination;

    public MyFileVisitor(Path source, Path destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) file;
        Path newDestination = destination.resolve(source.relativize(path));

        try {
            Files.copy(path, newDestination, StandardCopyOption.REPLACE_EXISTING);
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        Path path = (Path) dir;
        Path newDestination = destination.resolve(source.relativize(path));

        try {
            Files.copy(path, newDestination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileVisitResult.CONTINUE;
    }
}
