package com.example.everybooks.data;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class User
{

    int id;
    String email;
    String password;
    String nickname;
    Drawable img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }


    // 객체를 json 객체로 만드는 메소드
    public String toJSON()
    {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("Id", getId());
            jsonObject.put("email", getEmail());
            jsonObject.put("password", getPassword());
            jsonObject.put("nickname", getNickname());

            return jsonObject.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return "";
        }

    }
}
