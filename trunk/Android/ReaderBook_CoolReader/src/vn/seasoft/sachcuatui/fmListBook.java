package vn.seasoft.sachcuatui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.holoeverywhere.app.Fragment;
import vn.seasoft.sachcuatui.HttpServices.COMMAND_API;
import vn.seasoft.sachcuatui.HttpServices.ErrorType;
import vn.seasoft.sachcuatui.HttpServices.OnHttpServicesListener;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;
import vn.seasoft.sachcuatui.ResultObjects.Result_GetBookByCategory;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.adapter.GridBookAdapter;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.model.Book_Category;
import vn.seasoft.sachcuatui.widget.BookshelfView;
import vn.seasoft.sachcuatui.widget.Rotate3dAnimation;
import vn.seasoft.sachcuatui.widget.ViewError;

/**
 * User: XuanTrung
 * Date: 6/5/2014
 * Time: 10:27 AM
 */
public class fmListBook extends Fragment implements OnHttpServicesListener {
    BookshelfView listbook;
    RelativeLayout rootView;

    Book_Category book_category;
    GridBookAdapter adapter;

    Context mContext;
    ViewError viewError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getActivity();
        adapter = new GridBookAdapter(mContext);
        int idcategory = getArguments().getInt("idbookcategory");
        book_category = (new Book_Category()).getByID(idcategory);
        if (!book_category.getIdcategory().equals(0)) {
            GlobalData.ShowProgressDialog(mContext, R.string.loading);
            SSReaderApplication.getRequestServer(mContext, (OnHttpServicesListener) fmListBook.this).getBookByCategory(book_category.getIdcategory(), adapter.loadMoreData());
        }
        getSupportActionBar().setSubtitle(book_category.getCategory());
        viewError = new ViewError(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!book_category.getIdcategory().equals(0)) {
                    GlobalData.ShowProgressDialog(mContext, R.string.loading);
                    SSReaderApplication.getRequestServer(mContext, (OnHttpServicesListener) fmListBook.this).getBookByCategory(book_category.getIdcategory(), adapter.reloadData());
                }
            }
        });
        viewError.setColorText(Color.WHITE);

        //add tracker google
        // Get tracker.
        Tracker t = ((SSReaderApplication) getActivity().getApplication()).getTracker(
                SSReaderApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(book_category.getCategory());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.fm_listbook, container, false);
        listbook = (BookshelfView) rootView.findViewById(R.id.gv_listbook);
        listbook.setNumColumns(3);
        if (((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            // on a large screen device ...
            listbook.setNumColumns(5);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listbook.setNumColumns(4);
        }
        listbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent t = new Intent(getActivity(), CoolReader.class);
//                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/sample_1.epub");
//                t.putExtra(CoolReader.OPEN_FILE_PARAM, f.getAbsolutePath());
//                startActivity(t);
//                ImageView book = (ImageView) view.findViewById(R.id.grid_item_cover);
//                zoomImageFromThumb(book);
//                applyRotation(book, 0, -180);
//                Book book = adapter.getItem(i);
//                dlgInfoBook_tab dlg = new dlgInfoBook_tab(mContext, book);
//                dlg.show(getSupportActivity());
                Book book = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), actInfoBook.class);
                intent.putExtra("idbook", book.getIdbook());
                intent.putExtra("titlebook", book.getTitle());
                intent.putExtra("authorbook", book.getAuthor());
                intent.putExtra("idcategory", book.getIdcategory());
                intent.putExtra("countdownload", book.getCountdownload());
                intent.putExtra("countview", book.getCountview());
                intent.putExtra("summary", book.getSummary());
                intent.putExtra("cover", book.getImagecover());
                startActivity(intent);
            }
        });
        listbook.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapter.canLoadMoreData()) {
                    SSReaderApplication.getRequestServer(mContext, (OnHttpServicesListener) fmListBook.this).getBookByCategory(book_category.getIdcategory(), adapter.loadMoreData());
                }
            }
        });
        listbook.setAdapter(adapter);

//        if (book_category.getIdcategory().equals(0)) {
//            lstbooks = (new Book()).getAllData();
//            adapter.setData(lstbooks);
//        }
        return rootView;
    }

    private Rotate3dAnimation applyRotation(float start, float end) {
        // Find the center of the container
        final float centerX = 0;
        final float centerY = 0;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
                centerX, centerY, 0, true);
        rotation.setDuration(1000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //rotation.setAnimationListener(new DisplayNextView(position));

//        v.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
//                R.anim.zoom_out));
//        AnimationSet aniSet = new AnimationSet(false);
//        aniSet.setFillAfter(true);
//        aniSet.setDuration(1000);
//        aniSet.addAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
//                R.anim.zoom_out));
////        aniSet.addAnimation(rotation);
//
//        v.startAnimation(aniSet);
        return rotation;
    }

//    AnimatorSet mCurrentAnimator;
//    private int mShortAnimationDuration = 1000;
//    Rect startBounds;
//    Rect finalBounds;
//    float startScaleFinal;
//    ImageView expandedImageView;
//
//    /**
//     * Zoom imageview to full screen
//     *
//     * @param thumbView Image to zoom
//     */
//    private void zoomImageFromThumb(final ImageView thumbView) {
//        // If there's an animation in progress, cancel it immediately and proceed with this one.
//        if (mCurrentAnimator != null) {
//            mCurrentAnimator.cancel();
//        }
//        // Load the high-resolution "zoomed-in" image.
//        expandedImageView = (ImageView) getActivity().findViewById(R.id.expanded_image);
//        expandedImageView.setImageDrawable(thumbView.getDrawable());
//
//        // Calculate the starting and ending bounds for the zoomed-in image. This step
//        // involves lots of math. Yay, math.
//        startBounds = new Rect();
//        finalBounds = new Rect();
//        final Point globalOffset = new Point();
//
//        // The start bounds are the global visible rectangle of the thumbnail, and the
//        // final bounds are the global visible rectangle of the container view. Also
//        // set the container view's offset as the origin for the bounds, since that's
//        // the origin for the positioning animation properties (X, Y).
//        thumbView.getGlobalVisibleRect(startBounds);
//        getActivity().findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
//        startBounds.offset(-globalOffset.x, -globalOffset.y);
//        finalBounds.offset(-globalOffset.x, -globalOffset.y);
//
//        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
//        // "center crop" technique. This prevents undesirable stretching during the animation.
//        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            // Extend start bounds horizontally
//            startScale = (float) startBounds.height() / finalBounds.height();
//            float startWidth = startScale * finalBounds.width();
//            float deltaWidth = (startWidth - startBounds.width()) / 2;
//            startBounds.left -= deltaWidth;
//            startBounds.right += deltaWidth;
//        } else {
//            // Extend start bounds vertically
//            startScale = (float) startBounds.width() / finalBounds.width();
//            float startHeight = startScale * finalBounds.height();
//            float deltaHeight = (startHeight - startBounds.height()) / 2;
//            startBounds.top -= deltaHeight;
//            startBounds.bottom += deltaHeight;
//        }
//
//        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
//        // it will position the zoomed-in view in the place of the thumbnail.
//        //thumbView.setAlpha(0f);
//        expandedImageView.setVisibility(View.VISIBLE);
//
//        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
//        // the zoomed-in view (the default is the center of the view).
//        //expandedImageView.setPivotX(0f);
//        //expandedImageView.setPivotY(0f);
////        AnimatorProxy.wrap(expandedImageView).setPivotX(0f);
////        AnimatorProxy.wrap(expandedImageView).setPivotY(0f);
//        ViewHelper.setPivotX(expandedImageView, 0f);
//        ViewHelper.setPivotY(expandedImageView, 0f);
//        // Construct and run the parallel animation of the four translation and scale properties
//        // (X, Y, SCALE_X, and SCALE_Y).
//        AnimatorSet set = new AnimatorSet();
//        set
//                .play(ObjectAnimator.ofFloat(expandedImageView, "translationX", startBounds.left,
//                        finalBounds.left))
//                .with(ObjectAnimator.ofFloat(expandedImageView, "translationY", startBounds.top,
//                        finalBounds.top))
//                .with(ObjectAnimator.ofFloat(expandedImageView, "scaleX", startScale, 1f))
//                .with(ObjectAnimator.ofFloat(expandedImageView, "scaleY", startScale, 1f));
//        set.setDuration(mShortAnimationDuration);
//        set.setInterpolator(new DecelerateInterpolator());
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mCurrentAnimator = null;
////                Intent t = new Intent(getActivity(), CoolReader.class);
////                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/sample_1.epub");
////                int id = getResources().getIdentifier("sample", "raw", getActivity().getPackageName());
////                final String path = "android.resource://" + getActivity().getPackageName() + "/" + id;
////                t.putExtra(CoolReader.OPEN_FILE_PARAM, f.getAbsolutePath());
////                startActivity(t);
//
////                dlgInfoBook dlg = new dlgInfoBook(getActivity());
////                dlg.show(getSupportActivity());
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                mCurrentAnimator = null;
//            }
//        });
//        set.start();
//        mCurrentAnimator = set;
//
//        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
//        // and show the thumbnail instead of the expanded image.
//        startScaleFinal = startScale;
//
////        expandedImageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (mCurrentAnimator != null) {
////                    mCurrentAnimator.cancel();
////                }
////
////                // Animate the four positioning/sizing properties in parallel, back to their
////                // original values.
////                AnimatorSet set = new AnimatorSet();
////
////                set
////                        .play(ObjectAnimator.ofFloat(expandedImageView, "translationX", startBounds.left))
////                        .with(ObjectAnimator.ofFloat(expandedImageView, "translationY", startBounds.top))
////                        .with(ObjectAnimator
////                                .ofFloat(expandedImageView, "scaleX", startScaleFinal))
////                        .with(ObjectAnimator
////                                .ofFloat(expandedImageView, "scaleY", startScaleFinal));
////
////                set.setDuration(mShortAnimationDuration);
////                set.setInterpolator(new DecelerateInterpolator());
////                set.addListener(new AnimatorListenerAdapter() {
////                    @Override
////                    public void onAnimationEnd(Animator animation) {
////                        expandedImageView.setVisibility(View.GONE);
////                        mCurrentAnimator = null;
////                    }
////
////                    @Override
////                    public void onAnimationCancel(Animator animation) {
////                        expandedImageView.setVisibility(View.GONE);
////                        mCurrentAnimator = null;
////                    }
////                });
////                set.start();
////                mCurrentAnimator = set;
////            }
////        });
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Animate the four positioning/sizing properties in parallel, back to their
//        // original values.
//        if (expandedImageView != null) {
//            if (expandedImageView.getVisibility() == View.VISIBLE) {
//                AnimatorSet set = new AnimatorSet();
//                set
//                        .play(ObjectAnimator.ofFloat(expandedImageView, "translationX", startBounds.left))
//                        .with(ObjectAnimator.ofFloat(expandedImageView, "translationY", startBounds.top))
//                        .with(ObjectAnimator
//                                .ofFloat(expandedImageView, "scaleX", startScaleFinal))
//                        .with(ObjectAnimator
//                                .ofFloat(expandedImageView, "scaleY", startScaleFinal));
//
//                set.setDuration(mShortAnimationDuration);
//                set.setInterpolator(new DecelerateInterpolator());
//                set.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        expandedImageView.setVisibility(View.GONE);
//                        mCurrentAnimator = null;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        expandedImageView.setVisibility(View.GONE);
//                        mCurrentAnimator = null;
//                    }
//                });
//                set.start();
//            }
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
        rootView.removeView(viewError);
        rootView.addView(viewError);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        viewError.setLayoutParams(layoutParams);
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        rootView.removeView(viewError);
        if (urlMethod.equals(COMMAND_API.GET_BOOK_BY_CATEGORY)) {
            final Result_GetBookByCategory data = (Result_GetBookByCategory) resultData;
            adapter.SetListBooks(data.lstBooks);
//            if (!adapter.isHaveNew()) {
//
//            }
        }
        GlobalData.DissmissProgress();
    }
}
