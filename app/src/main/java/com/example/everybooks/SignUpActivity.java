package com.example.everybooks;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.everybooks.data.User;
import com.example.everybooks.data.Util;
import com.google.android.material.textfield.TextInputEditText;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_add_photo;
    TextInputEditText textInputEditText_nickname;
    TextInputEditText textInputEditText_email;
    TextInputEditText textInputEditText_password;
    TextInputEditText textInputEditText_confirm_password;
    Button button_register;

    Intent intent;

    // 회원가입
    User user;
    String img;
    String nickname;
    String email;
    String password;
    String confirmPassword;

    final String TAG ="테스트";

    // 인텐트 requestCode 상수
    final int TAKE_PICTURE = 1000;
    final int OPEN_GALLERY = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_signup);

        // 뷰 요소 초기화
        imageView_add_photo = findViewById(R.id.add_photo);
        textInputEditText_nickname = findViewById(R.id.nickname);
        textInputEditText_email = findViewById(R.id.email);
        textInputEditText_password = findViewById(R.id.password);
        textInputEditText_confirm_password = findViewById(R.id.confirm_password);
        button_register = findViewById(R.id.btn_register);

        // register 버튼 클릭 시 이메일 정보를 인텐트에 담아서 로그인 화면으로 이동한다
        button_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 이미지 뷰의 이미지를 비트맵으로 저장한다.
                BitmapDrawable drawable = (BitmapDrawable) imageView_add_photo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                // 비트맵을 문자열로 변환한다.
                Util util = new Util();
                img = util.bitMapToString(bitmap);

                Log.d(TAG, "SignUpActivity, 회원가입 시 저장한 이미지 : " + img);

                nickname = textInputEditText_nickname.getText().toString();
                email = textInputEditText_email.getText().toString();
                password = textInputEditText_password.getText().toString();
                confirmPassword = textInputEditText_confirm_password.getText().toString();

                // 비밀번호와 비밀번호 확인에 입력한 값이 일치하지 않으면 비밀번호를 확인하라는 안내를 한다.
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    textInputEditText_confirm_password.setText("");
                }
                else
                {
                    // 정상적으로 입력한 경우 유저 객체를 생성해서 사용자가 입력한 값을 담는다.
                    user = new User();
                    user.setImg(img);
                    user.setNickname(nickname);
                    user.setEmail(email);
                    user.setPassword(password);
                    
                    // json 형태로 바꾼 객체를 String 변수인 userString 에 저장하고
                    // 입력받은 이메일로 저장된 값을 받아온다.
                    String userString = user.toJSON();

                    Log.d(TAG, "회원가입 정보 : " + userString);

                    SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
                    String userInfoString = userInfo.getString(email, "false");

                    // 이미 등록된 이메일인 경우 이미 등록된 이메일이라고 안내한다.
                    if(userInfoString!="false")
                    {
                        Toast.makeText(getApplicationContext(), "이미 등록된 이메일입니다.", Toast.LENGTH_SHORT).show();
                        textInputEditText_email.setText("");
                    }
                    else
                    {
                        // 등록되지 않은 이메일인 경우
                        // userInfo 라는 SharedPreferences 파일에
                        // 키 : 입력받은 email
                        // 값 : img, nickname, email, password 데이터를 저장한다.
                        SharedPreferences.Editor editor = userInfo.edit();
                        editor.putString(email, userString);
                        editor.commit();

                        // 입력받은 이메일 데이터를 담아 로그인 화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("email", textInputEditText_email.getText().toString());
                        startActivity(intent);
                        finish();
                    }

                }

            }
        });


        // 이미지를 클릭하면
        imageView_add_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // 책 추가 이미지 클릭하면 팝업 메뉴 띄우기
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
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
                                imageView_add_photo.setImageResource(R.drawable.ic_account);
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();// 팝업 메뉴 보이기

            }
        });

    }

    private void dispatchTakePictureIntent()
    {
        // 권한 체크
        PermissionListener permissionListener = new PermissionListener()
        {
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
                    try
                    {
                        photoFile = createImageFile();
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.toString());
                    }

                    // 파일이 생성되면
                    if (photoFile != null)
                    {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        // 암묵적 인텐트 응답 결과에 따라 동작을 실행한다.
        // 카메라로 찍은 사진, 갤러리에서 불러온 사진을 이미지뷰에 보여준다.
        switch (requestCode)
        {
            case TAKE_PICTURE:
                if(resultCode != RESULT_CANCELED)
                {
                    if (resultCode == RESULT_OK && intent.hasExtra("data") )
                    {
                        Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                        if (bitmap != null)
                        {
                            imageView_add_photo.setImageBitmap(bitmap);
                        }
                    }
                }

                break;

            case OPEN_GALLERY:
                if(requestCode == OPEN_GALLERY && resultCode == RESULT_OK)
                {
                    try
                    {
                        InputStream is = getContentResolver().openInputStream(intent.getData());
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        is.close();
                        imageView_add_photo.setImageBitmap(bm);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else if(requestCode == OPEN_GALLERY && resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this,"선택 취소", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    // popup 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.img_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
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

}
