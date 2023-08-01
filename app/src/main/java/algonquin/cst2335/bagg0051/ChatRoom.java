package algonquin.cst2335.bagg0051;

import static algonquin.cst2335.bagg0051.R.id.item1;
import static algonquin.cst2335.bagg0051.R.id.item2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import algonquin.cst2335.bagg0051.data.ChatRoomViewModel;
import algonquin.cst2335.bagg0051.ChatMessage;
import algonquin.cst2335.bagg0051.ChatMessageDAO;
import algonquin.cst2335.bagg0051.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.bagg0051.databinding.ReceiveMessageBinding;
import algonquin.cst2335.bagg0051.databinding.SentMessageBinding;

public class
ChatRoom extends AppCompatActivity {

    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    MessageDatabase myDB;
    ChatMessageDAO myDAO;

    EditText editText;
    Button sendButton;
    Button receiveButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;

    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatRoomBinding binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar = binding.myToolbar;
        setSupportActionBar(myToolbar);

        myDB = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        myDAO = myDB.cmDAO();

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages;



        chatModel.selectedMessage.observe(this, (newChatMessage) -> {

            if(newChatMessage != null) {
                FragmentManager fMgr = getSupportFragmentManager();

                FragmentTransaction tx = fMgr.beginTransaction();

                MessageDetailsFragment frag = new MessageDetailsFragment( newChatMessage );

                tx.add(R.id.fragmentLocation, frag);
                tx.addToBackStack("x");
                tx.commit();
            }
        });

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()-> {
            List<ChatMessage> allMessages = myDAO.getAllMessages();
            messages.addAll(allMessages);
        });

        sendButton = binding.sendButton;
        receiveButton = binding.receiveButton;
        recyclerView = binding.recycleView;
        editText = binding.editText;


        sendButton.setOnClickListener(click -> {

            String typedMessage = editText.getText().toString();
            int type = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage newMessage = new ChatMessage(typedMessage, currentDateAndTime, type);
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                newMessage.id = myDAO.insertMessage(newMessage);
            });

            messages.add(newMessage);
            myAdapter.notifyDataSetChanged();
            //clear the previous text
            editText.setText("");
        });

        receiveButton.setOnClickListener(click -> {

            String typedMessage = editText.getText().toString();
            int type = 2;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage newMessage = new ChatMessage(typedMessage, currentDateAndTime, type);
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                newMessage.id = myDAO.insertMessage(newMessage);
            });

            messages.add(newMessage);
            myAdapter.notifyDataSetChanged();
            //clear the previous text
            editText.setText("");
        });


        recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage obj = messages.get(position);

                if(obj.getSendOrReceive() == 1)
                    return 1;
                else
                    return 2;

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item1) {
            System.out.println("hello");
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
            builder.setMessage("Do you want to delete the messages?")
                    .setTitle("Question")
                    .setPositiveButton("Go Ahead", (dlg, which) -> {
                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(() -> {
                            myDAO.deleteAllMessages();
                            messages.clear();
                            runOnUiThread(() -> {
                                myAdapter.notifyDataSetChanged();
                            });
                        });
                    })
                    .setNegativeButton("No", (dl, wh) -> {})
                    .create().show();
        } else if (itemId == R.id.item2) {
            Toast.makeText(this, "Version 1.0, created by Collin Baggio", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;


        public MyRowHolder(@NonNull View itemView){
            super(itemView);


            itemView.setOnClickListener(click -> {

                int position = getAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}



