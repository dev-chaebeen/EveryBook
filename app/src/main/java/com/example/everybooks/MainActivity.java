package com.example.everybooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.everybooks.data.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    // 로그인 여부 구별하는 변수
    static boolean isLogin = false;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    ToReadBookAdapter adapter;
    ArrayList<Book> toReadBookList = new ArrayList<>();

    // fragment 뷰들
    private HomeFragment homeFragment;
    private RecordFragment recordFragment;
    private EtcFragment etcFragment;
    private ProfileLogoutFragment profileLogoutFragment;
    private ProfileFragment profileFragment;
    
    // 뷰 요소 선언
    private BottomNavigationView bottomNavigationView;// 바텀 네비게이션 뷰. 하단 메뉴바
    private Toast toast;

    // 상수 선언
    final int HOME = 0;
    final int RECORD = 1;
    final int ETC = 2;
    final int PROFILE = 3;

    final String TAG = "테스트";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 화면 생성
        setContentView(R.layout.activity_bottom_navi);

        // 뷰 요소 초기화
        bottomNavigationView = findViewById(R.id.bottomNavi);

        // 프래그먼트 객체 생성
        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        etcFragment = new EtcFragment();
        profileLogoutFragment = new ProfileLogoutFragment();
        profileFragment = new ProfileFragment();
        adapter = new ToReadBookAdapter();

        setFragment(HOME);// 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 저장되어있는 값 어댑터에 보내주기
        try
        {
            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            String toReadBookListString = bookInfo.getString("toReadBookList", null);
            Log.d(TAG, toReadBookListString);

            if(toReadBookListString != null)
            {
                JSONArray jsonArray = new JSONArray(toReadBookListString);

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

                    Book book = new Book();
                    book.setBookId(bookId);
                    //book.setImg(img);
                    book.setTitle(title);
                    book.setWriter(writer);
                    book.setPublisher(publisher);
                    book.setPublishDate(publishDate);
                    book.setInsertDate(insertDate);
                    book.setState(state);

                    toReadBookList.add(0, book);

                    //어댑터에 보내기
                    adapter = new ToReadBookAdapter(toReadBookList);
                }

            }

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }



        // 메인 액티비티가 전면에 나올때마다 새로고침한다.
        refresh();

        // 하단 네비 바 아이템 클릭하면 해당하는 프래그먼트로 변경
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_home :
                        setFragment(HOME);
                        break;
                    case R.id.action_record :
                        setFragment(RECORD);
                        break;
                    case R.id.action_etc :
                        setFragment(ETC);
                        break;
                    case R.id.action_profile :
                        setFragment(PROFILE);
                        break;
                }
                return true;
            }
        });
    }

    // 프레그먼트 교체가 일어나는 메소드
    private void setFragment(int fragmentNum)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentNum)
        {
            case HOME :
                // HOME 클릭하면 HOME 프래그먼트로 전환
                fragmentTransaction.replace(R.id.main_frame, homeFragment);
                fragmentTransaction.commit();

                break;

            case RECORD :
                // RECORD 클릭하면 RECORD 프래그먼트로 전환
                fragmentTransaction.replace(R.id.main_frame, recordFragment);
                fragmentTransaction.commit();
                break;

            case ETC:
                // ETC 클릭하면 ETC 프래그먼트로 전환
                fragmentTransaction.replace(R.id.main_frame, etcFragment);
                fragmentTransaction.commit();
                break;

            case PROFILE :
                // PROFILE 클릭하면 PROFILE 프래그먼트로 전환

                if(MainActivity.isLogin == true)// 로그인된 상태라면 로그인 된 프로필 창으로 화면 전환
                {
                    fragmentTransaction.replace(R.id.main_frame, profileFragment);
                    fragmentTransaction.commit();
                }
                else    // 로그인 되지 않은 상태라면 로그아웃 상태의 프로필 창으로 화면 전환
                {
                    fragmentTransaction.replace(R.id.main_frame, profileLogoutFragment);
                    fragmentTransaction.commit();
                }
            break;
        }
    }

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed()
    {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리한다.
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000)
        {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
        {
            finish();
            toast.cancel();
        }
    }// end onBackPressed()

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        toast = Toast.makeText(this, "또 기록하러 와주세요 ", Toast.LENGTH_SHORT);
        toast.show();
    }

    // 새로고침
    public void refresh()
    {
        adapter.notifyDataSetChanged();
    }

    // 저장된 JsonArray 로부터 읽을 책 리스트를 얻는 메소드
    public ArrayList<Book> getToReadBookList() {

        try {
            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            String toReadBookListString = bookInfo.getString("toReadBookList", null);

            ArrayList<Book> toReadBookList = new ArrayList<>();

            if (toReadBookListString != null)
            {
                JSONArray jsonArray = new JSONArray(toReadBookListString);

                // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int bookId = jsonObject.getInt("bookId");
                    //String img = jsonObject.getString("img");
                    String title = jsonObject.getString("title");
                    String writer = jsonObject.getString("writer");
                    String publisher = jsonObject.getString("publisher");
                    String publishDate = jsonObject.getString("publishDate");
                    String insertDate = jsonObject.getString("insertDate");
                    String state = jsonObject.getString("state");

                    Book book = new Book();
                    book.setBookId(bookId);
                    //book.setImg(img);
                    book.setTitle(title);
                    book.setWriter(writer);
                    book.setPublisher(publisher);
                    book.setPublishDate(publishDate);
                    book.setInsertDate(insertDate);
                    book.setState(state);

                    toReadBookList.add(0, book);

                    //어댑터에 보내기
                    adapter = new ToReadBookAdapter(toReadBookList);
                }

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return toReadBookList;
    }


    // ArrayList<Book>을  sharedPreference 에 저장하는 메소드
    public void setToReadBookList()
    {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < ToReadBookAdapter.toReadBookList.size(); i++)
            {
                Book book = ToReadBookAdapter.toReadBookList.get(i);

                // json 객체에 입력받은 값을 저장한다.
                try
                {
                    JSONObject bookJson = new JSONObject();
                    bookJson.put("bookId", book.getBookId());
                    //bookJson.put("img", img);
                    bookJson.put("title", book.getTitle());
                    bookJson.put("writer", book.getWriter());
                    bookJson.put("publisher", book.getPublisher());
                    bookJson.put("publishDate", book.getPublishDate());
                    bookJson.put("state", book.getState());
                    bookJson.put("insertDate", book.getInsertDate());
                    jsonArray.put(bookJson);
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }

            String toReadBookListString = jsonArray.toString();

            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = bookInfo.edit();
            editor.putString("toReadBookList",toReadBookListString);
            editor.commit();
    }
}