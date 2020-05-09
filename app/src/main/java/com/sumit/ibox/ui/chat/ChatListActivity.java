package com.sumit.ibox.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.ChatListAdapter;
import com.sumit.ibox.controller.ExpandedArticlesAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ArticlesForYouData;
import com.sumit.ibox.model.ChatListData;
import com.sumit.ibox.model.ProgressDialog;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.student.TeacherActivity;
import com.sumit.ibox.ui.teacher.StudentsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private Context context;
    private List<ChatListData> chatListDataArray = new ArrayList<>();
    private String username;
    private int myType;
    private ProgressDialog loading;
    private AlertDialog errorDialog;
    private ChatListAdapter chatListAdapter;

    private ChatListAdapter.TouchHandler touchHandler = new ChatListAdapter.TouchHandler() {
        @Override
        public void onClick(ChatListData chatListData) {
            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.putExtra(Constant.KEY_EXTRA_CHAT_DATA, chatListData);
            startActivity(chatIntent);
        }
    };

    VolleyCallback chatListCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            //{"success":1,"message":"success","details":[{"message_thread_code":"d41d8cd98f00b20","sender":"Super Admin","reciever":"Vicky Mukherjee"}]}
            loading.dismiss();
            ChatListData chatListData;
            JSONObject jsonObject = (JSONObject) result;
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String receiverName = data.getString("reciever");
                    String senderName = data.getString("sender");
                    String receiverUserName = data.getString("reciever_username");
                    String senderUserName = data.getString("sender_username");
                    String name = username.equals(senderUserName)?receiverName:senderName;
                    int senderType = data.getInt("sender_type");
                    int receiverType = data.getInt("reciever_type");
                    int hisType = username.equals(senderUserName)?receiverType:senderType;
                    String threadId = data.getString("message_thread_code");
                    String dateTime = "";
                    chatListData = new ChatListData(name, threadId, dateTime, myType, hisType);
                    chatListDataArray.add(chatListData);
                }
                chatListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_chats));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        init();
        setUpToolbar();
        setUpRecyclerView();
        fetchChatList();
    }

    private void init() {
        context = this;
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
        myType = getIntent().getIntExtra(Constant.KEY_USER_TYPE, 99);
        username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
    }

    private void setUpRecyclerView(){
        RecyclerView chatListRecyclerView;
        chatListRecyclerView = findViewById(R.id.chatListRecyclerView);
        chatListDataArray = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatListDataArray, touchHandler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        chatListRecyclerView.setLayoutManager(mLayoutManager);
        chatListRecyclerView.setAdapter(chatListAdapter);
    }

    private void setUpToolbar(){
        Toolbar chatToolbar;
        chatToolbar = findViewById(R.id.chatListToolbar);
        chatToolbar.setTitle(R.string.chat);
        chatToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        chatToolbar.setNavigationIcon(R.drawable.arrow_left);
        chatToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchChatList() {
        loading.show();
        Perform.fetchChatList(context, chatListCallback, username, myType);
    }

    public void showMoreClicked(View view) {
        Intent moreIntent;
        if(myType == Constant.TYPE_TEACHER){
            moreIntent = new Intent(context, StudentsActivity.class);
        }else {
            moreIntent = new Intent(context, TeacherActivity.class);
        }
        startActivity(moreIntent);
    }
}
