package com.example.lulin.todolist.Utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 复制assets下的默认头像
 * 到sd卡包目录的工具类
 */
public class FileUtils {

    public void copyData(Context context) {
        InputStream in = null;
        FileOutputStream out = null;
        String path = context.getFilesDir().getAbsolutePath() + "/default_head.png"; // data/data目录
        Log.i("MainActivity", path);
        File file = new File(path);
        if (!file.exists()) {
            try
            {
                in = context.getAssets().open("default_head.png"); // 从assets目录下复制
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1)
                {
                    out.write(buf, 0, length);
                }
                out.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally{
                if (in != null)
                {
                    try {

                        in.close();

                    } catch (IOException e1) {

                        // TODO Auto-generated catch block

                        e1.printStackTrace();
                    }
                }
                if (out != null)
                {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

}
