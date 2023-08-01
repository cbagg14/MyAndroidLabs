package algonquin.cst2335.bagg0051;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.bagg0051.data.ChatRoomViewModel;
import algonquin.cst2335.bagg0051.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.bagg0051.databinding.ReceiveMessageBinding;
import algonquin.cst2335.bagg0051.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages;
    private ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel;
    ChatMessageDAO mDAO;
    MessageDatabase db;
    ChatMessage newMessage;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_1) {
            showDeleteConfirmationDialog();
            return true;
        } else if (itemId == R.id.item_2) {
            showAboutToast();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Message");
        builder.setMessage("Are you sure you want to delete this message?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Put your ChatMessage deletion code here
            // This code will execute when the user confirms the deletion
            // For now, let's show a toast message indicating deletion success
            Toast.makeText(this, "Message deleted successfully", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // This code will execute when the user cancels the deletion
            dialog.dismiss();
        });
        builder.show();
    }

    private void showAboutToast() {
        // Show a toast with the "About" message
        Toast.makeText(this, "Version 1.0, created by Collin Baggio", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
        if (messages == null) {

            chatModel.messages.setValue(messages = new ArrayList<ChatMessage>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getAllMessages());
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });
        }
        setSupportActionBar(binding.myToolbar);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");

                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (messages.get(position).isSentButton == true) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh:mm a");
            String currentDateandTime = sdf.format(new Date());
            String Input = binding.editText.getText().toString();
            boolean sender = true;
            newMessage = new ChatMessage(Input, currentDateandTime, sender);
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text
            binding.editText.setText("");
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    newMessage.id = mDAO.insertMessage(newMessage);
                }
            });
        });


        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh:mm a");
            String currentDateandTime = sdf.format(new Date());
            String Input = binding.editText.getText().toString();
            boolean sender = false;
            newMessage = new ChatMessage(Input, currentDateandTime, sender);
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text
            binding.editText.setText(""); //removed what was typed
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    newMessage.id = mDAO.insertMessage(newMessage);
                }
            });
        });


        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack("");
            tx.commit();

        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);

           /*     AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setTitle("Question:")
                        .setMessage("Do you want to delete the message: " + messageText.getText())
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            ChatMessage m = messages.get(position);
                            thread.execute(() -> {
                                mDAO.deleteMessage(m);
                            });
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", click -> {
                                        messages.add(position, m);
                                        runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                    })
                                    .show();
                        })
                        .create().show();
           */ });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}



