package com.sumit.ibox.ui.student.library;


import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.MyBookLibraryAdapter;
import com.sumit.ibox.model.MyBookLibraryData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookListFragment extends Fragment {

    private Context context;
    private RecyclerView libraryRecyclerView;
    private ArrayList<MyBookLibraryData> myBookLibraryDataArray =new ArrayList<>();
    MyBookLibraryAdapter myBookLibraryAdapter;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback bookListCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            MyBookLibraryData bookData;
            try {//success, details, name, author, issue_start_date, issue_end_date
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String bookName = post.getString("name");
                    String bookAuthor = post.getString("author");
                    String startDate = post.getString("issue_start_date");
                    String endDate = post.getString("issue_end_date");
                    bookData = new MyBookLibraryData(bookAuthor, bookName, endDate, startDate);
                    myBookLibraryDataArray.add(bookData);
                }
                myBookLibraryAdapter.notifyDataSetChanged();
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

    public MyBookListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_book_list, container, false);
        setUpRecyclerView(view);

        init();
        fetchBooks();

        return view;
    }

    private void init() {
        context = getContext();
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

    void setUpRecyclerView(View view){
        libraryRecyclerView = view.findViewById(R.id.library_recycler_view);
        myBookLibraryAdapter = new MyBookLibraryAdapter(getContext(), myBookLibraryDataArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        libraryRecyclerView.setLayoutManager(mLayoutManager);
        libraryRecyclerView.setAdapter(myBookLibraryAdapter);
    }

    private void fetchBooks(){
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        loading.show();
        Perform.fetchMyBookList(context, bookListCallback, studentId);
    }

}
