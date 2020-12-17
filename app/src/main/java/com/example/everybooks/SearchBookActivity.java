package com.example.everybooks;

import android.content.Intent;
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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.ExternalBook;
import com.example.everybooks.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    SearchView searchView_search;
    ImageView imageView_mic;
    TextView textView_total_num;
    TextView textView_search_num;
    ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    Intent intent;

    private RecyclerView recyclerView;
    private SearchBookAdapter adapter;

    ArrayList<ExternalBook> searchBookList = new ArrayList<>();
    ExternalBook searchBook;

    // test
    private List<String> list;                      // String 데이터를 담고있는 리스트
    boolean lastItemVisibleFlag = false; //  // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    String requestUrl;

    String TAG = "SearchBookActivity";
    String totalCount;
    String searchKeyword;
    int startNum=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_book);

        // 뷰 요소 초기화
        searchView_search = findViewById(R.id.search);
        imageView_mic = findViewById(R.id.mic);
        textView_total_num = findViewById(R.id.total_num);
        progressBar = findViewById(R.id.progressbar);

        // 인텐트로 전달받은 데이터 담기
        searchKeyword = getIntent().getStringExtra("keyword");

        progressBar.setVisibility(View.GONE);

        // 검색창에 키워드를 입력하고 검색 버튼을 누르면 검색 결과화면으로 이동한다.
        searchView_search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                intent = new Intent(getApplicationContext(), SearchBookActivity.class);
                intent.putExtra("keyword", searchView_search.getQuery().toString());
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.search_book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    private void getItem()
    {

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;

                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
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
                String text = URLEncoder.encode(searchKeyword, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/book?query="+ text + "&display=20" + "&start=" + startNum; // json 결과

                Log.d(TAG,"startNum : " + startNum);

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
                JSONObject jsonObject = new JSONObject(response.toString());
                totalCount = jsonObject.getString("total");
                Log.d(TAG, "total : " + totalCount);

                String items = jsonObject.getString("items");
                JSONArray jsonArray = new JSONArray(items);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = (JSONObject) jsonArray.get(i);
                    String title = item.getString("title");
                    String image = item.getString("image");
                    String writer = item.getString("author");
                    String publisher = item.getString("publisher");
                    String publishDate = item.getString("pubdate");
                    String description = item.getString("description");
                    String link = item.getString("link");

                    Util util = new Util();
                    title = util.getOnlyKor(title);
                    description = util.getOnlyKor(description);

                    searchBook = new ExternalBook();
                    searchBook.setTitle(title);
                    searchBook.setWriter(writer);
                    searchBook.setImgFilePath(image);
                    searchBook.setPublisher(publisher);
                    searchBook.setPublishDate(publishDate);
                    searchBook.setDescription(description);
                    searchBook.setLink(link);

                    searchBookList.add(searchBook);

                }

                Log.d(TAG, "for 문 돌린 뒤 searchBookList.size() :" + searchBookList.size());
                Log.d(TAG, "total : " + totalCount);

            } catch (Exception e) {
                System.out.println(e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            SearchBookAdapter adapter = new SearchBookAdapter(getApplicationContext(), searchBookList);
            recyclerView.setAdapter(adapter);

            Log.d(TAG, "totalCount 어댑터 연결하고 " + totalCount);
            textView_total_num.setText(totalCount);
        }

    }

}
