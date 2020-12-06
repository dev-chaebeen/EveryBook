package com.example.everybooks.data;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class User
{
    String email;
    String password;
    String nickname;
    String img;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // 객체를 json 객체로 만드는 메소드
    public String toJSON()
    {
        JSONObject jsonObject= new JSONObject();
        try {

            jsonObject.put("img", getImg());
            jsonObject.put("email", getEmail());
            jsonObject.put("password", getPassword());
            jsonObject.put("nickname", getNickname());

            return jsonObject.toString();

        } catch (JSONException e)
        {
            e.printStackTrace();
            return "";
        }

    }
}
