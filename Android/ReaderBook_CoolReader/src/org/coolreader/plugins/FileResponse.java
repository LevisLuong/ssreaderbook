package org.coolreader.plugins;

import android.util.Log;
import org.coolreader.plugins.litres.ResponseCallback;

import java.io.File;

public class FileResponse implements ResponseCallback, AsyncResponse {
    public OnlineStoreBook book;
    public File fileToSave;
    public boolean isTrial;
    private int errorCode = -1;
    private String errorMessage;

    public FileResponse(OnlineStoreBook book, File fileToSave, boolean isTrial) {
        this.book = book;
        this.fileToSave = fileToSave;
        this.isTrial = isTrial;
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        Log.e("litres", "error " + errorCode + ": " + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public AsyncResponse getResponse() {
        if (errorCode != -1)
            return new ErrorResponse(errorCode, errorMessage);
        return this;
    }
}
