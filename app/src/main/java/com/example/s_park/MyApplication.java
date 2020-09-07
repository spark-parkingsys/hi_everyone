package com.example.s_park;

import android.app.Application;

public class MyApplication extends Application {
    private String mGlobalString = "";
    private boolean LoginSuccess = false;

    public String getGlobalString()
    {
        return mGlobalString;
    }

    public void setGlobalString(String globalString)
    {
        this.mGlobalString = globalString;
    }

    public boolean getLoginSuccess(){
        return LoginSuccess;
    }

    public void setLoginSuccess(boolean LoginSuccess)
    {
        this.LoginSuccess = LoginSuccess;
    }
}
