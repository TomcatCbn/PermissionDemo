package lint.permission.netease.com.permissiondemo;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCamera();
        Log.d("123", "123");
    }

    private void showCamera() {
//        checkPermission();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void checkPermission() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        externalStorageDirectory = Environment.getExternalStoragePublicDirectory("aaa");
    }

    private void checkAAPermission() {

    }
}
