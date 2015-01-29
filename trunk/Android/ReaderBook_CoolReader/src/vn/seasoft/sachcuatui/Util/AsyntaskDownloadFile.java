package vn.seasoft.sachcuatui.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import org.holoeverywhere.app.ProgressDialog;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("NewApi")
public class AsyntaskDownloadFile extends AsyncTask<String, String, String> {
    private static final String TAG = AsyntaskDownloadFile.class.getSimpleName();
    ProgressDialog loadingProgress;
    //Idelegate delegate;
    String url;
    String[] arrUrl;
    Context ct;
    IDownLoadMood listener;

    public AsyntaskDownloadFile(Context ct, String url) {
        this.ct = ct;
        this.url = url;
    }

    public AsyntaskDownloadFile(Context ct, String... arrurl) {
        this.ct = ct;
        arrUrl = arrurl;
    }

    public void setListenerDownload(IDownLoadMood _listener) {
        listener = _listener;
    }

    public void startDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ExecutorService modelExecutor = Executors.newFixedThreadPool(5);
            if (arrUrl != null) {
                this.executeOnExecutor(modelExecutor, arrUrl);
            } else {
                this.executeOnExecutor(modelExecutor, url);
            }
        } else {
            if (arrUrl != null) {
                this.execute(arrUrl);
            } else {
                this.execute(url);
            }
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingProgress = new ProgressDialog(ct);
        loadingProgress.setMessage("Đang tải");
        loadingProgress.setProgressNumberFormat("Đã tải: %1d/%2d");
        loadingProgress.setIndeterminate(false);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loadingProgress.setCanceledOnTouchOutside(false);
        loadingProgress.show();
    }


    @Override
    protected String doInBackground(String... f_url) {
        int count;
        File file = null;
        try {
            if (f_url.length > 1) { //Download picture book
                loadingProgress.setMax(f_url.length);
                //Save file
                for (int i = 0; i < f_url.length; i++) {
                    publishProgress("" + (i + 1));
                    f_url[i] = f_url[i].replace(" ", "%20");
                    file = SSUtil.downloadPictureBook(ct, f_url[i]);
                    if (file.exists()) {
                        continue;
                    }
                    URL url = new URL(f_url[i]);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    // Output stream to write file
                    // OutputStream output = new
                    // FileOutputStream("/sdcard/downloadedfile.jpg");
                    OutputStream output = new FileOutputStream(file);
                    byte data[] = new byte[1024];
                    while ((count = input.read(data)) != -1) {
                        // After this onProgressUpdate will be called
//                        publishProgress("" + ((totaldownload * 100) / lenghtOfFile));
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();
                }
            } else {//Download text book
                String urlFile = f_url[0].replace(" ", "%20");
                file = SSUtil.downloadBook(urlFile);
                if (file.exists()) {
                    return file.getAbsolutePath();
                } else {
                    URL url = new URL(urlFile);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();
                    loadingProgress.setMax(lenghtOfFile);
                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    // Output stream to write file
                    // OutputStream output = new
                    // FileOutputStream("/sdcard/downloadedfile.jpg");
                    OutputStream output = new FileOutputStream(file);
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + total);
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();
                }
            }
            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Updating progress bar
     */

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        loadingProgress.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */
    @Override
    protected void onPostExecute(String file_url) {
        loadingProgress.dismiss();
        if (file_url.equals("")) {
            listener.onCanceled();
        } else {
            listener.onDownloadComplete(file_url);
        }
    }


    public interface IDownLoadMood {
        public void onDownloadComplete(String urlResultMood);

        public void onCanceled();
    }
}