package lin.com.lin.View;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lin.com.observablescrollviewlibrary.observablescrollview.ObservableWebView;

/**
 * webview 基础控件封装
 *
 * @author Lin
 */
public class LWebView extends ObservableWebView {

    private String TAG = LWebView.class.getName();

    private Context mContext;

    private JavaScriptCallBackListen mJavaScriptCallBackListen;

    public void setJavaScriptCallBackListen(
            JavaScriptCallBackListen javaScriptCallBackListen) {
        this.mJavaScriptCallBackListen = javaScriptCallBackListen;
    }

    interface JavaScriptCallBackListen {
        void JavaScriptCallBack(String result);
    }

    public LWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void stringByEvaluatingJavaScriptFromString(String script,
                                                       JavaScriptCallBackListen javaScriptCallBackListen) {
        this.mJavaScriptCallBackListen = javaScriptCallBackListen;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                // final String CHROMIUM_WEBVIEW_FACTORY
                // ="com.android.webview.chromium.WebViewChromiumFactoryProvider";
                // com.android.webview.chromium.WebViewChromiumFactoryProvider
                // Field mp = WebView.class.getDeclaredField("mProvider");
                //
                // mp.setAccessible(true);
                //
                // Object webViewObject = mp.get(this);
                String mresult;
                this.evaluateJavascript(script, new ValueCallback<String>() {

                    @Override
                    public void onReceiveValue(String value) {
                        // TODO Auto-generated method stub

                        if (mJavaScriptCallBackListen != null) {
                            mJavaScriptCallBackListen.JavaScriptCallBack(value);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            try {

                Field mp = WebView.class.getDeclaredField("mProvider");

                mp.setAccessible(true);

                Object webViewObject = mp.get(this);

                Field wc = webViewObject.getClass().getDeclaredField(
                        "mWebViewCore");

                wc.setAccessible(true);

                Object webViewCore = wc.get(webViewObject);

                Field bf = webViewCore.getClass().getDeclaredField(
                        "mBrowserFrame");

                bf.setAccessible(true);

                Object browserFrame = bf.get(webViewCore);

                Method stringByEvaluatingJavaScriptFromString = browserFrame
                        .getClass()

                        .getDeclaredMethod(
                                "stringByEvaluatingJavaScriptFromString",
                                String.class);

                stringByEvaluatingJavaScriptFromString.setAccessible(true);

                Object obj_value = stringByEvaluatingJavaScriptFromString
                        .invoke(

                                browserFrame, script);
                if (mJavaScriptCallBackListen != null) {
                    mJavaScriptCallBackListen.JavaScriptCallBack(String
                            .valueOf(obj_value));
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            try {

                Field[] fields = WebView.class.getDeclaredFields();

                // 由webview取到webviewcore

                Field field_webviewcore = WebView.class
                        .getDeclaredField("mWebViewCore");

                field_webviewcore.setAccessible(true);

                Object obj_webviewcore = field_webviewcore.get(this);

                // 由webviewcore取到BrowserFrame

                Field field_BrowserFrame = obj_webviewcore.getClass()
                        .getDeclaredField(

                                "mBrowserFrame");

                field_BrowserFrame.setAccessible(true);

                Object obj_frame = field_BrowserFrame.get(obj_webviewcore);

                // 获取BrowserFrame对象的stringByEvaluatingJavaScriptFromString方法

                Method method_stringByEvaluatingJavaScriptFromString = obj_frame
                        .getClass()

                        .getMethod("stringByEvaluatingJavaScriptFromString",
                                String.class);

                // 执行stringByEvaluatingJavaScriptFromString方法

                Object obj_value = method_stringByEvaluatingJavaScriptFromString
                        .invoke(

                                obj_frame,

                                script);

                // 返回执行结果
                if (mJavaScriptCallBackListen != null) {
                    mJavaScriptCallBackListen.JavaScriptCallBack(String
                            .valueOf(obj_value));
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }
}
