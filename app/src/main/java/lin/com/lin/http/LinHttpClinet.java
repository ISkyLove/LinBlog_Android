package lin.com.lin.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络请求框架
 *
 */
public class LinHttpClinet {
    private static AsyncHttpClient client=new AsyncHttpClient();

    public static void get(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.get(url,params,responseHandler);
    }

    public static void post(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.post(url, params,responseHandler);
    }
}
