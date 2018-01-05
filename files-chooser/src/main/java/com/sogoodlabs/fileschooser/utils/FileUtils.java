package com.sogoodlabs.fileschooser.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 10.08.2017.
 */

public class FileUtils {

    public static List<File> filesInDirectory(String dir){
        List<File> result = new ArrayList<>();
        File directory = new File(dir);
        File[] files = directory.listFiles();
        for(File file : files){
            result.add(file);
        }
        return result;
    }

    public static HashMap<String, List<File>> filesInDirectoryHMap(String dir){
        HashMap<String, List<File>> result = new HashMap<String, List<File>>();
        result.put("files", new ArrayList<File>());
        result.put("dirs", new ArrayList<File>());
        File directory = new File(dir);
        File[] files = directory.listFiles();
        if(files!=null) {
            for (File file : files) {
                if (file.isDirectory()) result.get("dirs").add(file);
                else result.get("files").add(file);
            }
        }
        return result;
    }

    public static String getExtension(String file){
        String extension = "";

        int i = file.lastIndexOf('.');
        int p = Math.max(file.lastIndexOf('/'), file.lastIndexOf('\\'));

        if (i > p) {
            extension = file.substring(i+1);
        }
        return extension;
    }

}
