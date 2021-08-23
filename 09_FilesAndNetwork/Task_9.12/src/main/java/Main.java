import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String urlPath = "https://rbc.ru";
        String imgDirPath = getPath.apply("Введите адрес папки для изображений: ");
        String imgUrl = null;
        File imgDir = new File(imgDirPath);
        Document document;
        Elements images = null;
        if (!imgDir.exists())
            imgDir.mkdir();

        try {
            document = Jsoup.connect(urlPath).get();
            images = document.getElementsByTag("img");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert images != null;
        for (Element element : images) {
            try {
                imgUrl = element.absUrl("src");
                if (!validateImage.apply(imgUrl)) {
                    System.out.println(imgUrl + " not an image!");
                    continue;
                }
                downloadImageByURL.accept(new URL(imgUrl), imgDirPath);
            } catch (Exception ex) {
                System.err.println(imgUrl + " download failed!");
                continue;
            }
            System.out.println(imgUrl + " download complete!");
        }
    }

    private static Function<String, String> getImageName = src -> {
        int index = src.lastIndexOf("/");
        if (index == src.length()) {
            src = src.substring(1, index);
        }
        index = src.lastIndexOf("/");
        return src.substring(index);
    };

    private static BiConsumer<URL, String> downloadImageByURL = (url, dir) -> {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] readBuffer = new byte[(int) Math.pow(2, 10)];
        try {
            inputStream = url.openStream();
            outputStream = new BufferedOutputStream(new FileOutputStream(dir + getImageName.apply(url.toString())));
            while (inputStream.read(readBuffer) != -1) {
                outputStream.write(readBuffer);
            }
        } catch (IOException ignored) {
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private static Function<String, String> getPath = message -> {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    };

    private static Function<String, Boolean> validateImage = path -> {
        String patternString = "^.+\\.(png|gif|jpe?g|svg)$";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(path);
        return matcher.find();
    };
}