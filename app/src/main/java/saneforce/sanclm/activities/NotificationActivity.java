package saneforce.sanclm.activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ChatModel;
import saneforce.sanclm.adapter_class.AdapterForChat;
import saneforce.sanclm.adapter_class.AdapterNotificationNameList;

public class NotificationActivity extends AppCompatActivity {

    ListView list_name,list_chat;
    ArrayList<String> name_array=new ArrayList<>();
    ArrayList<ChatModel> chat_array=new ArrayList<>();
    ImageView btn_send;
    EditText edt_msg;
    AdapterForChat apt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        list_name=(ListView)findViewById(R.id.list_name);
        list_chat=(ListView)findViewById(R.id.list_chat);
        btn_send=(ImageView)findViewById(R.id.btn_send);
        edt_msg=(EditText)findViewById(R.id.edt_msg);
        name_array.add("Ezhilarasi");
        name_array.add("Ezhilarasi");
        name_array.add("Ezhilarasi");
        name_array.add("Ezhilarasi");
        chat_array.add(new ChatModel("hi",true,"10/17/2019"));
        chat_array.add(new ChatModel("hello",false,""));
        chat_array.add(new ChatModel("how are you",true,""));
        chat_array.add(new ChatModel("Fine... u",false,""));
        chat_array.add(new ChatModel("Am Fine...",true,""));
        chat_array.add(new ChatModel("Ok bye",false,""));

        AdapterNotificationNameList adpt=new AdapterNotificationNameList(NotificationActivity.this,name_array);
        list_name.setAdapter(adpt);
        adpt.notifyDataSetChanged();

         apt=new AdapterForChat(NotificationActivity.this,chat_array);
        list_chat.setAdapter(apt);
        apt.notifyDataSetChanged();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=edt_msg.getText().toString();
                if(!TextUtils.isEmpty(msg)) {
                    chat_array.add(new ChatModel(msg, false, ""));
                    apt.notifyDataSetChanged();
                    edt_msg.setText("");
                }
                else
                    Toast.makeText(NotificationActivity.this,"No Msg",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
