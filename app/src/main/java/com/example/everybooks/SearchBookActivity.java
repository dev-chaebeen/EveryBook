package com.example.everybooks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.RecommendBook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_mic;
    TextView textView_search_num;
    ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    ArrayList<Book> searchBookList = new ArrayList<>();

    // test
    private List<String> list;                      // String 데이터를 담고있는 리스트
    boolean lastItemVisibleFlag = false; //  // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    String requestUrl;

    String TAG = "SearchBookActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_book);

        // 뷰 요소 초기화
        imageView_mic = findViewById(R.id.mic);
        textView_search_num = findViewById(R.id.search_num);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);

        for (int i = 0; i < 20; i++) {
            // 임시로 데이터 추가
            ArrayList<Book> list = SearchBookAdapter.searchBookList;
            Book book = new Book();
            book.setTitle("검색한책"+i);
            book.setWriter("검색한책작가"+i);
            book.setPlot("줄거리"+i);
            list.add(book);
        }

        // 리사이클러뷰 생성
       /* recyclerView = findViewById(R.id.search_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchBookAdapter(SearchBookAdapter.searchBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                // 리사이클러뷰의 마지막 도착 ! 다음 데이터 로드
                if (lastVisibleItemPosition == itemTotalCount)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    // 다음 데이터를 불러온다.
                    getItem();
                }

            }
        });*/

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    private void getItem()
    {

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 다음 20개의 데이터를 불러와서 리스트에 저장한다.
        for (int i = 0; i < 20; i++) {
            // 임시로 데이터 추가
            ArrayList<Book> list = SearchBookAdapter.searchBookList;
            Book book = new Book();
            book.setTitle("추가한책"+i);
            book.setWriter("추가한책작가"+i);
            book.setPlot("추가한줄거리"+i);
            list.add(book);
        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);

    }



    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String clientId = getString(R.string.search_book_api_client_id);//애플리케이션 클라이언트 아이디값";
            String clientSecret = getString(R.string.search_book_api_client_secret);//애플리케이션 클라이언트 시크릿값";
            try {
                String text = URLEncoder.encode("천개의", "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/book?query="+ text; // json 결과
                //String apiURL = "https://openapi.naver.com/v1/search/book.xml?query="+ text; // xml 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                Log.d(TAG,"response.toString()" + response.toString());

                // JSON 형식으로 얻은 값을 담아준다.


            } catch (Exception e) {
                System.out.println(e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

          //  Log.d(TAG, "searchBookList.size() : " + searchBookList.size());
            /*//어답터 연결
            RecommendBookAdapter adapter = new RecommendBookAdapter(getApplicationContext(), searchBookList);
            recyclerView.setAdapter(adapter);

            textView_total_num.setText(totalCount);*/
        }

    }


    public static String getOnlyKor(String str)
    {
        String textWithoutTag = str.replaceAll("&nbsp;", " ");
        textWithoutTag = textWithoutTag.replaceAll("&rsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("&lsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        return textWithoutTag;
    }



    /*
    String clientId = "YOUR_CLIENT_ID";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "YOUR_CLIENT_SECRET";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode("안녕하세요. 오늘 기분은 어떻습니까?", "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=ko&target=en&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    */


}
