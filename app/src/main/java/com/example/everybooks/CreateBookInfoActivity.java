package com.example.everybooks;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateBookInfoActivity extends AppCompatActivity {

    // 뷰 요소 선언
    ImageView imageView_img_book;
    TextView  textView_save;

    EditText editText_title;
    EditText editText_writer;
    EditText editText_publisher;
    EditText editText_publish_date;

    // 리스너 선언
    View.OnClickListener click;

    // 리사이클러뷰 어댑터 선언
    ToReadBookAdapter adapter;

    // 인텐트 requestCode 상수
    final int TAKE_PICTURE = 1000;
    final int OPEN_GALLERY = 1001;


    Intent intent;

    @Override
    protected void onStart() {
        super.onStart();

       /* if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 화면 정의
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book_info);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        imageView_img_book = findViewById(R.id.add_photo);
        editText_title = findViewById(R.id.title);
        editText_writer = findViewById(R.id.writer);
        editText_publisher = findViewById(R.id.publisher);
        editText_publish_date = findViewById(R.id.publish_date);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.save:
                        // save 클릭했을 때 수행할 동작

                        // 임시
                        Book book = new Book();
                        book.setImg(imageView_img_book.getDrawable());
                        book.setTitle(editText_title.getText().toString());
                        book.setWriter(editText_writer.getText().toString());
                        book.setPublisher(editText_publisher.getText().toString());
                        book.setPublishDate(editText_publish_date.getText().toString());

                        adapter = new ToReadBookAdapter();
                        adapter.addItem(book);

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();

                        break;

                    case R.id.add_photo:

                        // 책 추가 이미지 클릭하면 팝업 메뉴 띄우기
                        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                        getMenuInflater().inflate(R.menu.img_menu, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                switch(menuItem.getItemId())
                                {
                                    case R.id.camera :
                                        //openCamera();
                                        dispatchTakePictureIntent();
                                        break;

                                    case R.id.gallery :
                                        openGallery();
                                        break;

                                    case R.id.basic :
                                        imageView_img_book.setImageResource(R.drawable.icon_book);
                                        break;
                                }
                                return true;
                            }
                        });

                     popupMenu.show();// 팝업 메뉴 보이기
                }
            }
        };

        // 각 요소가 클릭되면 수행
        textView_save.setOnClickListener(click);
        imageView_img_book.setOnClickListener(click);

    }// end onCreate()


    // 카메라 실행 메소드
    private void openCamera()
    {
        // 권한 체크
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted()
            {
                // 권한 허가 상태일 때 암묵적 인텐트를 사용해서 카메라 어플을 이용한다.
                Toast.makeText(getApplicationContext(), "권한 허가",Toast.LENGTH_SHORT).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions)
            {
                // 권한 허가하지 않았을 때 안내 메세지를 보여준다.
                Toast.makeText(getApplicationContext(), "권한을 허용하지 않으면 서비스를 이용할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
        };

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 접근 권한이 필요해요")
                .setDeniedMessage(" [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

    }// end openCamera();

    // 갤러리 실행 메소드
    public void openGallery()
    {
        // 권한 체크
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted()
            {
                // 권한 허가 상태일 때
                Toast.makeText(getApplicationContext(), "권한 허가",Toast.LENGTH_SHORT).show();

                // 갤러리 열기
               /* Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Get Album"), OPEN_GALLERY);*/

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, OPEN_GALLERY);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions)
            {
                // 권한 허가하지 않았을 때
                Toast.makeText(getApplicationContext(), "권한을 허용하지 않으면 서비스를 이용할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
        };

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(" 갤러리 접근 권한이 필요합니다.")
                .setDeniedMessage(" [설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    //! 주석 추가하기
        // 암묵적 인텐트 응답 결과에 따라 동작 실행
        switch (requestCode) {

            case TAKE_PICTURE:

                // 사진을 찍었을 때 이미지 뷰에 보여준다
                if(resultCode != RESULT_CANCELED)
                {
                    if (resultCode == RESULT_OK && intent.hasExtra("data") ) {
                        // 데이터를 수신해서 비트맵으로 형변환하고
                        Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                        if (bitmap != null) // null 이 아니라면
                        {
                            imageView_img_book.setImageBitmap(bitmap);  // 이미지뷰에 그려준다.
                        }
                    }
                }

                break;

            case OPEN_GALLERY:

                // 이미지 뷰에 보여주기
                if(requestCode == OPEN_GALLERY && resultCode == RESULT_OK)
                {
                   try
                    {
                        InputStream is = getContentResolver().openInputStream(intent.getData());
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        is.close();
                        imageView_img_book.setImageBitmap(bm);
                    }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                   }

                }else if(requestCode == OPEN_GALLERY && resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this,"선택 취소", Toast.LENGTH_SHORT).show();
                }

                break;

        } // end switch // 이런 주석 xx

    }// end onActivityResult()




    // popup 메뉴 생성 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.img_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==1)// 메뉴가 하나라면
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // 사진 저장될 경로
    String currentPhotoPath;

    //사진을 파일로 만드는 메소드

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // 그림 파일 저장 (/mnt/sdcard/Pictures)
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // test
    /*
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEC_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");

        if(!storageDir.exists())
        {
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        currentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }
    */


    // test 사진 촬영
    private void dispatchTakePictureIntent() {

        // 권한 체크
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted()
            {
                // 권한 허가 상태일 때
                Toast.makeText(getApplicationContext(), "권한 허가",Toast.LENGTH_SHORT).show();

                // 카메라 열기
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // 파일을 생성한다
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();

                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }

                    // 파일이 생성되면
                    if (photoFile != null) {

                        // test
                        // Toast.makeText(getApplicationContext(),"여기 왔니...??", Toast.LENGTH_SHORT).show();

                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "com.example.everybooks",
                                photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, TAKE_PICTURE);
                    }
                }

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions)
            {
                // 권한 허가하지 않았을 때
                Toast.makeText(getApplicationContext(), "권한을 허용하지 않으면 서비스를 이용할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
        };

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(" 카메라 접근 권한이 필요합니다")
                .setDeniedMessage(" [설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

    }

    // 갤러리에 사진 추가
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }




}// end class
