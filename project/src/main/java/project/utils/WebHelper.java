package project.utils;

import com.typesafe.config.ConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.data.classes.ParliamentFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class WebHelper {
    private static final String protocollUrl = "https://search.dip.bundestag.de/api/v1/plenarprotokoll?apikey=GmEPb1B.bfqJLIhcGAsH9fTJevTglhFpCoZyAAAdhp&format=xml";

    private static final String dtdUrl = "https://www.bundestag.de/resource/blob/575720/70d7f2af6e4bebd9a550d9dc4bc03900/dbtplenarprotokoll-data.dtd";
    private static final String period20Url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354";

    private static final String period19Url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410";

    private static final String baseUrl = "https://www.bundestag.de";
    private static final String xmlFolder = System.getenv("USERPROFILE") + "/Documents/protocolXml";


    private WebHelper() {

    }

    private static void createXmlFolder() throws IOException {
        Path xmlFolderPath = Paths.get(xmlFolder);
        if (!Files.exists(xmlFolderPath)) {
            Files.createDirectory(xmlFolderPath);
        }
    }

    public static List<File> getDownloadedXmlFiles() {
        return Stream.of(new File(xmlFolder).listFiles()).filter(file -> !file.isDirectory() && file.getName().contains(".xml")).collect(Collectors.toList());
    }

    public static List<Path> downloadProtocols() throws IOException {
        createXmlFolder();
        List<String> urls = getProtocolsUrls();
        List<Path> downloadedFiles = new ArrayList<>();
        urls.forEach(url -> {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            try {
                Path path = Paths.get(xmlFolder + "/" + fileName);
                if (!Files.exists(path)) {
                    InputStream in = getStream(url);
                    Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Downloading " + fileName);
                }
                ParliamentFactory.getInstance().loadPlenaryProtocolFromPath(path);
                downloadedFiles.add(path);
            } catch (IOException e) {
                System.out.println("Failed to download: " + e.getMessage());
            }
        });
        return downloadedFiles;
    }

    private static InputStream getStream(String urlString) throws IOException {
        //Sleeping so the API doesn't think it is an attack
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
        }
        return new URL(urlString).openStream();
    }


    public static List<String> getProtocolsUrls() throws IOException {
        List<String> urls = new ArrayList<>();
        addProtocolUrlsForLink(urls, period19Url);
        System.out.println("Loaded links for 19 Period");
        addProtocolUrlsForLink(urls, period20Url);
        System.out.println("Loaded links for 20 Period");
        return urls;
    }

    private static void addProtocolUrlsForLink(List<String> urlList, String link) throws IOException {
        int offset = 0;
        int setSize = 0;
        do {
            setSize = urlList.size();
            String url = link + "?offset=" + String.valueOf(offset);
            BufferedReader htmlReader = new BufferedReader(new InputStreamReader(getStream(url)));
            htmlReader.lines().filter(WebHelper::containsHref).map(WebHelper::getFileName).forEach(urlList::add);
            offset += 10;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        } while (urlList.size() != setSize);
    }

    private static boolean containsHref(String line) {
        return line.contains("href");
    }

    private static String getFileName(String line) {
        String endPart = "data.xml";
        return baseUrl + line.substring(line.indexOf("/resource"), line.indexOf(endPart) + endPart.length());
    }

}
