package org.coolreader.crengine;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import org.coolreader.CoolReader;
import vn.seasoft.readerbook.R;

public class OPDSCatalogEditDialog extends Dialog {

    private final CoolReader mActivity;
    private final LayoutInflater mInflater;
    private final FileInfo mItem;
    private final EditText nameEdit;
    private final EditText urlEdit;
    private final Runnable mOnUpdate;

    public OPDSCatalogEditDialog(CoolReader activity, FileInfo item, Runnable onUpdate) {
        super(activity);
        mActivity = activity;
        mItem = item;
        mOnUpdate = onUpdate;
        mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(R.layout.catalog_edit_dialog, null);
        nameEdit = (EditText) view.findViewById(R.id.catalog_name);
        urlEdit = (EditText) view.findViewById(R.id.catalog_url);
        nameEdit.setText(mItem.filename);
        urlEdit.setText(mItem.getOPDSUrl());
        setContentView(view);
    }

//    @Override
//    protected void onPositiveButtonClick() {
//        String url = urlEdit.getText().toString();
//        boolean blacklist = checkBlackList(url);
//        if (OPDSConst.BLACK_LIST_MODE == OPDSConst.BLACK_LIST_MODE_FORCE) {
//            mActivity.showToast(R.string.black_list_enforced);
//        } else if (OPDSConst.BLACK_LIST_MODE == OPDSConst.BLACK_LIST_MODE_WARN) {
//            mActivity.askConfirmation(R.string.black_list_warning, new Runnable() {
//                @Override
//                public void run() {
//                    save();
//                    //OPDSCatalogEditDialog.super.onPositiveButtonClick();
//                }
//
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    //onNegativeButtonClick();
//                }
//            });
//        } else {
//            save();
//           // super.onPositiveButtonClick();
//        }
//    }

    private boolean checkBlackList(String url) {
        for (String s : OPDSConst.BLACK_LIST) {
            if (s.equals(url))
                return true;
        }
        return false;
    }

    private void save() {
        mActivity.getDB().saveOPDSCatalog(mItem.id,
                urlEdit.getText().toString(), nameEdit.getText().toString());
        mOnUpdate.run();
        dismiss();
    }
}
