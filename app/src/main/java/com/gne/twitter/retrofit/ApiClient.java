package com.gne.twitter.retrofit;

import com.gne.twitter.BuildConfig;
import com.gne.twitter.custom.HmacSha1Signature;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.text.Normalizer;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiClient {

    private static ApiInterface apiInterface;
    private ApiClient(){}

    public static ApiInterface getInstance(){
        if(apiInterface ==null){
            TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            TwitterAuthToken authToken = session.getAuthToken();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String nounce=getNounce();
                        String timeStamp= Long.toString(Calendar.getInstance().getTimeInMillis()/1000);

                        String baseUrl=chain.request().url().toString().split("\\?")[0];
                        String reqMethod=chain.request().method();

                        String paramString=getParamString(chain, authToken,nounce,timeStamp);

                        String signatureBaseString=reqMethod.toUpperCase()+"&"+
                                urlEncode(baseUrl)+"&"+
                                urlEncode(paramString);

                        String signingKey=BuildConfig.api_secret+"&"+authToken.secret;

                        String signature="";
                        try {
                            signature=HmacSha1Signature.calculateRFC2104HMAC(signatureBaseString,signingKey);
                            byte[] decodedHex = Hex.decodeHex(signature.toCharArray());
                            signature = Base64.encodeBase64String(decodedHex);
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (DecoderException e) {
                            e.printStackTrace();
                        }

                        String header="OAuth oauth_consumer_key=\""+BuildConfig.api_key+"\", "+
                                "oauth_nonce=\""+nounce+"\", "+
                                "oauth_signature=\""+urlEncode(signature)+"\", "+
                                "oauth_signature_method=\"HMAC-SHA1\", "+
                                "oauth_timestamp=\""+timeStamp+"\", "+
                                "oauth_token=\""+authToken.token+"\", "+
                                "oauth_version=\"1.0\"";

                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization",header)
                                .build();
                        return chain.proceed(request);
                    }).build();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BuildConfig.base_url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface =retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }

    private static String getNounce(){
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        String nounce= Base64.encodeBase64String(randomBytes);
        String normalizednounce = Normalizer.normalize(nounce, Normalizer.Form.NFD);
        nounce = normalizednounce.replaceAll("[^A-Za-z0-9]", "");
        return nounce;
    }

    private static String getParamString(Interceptor.Chain chain,TwitterAuthToken authToken, String nounce, String timeStamp){
        String params=chain.request().url().toString().split("\\?")[1];
//        String[] queries=params.split("&");
//        if(queries.length!=0){
//            String newParams="";
//            for(int i=0; i<queries.length; i++){
//                newParams+=queries[i];
//            }
//            params=newParams;
//        }

        String paramString="";
        if(params.contains("count") || params.contains("max_id")){
            paramString=params.split("&")[0]+"&";
            params=params.split("&")[1];
        }
        paramString=paramString+"oauth_consumer_key="+urlEncode(BuildConfig.api_key)+"&"+
                "oauth_nonce="+urlEncode(nounce)+"&"+
                "oauth_signature_method=HMAC-SHA1&"+
                "oauth_timestamp="+urlEncode(timeStamp)+"&"+
                "oauth_token="+authToken.token+"&"+
                "oauth_version=1.0"+"&"+
                params;
        return paramString;
    }

    private static String urlEncode(String value){
        try {
            return URLEncoder.encode(value,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
