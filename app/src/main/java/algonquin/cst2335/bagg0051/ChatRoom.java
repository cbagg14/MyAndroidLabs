package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.bagg0051.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.bagg0051.databinding.ReceiveMessageBinding;
import algonquin.cst2335.bagg0051.databinding.SentMessageBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    private MyAdapter myAdapter;
    ChatRoomViewModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages);
        }

        // Create and set up the RecyclerView adapter
        myAdapter = new MyAdapter();
        binding.recyleView.setAdapter(myAdapter);
        binding.recyleView.setLayoutManager(new LinearLayoutManager(this));

        EditText editText = findViewById(R.id.editText);
        Button sendButton = findViewById(R.id.send);
        Button receiveButton = findViewById(R.id.receive);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString().trim();
                if (!message.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(message, currentDateAndTime, true);
                    messages.add(chatMessage);
                    myAdapter.notifyItemInserted(messages.size() - 1);
                    editText.setText("");
                }
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString().trim();
                if (!message.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(message, currentDateAndTime, false);
                    messages.add(chatMessage);
                    myAdapter.notifyItemInserted(messages.size() - 1);
                    editText.setText("");
                }
            }
        });
    }

    // Inner class representing a row in the RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    // Custom Adapter class
    private class MyAdapter extends RecyclerView.Adapter<MyRowHolder> {
        private static final int VIEW_TYPE_SEND = 0;
        private static final int VIEW_TYPE_RECEIVE = 1;

        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {
                SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                return new MyRowHolder(binding.getRoot());
            } else {
                ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(inflater, parent, false);
                return new MyRowHolder(binding.getRoot());
            }
        }


        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            ChatMessage chatMessage = messages.get(position);
            holder.messageText.setText(chatMessage.getMessage());
            holder.timeText.setText(chatMessage.getTimeSent());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage chatMessage = messages.get(position);
            if (chatMessage.isSentButton()) {
                return VIEW_TYPE_SEND;
            } else {
                return VIEW_TYPE_RECEIVE;
            }
        }
    }
}


