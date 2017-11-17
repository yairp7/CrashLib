package com.pech.crashlib.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yairp on 16/10/2017.
 */

public class FileManager
{
    private static final String TAG = "FileManager";

    private static final String MAIN_FOLDER = "CrashLib";
    private static final String REPORTS_FOLDER = "Reports";

    public static void createFolders()
    {
        String mainPath = getMainPath();
        String reportsPath = getReportsPath();

        File mainFolder = new File(mainPath);
        File reportsFolder = new File(reportsPath);

        boolean success = true;
        if(!mainFolder.exists())
            success = mainFolder.mkdirs();

        if(success)
        {
            if(!reportsFolder.exists())
                success = reportsFolder.mkdirs();

            if(!success)
                Log.d(TAG, "Failed creating reports folder!");
        }
        else
            Log.d(TAG, "Failed creating main folder!");
    }

    public static boolean delete(String file)
    {
        File fileToDelete = new File(file);
        return fileToDelete.delete();
    }

    public static boolean append(String file, String data)
    {
        String filePath = file;

        try(FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(data);
        }
        catch(IOException e)
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public static List<String> readFiles(String dir)
    {
        List<String> files = new ArrayList<>();

        File dirFile = new File(dir);

        for(File file : dirFile.listFiles())
        {
            StringBuilder fileData = new StringBuilder();

            try(FileReader fr = new FileReader(file.getPath());
                BufferedReader br = new BufferedReader(fr))
            {
                fileData.append(br.readLine());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            files.add(fileData.toString());
        }

        return files;
    }

    public static String getMainPath()
    {
        return Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER + "/";
    }

    public static String getReportsPath()
    {
        return getMainPath() + REPORTS_FOLDER + "/";
    }
}
