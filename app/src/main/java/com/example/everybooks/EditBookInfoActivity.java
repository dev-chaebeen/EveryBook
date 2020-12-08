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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Memo;
import com.example.everybooks.data.Util;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;

    // 뷰 요소 선언
    TextView textView_edit;
    TextView textView_delete;

    ImageView imageView_img;
    EditText editText_title;
    EditText editText_writer;
    EditText editText_publisher;
    EditText editText_publish_date;

    // 전달받는 데이터 선언
    int bookId;

    final String TAG = "테스트";

    // 인텐트 requestCode 상수
    final int TAKE_PICTURE = 1000;
    final int OPEN_GALLERY = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 뷰 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_info);

        // 뷰 요소 초기화
        textView_edit = findViewById(R.id.edit);
        textView_delete = findViewById(R.id.delete);

        imageView_img = findViewById(R.id.img);
        editText_title = findViewById(R.id.title);
        editText_writer = findViewById(R.id.writer);
        editText_publisher = findViewById(R.id.publisher);
        editText_publish_date = findViewById(R.id.publish_date);

        // 인텐트로 전달받은 데이터 저장
        bookId = getIntent().getIntExtra("bookId", -1);

        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.edit :

                        // 사용자에게 입력받은 값을 변수에 담는다.
                        String title = editText_title.getText().toString();
                        String writer = editText_writer.getText().toString();
                        String publisher = editText_publisher.getText().toString();
                        String publishDate = editText_publish_date.getText().toString();

                        // 이미지의 경우 문자열로 저장하기 위해서 ImageView 의 이미지를 bitmap 으로 가져온뒤 Bitmap 을 문자열로 바꾼다.
                        BitmapDrawable drawable = (BitmapDrawable) imageView_img.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Util util = new Util();
                        String imgString = util.bitMapToString(bitmap);

                        Log.d(TAG, "바꾸려는 제목 : " + title );

                        // bookInfo 파일에 "bookList" 키의 값으로 저장되어있는 책 리스트 정보를 가져온다.
                        // jsonObject 단위로 접근하기 위해 문자열을 JsonArray 형태로 바꾼다.
                        // 인텐트로 전달받은 bookId 와 동일한 bookId 를 가진 jsonObject 찾는다.
                        // jsonObject 의 값을 사용자에게 입력받은 값으로 바꾼다.
                        // jsonArray 를 문자열의 형태로 다시 바꾼뒤 "bookList" 키의 값으로 저장한다.
                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                        String bookListString= bookInfo.getString("bookList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(bookListString);
                            for (int i = 0; i < jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(jsonObject.getInt("bookId") == bookId)
                                {
                                    jsonObject.put("title", title);
                                    jsonObject.put("writer", writer);
                                    jsonObject.put("publisher", publisher);
                                    jsonObject.put("publishDate", publishDate);
                                    jsonObject.put("img", imgString);
                                }
                            }

                            // test ok
                            Log.d(TAG," jsonArray.toString: " + jsonArray.toString());

                            SharedPreferences.Editor editor = bookInfo.edit();
                            editor.putString("bookList", jsonArray.toString());
                            editor.commit();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }

                        // 정보 변경이 끝나면 메인 액티비티로 화면을 전환하고 현재 액티비티를 종료한다.
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        break;

                    case R.id.delete :

                        // delete 를 클릭하면 책을 삭제한다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        Log.d(TAG, "읽을 책 편집 화면에서 삭제 버튼 클릭 ");

                                        // 문자열로 저장되어있는 읽을 책 리스트를 불러온 뒤 jsonArray 의 형태로 바꾼다.
                                        // JsonObject 단위로 삭제할 수 없기 때문에 전달받은 bookId 와 일치하지 않는 jsonObject 만 ArrayList<Book> 에 담는다.
                                        // ArrayList<Book> 에 담긴 Book 객체를 jsonObject 로 바꾼 다음 JsonArray 에 담고 문자열의 형태로 바꾼다.
                                        // 문자열을 "bookList" 키의 값으로 저장한다.
                                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                        String bookListString = bookInfo.getString("bookList", null);
                                        ArrayList<Book> bookArrayList = new ArrayList<>();

                                        if (bookListString != null)
                                        {
                                            try
                                            {
                                                JSONArray jsonArray = new JSONArray(bookListString);

                                                for (int i = 0; i < jsonArray.length(); i++)
                                                {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                    if(jsonObject.getInt("bookId") != bookId)
                                                    {
                                                        int bookId = jsonObject.getInt("bookId");
                                                        String img = jsonObject.getString("img");
                                                        String title = jsonObject.getString("title");
                                                        String writer = jsonObject.getString("writer");
                                                        String publisher = jsonObject.getString("publisher");
                                                        String publishDate = jsonObject.getString("publishDate");
                                                        String insertDate = jsonObject.getString("insertDate");
                                                        String startDate = jsonObject.getString("startDate");
                                                        String endDate = jsonObject.getString("endDate");
                                                        String readTime = jsonObject.getString("readTime");
                                                        String state = jsonObject.getString("state");
                                                        int starNum = jsonObject.getInt("starNum");

                                                        Book book = new Book();
                                                        book.setBookId(bookId);
                                                        book.setImg(img);
                                                        book.setTitle(title);
                                                        book.setWriter(writer);
                                                        book.setPublisher(publisher);
                                                        book.setPublishDate(publishDate);
                                                        book.setInsertDate(insertDate);
                                                        book.setStartDate(startDate);
                                                        book.setEndDate(endDate);
                                                        book.setReadTime(readTime);
                                                        book.setState(state);
                                                        book.setStarNum(starNum);
                                                        bookArrayList.add(0, book);
                                                    }
                                                }

                                                Log.d(TAG, " 삭제 뒤 저장되어있는 bookList : " + bookArrayList.size());

                                            }
                                            catch (Exception e)
                                            {
                                                System.out.println(e.toString());
                                            }

                                            JSONArray jsonArray = new JSONArray();

                                            for (int i = 0; i < bookArrayList.size(); i++)
                                            {
                                                Book book = bookArrayList.get(i);

                                                try
                                                {
                                                    JSONObject bookJson = new JSONObject();
                                                    bookJson.put("bookId", book.getBookId());
                                                    bookJson.put("img", book.getImg());
                                                    bookJson.put("title", book.getTitle());
                                                    bookJson.put("writer", book.getWriter());
                                                    bookJson.put("publisher", book.getPublisher());
                                                    bookJson.put("publishDate", book.getPublishDate());
                                                    bookJson.put("state", book.getState());
                                                    bookJson.put("insertDate", book.getInsertDate());
                                                    bookJson.put("startDate", book.getStartDate());
                                                    bookJson.put("endDate", book.getEndDate());
                                                    bookJson.put("readTime", book.getReadTime());
                                                    bookJson.put("starNum", book.getStarNum());

                                                    jsonArray.put(bookJson);
                                                }
                                                catch (Exception e)
                                                {
                                                    System.out.println(e.toString());
                                                }
                                            }

                                            bookListString = jsonArray.toString();

                                            bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = bookInfo.edit();
                                            editor.putString("bookList", bookListString);
                                            editor.commit();

                                            Log.d(TAG, "삭제한 뒤 저장되어있는 bookListString : " + bookListString);

                                        }

                                        // 삭제되는 책과 관련된 메모를 삭제하기 위해서 저장되어있는 메모 리스트를 불러온다.
                                        SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                        String memoListString = memoInfo.getString("memoList", null);
                                        ArrayList<Memo> allMemoList = new ArrayList<>();

                                        if (memoListString != null)
                                        {
                                            try
                                            {
                                                JSONArray jsonArray = new JSONArray(memoListString);

                                                // JsonArray 형태로는 객체를 삭제할 수 없기 때문에
                                                // jsonArray 의 길이만큼 반복해서 jsonObject 를 가져오고,
                                                // 삭제할 bookId 와 일치하지 않는 jsonObject 만 Memo 객체에 담은 뒤 ArrayList<Memo> 에 담는다.
                                                for (int i = 0; i < jsonArray.length(); i++)
                                                {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                    if(jsonObject.getInt("bookId") != bookId)
                                                    {
                                                        Memo memo = new Memo();
                                                        memo.setMemoId(jsonObject.getInt("memoId"));
                                                        memo.setBookId(jsonObject.getInt("bookId"));
                                                        memo.setMemoText(jsonObject.getString("memoText"));
                                                        memo.setMemoDate(jsonObject.getString("memoDate"));

                                                        allMemoList.add(memo);
                                                    }
                                                }

                                                Log.d(TAG, "저장되어있는 allMemoList.size : " + allMemoList.size());

                                            }
                                            catch (Exception e)
                                            {
                                                System.out.println(e.toString());
                                            }


                                            // ArrayList<Memo> 에 담긴 Memo 객체를 JsonObject 로 바꾸고 JsonArray 에 담는다.
                                            // JsonArray 를 문자열로 바꿔서 "memoList" 키의 값으로 저장한다.
                                            JSONArray jsonArray = new JSONArray();

                                            for (int i = 0; i < allMemoList.size(); i++)
                                            {
                                                Memo memo = allMemoList.get(i);

                                                try
                                                {
                                                    JSONObject jsonObject = new JSONObject();

                                                    jsonObject.put("memoId", memo.getMemoId());
                                                    jsonObject.put("bookId", memo.getBookId());
                                                    jsonObject.put("memoText", memo.getMemoText());
                                                    jsonObject.put("memoDate", memo.getMemoDate());

                                                    jsonArray.put(jsonObject);
                                                }
                                                catch (Exception e)
                                                {
                                                    System.out.println(e.toString());
                                                }
                                            }

                                            memoListString = jsonArray.toString();

                                            memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = memoInfo.edit();
                                            editor.putString("memoList", memoListString);
                                            editor.commit();

                                            //Log.d(TAG, "삭제한 뒤 저장되어있는 memoListString : " + afterMemoListString);
                                        }

                                        dialog.dismiss();

                                        // 정보 삭제가 끝나면 메인 액티비티로 화면을 전환하고 현재 액티비티를 종료한다.
                                        intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        finish();

                                    }
                                });

                        builder.setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                        builder.show();

                        break;

                    case R.id.img :

                        Log.d(TAG, "EditBookInfoActivity, img 클릭");
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
                                        imageView_img.setImageResource(R.drawable.icon_book);
                                        break;
                                }
                                return true;
                            }
                        });

                        popupMenu.show();// 팝업 메뉴 보이기

                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        textView_edit.setOnClickListener(click);
        textView_delete.setOnClickListener(click);
        imageView_img.setOnClickListener(click);


        String title="";
        String writer="";
        String publisher="";
        String publishDate="";
        String imgString="";

        // 전달받은 bookId 에 해당하는 정보를 보여주기 위해서 저장되어있는 책 리스트를 불러온다.
        // 문자열의 형태로 저장되어있기 때문에 jsonObject 로 접근하기 위해서 JsonArray 의 형태로 바꿔준다.
        // 전달받은 bookId 와 동일한 bookId 를 가지고 있는 jsonObject 에 저장된 값을 변수에 담아준다.
        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if( bookId == jsonObject.getInt("bookId"))
                {
                    title = jsonObject.getString("title");
                    writer = jsonObject.getString("writer");
                    publisher = jsonObject.getString("publisher");
                    publishDate = jsonObject.getString("publishDate");
                    imgString = jsonObject.getString("img");
                }
            }

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }


        // 변수에 담은 저장된 정보들을 뷰 요소에 배치해준다.
        // 이미지의 경우 문자열로 저장되어 있기 때문에 비트맵으로 바꿔서 배치한다.
        Util util = new Util();
        Bitmap bitmap  = util.stringToBitmap(imgString);
        imageView_img.setImageBitmap(bitmap);
        editText_title.setText(title);
        editText_writer.setText(writer);
        editText_publisher.setText(publisher);
        editText_publish_date.setText(publishDate);

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

                /*
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, OPEN_GALLERY);

                 */
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
                        imageView_img.setImageBitmap(bm);
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
