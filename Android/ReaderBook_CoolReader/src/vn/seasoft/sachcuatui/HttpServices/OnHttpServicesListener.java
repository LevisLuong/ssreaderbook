package vn.seasoft.sachcuatui.HttpServices;


public interface OnHttpServicesListener {
    void onDataError(int errortype, String urlMethod);

    void onGetData(ResultObject resultData, String urlMethod, int id);
}