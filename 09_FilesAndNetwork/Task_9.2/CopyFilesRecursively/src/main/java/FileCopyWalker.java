import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileCopyWalker {

    public static void copyRecursively(String pathFrom, String pathTo) throws FileNotFoundException {
        File fileFrom = new File(pathFrom);
        File fileTo = new File(pathTo);
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        String[] files;

        try {

            files = fileFrom.list();

            if (fileFrom.isDirectory()) {

                if (!fileTo.exists()) {
                    fileTo.mkdir();
                }

                for (String f : files) {
                    File destination = new File(fileTo, f);
                    File source = new File(fileFrom, f);
                    copyRecursively(source.getAbsolutePath(), destination.getAbsolutePath());
                }
            } else {
                fileInputStream = new FileInputStream(fileFrom);
                fileOutputStream = new FileOutputStream(fileTo);

                byte[] buffer = new byte[(int) (10 * Math.pow(2, 20))];
                int read;

                while ((read = fileInputStream.read(buffer)) >= 0) {
                    fileOutputStream.write(buffer, 0, read);
                }

                if (fileInputStream != null) {
                    fileInputStream.close();
                }

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
