package org.example;

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

public class Main {
    public static void main(String[] args) {
        try {
            String FILE_DIR = "/Users/piopoi/Downloads/test/";
            File dir = new File(FILE_DIR);

            FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains("jpeg");
                }
            };
            File[] files = dir.listFiles(filenameFilter);

            for (File file : files) {
                Metadata metadata = JpegMetadataReader.readMetadata(file);
                Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                String creationDateStr = directory.getDescription(ExifDirectoryBase.TAG_DATETIME);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                Date date = sdf.parse(creationDateStr);
                //System.out.println(file.getName() + " : " + creationDateStr);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = sdf2.format(date);
                //System.out.println(file.getName() + " : " + newFileName);

                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                File newFile = new File(FILE_DIR + newFileName + "." + ext);
                boolean result = file.renameTo(newFile);

                if(!result) {
                    throw new RuntimeException("[ERROR] 파일명 변경 에러 발생 - " + file.getName());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JpegProcessingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
