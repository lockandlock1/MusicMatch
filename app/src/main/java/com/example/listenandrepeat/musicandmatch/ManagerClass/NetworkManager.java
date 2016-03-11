package com.example.listenandrepeat.musicandmatch.ManagerClass;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.listenandrepeat.musicandmatch.DataClass.CommentDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.MatchingDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.MemberProfileResult;
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
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    private static final String URL_FORMAT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";

    private static final String URL_PROFILE_MEMBER = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/%s";

    private static final String URL_MATCHING_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s";

    private static final String URL_COMMENT_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies?page=%s";

    private static final String URL_STROY_WRITE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts";



    public  Request putStroyWrite(Context context,String title,String content,final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException{
        String url = URL_STROY_WRITE;

        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();




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
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

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

    public Request getMatchingDetail(Context context,int page,final OnResultListener<MatchingDetailResult> listener)throws UnsupportedEncodingException{

        String url = String.format(URL_MATCHING_CONTENT,page);

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

/*
    public Request getNaverMovie(Context context, String keyword, int start, int display,
                                 final OnResultListener<NaverMovies> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_FORMAT, URLEncoder.encode(keyword, "utf-8"), start, display);

        final CallbackObject<NaverMovies> callbackObject = new CallbackObject<NaverMovies>();

        Request request = new Request.Builder().url(url)
                .header("X-Naver-Client-Id", "FRzO_6MMu6zwQYAaXlZr")
                .header("X-Naver-Client-Secret", "z0iOB55iQk")
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
                XMLParser parser = new XMLParser();
                NaverMovies movies = parser.fromXml(response.body().byteStream(), "channel", NaverMovies.class);
                callbackObject.result = movies;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }
*/
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
}
