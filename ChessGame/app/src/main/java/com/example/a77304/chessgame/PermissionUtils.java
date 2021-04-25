package com.example.a77304.chessgame;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by 77304 on 2021/4/20.
 */

public class PermissionUtils {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSIONS_STORAGE, requestCode);
                return false;
            }
        }
        return true;
    }
}
