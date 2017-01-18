package demo1.xiaokun.com.imageframe;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/1/17.
 */

public class ImageLoader {

    private final LocalCache localCache;
    private final MemoryCache memoryCache;
    private final NetCache netCache;

    public ImageLoader() {
        localCache = new LocalCache();
        memoryCache = new MemoryCache();
        netCache = new NetCache(memoryCache, localCache);
    }

    public void displayImage(ImageView imageView, String url) {
        Bitmap bitmap;
        bitmap = memoryCache.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("xiaocai", "从内存中获取图片");
        }
        bitmap = localCache.getBitmapFromLocal(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("xiaocai", "从本地中获取图片");
            memoryCache.putBitmapToMemory(url, bitmap);
        }

        netCache.getBitmapFromNet(imageView, url);
    }
}
