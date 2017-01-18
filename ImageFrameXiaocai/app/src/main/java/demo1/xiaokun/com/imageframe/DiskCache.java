package demo1.xiaokun.com.imageframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/1/18.
 */

public class DiskCache {

    private static DiskLruCache mCache;

    /**
     * 创建DiskLruCache
     *
     * @param context
     */
    public static void openCache(Context context) {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                //内存卡可用
                mCache = DiskLruCache.open(context.getExternalCacheDir(), 0, 1, 10 * 1024 * 1024);
            } else {
                mCache = DiskLruCache.open(context.getCacheDir(), 0, 1, 10 * 1024 * 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片bitmap写入disk内存中
     *
     * @param bitmap 图片的bitmap
     * @param url    将url地址md5后，当作文件名
     * @throws Exception
     */
    public static void putBitmapToDisk(Bitmap bitmap, String url) throws Exception {
        if (mCache == null) throw new IllegalStateException("必须先调用openCache()方法");

        DiskLruCache.Editor editor = mCache.edit(MD5.md5String(url));
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            if (compress) {
                editor.commit();
            } else {
                editor.abort();
            }
        }
    }

    /**
     * 从disk中获取到bitmap
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static Bitmap getBitmapFromDisk(String url) throws Exception {
        if (mCache == null) throw new IllegalStateException("必须先调用openCache()方法");
        DiskLruCache.Snapshot snapshot = mCache.get(MD5.md5String(url));

        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 判断disk是否有缓存
     *
     * @param url
     * @return
     */
    public static boolean hasCache(String url) {
        try {
            return mCache.get(MD5.md5String(url)) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 同步日志
     */
    public static void syncLog() {
        try {
            mCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭DiskLruCache
     */
    public static void closeCache() {
        syncLog();
    }
}
