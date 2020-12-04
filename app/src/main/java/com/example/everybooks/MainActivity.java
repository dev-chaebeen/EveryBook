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
import com.example.everybooks.data.Memo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // 로그인 여부 구별하는 변수
    static boolean isLogin = false;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    ToReadBookAdapter toReadBookAdapter;
    ReadingBookAdapter readingBookAdapter;
    ReadBookAdapter readBookAdapter;

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

        setFragment(HOME);// 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택


    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ArrayList<Book> toReadBookList = new ArrayList<>();
        ArrayList<Book> readingBookList = new ArrayList<>();
        ArrayList<Book> readBookList = new ArrayList<>();

        // 메인 액티비티가 전면에 나올때마다 새로고침한다.
        //refresh();

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

        // 저장되어있는 값 어댑터에 보내주기
        try
        {
            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            String bookListString = bookInfo.getString("bookList", null);
            Log.d(TAG, "MainActivity, 저장되어있는 책 목록 : " + bookListString);

            if(bookListString != null)
            {
                JSONArray jsonArray = new JSONArray(bookListString);

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
                    String startDate = jsonObject.getString("startDate");
                    String endDate = jsonObject.getString("endDate");
                    String state = jsonObject.getString("state");
                    String readTime = jsonObject.getString("readTime");
                    int starNum = jsonObject.getInt("starNum");


                    Book book = new Book();
                    book.setBookId(bookId);
                    //book.setImg(img);
                    book.setTitle(title);
                    book.setWriter(writer);
                    book.setPublisher(publisher);
                    book.setPublishDate(publishDate);
                    book.setInsertDate(insertDate);
                    book.setStartDate(startDate);
                    book.setEndDate(endDate);
                    book.setState(state);
                    book.setReadTime(readTime);
                    book.setStarNum(starNum);

                    if(book.getState().equals("toRead"))
                    {
                        toReadBookList.add(0, book);
                    }
                    else if(book.getState().equals("reading"))
                    {
                        readingBookList.add(0, book);
                    }
                    else if(book.getState().equals("read"))
                    {
                        readBookList.add(0, book);
                    }

                }

                //어댑터에 보내기
                Log.d(TAG, "MainActivity, 어댑터에 보내는 toReadBookList.size : " + toReadBookList.size());
                toReadBookAdapter = new ToReadBookAdapter(getApplicationContext(), toReadBookList);
                toReadBookAdapter.notifyDataSetChanged();
                readingBookAdapter = new ReadingBookAdapter(getApplicationContext(), readingBookList);
                readingBookAdapter.notifyDataSetChanged();
                readBookAdapter = new ReadBookAdapter(getApplicationContext(), readBookList);
                readBookAdapter.notifyDataSetChanged();
            }

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

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
    protected void onPause()
    {
        super.onPause();

        // 책 리스트
       // ArrayList<Book> toReadBookList = new ArrayList<>();
        //ArrayList<Book> readingBookList = new ArrayList<>();
       // ArrayList<Book> readBookList = new ArrayList<>();

        //toReadBookList = ToReadBookAdapter.toReadBookList;
       // readingBookList = ReadingBookAdapter.readingBookList;
       // readBookList = ReadBookAdapter.readBookList;

        /// JSONArray 로 변환해서 다시 저장하기
        /*
        JSONArray jsonArray = new JSONArray();


        // 읽을 책 저장
        for (int i = 0; i < toReadBookList.size(); i++)
        {
            Book book = toReadBookList.get(i);

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
                bookJson.put("startDate", book.getStartDate());
                bookJson.put("endDate", book.getEndDate());
                bookJson.put("readTime", book.getReadTime());
                jsonArray.put(bookJson);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }

        // 읽는 책 저장
        for (int i = 0; i < readingBookList.size(); i++)
        {
            Book book = readingBookList.get(i);

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
                bookJson.put("startDate", book.getStartDate());
                bookJson.put("endDate", book.getEndDate());
                bookJson.put("readTime", book.getReadTime());
                jsonArray.put(bookJson);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }

        // 읽은 책 저장
        for (int i = 0; i < readBookList.size(); i++)
        {
            Book book = readBookList.get(i);

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
                bookJson.put("startDate", book.getStartDate());
                bookJson.put("endDate", book.getEndDate());
                bookJson.put("readTime", book.getReadTime());
                jsonArray.put(bookJson);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }

        String bookListString = jsonArray.toString();
        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = bookInfo.edit();
        editor.putString("bookList", bookListString);
        editor.commit();
*/

        // 어댑터에 있는 메모리스트 저장 - ing
        ArrayList<Memo> memoList = new ArrayList<>();

    }

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
        toReadBookAdapter.notifyDataSetChanged();
    }


}