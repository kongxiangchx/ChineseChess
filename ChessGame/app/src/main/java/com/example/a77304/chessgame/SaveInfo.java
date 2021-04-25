package com.example.a77304.chessgame;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by 77304 on 2021/4/19.
 */

public class SaveInfo {
    public static void SerializeChessInfo(ChessInfo chessInfo, String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(chessInfo);
        oo.close();
    }

    public static ChessInfo DeserializeChessInfo(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        ChessInfo chessInfo = (ChessInfo) ois.readObject();
        return chessInfo;
    }

    public static void SerializeInfoSet(InfoSet infoSet, String name) throws FileNotFoundException, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(infoSet);
        oo.close();
    }

    public static InfoSet DeserializeInfoSet(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        InfoSet infoSet = (InfoSet) ois.readObject();
        return infoSet;
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File targetDir = new File(rootDir, "ChessGame");
            File f = new File(targetDir, strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
