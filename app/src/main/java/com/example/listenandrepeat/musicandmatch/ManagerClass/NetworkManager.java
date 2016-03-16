package com.example.listenandrepeat.musicandmatch.ManagerClass;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.listenandrepeat.musicandmatch.DataClass.CommentDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.CommentResult;
import com.example.listenandrepeat.musicandmatch.DataClass.LoginAndSignUpResult;
import com.example.listenandrepeat.musicandmatch.DataClass.MatchingDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.MemberProfileResult;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileChange;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileMe;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileOther;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWriteResult;
import com.example.listenandrepeat.musicandmatch.MyApplication;
import com.example.listenandrepeat.musicandmatch.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ListenAndRepeat on 2016. 2. 22..
 */
public class NetworkManager {
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    OkHttpClient mClient;
    private static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;

    private NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();
        File cachefile = new File(context.getExternalCacheDir(), "mycache");
        if (!cachefile.exists()) {
            cachefile.mkdirs();
        }
        Cache cache = new Cache(cachefile, MAX_CACHE_SIZE);
        builder.cache(cache);

        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        disableCertificateValidation(context, builder);

        mClient = builder.build();
    }

    static void disableCertificateValidation(Context context, OkHttpClient.Builder builder) {

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().openRawResource(R.raw.site);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, tmf.getTrustManagers(), null);
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            sc.init(null, tmf.getTrustManagers(), null);
            builder.sslSocketFactory(sc.getSocketFactory());
            builder.hostnameVerifier(hv);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cancelAll() {
        mClient.dispatcher().cancelAll();
    }

    public void cancelTag(Object tag) {
        Dispatcher dispatcher = mClient.dispatcher();
        List<Call> calls = dispatcher.queuedCalls();
        for (Call call : calls) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
        calls = dispatcher.runningCalls();
        for (Call call : calls) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
    }

    public interface OnResultListener<T> {
        public void onSuccess(Request request, T result);

        public void onFailure(Request request, int code, Throwable cause);
    }

    private static final int MESSAGE_SUCCESS = 0;
    private static final int MESSAGE_FAILURE = 1;

    static class NetworkHandler extends Handler {
        public NetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CallbackObject object = (CallbackObject) msg.obj;
            Request request = object.request;
            OnResultListener listener = object.listener;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    listener.onSuccess(request, object.result);
                    break;
                case MESSAGE_FAILURE:
                    listener.onFailure(request, -1, object.exception);
                    break;
            }
        }
    }

    Handler mHandler = new NetworkHandler(Looper.getMainLooper());

    static class CallbackObject<T> {
        Request request;
        T result;
        IOException exception;
        OnResultListener<T> listener;
    }

    public void cancelAll(Object tag) {

    }

    //
    private static  final MediaType MEDIA_TYPE = MediaType.parse("image/png");

    private static final String URL_FORMAT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";

    private static final String URL_PROFILE_MEMBER = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/%s";

    // Matching and Story Tab URL
    private static final String URL_MATCHING_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s";

    private static final String URL_STORY_WRITE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts";

    private static final String URL_STORY_DELETE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s";

    private static final String URL_STORY_MODIFY = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s";
    // Comment URL
    private static final String URL_COMMENT_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies?page=%s";

    private static final String URL_COMMENT_WRITE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies";

    private static final String URL_COMMENT_DELETE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies/%s";

    private static final String URL_COMMENT_MODIFY = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies/%s";

    // Login URL
    private static final String URL_SIGN_UP = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members";

    private static final String URL_LOCAL_LOGIN = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/login";

    public Request login(Context context, String userName, String passWord , final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException{
        String url = URL_LOCAL_LOGIN;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",userName)
                .add("password",passWord)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                LoginAndSignUpResult result = parser.fromJson(response.body().string(),LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }
    //Login Function
    public Request signUp(Context context, String userName, String nickName, String passWord, final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException{
        String url = URL_SIGN_UP;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();

        RequestBody requestBody = new FormBody.Builder()
                .add("username",userName)
                .add("nickname",nickName)
                .add("password",passWord)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                LoginAndSignUpResult result = parser.fromJson(response.body().string(),LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }


    // Story Function

     // modify
    public Request modifyStory(Context context,int pid,String title,String contents,int limitPeo,int decidePeo,final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException{
        String url = String .format(URL_STORY_MODIFY,pid);

        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();

        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .add("title",title)
                .add("limit_people","" + limitPeo)
                .add("decide_people","" + decidePeo)
                .build();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(requestBody)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }

    // delete
    public Request deleteStory(Context context,int pid,
                                 final OnResultListener<StoryWriteResult> listener) throws  UnsupportedEncodingException{
        String url = String.format(URL_STORY_DELETE,pid);
        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();




        Request request = new Request.Builder().url(url)
                .tag(context)
                .delete()
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }

    // write
    public Request postStoryWrite(Context context,String title,String content,int limitPeo,int decidePeo,File file,final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException{
        String url = URL_STORY_WRITE;

        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("content", content)
                .addFormDataPart("limit_people", "" + limitPeo)
                .addFormDataPart("decide_people", "" + decidePeo);
        if (file != null) {
            builder.addFormDataPart("photo", "photo.jpg", RequestBody.create(MEDIA_TYPE, file));
        }

        RequestBody requestBody = builder.build();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }
    public Request getMatchingDetail(Context context,int page,final OnResultListener<MatchingDetailResult> listener)throws UnsupportedEncodingException{

        String url = String.format(URL_MATCHING_CONTENT, page);

        final CallbackObject<MatchingDetailResult> callbackObject = new CallbackObject<MatchingDetailResult>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                MatchingDetailResult result = parser.fromJson(response.body().string(), MatchingDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }
    /*
    ************************************************************************************
    *
     */

    // Comment Function
    public Request  modifyComment(Context context,int pid,int rid,String contents,
                                 final OnResultListener<CommentResult> listener) throws  UnsupportedEncodingException{
        String url = String.format(URL_COMMENT_MODIFY,pid,rid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();


        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }


    public Request deleteComment(Context context,int pid,int rid,
                                    final OnResultListener<CommentResult> listener) throws  UnsupportedEncodingException{
        String url = String.format(URL_COMMENT_DELETE,pid,rid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();




        Request request = new Request.Builder().url(url)
                .tag(context)
                .delete()
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }

    public Request postCommentWrite(Context context,int pid,String contents,
                                    final OnResultListener<CommentResult> listener) throws  UnsupportedEncodingException{
        String url = String.format(URL_COMMENT_WRITE,pid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();


        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }


    public Request getCommentDetail(Context context,int pid,int page,final OnResultListener<CommentDetailResult> listener) throws UnsupportedEncodingException{
       String url = String.format(URL_COMMENT_CONTENT,pid,page);

       final CallbackObject<CommentDetailResult> callbackObject = new CallbackObject<CommentDetailResult>();

       Request request = new Request.Builder().url(url)
               .tag(context)
               .build();

       callbackObject.request = request;
       callbackObject.listener = listener;

       mClient.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               callbackObject.exception = e;
               Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
               mHandler.sendMessage(msg);
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentDetailResult result = parser.fromJson(response.body().string(),CommentDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
           }
       });
       return request;
   }
    /*
      ************************************************************************************
      *
       */
    // Profile Function
    public Request getMemberProfile(Context context, String mid, final OnResultListener<MemberProfileResult> listener) throws UnsupportedEncodingException {
        try {

            String url = String.format(URL_PROFILE_MEMBER, URLEncoder.encode(mid, "utf-8"));


            final CallbackObject<MemberProfileResult> callbackObject = new CallbackObject<MemberProfileResult>();

            Request request = new Request.Builder().url(url)
                    .tag(context)
                    .build();

            callbackObject.request = request;
            callbackObject.listener = listener;
            mClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callbackObject.exception = e;
                    Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Gson parser = new Gson();
                    MemberProfileResult result = parser.fromJson(response.body().string(), MemberProfileResult.class);
                    callbackObject.result = result;
                    Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                    mHandler.sendMessage(msg);
                }
            });

            return request;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Request testSSL(Context context, final OnResultListener<String> listener) {
        Request request = new Request.Builder().url("https://192.168.210.51:8443/test.html").build();
        final CallbackObject<String> callbackObject = new CallbackObject<String>();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackObject.result = response.body().string();
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;

    }


    //////////////////////////////////////////////// 인호 추가//////////////////////////////////////////////////////
    ////////////////////////////////////준수꺼
    private static final String URL_FORMAT_JOIN = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members";
    private static final String URL_FORMAT_LOGIN_LINK = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/soundcloud";
    private static final String URL_FORMAT_LOGIN_LOCAL = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/login";
    private static final String URL_FORMAT_LOGOUT_LOCAL = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/logout";

    private static final String URL_FORMAT_PROFILE_ME = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";
    private static final String URL_FORMAT_PROFILE_OTHER = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/1";
    private static final String URL_FORMAT_TRACK_LIST_OTHER = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/1/tracks";
    private static final String URL_FORMAT_TRACK_LIST_ME = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me/tracks";

    private static final String URL_FORMAT_PROFILE_CHANGE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";
    private static final String URL_FORMAT_PROFILE_PHOTO_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts";

    private static final String URL_FORMAT_STORY_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
    private static final String URL_FORMAT_STORY_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
    private static final String URL_FORMAT_STORY_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts?page=1";
    private static final String URL_FORMAT_STORY_SEARCH = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies";

    private static final String URL_FORMAT_COMMENT_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
    private static final String URL_FORMAT_COMMENT_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
    private static final String URL_FORMAT_COMMENT_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/replies?page=1";
    private static final String URL_FORMAT_COMMENT_DETAIL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/photos";

    ////////////////////////////////// 용이형 유알엘주소
//    private static final String URL_FORMAT_JOIN = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members";
//    private static final String URL_FORMAT_LOGIN_LINK = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/soundcloud";
//    private static final String URL_FORMAT_LOGIN_LOCAL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/login";
//    private static final String URL_FORMAT_LOGOUT_LOCAL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/logout";
//
//    private static final String URL_FORMAT_PROFILE_ME = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me";
//    private static final String URL_FORMAT_PROFILE_OTHER = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/%s";
//    private static final String URL_FORMAT_TRACK_LIST_OTHER = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/1/tracks";
//    private static final String URL_FORMAT_TRACK_LIST_ME = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me/tracks";
//
//    private static final String URL_FORMAT_PROFILE_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me";
//    private static final String URL_FORMAT_PROFILE_PHOTO_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me/photos";
//
//    private static final String URL_FORMAT_STORY_WRITE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts";
//    private static final String URL_FORMAT_STORY_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
//    private static final String URL_FORMAT_STORY_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
//    private static final String URL_FORMAT_STORY_SEARCH = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts?page=1";
//
//    private static final String URL_FORMAT_COMMENT_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies";
//    private static final String URL_FORMAT_COMMENT_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
//    private static final String URL_FORMAT_COMMENT_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
//    private static final String URL_FORMAT_COMMENT_DETAIL = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/replies?page=1";
//    private static final String URL_FORMAT_FILE_UPLOAD = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/photos";


    public Request getProfileMe(Context context, final OnResultListener<ProfileMe> listener) throws UnsupportedEncodingException {

        String url = URL_FORMAT_PROFILE_ME;

        final CallbackObject<ProfileMe> callbackObject = new CallbackObject<ProfileMe>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ProfileMe profileMe = gson.fromJson(response.body().string(), ProfileMe.class);
                callbackObject.result = profileMe;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

    public Request getProfileOther(Context context,int mid, final OnResultListener<ProfileOther> listener) throws UnsupportedEncodingException {

        String url = String.format(URL_FORMAT_PROFILE_OTHER,mid);

        final CallbackObject<ProfileOther> callbackObject = new CallbackObject<ProfileOther>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ProfileOther profileOther = gson.fromJson(response.body().string(), ProfileOther.class);
                callbackObject.result = profileOther;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

//    private static  final MediaType MEDIA_TYPE = MediaType.parse("image/png");
    public Request putProfileChange(Context context, String username, String password, String photo, String nickname, String intro, int genre, int position, File file , final OnResultListener<ProfileChange> listener) throws UnsupportedEncodingException {

        String url = URL_FORMAT_PROFILE_CHANGE;

        final CallbackObject<ProfileChange> callbackObject = new CallbackObject<ProfileChange>();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("photo", photo)
                .addFormDataPart("nickname", nickname)
                .addFormDataPart("intro", intro)
                .addFormDataPart("genre", "" + genre)
                .addFormDataPart("position", "" + position);
        if (file != null) {
            builder.addFormDataPart("photo", file.getName(), RequestBody.create(MEDIA_TYPE, file));
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(body)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ProfileChange profileChange = gson.fromJson(response.body().string(), ProfileChange.class);
                callbackObject.result = profileChange;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }


}
