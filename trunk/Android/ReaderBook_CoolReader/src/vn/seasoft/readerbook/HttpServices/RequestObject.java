package vn.seasoft.readerbook.HttpServices;

import android.content.Context;

/**
 * User: XuanTrung
 * Date: 12/4/13
 * Time: 11:32 AM
 */
public abstract class RequestObject {
    public RequestHttpToServer request;
    public Context mContext;

    public RequestObject(Context context) {
        mContext = context;
        request = new RequestHttpToServer(context);
    }

    public void SetListener(OnHttpServicesListener _listener) {
        request.SetListener(_listener);
    }

    public void destroy() {
        request.destroy();
    }
}
