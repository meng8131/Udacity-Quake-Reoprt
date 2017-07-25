package com.example.android.quakereport;

/**
 * Created by lyman on 2017/6/9.
 */

public class QuakeInfoClass{

    //define variables
    Double mMag;
    String mLocation;
    Long mTime;
    String mURL;

    //define constructor
    public QuakeInfoClass(Double mag, String location, Long time, String url){
        mMag = mag;
        mLocation = location;
        mTime = time;
        mURL = url;
    }

    //define getter
    public Double getMag(){
        return mMag;
    }
    public String getLocation(){
        return mLocation;
    }
    public Long  getTime(){
        return mTime;
    }
    public String  getURL(){
        return mURL;
    }

    @Override
    public String toString() {
        return "QuakeInfoClass is " + mMag + "-" + "-" + mTime;
    }
}
