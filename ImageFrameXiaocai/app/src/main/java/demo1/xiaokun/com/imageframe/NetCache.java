package demo1.xiaokun.com.imageframe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/1/17.
 */

public class NetCache {
    private MemoryCache mMemoryCache;
    private LocalCache mLocalCache;

    public NetCache(MemoryCache memoryCache, LocalCache localCache) {
        mMemoryCache = memoryCache;
        mLocalCache = localCache;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
//        new BitmapTask().execute(imageView,url);//启动AsyncTask
        //启动5个线程的线程池AsyncTask
        new BitmapTask().executeOnExecutor(Executors.newFixedThreadPool(5), imageView, url);
    }

    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imageView;
        private String url;

        /**
         * 后台耗时操作存在于子线程
         *
         * @param params 参数数组
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                mLocalCache.setBitmapToLacal(url, bitmap);//缓存进本地中
                mMemoryCache.putBitmapToMemory(url, bitmap);//缓存进内存中

            }
        }
    }

    /**
     * 从网络上下载bitmap
     *
     * @param url
     * @return
     */
    private Bitmap downLoadBitmap(String url) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {//响应码等于200时代表连接成功
                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//宽和高压缩为原来的1/2
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream(), null, options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
        return null;
    }
}
