package vn.seasoft.readerbook.HttpServices;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class RequestHttpToServer {
    Context mContext;
    private HttpServicesConnection hsc;
    private Map<String, String> params;

    public RequestHttpToServer(Context c) {
        mContext = c;
        hsc = new HttpServicesConnection();
    }

    public void SetListener(OnHttpServicesListener _listener) {
        hsc.setListener(_listener);
    }

    public void destroy() {
        hsc.destroy();
        hsc = null;
    }

    public void stop() {
        hsc.stop();
    }

    public void initParam() {
        params = new HashMap<String, String>();
    }

    public Map<String, String> getParam() {
        return params;
    }

    public void addParam(String key, Object value) {
        params.put(key, "" + value);
    }

    public void requestStringToServer(ResultObject resultObject, String method) {
        System.out.println("request server: " + params);
        hsc.loadMethodString(params, resultObject, method);
    }

    public void requestStringToServer(ResultObject resultObject, String method, int id) {
        System.out.println("request server: " + params);
        hsc.loadMethodString(params, resultObject, method, id);
    }
}
