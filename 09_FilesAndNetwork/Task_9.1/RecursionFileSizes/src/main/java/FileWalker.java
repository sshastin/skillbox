import java.io.File;

public class FileWalker {
    private static Double sum = 0.0;

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

    public void walk(String path) {
        File file = new File(path);

        try {
            File[] files = file.listFiles();

            if (files == null)
                return;

            for (File f : files) {
                if (f.isDirectory()) {
                    walk(f.getAbsolutePath());
                } else {
                    sum += f.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
