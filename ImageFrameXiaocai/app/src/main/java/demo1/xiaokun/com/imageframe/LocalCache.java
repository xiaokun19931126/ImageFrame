package demo1.xiaokun.com.imageframe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by xiaocai on 2017/1/17.
 * 本地缓存
 */

public class LocalCache {
    /**
     * 文件保存路径
     */
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/localCache";

    /**
     * 向本地SD卡写网络图片
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLacal(String url, Bitmap bitmap) {
        //由于url中有非法字符，不能直接作为文件的名字，所以需要先进行MD5
        String fileName = MD5.md5String(url);//文件名
        File file = new File(CACHE_PATH, fileName);//创建文件流，指向该路径，文件名叫做filename
        //通过得到文件的父文件，判断父文件是否存在
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {//如果父文件不存在
            parentFile.mkdirs();//创建文件夹
        }
        //把图片保存至本地
        try {
            //FileOutputStream将字节写入文件的输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //将位图的压缩版本以PNG格式100的质量写入指定的输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromLocal(String url) {
        String fileName = MD5.md5String(url);
        File file = new File(CACHE_PATH, fileName);
        try {
            //从文件读字节的输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
