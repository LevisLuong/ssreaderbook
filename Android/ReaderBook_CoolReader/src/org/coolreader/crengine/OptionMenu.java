package org.coolreader.crengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.holoeverywhere.widget.*;
import vn.seasoft.readerbook.R;
import yuku.ambilwarna.AmbilWarnaDialog;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * User: XuanTrung
 * Date: 6/23/2014
 * Time: 10:31 AM
 */
public class OptionMenu extends LinearLayout {
    final String[] mInterlineSpacesTitle = new String[]{"Hẹp", "Bình thường", "Nới rộng"};
    BaseActivity mActivity;
    ReaderView mReaderView;
    Properties mProperties;
    String[] mFontSizes = new String[]{
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "42", "44", "48", "52", "56", "60", "64", "68", "72"
    };
    String[] mInterlineSpaces = new String[]{
            "100", "150", "200"
    };
    String[] mFontColor = new String[]{String.valueOf(Color.BLACK), String.valueOf(Color.WHITE), String.valueOf(Color.RED)};
    String[] mFontColorTitle = new String[]{"Đen", "Trắng", "Đỏ"};
    ThumbnailCache textureSampleCache = new ThumbnailCache(64, 64, 100);
    BackgroundTextureInfo[] textures;
    private Button optionmenuBtnSizeFontSmaller;
    private Button optionmenuBtnSizeFontBigger;
    private Spinner optionmenuSpinFontFace;
    private Spinner optionmenuSpinLineSpace;
    private ImageView optionmenuImgFontColor;
    private Spinner optionmenuImgBackground;
    public OptionMenu(BaseActivity context, ReaderView readerView) {
        super(context);
        mActivity = context;
        mReaderView = readerView;
        initView();
    }

    private void assignViews(View root) {
        optionmenuBtnSizeFontBigger = (Button) root.findViewById(R.id.optionmenu_btnSizeFontBigger);
        optionmenuBtnSizeFontSmaller = (Button) root.findViewById(R.id.optionmenu_btnSizeFontSmaller);
        optionmenuSpinFontFace = (Spinner) root.findViewById(R.id.optionmenu_spinFontFace);
        optionmenuSpinLineSpace = (Spinner) root.findViewById(R.id.optionmenu_spinLineSpace);
        optionmenuImgFontColor = (ImageView) root.findViewById(R.id.optionmenu_imgFontColor);
        optionmenuImgBackground = (Spinner) root.findViewById(R.id.optionmenu_spinBackground);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.option_menu, this, true);
        assignViews(view);
        mProperties = new Properties(mActivity.settings()); //  readerView.getSettings();
        initSpinner();
    }

    private void ShowDialogPickerColor(final String property, String title) {
        ColorPickerDialog dlg = new ColorPickerDialog(mActivity, new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                mProperties.setColor(property, color);
                //optionmenuImgcolor.setBackgroundColor(color);
                if (property.equals(Settings.PROP_BACKGROUND_COLOR)) {
                    String texture = mProperties.getProperty(Settings.PROP_PAGE_BACKGROUND_IMAGE, Engine.NO_TEXTURE.id);
                    if (texture != null && !texture.equals(Engine.NO_TEXTURE.id)) {
                        // reset background image
                        mProperties.setProperty(Settings.PROP_PAGE_BACKGROUND_IMAGE, Engine.NO_TEXTURE.id);
                        // TODO: show notification?
                    }
                }
                SetSetting();
            }
        }, mProperties.getColor(property, Color.WHITE), title);
        dlg.show();
    }

    private void initSpinner() {
        //Init spinner font face
        final String[] mFontFaces = Engine.getFontFaceList();
        ArrayAdapter<String> adapterFontFace = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, mFontFaces);
        optionmenuSpinFontFace.setAdapter(adapterFontFace);
        optionmenuSpinFontFace.setSelection(getSelectedItemIndex(mFontFaces, Settings.PROP_FONT_FACE));
        //Init spinner line space
        ArrayAdapter<String> adapterLineSpace = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, mInterlineSpacesTitle);
        optionmenuSpinLineSpace.setAdapter(adapterLineSpace);
        optionmenuSpinLineSpace.setSelection(getSelectedItemIndex(mInterlineSpaces, Settings.PROP_INTERLINE_SPACE));
        //optionmenuSpinFontFace.setSelection(getSelectedItemIndex(mFontFaces, Settings.PROP_INTERLINE_SPACE));
        //Init font color
//        optionmenuSpinFontColor.setAdapter(new AdapterFontColor(mActivity, R.layout.spinner_item, mFontColorTitle));
//        optionmenuSpinFontColor.setSelection(getSelectedColorIndex(mFontColor, Settings.PROP_FONT_COLOR));
        optionmenuImgFontColor.setBackgroundColor(mProperties.getColor(Settings.PROP_FONT_COLOR, Color.BLACK));
        //Init background
        textures = mReaderView.getEngine().getAvailableTextures();
        optionmenuImgBackground.setAdapter(new AdapterBackground());
        optionmenuImgBackground.setSelection(findValueBackground(textures, mProperties.getProperty(Settings.PROP_PAGE_BACKGROUND_IMAGE)));

        initListener();
    }

    private int getPositionArr(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void initListener() {
        optionmenuBtnSizeFontBigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = mProperties.getProperty(Settings.PROP_FONT_SIZE, "20");
                int pos = getPositionArr(mFontSizes, size);
                pos++;
                if (pos >= mFontSizes.length) {
                    pos = mFontSizes.length - 1;
                }
                mProperties.setProperty(Settings.PROP_FONT_SIZE, mFontSizes[pos]);
                //mReaderView.reloadDocument();
                SetSetting();
            }
        });
        optionmenuBtnSizeFontSmaller.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = mProperties.getProperty(Settings.PROP_FONT_SIZE, "20");
                int pos = getPositionArr(mFontSizes, size);
                pos--;
                if (pos < 0) {
                    pos = 0;
                }
                mProperties.setProperty(Settings.PROP_FONT_SIZE, mFontSizes[pos]);
                //mReaderView.reloadDocument();
                SetSetting();
            }
        });
        optionmenuSpinFontFace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProperties.setProperty(Settings.PROP_FONT_FACE, adapterView.getItemAtPosition(i).toString());
                //mReaderView.reloadDocument();
                SetSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        optionmenuSpinLineSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProperties.setProperty(Settings.PROP_INTERLINE_SPACE, String.valueOf(mInterlineSpaces[i]));
                //mReaderView.reloadDocument();
                SetSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        optionmenuImgFontColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dlg = new AmbilWarnaDialog(mActivity, mProperties.getColor(Settings.PROP_FONT_COLOR, Color.BLACK), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        optionmenuImgFontColor.setBackgroundColor(color);
                        mProperties.setColor(Settings.PROP_FONT_COLOR, color);
                        //mReaderView.reloadDocument();
                        SetSetting();
                    }
                });
                dlg.show();
            }
        });
        optionmenuImgBackground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProperties.setProperty(Settings.PROP_PAGE_BACKGROUND_IMAGE, textures[i].id);
                SetSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SetSetting() {
        mActivity.setSettings(mProperties, 1000, true);

    }

    public int findValue(String[] arr, String value) {
        if (value == null)
            return -1;
        for (int i = 0; i < arr.length; i++) {
            if (value.equals(arr[i]))
                return i;
        }
        return -1;
    }

    public int findValueBackground(BackgroundTextureInfo[] arr, String value) {
        if (value == null)
            return -1;
        for (int i = 0; i < arr.length; i++) {
            if (value.equals(arr[i].id))
                return i;
        }
        return -1;
    }

    public int getSelectedItemIndex(String[] arr, String nameProperty) {
        return findValue(arr, mProperties.getProperty(nameProperty));
    }

    public int getSelectedColorIndex(String[] arr, String nameProperty) {
        return findValue(arr, String.valueOf(mProperties.getColor(nameProperty, Color.WHITE)));
    }

    public class AdapterFontColor extends ArrayAdapter<String> {

        public AdapterFontColor(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        public AdapterFontColor(Context ctx, int txtViewResourceId) {
            super(ctx, txtViewResourceId);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.spinner_item, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(android.R.id.text1);
            main_text.setText(mFontColorTitle[position]);
            main_text.setTextColor(Integer.parseInt(mFontColor[position]));
            return mySpinner;
        }
    }

    class ThumbnailCache {
        final int maxcount;
        final int dx;
        final int dy;
        ArrayList<Item> list = new ArrayList<Item>();

        public ThumbnailCache(int dx, int dy, int maxcount) {
            this.dx = dx;
            this.dy = dy;
            this.maxcount = maxcount;
        }

        private void remove(int maxsize) {
            while (list.size() > maxsize) {
                Item item = list.remove(0);
                item.clear();
            }
        }

        private Drawable createDrawable(String path) {
            File f = new File(path);
            if (!f.isFile() || !f.exists())
                return null;
            try {
                BitmapDrawable drawable = (BitmapDrawable) BitmapDrawable.createFromPath(path);
                if (drawable == null)
                    return null;
                Bitmap src = drawable.getBitmap();
                Bitmap bmp = Bitmap.createScaledBitmap(src, dx, dy, true);
                //Canvas canvas = new Canvas(bmp);
                BitmapDrawable res = new BitmapDrawable(bmp);
                //src.recycle();
                Item item = new Item();
                item.path = path;
                item.drawable = res; //drawable;
                item.bmp = bmp;
                list.add(item);
                remove(maxcount);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private Drawable createDrawable(int resourceId) {
            try {
                //Drawable drawable = mReaderView.getActivity().getResources().getDrawable(resourceId);
                InputStream is = getContext().getResources().openRawResource(resourceId);
                if (is == null)
                    return null;
                BitmapDrawable src = new BitmapDrawable(is);
                Item item = new Item();
                item.id = resourceId;
                Bitmap bmp = Bitmap.createScaledBitmap(src.getBitmap(), dx, dy, true);
                BitmapDrawable res = new BitmapDrawable(bmp);
                item.drawable = res;
                item.bmp = bmp;
                list.add(item);
                remove(maxcount);
                return res;
            } catch (Exception e) {
                return null;
            }
        }

        public Drawable getImage(String path) {
            if (path == null || !path.startsWith("/"))
                return null;
            // find existing
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).path != null && path.equals(list.get(i).path)) {
                    Item item = list.remove(i);
                    list.add(item);
                    return item.drawable;
                }
            }
            return createDrawable(path);
        }

        public Drawable getImage(int resourceId) {
            if (resourceId == 0)
                return null;
            // find existing
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == resourceId) {
                    Item item = list.remove(i);
                    list.add(item);
                    return item.drawable;
                }
            }
            return createDrawable(resourceId);
        }

        public void clear() {
            remove(0);
        }

        class Item {
            Drawable drawable;
            Bitmap bmp;
            String path;
            int id;

            public void clear() {
                if (bmp != null) {
                    //bmp.recycle();
                    bmp = null;
                }
                if (drawable != null)
                    drawable = null;
            }
        }
    }

    public class AdapterBackground extends BaseAdapter {

        public AdapterBackground() {
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public int getCount() {
            return textures.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.spinner_background_item, parent, false);
            ImageView img = (ImageView) mySpinner.findViewById(R.id.spinneritem_background);

            BackgroundTextureInfo texture = textures[position];
            //img.setBackgroundColor(cl);
            if (texture.resourceId != 0) {
//				img.setImageDrawable(null);
//				img.setImageResource(texture.resourceId);
//				img.setBackgroundColor(Color.TRANSPARENT);
                Drawable drawable = textureSampleCache.getImage(texture.resourceId);
                if (drawable != null) {
                    img.setImageResource(0);
                    img.setImageDrawable(drawable);
                    //img.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    //img.setBackgroundColor(cl);
                    img.setImageResource(0);
                    img.setImageDrawable(null);
                }
            } else {
                // load image from file
                Drawable drawable = textureSampleCache.getImage(texture.id);
                if (drawable != null) {
                    img.setImageResource(0);
                    img.setImageDrawable(drawable);
                    //img.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    //img.setBackgroundColor(cl);
                    img.setImageResource(0);
                    img.setImageDrawable(null);
                }
            }

            return mySpinner;
        }
    }
}
