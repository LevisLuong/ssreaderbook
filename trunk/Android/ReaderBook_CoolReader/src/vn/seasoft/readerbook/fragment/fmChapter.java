package vn.seasoft.readerbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnListViewOnScrollListener;
import org.coolreader.CoolReader;
import org.coolreader.crengine.BackgroundThread;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.ResultObjects.Result_GetBookChapter;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.AsyntaskDownloadFile;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.actInfoBook;
import vn.seasoft.readerbook.actReadPictureBook;
import vn.seasoft.readerbook.adapter.BookChapterAdapter;
import vn.seasoft.readerbook.dialog.dlgGoChapter;
import vn.seasoft.readerbook.listener.IDialogEditText;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Chapter;
import vn.seasoft.readerbook.widget.ViewError;

import java.util.Collections;
import java.util.Comparator;

/**
 * User: XuanTrung
 * Date: 10/16/2014
 * Time: 11:21 AM
 */
public class fmChapter extends Fragment implements OnHttpServicesListener {
    Book book;
    BookChapterAdapter adapter;
    ListView listview;

    Context mContext;


    public fmChapter(Context _ct, Book book) {
        super();
        mContext = _ct;
        this.book = book;
    }

    private RelativeLayout dlginfoContainer;

    private void assignViews(View root) {
        dlginfoContainer = (RelativeLayout) root.findViewById(R.id.fmchapter_container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adapter = new BookChapterAdapter(mContext);
        listview = new ListView(mContext);
        listview.setDivider(null);
        listview.setCacheColorHint(android.R.color.transparent);
        listview.setSelector(android.R.color.transparent);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadBook(i);
            }
        });

        adapter = new BookChapterAdapter(mContext);
        adapter.setListener(new BookChapterAdapter.IAdapterBookChapter() {
            @Override
            public void DownloadAll(final Book_Chapter book_chapter) {
                if (book.getIdcategory() == 8) {//id truyen tranh
                    String[] arrPic = book_chapter.getFilename().split(",");
                    for (int i = 0; i < arrPic.length; i++) {
                        arrPic[i] = GlobalData.getUrlPictureBook(book.getIdbook(), book_chapter.getIdbook_chapter()) + arrPic[i];
                    }
                    AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, arrPic);
                    download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                        @Override
                        public void onDownloadComplete(String urlResultMood) {
                            book.addNewData();
                            book_chapter.setIsDownloaded(true);
                            book_chapter.updateData();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(mContext, R.string.download_success, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCanceled() {
                            Toast.makeText(mContext, R.string.download_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                    download.startDownload();
                } else {
                    AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, GlobalData.getUrlBook(book_chapter));
                    download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                        @Override
                        public void onDownloadComplete(String urlResultMood) {
                            book.addNewData();
                            book_chapter.setIsDownloaded(true);
                            book_chapter.updateData();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(mContext, R.string.download_success, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCanceled() {
                            Toast.makeText(mContext, R.string.download_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                    download.startDownload();
                }
                //thong ke download
                SSReaderApplication.getRequestServer(mContext).addCountBook(book.getIdbook());
            }

            @Override
            public void DeleteAll(Book_Chapter book_chapter) {
                if (book.getIdcategory() == 8) {//id truyen tranh
                    SSUtil.deletePictureBook(mContext, book_chapter);
                } else {
                    SSUtil.deleteBook(GlobalData.getUrlBook(book_chapter));
                }
                book_chapter.setIsDownloaded(false);
                book_chapter.updateData();
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "Đã xóa khỏi máy", Toast.LENGTH_SHORT).show();
            }
        });
        listview.setAdapter(adapter);

        requestBookChapter();
//
//
//        listview.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
//            @Override
//            public void onClick(View itemView, View clickedView, int position) {
//
//            }
//        }, R.id.buttonA, R.id.buttonB);
    }

    public void setSelectionListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listview != null) {
                    int position = 0;
                    for (Book_Chapter bookchap : adapter.getList()) {
                        if (bookchap.getCurrentread()) {
                            break;
                        }
                        position++;
                    }
                    if (position == adapter.getList().size()) {
                        position = 0;
                    }
                    listview.setSelection(position);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmchapter, container, false);
        assignViews(v);
        /*
        Khi view duoc khoi tao:
          + Add listview vao container
          + Neu load bi loi adapter rong add view error
         */
//        adapter.SetListBooks(lstBookchapter);

//        setListToCard();
//
//        //Select current read in listview
//        setSelectionListview();

        if (adapter.isEmpty()) {
            SSUtil.addViewContainer(dlginfoContainer, new ViewError(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SSUtil.addViewContainer(dlginfoContainer, new ProgressBar(mContext), true);
                    requestBookChapter();
                }
            }).setColorText(Color.BLACK), true);
        } else {
            SSUtil.addViewContainer(dlginfoContainer, listview, false);
        }


        //Init quick return listview

        ImageView sortchapter = (ImageView) v.findViewById(R.id.fmchapter_qr_sort);
        ImageView gochapter = (ImageView) v.findViewById(R.id.fmchapter_qr_gochapter);

        sortchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortChapter();
            }
        });
        gochapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotochapter();
            }
        });

        RelativeLayout quickreturn = (RelativeLayout) v.findViewById(R.id.fmchapter_quickreturn);
        listview.setOnScrollListener(new SpeedyQuickReturnListViewOnScrollListener(getActivity(), QuickReturnType.FOOTER, null, quickreturn));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.SetListBooks((new Book_Chapter()).getByidBook(book.getIdbook()));
                sortChapter(old_asc);
                ((actInfoBook) getActivity()).setCountChapter(adapter.getCount() + "");
            }
        });
    }

    boolean asc = true;
    boolean old_asc = false;

    public void gotochapter() {
        if (!adapter.getList().isEmpty()) {
            dlgGoChapter dlg = new dlgGoChapter(mContext);
            dlg.setTitle("Nhập chương để đọc");
            dlg.setTextView("/" + adapter.getList().size());
            dlg.setListener(new IDialogEditText() {
                @Override
                public void getValue(String value) {
                    try {
                        int page = Integer.parseInt(value.trim());
                        if (page > 0 && page <= adapter.getList().size()) {
                            page = page - 1;
                            if (!asc) {
                                page = adapter.getList().size() - 1 - page;
                            }
                            loadBook(page);
                        } else {
                            Toast.makeText(mContext, "Không có chương này !", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, "Không có chương này !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dlg.show(getSupportActivity());
        }
    }

    public void sortChapter() {
        if (!adapter.getList().isEmpty()) {
            old_asc = asc;
            if (asc) {
                Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
                asc = false;
            } else {
                Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() < t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
                asc = true;
            }
//            fmchapter.setListData(lstBookchapter);
            adapter.notifyDataSetChanged();
            setSelectionListview();
        }
    }

    public void sortChapter(boolean asc) {
        if (!adapter.getList().isEmpty()) {
            if (asc) {
                Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
            } else {
                Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() < t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
            }
            //            fmchapter.setListData(lstBookchapter);
            adapter.notifyDataSetChanged();
            setSelectionListview();
        }
    }

    public void requestBookChapter() {
        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);
    }

    public void loadContinueBook() {
        GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
        BackgroundThread.instance().executeGUI(new Runnable() {
            @Override
            public void run() {
                if (!adapter.getList().isEmpty()) {
                    int position;
                    position = -1;
                    for (int y = 0; y < adapter.getList().size(); y++) {
                        Book_Chapter book_chapter = adapter.getList().get(y);
                        if (book_chapter.getCurrentread()) {
                            position = y;
                            break;
                        }
                    }
                    if (position == -1) {
                        if (!asc) {
                            position = adapter.getList().size() - 1;
                        } else {
                            position = 0;
                        }
                    }
                    loadBook(position);
                }
                GlobalData.DissmissProgress();
            }
        });
    }

    public void loadBook(final int position) {
        final Book_Chapter book_chapter = adapter.getList().get(position);
        if (book.getIdcategory() == 8) {
            book.addNewData();
            adapter.setLoadBook(position);
            Intent t = new Intent(mContext, actReadPictureBook.class);
            t.putExtra("arrbook", book_chapter.getFilename());
            t.putExtra("idbook", book_chapter.getIdbook());
            t.putExtra("idbookchapter", book_chapter.getIdbook_chapter());
            t.putExtra("position", book_chapter.getReadposition());
            mContext.startActivity(t);
        } else {
            AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, GlobalData.getUrlBook(book_chapter));
            download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                @Override
                public void onDownloadComplete(String urlResultMood) {
                    book.addNewData();
                    adapter.setLoadBook(position);
                    Intent t = new Intent(mContext, CoolReader.class);
                    t.putExtra(CoolReader.OPEN_FILE_PARAM, urlResultMood);
                    mContext.startActivity(t);
                }

                @Override
                public void onCanceled() {
                    org.holoeverywhere.widget.Toast.makeText(mContext, R.string.download_fail, org.holoeverywhere.widget.Toast.LENGTH_SHORT).show();
                }
            });
            download.startDownload();
        }
        //thong ke download
        SSReaderApplication.getRequestServer(mContext).addCountBook(book.getIdbook());
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
        if (adapter.isEmpty()) {
            SSUtil.addViewContainer(dlginfoContainer, new ViewError(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SSUtil.addViewContainer(dlginfoContainer, new ProgressBar(mContext), true);
                    requestBookChapter();
                }
            }).setColorText(Color.BLACK), true);
        } else {
            SSUtil.addViewContainer(dlginfoContainer, listview, false);
        }
    }

    @Override
    public void onGetData(final ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_BOOK_CHAPTER)) {
            Result_GetBookChapter data = (Result_GetBookChapter) resultData;
            adapter.SetListBooks(data.lstBookChaps);
            SSUtil.addViewContainer(dlginfoContainer, listview, false);
            try {
                ((actInfoBook) getActivity()).setCountChapter(adapter.getCount() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GlobalData.DissmissProgress();
    }
}
