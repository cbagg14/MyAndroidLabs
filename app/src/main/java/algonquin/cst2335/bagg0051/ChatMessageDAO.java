package algonquin.cst2335.bagg0051;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    long insertMessage(ChatMessage message);

    @Query("SELECT * FROM chat_messages")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage message);
}
