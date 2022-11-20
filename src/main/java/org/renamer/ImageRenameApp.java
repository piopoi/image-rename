package org.renamer;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageRenameApp {
    public static void main(String[] args) {
        String FILE_DIR = "/Users/piopoi/Downloads/test/";

        FilenameFilter filenameFilter = (dir, name) -> name.contains("jpeg");

        try {
            File allFile = new File(FILE_DIR);
            File[] files = allFile.listFiles(filenameFilter);

            for (File orgFile : files) {
                String orgFileName = orgFile.getName();

                Metadata metadata = JpegMetadataReader.readMetadata(orgFile);
                Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                String creationDateStr = directory.getDescription(ExifDirectoryBase.TAG_DATETIME);

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                Date creationDate = sdf1.parse(creationDateStr);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileExt = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);
                String newFileName = sdf2.format(creationDate) + "." + fileExt;
                File newFile = new File(FILE_DIR + newFileName);

                //rename
                boolean result = orgFile.renameTo(newFile);

                if (!result) {
                    throw new RuntimeException("[ERROR] " + orgFileName);
                }
                System.out.printf("[SUCCESS] %s -> %s\n", orgFileName, newFile.getName());
            }

        } catch (IOException | JpegProcessingException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
