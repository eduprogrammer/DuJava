/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava.file;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileOperations {
    private FileCallback fileCallback;

    public FileOperations() {

    }

    public boolean extractZipFile(String srcFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(srcFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = null;
            int total = zipInputStream.available();
            int count = 0;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                count++;
                fileCallback.onReadingZipEntries(zipEntry.getName(),zipEntry.getTime(),zipEntry.getSize(),count,total);
            }

            fileInputStream.close();
            zipInputStream.close();

            fileInputStream = new FileInputStream(srcFile);
            zipInputStream = new ZipInputStream(fileInputStream);

            fileCallback.onZipExtracting();

            count = 0;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                byte[] bytes = new byte[1];
                String name = zipEntry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(name);

                while ((count = zipInputStream.read(bytes)) > 0) {
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                }

                fileOutputStream.close();
                zipInputStream.closeEntry();



                    fileCallback.onZipExtractingProgress(count,total);
            }


            fileCallback.onZipExtractingFinish();

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    public boolean createZipFile(String fileName, File[] files) {
        try {
            int count = 0;
            File file = new File(fileName + ".zip");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileInputStream fileInputStream = null;
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            for(File singleFile : files) {

                count++;

                byte[] bytes = new byte[1];
                fileInputStream = new FileInputStream(singleFile);
                ZipEntry zipEntry = new ZipEntry(singleFile.getName());
                zipOutputStream.putNextEntry(zipEntry);
                while ((count = fileInputStream.read(bytes)) > 0) {
                    zipOutputStream.write(bytes);
                    zipOutputStream.flush();

                    fileCallback.onZipCreating(count, files.length);
                }
                zipOutputStream.closeEntry();


            }
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();

            count = 0;

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    public void setFileCallback(FileCallback fileCallback) {
        this.fileCallback = fileCallback;
    }

    public String getFile(String description, String[] extensions, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description,extensions);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(stage).getPath();
    }

    public ArrayList<String> getListOfFiles(String description, String[] extensions, Stage stage) {
        ArrayList<String> res = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description,extensions);
        fileChooser.getExtensionFilters().add(extensionFilter);
        for(File file : fileChooser.showOpenMultipleDialog(stage)) {
            res.add(file.getPath());
        }

        return res;
    }

    public String saveFile(String description,String[] extensions, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description,extensions);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showSaveDialog(stage).getPath();
    }

    public String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String getSizeOfFile(String path) {
        File file = new File("");
        long size = file.length();
        if(size < 1024)
            return "" + size + " bits";
        else if(size >= 1024 && size < 1048576) {
            return "" + (size / 1024) + " kilobytes";
        } else if(size >= 1048576 && size < 1073741824)
            return "" + (size / 1048576);
        else
            return "" + size + " gigabytes";
    }

    public boolean writeToFile(byte [] input, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(input);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public byte[] readFromFile(String srcPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(srcPath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1];
            int count = 0;
            while ((count = fileInputStream.read(bytes)) > 0) {
                byteArrayOutputStream.write(bytes);
                byteArrayOutputStream.flush();
            }

            return byteArrayOutputStream.toByteArray();

        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<String> scanFilesInDir(String dirPath) {
        File file = new File(dirPath);
        if(file.exists()) {
            File[] files = file.listFiles();

            ArrayList<String> res = new ArrayList<>();
            for(File f : files) {
                res.add(f.getPath());
            }

            return res;

        } else {
            return null;
        }
    }
}
