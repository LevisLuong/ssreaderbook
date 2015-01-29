package vn.seasoft.sachcuatui;

import android.os.Bundle;
import android.os.Environment;
import com.joanzapata.pdfview.PDFView;
import org.holoeverywhere.app.Activity;
import vn.seasoft.sachcuatui.Util.GlobalData;

import java.io.File;

/**
 * User: XuanTrung
 * Date: 1/28/2015
 * Time: 4:26 PM
 */
public class actPdfView extends Activity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        String fileNameTestPdf = "test.pdf";
        File testFilePdf = new File(Environment.getExternalStorageDirectory()
                + "/" + GlobalData.LOCATION_SAVE_BOOK + "/" + fileNameTestPdf);
        //read file pdf from file
        pdfView.fromFile(testFilePdf).load();

    }
}
