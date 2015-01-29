package org.coolreader.crengine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import org.holoeverywhere.widget.*;
import vn.seasoft.sachcuatui.R;

public class CRToolBar extends LinearLayout {

    BaseActivity mContext;
    ReaderView mReaderView;
    OptionMenu menu;
    private LinearLayout toolbarLnTop;
    private ImageButton toolbarBtnback;
    private TextView toolbarTxtTitle;
    private TextView toolbarTxtPage;
    private SeekBar toolbarSeekbar;
    private RelativeLayout toolbarEmpty;
    private LinearLayout toolbarLnBottom;
    private ImageButton toolbarBtnAutoScroll;
    private ImageButton toolbarBtnDayNight;
    private ImageButton toolbarBtnBookMark;
    private ImageButton toolbarBtnTOC;
    private ImageButton toolbarBtnChangeOri;
    private ImageButton toolbarBtnSetting;
    public CRToolBar(BaseActivity context, ReaderView readerView) {
        super(context);
        this.mContext = context;
        mReaderView = readerView;
        initView();
    }

    public CRToolBar(BaseActivity context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void assignViews(View rootview) {
        toolbarTxtTitle = (TextView) rootview.findViewById(R.id.toolbar_txtTitle);
        toolbarBtnback = (ImageButton) rootview.findViewById(R.id.toolbar_btnback);
        toolbarTxtPage = (TextView) rootview.findViewById(R.id.toolbar_txtPage);
        toolbarSeekbar = (SeekBar) rootview.findViewById(R.id.toolbar_seekbar);
        toolbarEmpty = (RelativeLayout) rootview.findViewById(R.id.toolbar_empty);
        toolbarBtnAutoScroll = (ImageButton) rootview.findViewById(R.id.toolbar_btnAutoScroll);
        toolbarBtnChangeOri = (ImageButton) rootview.findViewById(R.id.toolbar_btnChangeOri);
        toolbarBtnDayNight = (ImageButton) rootview.findViewById(R.id.toolbar_btnDayNight);
        toolbarBtnBookMark = (ImageButton) rootview.findViewById(R.id.toolbar_btnBookMark);
        toolbarBtnTOC = (ImageButton) rootview.findViewById(R.id.toolbar_btnTOC);
        toolbarBtnSetting = (ImageButton) rootview.findViewById(R.id.toolbar_btnSetting);
        toolbarLnTop = (LinearLayout) rootview.findViewById(R.id.toolbar_lnTop);
        toolbarLnBottom = (LinearLayout) rootview.findViewById(R.id.toolbar_lnBottom);

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_toolbar_item, this, true);
        assignViews(view);
        setListenerControl();
        //Add menu
        menu = new OptionMenu(mContext, mReaderView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        toolbarEmpty.addView(menu, params);
        menu.setVisibility(View.GONE);
        //Init seekbar page
        showProgressPage();
    }

    public void SetVisible() {
        Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        setVisibility(View.VISIBLE);
        toolbarLnTop.startAnimation(slideDown);
        toolbarLnBottom.startAnimation(slideUp);
        menu.setVisibility(View.GONE);
    }

    public void SetInvisible() {
        menu.setVisibility(View.GONE);
        setVisibility(View.GONE);
    }

    private void setListenerControl() {
        toolbarBtnback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.finish();
            }
        });
        toolbarBtnAutoScroll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReaderView.toggleAutoScroll();
                SetInvisible();
            }
        });
        toolbarBtnBookMark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((org.coolreader.CoolReader) mContext).showBookmarksDialog();
            }
        });
        toolbarBtnDayNight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReaderView.toggleDayNightMode();
            }
        });
        toolbarBtnTOC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReaderView.showTOC();
                SetInvisible();
            }
        });
        toolbarBtnSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu.getVisibility() == View.GONE) {
                    menu.setVisibility(View.VISIBLE);
                } else {
                    menu.setVisibility(View.GONE);
                }

            }
        });
        toolbarBtnChangeOri.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReaderView.toggleScreenOrientation();
            }
        });
        toolbarEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE);
            }
        });
    }

    public void updateCurrrentPosition(FileInfo book, PositionProperties props) {
        String posText = null;
        int pageNumber = props.pageNumber + 1;
        if (props != null) {
            int percent = (int) (10000 * (long) props.y / props.fullHeight);
            String percentText = "" + (percent / 100) + "." + (percent % 10) + "%";
            posText = "" + pageNumber + " / " + props.pageCount + " (" + percentText + ")";
        }
        toolbarTxtTitle.setText(book.getTitle());
        toolbarTxtPage.setText(posText);
        toolbarSeekbar.setOnSeekBarChangeListener(null);
        toolbarSeekbar.setMax(props.pageCount + 1);
        toolbarSeekbar.setProgress(pageNumber);
        toolbarSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mReaderView.goToPage(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void showProgressPage() {
        mReaderView.getCurrentPositionProperties(new ReaderView.PositionPropertiesCallback() {
            @Override
            public void onPositionProperties(final PositionProperties props, final String positionText) {
                if (props == null)
                    return;
                int pageNumber = props.pageNumber + 1;
                toolbarTxtPage.setText(positionText);
                toolbarSeekbar.setOnSeekBarChangeListener(null);
                toolbarSeekbar.setMax(props.pageCount + 1);
                toolbarSeekbar.setProgress(pageNumber);
                toolbarSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mReaderView.goToPage(i);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
    }
}
