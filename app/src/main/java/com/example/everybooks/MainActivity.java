package com.example.everybooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{

    // 로그인 여부 구별하는 변수
    static boolean isLogin = false;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    Intent intent;
    ToReadBookAdapter adapter;

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

    @Override
    protected void onStart() {
        super.onStart();

        // test
        //Toast.makeText(getApplicationContext(), "여기 왔니?", Toast.LENGTH_SHORT).show();

       /* if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        }*/

    }

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
    protected void onResume() {
        super.onResume();

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
        // Transaction → 실제적인 프레그먼트교체가 이루어질때 프레그먼트를 가져와서 트랜잭션을 하려는 행위를 말한다.

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
    protected void onDestroy() {
        super.onDestroy();
        toast = Toast.makeText(this, "또 기록하러 와주세요 ", Toast.LENGTH_SHORT);
        toast.show();
    }

    // 새로고침
    public void refresh()
    {
        adapter.notifyDataSetChanged();
    }
}