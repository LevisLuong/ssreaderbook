package vn.seasoft.sachcuatui.HttpServices;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.sachcuatui.SSReaderApplication;
import vn.seasoft.sachcuatui.Util.SSUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA. User: XuanTrung Date: 11/19/13 Time: 10:49 AM To
 * change this template use File | Settings | File Templates.
 */
public class HttpServicesConnection {
    private final static int MAX_DOWN_THREAD = 3;
    private final static int MAX_LENGTH = 15;
    public OnHttpServicesListener mCallback;
    private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
            MAX_LENGTH);
    private ThreadPoolExecutor pool;

    public HttpServicesConnection(OnHttpServicesListener mCallback) {
        this.mCallback = mCallback;
        pool = new ThreadPoolExecutor(MAX_DOWN_THREAD, MAX_LENGTH, 15,
                TimeUnit.SECONDS, queue);
    }

    public HttpServicesConnection() {
        pool = new ThreadPoolExecutor(MAX_DOWN_THREAD, MAX_LENGTH, 15,
                TimeUnit.SECONDS, queue);
    }

    public void setListener(OnHttpServicesListener _listener) {
        this.mCallback = _listener;
    }

    public void destroy() {
        try {
            mCallback = null;
            pool.shutdownNow();
            pool = null;
            queue.clear();
            queue = null;
        } catch (Exception e) {
            System.out.println("mOnDestroy");
        }
    }

    public void stop() {
        try {
            queue.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMethodString(final Map<String, String> param,
                                 final ResultObject result, final String urlMethod) {
        HttpserviceRequestObject hro = new HttpserviceRequestObject(param,
                result, urlMethod);
        ThreadLoad td = new ThreadLoad(hro);
        try {
            pool.execute(td);
        } catch (Exception e) {
        }
    }

    public void loadMethodString(final Map<String, String> param,
                                 final ResultObject result, final String urlMethod, final int id) {
        HttpserviceRequestObject hro = new HttpserviceRequestObject(param,
                result, urlMethod, id);
        ThreadLoad td = new ThreadLoad(hro);
        try {
            pool.execute(td);
        } catch (Exception e) {
        }
    }

    private class ThreadLoad implements Runnable {
        private HttpserviceRequestObject hro;

        /**
         * Thread request to server
         *
         * @param _hro
         */
        public ThreadLoad(final HttpserviceRequestObject _hro) {
            this.hro = _hro;
        }

        @Override
        public void run() {
            loadStringWithVolley(hro.paramString, hro.result, hro.urlMethod, hro.id);
            hro.paramString = null;
            hro.result = null;
            hro = null;
        }

        private void loadStringWithVolley(final Map<String, String> params,
                                          final ResultObject result, final String urlMethod, final int id) {
            CustomRequest myReq = new CustomRequest(Request.Method.POST, Config.IP_SERVER
                    + "/" + urlMethod, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SSUtil.App_Log("Response from server: ", response + "");
                            try {
                                int errorCode = 0;
                                if (!response.isNull("errorcode")) {
                                    errorCode = response.getInt("errorcode");
                                }
                                if (errorCode == 0) { // No error
                                    if (mCallback != null && result != null) {
                                        result.setData(response);
                                        mCallback.onGetData(result, urlMethod, id);
                                    }
                                } else { // Get error code 1
                                    if (mCallback != null) {
                                        mCallback.onDataError(errorCode, urlMethod);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (error instanceof TimeoutError) {
                        if (mCallback != null) {
                            mCallback.onDataError(ErrorType.ERROR_TIMEOUT, urlMethod);
                        }
                    } else if ((error instanceof NetworkError) || (error instanceof NoConnectionError)) {
                        if (mCallback != null) {
                            mCallback.onDataError(ErrorType.NO_NETWORK_CONNECTION, urlMethod);
                        }
                    } else if ((error instanceof ServerError) || (error instanceof AuthFailureError)) {
                        if (mCallback != null) {
                            mCallback.onDataError(ErrorType.ERROR_INTERNALSERVERERROR, urlMethod);
                        }
                    }
                }
            }
            );
            myReq.setRetryPolicy(new DefaultRetryPolicy(Config.SERVER_TIMEOUT, Config.COUNTDOWN, 1.0f));
            SSReaderApplication.getInstance().addToRequestQueue(myReq);
        }


    }
}

//Custom request string
class CustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    ;

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        AbbottApp.getInstance().checkSessionCookie(response.headers);
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}

class HttpserviceRequestObject {
    Map<String, String> paramString;
    ResultObject result;
    String urlMethod;
    int id;

    public HttpserviceRequestObject(Map<String, String> param,
                                    ResultObject result, String _urlMethod) {
        this.paramString = param;
        if (result != null) {
            this.result = result;
        }
        this.urlMethod = _urlMethod;
        id = 0;
    }

    public HttpserviceRequestObject(Map<String, String> param,
                                    ResultObject result, String _urlMethod, int id) {
        this.paramString = param;
        if (result != null) {
            this.result = result;
        }
        this.urlMethod = _urlMethod;
        this.id = id;
    }
}