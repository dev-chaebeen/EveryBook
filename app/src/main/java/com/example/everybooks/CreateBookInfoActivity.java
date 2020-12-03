package com.example.everybooks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.everybooks.data.Book;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateBookInfoActivity extends AppCompatActivity
{

    final String TAG = "테스트";

    Intent intent;

    // 뷰 요소 선언
    ImageView imageView_img_book;
    TextView  textView_save;

    EditText editText_title;
    EditText editText_writer;
    EditText editText_publisher;
    EditText editText_publish_date;

    // 리스너 선언
    View.OnClickListener click;
    ToReadBookAdapter adapter;

    // 인텐트 requestCode 상수
    final int TAKE_PICTURE = 1000;
    final int OPEN_GALLERY = 1001;


    int bookId;
    String img;
    String toReadBookListString;

    ArrayList<Book> toReadBookList = new ArrayList<>();
    JSONArray jsonArray;

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
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.save:
                        // save 클릭했을 때 수행할 동작

                        Log.d(TAG, "save 버튼 클릭");

                        // bookInfo 라는 SharedPreferences 파일에서 bookId 를 가져온다.
                        // 저장된 값이 존재하지 않는다면 0을 가져온다.
                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                        bookId = bookInfo.getInt("bookId", 0);

                        Log.d(TAG, "저장되어있던 bookId" + bookId);

                        // 이미지뷰의 resource를 비트맵으로 가져오기
                        //BitmapDrawable drawable = (BitmapDrawable) imageView_img_book.getDrawable();
                        //Bitmap bitmap = drawable.getBitmap();
                        //img = bitmap.toString();

                        // 기존

                        // 입력받은 정보를 book 객체에 저장한다.

                        Book book = new Book();
                        book.setBookId(bookId);
                        //book.setImg(imageView_img_book.getDrawable());
                        book.setTitle(editText_title.getText().toString());
                        book.setWriter(editText_writer.getText().toString());
                        book.setPublisher(editText_publisher.getText().toString());
                        book.setPublishDate(editText_publish_date.getText().toString());
                        book.setState("toRead");

                        //현재 년도, 월, 일을 책 등록일에 저장한다.
                        Calendar cal = Calendar.getInstance();

                        int year = cal.get ( cal.YEAR );
                        int month = cal.get ( cal.MONTH ) + 1 ;
                        int date = cal.get ( cal.DATE ) ;

                        String today = year + "." + month + "." + date;
                        book.setInsertDate(today);

                        try
                        {
                            // json 객체에 입력받은 값을 저장한다.
                            JSONObject bookJson = new JSONObject();

                            bookJson.put("bookId", book.getBookId());
                            //bookJson.put("img", img);
                            bookJson.put("title", book.getTitle());
                            bookJson.put("writer", book.getWriter());
                            bookJson.put("publisher", book.getPublisher());
                            bookJson.put("publishDate", book.getPublishDate());
                            bookJson.put("state", book.getState());
                            bookJson.put("insertDate", book.getInsertDate());

                            // 책을 구분하기 위해 저장된 책의 bookId 가 겹치지 않도록 bookInfo 에 저장된 bookId의 값을 1 증가시킨다.
                            SharedPreferences.Editor editor = bookInfo.edit();
                            editor.putInt("bookId", bookId + 1);
                            editor.commit();

                            Log.d(TAG, "1증가시키고 저장해둔 bookId" + bookInfo.getInt("bookId",0));


                            // 기존에 저장된 jsonArray에 저장하기 위해서
                            // SharedPreference bookInfo 파일에서 "toReadBookLIst" 키로 저장된 String 값을 불러온다.
                            toReadBookListString = bookInfo.getString("toReadBookList", null);

                            // 저장된 값이 있을 때
                            if(toReadBookListString != null)
                            {
                                jsonArray = new JSONArray(toReadBookListString);
                                Log.d(TAG, "저장되어 있던 JsonArray 길이 : " + jsonArray.length());

                                jsonArray.put(bookJson);

                                toReadBookListString = jsonArray.toString();

                                editor.putString("toReadBookList",toReadBookListString);
                                editor.commit();

                                Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());

                            }
                            else
                            {
                                // 처음 저장할 때
                                jsonArray = new JSONArray();
                                jsonArray.put(bookJson);

                                toReadBookListString = jsonArray.toString();
                                editor.putString("toReadBookList", toReadBookListString);
                                editor.commit();
                                Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());
                            }

                            // jsonArray를 ArrayList<Book> 형태로 변환한다.
                            toReadBookListString = bookInfo.getString("toReadBookList", "");
                            JSONArray jsonArray = new JSONArray(toReadBookListString);

                            Log.d(TAG, " 변환하려고 불러온 jsonArray length : " + jsonArray.length());

                            Log.d(TAG, toReadBookListString);

                            // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int bookId = jsonObject.getInt("bookId");
                                //String img = jsonObject.getString("img");
                                String title = jsonObject.getString("title");
                                String writer = jsonObject.getString("writer");
                                String publisher = jsonObject.getString("publisher");
                                String publishDate = jsonObject.getString("publishDate");
                                String insertDate = jsonObject.getString("insertDate");
                                String state = jsonObject.getString("state");

                                // test ok
                                //Log.d(TAG, "bookId :" + bookId);
                                //Log.d(TAG, "title :" + title);
                                //Log.d(TAG, "img :" + img);
                                //Log.d(TAG, "insertDate :" + insertDate);
                                //Log.d(TAG, "state :" + state);

                                // ArrayList<Book> 에 저장
                                toReadBookList.add(0, book);
                                Log.d(TAG, "toReadBookList.size : " + toReadBookList.size());

                                //어댑터에 보내기
                                adapter = new ToReadBookAdapter(toReadBookList);
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }


                        //어댑터에 추가
                        //adapter = new ToReadBookAdapter(toReadBookList);
                        //adapter.addItem(book);

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
    }


    // 카메라 실행 메소드
    private void openCamera()
    {
        // 권한 체크
        PermissionListener permissionListener = new PermissionListener()
        {
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
                            imageView_img_book.setImageBitmap(bitmap);
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
                        imageView_img_book.setImageBitmap(bm);
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

    // test 사진 촬영
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

    // 갤러리에 사진 추가
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void setStringArrayPref(Context context, String key, ArrayList values) {

        SharedPreferences prefs = getSharedPreferences("bookInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }



}
