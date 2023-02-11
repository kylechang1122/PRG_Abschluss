package project.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class FileHelper {
    private static final String mdbZipUrl = "https://www.bundestag.de/resource/blob/472878/4d360eba29319547ed7fce385335a326/MdB-Stammdaten-data.zip";
    private FileHelper(){

    }


    public static Set<File> getMDBXMLFilesFromWeb() throws IOException {
        Set<File> xmlFiles = new HashSet<>();
        ZipInputStream zipStream = new ZipInputStream(new URL(mdbZipUrl).openStream());
        ZipEntry entry = null;
        while((entry = zipStream.getNextEntry()) != null){
            xmlFiles.add(createXMLFile(entry,zipStream));
        }
        zipStream.closeEntry();
        zipStream.close();
        return xmlFiles;
    }

    private static File createXMLFile(ZipEntry entry,ZipInputStream zipStream) throws IOException{
        String[] fileName = entry.getName().split("\\.");
        File xmlFile = new File(entry.getName());
        FileOutputStream xmlFileStream = new FileOutputStream(xmlFile);
        int readBytes = 0;
        byte[] buffer = new byte[1024];
        while((readBytes = zipStream.read(buffer)) > 0 ){
            xmlFileStream.write(buffer,0,readBytes);
        }
        xmlFileStream.close();
        return xmlFile;
    }
}
