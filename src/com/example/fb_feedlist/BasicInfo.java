package com.example.fb_feedlist;

import java.text.SimpleDateFormat;

import android.graphics.Bitmap;

import com.facebook.android.Facebook;

public class BasicInfo {
	public static final int REQ_CODE_FACEBOOK_LOGIN = 1001;

	public static boolean FacebookLogin = false;
	public static boolean RetryLogin = false;

	public static Facebook FacebookInstance = null;

	public static String[] FACEBOOK_PERMISSIONS = {};

	public static String FACEBOOK_ACCESS_TOKEN = "";
	public static String FACEBOOK_APP_ID = "223466301185491";
	public static String FACEBOOK_API_KEY = "i3rb1sH7MyiaFYKU4PJZg22GSWQ";
	public static String FACEBOOK_APP_SECRET = "5ec9c0f2e3043d30ef6a35ffae5f360b";

	public static String FACEBOOK_NAME = "";

	public static SimpleDateFormat OrigDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy³â MM¿ù ddÀÏ");

	public static Bitmap BasicPicture = null;

}
