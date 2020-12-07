package com.example.everybooks;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.everybooks.data.Util;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_user_nickname;
    TextView textView_user_email;
    CircleImageView imageView_img;

    LinearLayout linearLayout_edit_photo;
    LinearLayout linearLayout_edit_nickname;
    LinearLayout linearLayout_withdraw;

    View.OnClickListener click;

    String nickname;
    String img;

    String loginUser;
    String[] emailPassword;
    String loginEmail;
    String userInfoString;
    JSONObject jsonObject;

    final String TAG = "테스트";
    // 인텐트 requestCode 상수
    final int TAKE_PICTURE = 1000;
    final int OPEN_GALLERY = 1001;

    SharedPreferences userInfo;
    SharedPreferences autoLogin;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 화면 구성
        setContentView(R.layout.activity_edit_profile);

        // 뷰 요소 초기화
        textView_user_nickname = findViewById(R.id.user_nickname);
        textView_user_email = findViewById(R.id.user_email);
        imageView_img = (CircleImageView) findViewById(R.id.img);
        linearLayout_edit_photo = findViewById(R.id.edit_photo);
        linearLayout_edit_nickname = findViewById(R.id.edit_nickname);
        linearLayout_withdraw = findViewById(R.id.withdraw);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.edit_photo :

                        // edit_photo 클릭했을 때 팝업 메뉴 띄우기
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
                                        imageView_img.setImageResource(R.drawable.ic_account);

                                        BitmapDrawable drawable = (BitmapDrawable) imageView_img.getDrawable();
                                        Bitmap bitmap = drawable.getBitmap();
                                        Util util = new Util();
                                        img = util.bitMapToString(bitmap);

                                        Toast.makeText(getApplicationContext(), "사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                        break;

                                }

                                return true;
                            }

                        });

                        popupMenu.show();// 팝업 메뉴 보이기

                        break;

                    case R.id.edit_nickname :
                        editNickname();
                        break;

                    case R.id.withdraw :
                        // withdraw 클릭했을 때 수행할 동작
                        withdraw();
                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        linearLayout_edit_photo.setOnClickListener(click);
        linearLayout_edit_nickname.setOnClickListener(click);
        linearLayout_withdraw.setOnClickListener(click);


        // 현재 로그인 되어 있는 이메일 정보를 가져온다.
        // email,password 의 문자열로 저장되어있으므로 , 을 기준으로 문자열을 나눠서 문자열 배열에 저장한다.
        // 배열의 0번째 칸에 email ,  1번째 칸에 password 정보가 담긴다.
        autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
        loginUser = autoLogin.getString("loginUser", null);
        emailPassword = loginUser.split(",");
        loginEmail = emailPassword[0];

        // 해당 이메일을 키로 저장되어있는 유저의 정보를 가져온다.
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userInfoString = userInfo.getString(loginEmail, null);

        try
        {
            // 이미지와 닉네임, 이메일을 가져온다.
            jsonObject = new JSONObject(userInfoString);
            img = jsonObject.getString("img");
            nickname = jsonObject.getString("nickname");
            loginEmail = jsonObject.getString("email");

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        //Log.d(TAG, "EditProfileActivity, 저장되어있던 이미지" + img);

        //  유저 이미지, 닉네임, 이메일 정보 보여주기
        Util util = new Util();
        Bitmap bitmap =util.stringToBitmap(img);
        imageView_img.setImageBitmap(bitmap);
        textView_user_nickname.setText(nickname);
        textView_user_email.setText(loginEmail);

    }// onCreate()

    @Override
    protected void onPause() {
        super.onPause();

        // 해당 이메일을 키로 저장되어있는 유저의 정보를 가져온다.
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userInfoString = userInfo.getString(loginEmail, null);

        try
        {
            // 저장된 이미지를 변경한다.
            jsonObject = new JSONObject(userInfoString);
            jsonObject.put("img", img);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString(loginEmail, jsonObject.toString());
            editor.commit();

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    void editNickname()
    {
        // 닉네임 입력받을 EditText
        final EditText editText = new EditText(this);

        // 기존 닉네임을 보여준다.
        editText.setText(nickname);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임 변경");                    // 다이얼로그 제목
        builder.setMessage("변경할 닉네임을 입력해주세요.");  // 다이얼로그 내용
        builder.setView(editText);
        builder.setPositiveButton("입력",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 입력 클릭했을 때
                    // 변경한 닉네임 저장

                    try
                    {
                        SharedPreferences.Editor editor = userInfo.edit();
                        jsonObject.put("nickname", editText.getText().toString());
                        editor.putString(loginEmail, jsonObject.toString());
                        editor.commit();

                        Log.d(TAG, "EditProfileActivity, 닉네임 변경 후 유저 정보 : " + jsonObject.toString());
                    }
                    catch (Exception e)
                    {
                           System.out.println(e.toString());
                    }
                    // test 바꾼 닉네임 출력
                    Toast.makeText(getApplicationContext(), "닉네임이 변경되었습니다." ,Toast.LENGTH_SHORT).show();

                    // 변경된 닉네임을 반영하기 위해 액티비티 다시 호출
                    intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        builder.setNegativeButton("취소",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 취소 클릭했을 때
                    Toast.makeText( getApplicationContext(), "변경 취소" ,Toast.LENGTH_SHORT).show();
                }
            });

        builder.show();

    }

    private void withdraw()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("탈퇴하기");                      // 다이얼로그 제목
        builder.setMessage(" 정말 탈퇴하시겠습니까? ");  // 다이얼로그 내용
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 로그인된 이메일을 키로 가진 회원 정보 삭제
                        SharedPreferences.Editor editor = userInfo.edit();
                        editor.remove(loginEmail);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "탈퇴가 완료되었습니다." ,Toast.LENGTH_SHORT).show();

                        // 자동로그인 정보 삭제
                        editor = autoLogin.edit();
                        editor.clear();
                        editor.commit();

                        // 로그인 화면으로 전환
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 취소 클릭했을 때
                        Toast.makeText( getApplicationContext(), "변경 취소" ,Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Get Album"), OPEN_GALLERY);

               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, OPEN_GALLERY);*/
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
                            imageView_img.setImageBitmap(bitmap);
                            Util util = new Util();
                            img = util.bitMapToString(bitmap);
                            Toast.makeText(getApplicationContext(), "사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();
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
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        imageView_img.setImageBitmap(bitmap);
                        Util util = new Util();
                        img = util.bitMapToString(bitmap);
                        Toast.makeText(getApplicationContext(), "사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();
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
