package com.sumit.ibox.ui.student.library;


import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.RequestBookLibraryAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.RequestBookLibraryData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestBookLibraryFragment extends Fragment {

    private Context context;
    private RecyclerView requestBookLibraryRecyclerView;
    private ArrayList<RequestBookLibraryData> requestBookLibrariesArray =new ArrayList<>();
    private EditText etSearch;
    RequestBookLibraryAdapter requestBookLibraryAdapter;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback bookListCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            RequestBookLibraryData bookData;
            try {//success, details, book_id, name, author
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String bookId = post.getString("book_id");
                    String bookName = post.getString("name");
                    String bookAuthor = post.getString("author");
                    bookData = new RequestBookLibraryData(bookId, bookAuthor, bookName);
                    requestBookLibrariesArray.add(bookData);
                }
                requestBookLibraryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.logPrint(getClass(), "response error1", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_books));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(), "response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(), "no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }
    };

    RequestBookLibraryAdapter.TouchEvents touchEvents = new RequestBookLibraryAdapter.TouchEvents() {
        @Override
        public void onClick(final RequestBookLibraryData bookData) {
            AlertDialog reqDialog = new AlertDialog.Builder(context)
                    .setMessage("Want to issue "+ bookData.getBookName() +" book by author "+ bookData.getBookAuthor()+"?")
                    .setPositiveButton("send request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestBook(bookData.getBookId());
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            reqDialog.show();

        }
    };

    private void requestBook(String bookId) {
        VolleyCallback requestCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.request_sent));
                errorDialog.show();
            }

            @Override
            public void noDataFound() {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.no_books));
                errorDialog.show();
            }

            @Override
            public void onCatch(JSONException e) {
                loading.dismiss();
                Utils.logPrint(getClass(), "response error2", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }

            @Override
            public void onError(VolleyError e) {
                loading.dismiss();
                Utils.logPrint(getClass(), "no response error", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        };
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        loading.show();
        Perform.requestBook(context, requestCallback, studentId, bookId);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ArrayList<RequestBookLibraryData> selectedBooksArray = new ArrayList<>();
            for(RequestBookLibraryData book: requestBookLibrariesArray){
                if(book.getBookName().toLowerCase().contains(s.toString().toLowerCase())){
                    selectedBooksArray.add(book);
                }
            }
            setUpRecyclerView(selectedBooksArray);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public RequestBookLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_request_book_library, container, false);

        init(view);
        setUpRecyclerView(requestBookLibrariesArray);
        fetchData();

        return view;
    }

    private void init(View view) {
        context = getContext();
        etSearch = view.findViewById(R.id.req_book_search);
        etSearch.addTextChangedListener(textWatcher);
        requestBookLibraryRecyclerView = view.findViewById(R.id.req_book_library_recycler_view);
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.cancel();
                    }
                })
                .create();
    }

    private void setUpRecyclerView(ArrayList<RequestBookLibraryData> requestBookLibrariesArray){
        requestBookLibraryAdapter = new RequestBookLibraryAdapter(getContext(),requestBookLibrariesArray, touchEvents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        requestBookLibraryRecyclerView.setLayoutManager(mLayoutManager);
        requestBookLibraryRecyclerView.setAdapter(requestBookLibraryAdapter);
    }

    private void fetchData(){
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        String klass = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);
        cookiesAdapter.close();
        loading.show();
        Perform.fetchBookList(context, bookListCallback, klass);
    }

}
