package com.example.kwonkiseokee.setwallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final int PICKER = 3;

    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;

    //아래 권한은 쓰기 권한인데, 굳이 권한을 요청하지 않고 매니패스트에서 써놓기만해도 됨. 블로그 참조 http://gun0912.tistory.com/55
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    //현재의 배경화면을 가져오는 버튼
    private Button getCurrentWallpaper;

    //이미지를 배경화면으로 지정해주는 버튼
    private Button setAsWallpaper;


    //현재 배경화면 보여줄 이미지뷰
    private ImageView ViewCurrentWallpaper;

    //배경화면으로 지정할 이미지를 보여주는 이미지뷰
    private ImageView ViewSetAsWallpaper;

    //리사이클러뷰에서 사진을 선택하는 액티비티로 넘어감.
    Button goPickRecyclerview;

    Context context;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Uri imageLocation = data.getData();


        try {
            if (requestCode == PICKER && resultCode == RESULT_OK) {
                //앨범에서 선택한 이미지 파일의 주소를 가지고 온다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocation);
                int imageSize = (int)bitmap.getHeight() * (1024 / bitmap.getWidth());
                if(imageSize >= 10000){
                    Toast.makeText(MainActivity.this,"이미지가 너무 큽니다.", Toast.LENGTH_SHORT).show();
                }

            }
            //이미지 뷰에 띄운다
            ViewSetAsWallpaper.setImageURI(imageLocation);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //사용자가 권한 요청을 허용했을 때
                } else {
                    Toast.makeText(MainActivity.this, "읽기 허용하라우", Toast.LENGTH_SHORT).show();
                    //사용자가 권한 요청을 거부했을 때

                }
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //사용자가 권한 요청을 허용했을 때
                } else {
                    //사용자가 권한 요청을 거부했을 대
                    Toast.makeText(MainActivity.this, "쓰기 허용하라우!", Toast.LENGTH_SHORT).show();
                }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //읽기 권한 체크
        int ReadpermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int WritepermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        goPickRecyclerview = findViewById(R.id.goRecyclerPicks);
        ViewCurrentWallpaper = findViewById(R.id.ViewCurrentWallpaper);
        ViewSetAsWallpaper = findViewById(R.id.ViewSetAsWallpaper);

        //기기에 권한이 있는지 없는지 체크하는 조건문
        if (ReadpermissionCheck == PackageManager.PERMISSION_DENIED) {
            //권한이 없는 경우, 없으므로 권한 요청 메세지 띄운다.
            //요청 메세지가 띄워지면 '허용'과 '거절'이 보이는데 둘중 어느 것을 누르냐에 대한 처리는 onRequestPermissionsResult에서 처리한다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);

        } else {
            //권한이 있는 경우
            Toast.makeText(MainActivity.this, "읽기 권한이 있었구만?", Toast.LENGTH_SHORT).show();

        }

        if (WritepermissionCheck == PackageManager.PERMISSION_DENIED) {
            //권한이 없는 경우, 없으므로 권한 요청 메세지 띄운다.
            //요청 메세지가 띄워지면 '허용'과 '거절'이 보이는데 둘중 어느 것을 누르냐에 대한 처리는 onRequestPermissionsResult에서 처리한다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            //권한이 있는 경우
            Toast.makeText(MainActivity.this, "쓰기 권한이 있었구만?", Toast.LENGTH_SHORT).show();
        }


        //현재 기기의 배경화면을 불러오는 버튼 불러오기 및 현재 배경화면 불러오는 버튼
        getCurrentWallpaper = findViewById(R.id.getCurrentWallpaper);
        getCurrentWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable wallpaperDrawable = wallpaperManager.getDrawable();

                ViewCurrentWallpaper.setImageDrawable(wallpaperDrawable);
            }
        });

        //배경화면으로 지정할 이미지뷰 지정 및 배경화면으로 지정하는 버튼
        setAsWallpaper = findViewById(R.id.setAsWallpaper);
        setAsWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setWallpaper setter = new setWallpaper();
               setter.setter(ViewSetAsWallpaper, context);
            }
        });

        //이미지 뷰 클릭시 앨범에서 사진을 가지고 올 수 있도록
        ViewSetAsWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "사진 선택하기"), PICKER);

            }
        });

        goPickRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, pickon_recyclerview.class));
            }
        });


    }
}
