package com.example.everybooks.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Util
{

    public String bitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return temp;
    }

    public Bitmap stringToBitmap(String encodedString)
    {
        try
        {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

    public String stringFromCalendar(Calendar cal)
    {
        // 날짜를 통신용 문자열로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(cal.getTime());
    }

    public static String getOnlyKor(String str)
    {
        String textWithoutTag = str.replaceAll("&nbsp;", " ");
        textWithoutTag = textWithoutTag.replaceAll("&rsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("&lsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        return textWithoutTag;
    }


}
