package demo1.xiaokun.com.imageframe;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by xiaocai on 2017/1/17.
 * 内存缓存
 */

public class MemoryCache {
    //    private HashMap<String, Bitmap> mMemoryCache = new HashMap<>();//1.因为强引用容易造成内存溢出，所以考虑使用下面弱引用的做法
//    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();//2.因为在Android2.3后，系统会优先回收弱引用对象，官方提出使用LruCache
    private LruCache<String, Bitmap> mMemoryCache = null;

    public MemoryCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();//得到手机最大允许内存
        long maxSize = maxMemory / 8;//得到回收内存的临界值
        mMemoryCache = new LruCache<String, Bitmap>((int) maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 从内存中读图片
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemory(String key) {
//        Bitmap bitmap = mMemoryCache.get(key);1.强引用范例
        /*2.若引用范例
        SoftReference<Bitmap> bitmapSoftReference = mMemoryCache.get(key);
        if (bitmapSoftReference != null) {
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }
        */
        Bitmap bitmap = mMemoryCache.get(key);
        return bitmap;
    }

    /**
     * 将图片写入内存中
     *
     * @param key
     * @param bitmap
     */
    public void putBitmapToMemory(String key, Bitmap bitmap) {
//        mMemoryCache.put(key,bitmap);1.强引用范例
//        mMemoryCache.put(key,new SoftReference<>(bitmap));2.若引用范例
        mMemoryCache.put(key, bitmap);
    }
}