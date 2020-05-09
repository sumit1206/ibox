package com.sumit.ibox.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.ChatAdapter;
import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ChatData;
import com.sumit.ibox.model.ChatListData;
import com.sumit.ibox.model.ProgressDialog;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Context context;
    public static final String TIME_FORMAT = "dd MMM hh:mm a";
    private RecyclerView mMessageRecycler;
    private ChatAdapter chatAdapter;
    private List<ChatData> chats = new ArrayList<>();
    private ChatListData chatListData;
    private ProgressDialog loading;
    private AlertDialog errorDialog;
    private String username;
    private EditText etMessage;

    VolleyCallback chatCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            chats.clear();
            ChatData chat;
            JSONObject jsonObject = (JSONObject) result;
            try {
//                {"success":1,"message":"success","details":[{"message":"Hi Srijeet.. This is a test message","sender":"Super Admin","reciever":"Srijeet Mukherjee","timestamp":"29 Feb. 21:47PM"}
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String msg = data.getString("message");
                    String msgDateTime = data.getString("timestamp");
                    String receiverUserName = data.getString("reciever_username");
                    String senderUserName = data.getString("sender_username");
                    int msgType = username.equals(senderUserName)?ChatAdapter.VIEW_TYPE_MESSAGE_SENT:ChatAdapter.VIEW_TYPE_MESSAGE_RECEIVED;
                    int receiverType = 0;
                    chat = new ChatData(msgType, msgDateTime, msg, receiverType);
                    chats.add(chat);
                }
                chatAdapter.notifyDataSetChanged();
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

    VolleyCallback replyCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
//            errorDialog.setMessage(getString(R.string.sent));
//            errorDialog.show();
            etMessage.setText("");
            fetchChat();
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.not_sent));
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
        setContentView(R.layout.activity_chat);

        init();
        toolbarSetUp();
        setupRecyclerView();
        fetchChat();
    }

    private void init() {
        context = this;
        chatListData = (ChatListData) getIntent().getSerializableExtra(Constant.KEY_EXTRA_CHAT_DATA);
        username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
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
        etMessage = findViewById(R.id.chat_message);
    }

    private void toolbarSetUp(){
        Toolbar chatToolbar;
        chatToolbar = findViewById(R.id.chat_toolbar);
        chatToolbar.setTitle(chatListData.getReceiverName());
        chatToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        chatToolbar.setNavigationIcon(R.drawable.arrow_left);
        chatToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void setupRecyclerView() {
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        chatAdapter = new ChatAdapter(context, chats);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,true);
        mMessageRecycler.setLayoutManager(mLayoutManager);
        mMessageRecycler.setAdapter(chatAdapter);
    }

    private void fetchChat() {
        loading.show();
        Perform.fetchChat(context, chatCallback, chatListData.getThreadId());
    }

    public void sendClicked(View view) {
        String message = etMessage.getText().toString().trim();
        if(message.equals("")){
            errorDialog.setMessage("Type a message to Send");
            errorDialog.show();
            return;
        }
        loading.show();
        long now = System.currentTimeMillis();
        String time = Utils.getTimestampInFormat(now, TIME_FORMAT);
        Perform.replyMessage(context, replyCallback, chatListData.getSenderType()+"", username, chatListData.getThreadId(), message, time);
    }
}
