package com.example.originsofworldwari;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFile {
    private static LogFile instance;
    private String path;
    private File file;

    private LogFile(String pathInput){
        this.path = pathInput;
        this.file = new File(path);
    }

    public static LogFile getInstance(String pathInput){
        if (instance == null)
            instance = new LogFile(pathInput);
        return instance;
    }

    public static LogFile getInstance(){
        if (instance == null)
            instance = new LogFile("activities.log");
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void WriteFile(String msg) {
        try(FileWriter fw = new FileWriter(this.path, true);
            BufferedWriter buffW = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(buffW)) {
            out.println(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void ClearFile(){
        if (this.file.exists())
            file.delete();
    }
}
