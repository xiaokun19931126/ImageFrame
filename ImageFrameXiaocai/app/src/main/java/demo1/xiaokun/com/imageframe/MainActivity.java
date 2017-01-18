package demo1.xiaokun.com.imageframe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout activityMain;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.displayImage(image,"http://img2.imgtn.bdimg.com/it/u=2320967390,3159767866&fm=23&gp=0.jpg");
    }

    private void initView() {
        activityMain = (RelativeLayout) findViewById(R.id.activity_main);
        image = (ImageView) findViewById(R.id.image);
    }
}
