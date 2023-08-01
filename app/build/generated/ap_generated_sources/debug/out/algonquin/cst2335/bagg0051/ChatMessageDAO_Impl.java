package algonquin.cst2335.bagg0051;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ChatMessageDAO_Impl implements ChatMessageDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChatMessage> __insertionAdapterOfChatMessage;

  private final EntityDeletionOrUpdateAdapter<ChatMessage> __deletionAdapterOfChatMessage;

  private final EntityDeletionOrUpdateAdapter<ChatMessage> __updateAdapterOfChatMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMessages;

  public ChatMessageDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChatMessage = new EntityInsertionAdapter<ChatMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ChatMessage` (`id`,`message`,`TimeSent`,`SendOrReceive`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChatMessage value) {
        stmt.bindLong(1, value.id);
        if (value.getMessage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMessage());
        }
        if (value.getTimeSent() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTimeSent());
        }
        stmt.bindLong(4, value.getSendOrReceive());
      }
    };
    this.__deletionAdapterOfChatMessage = new EntityDeletionOrUpdateAdapter<ChatMessage>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ChatMessage` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChatMessage value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfChatMessage = new EntityDeletionOrUpdateAdapter<ChatMessage>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ChatMessage` SET `id` = ?,`message` = ?,`TimeSent` = ?,`SendOrReceive` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChatMessage value) {
        stmt.bindLong(1, value.id);
        if (value.getMessage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMessage());
        }
        if (value.getTimeSent() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTimeSent());
        }
        stmt.bindLong(4, value.getSendOrReceive());
        stmt.bindLong(5, value.id);
      }
    };
    this.__preparedStmtOfDeleteAllMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ChatMessage";
        return _query;
      }
    };
  }

  @Override
  public long insertMessage(final ChatMessage messageToInsert) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfChatMessage.insertAndReturnId(messageToInsert);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteMessage(final ChatMessage messageToDelete) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfChatMessage.handle(messageToDelete);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int updateMessage(final ChatMessage updatedMessage) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfChatMessage.handle(updatedMessage);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllMessages.release(_stmt);
    }
  }

  @Override
  public List<ChatMessage> getAllMessages() {
    final String _sql = "Select * from ChatMessage";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
      final int _cursorIndexOfTimeSent = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeSent");
      final int _cursorIndexOfSendOrReceive = CursorUtil.getColumnIndexOrThrow(_cursor, "SendOrReceive");
      final List<ChatMessage> _result = new ArrayList<ChatMessage>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChatMessage _item;
        _item = new ChatMessage();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfMessage)) {
          _item.message = null;
        } else {
          _item.message = _cursor.getString(_cursorIndexOfMessage);
        }
        if (_cursor.isNull(_cursorIndexOfTimeSent)) {
          _item.timeSent = null;
        } else {
          _item.timeSent = _cursor.getString(_cursorIndexOfTimeSent);
        }
        _item.sendOrReceive = _cursor.getInt(_cursorIndexOfSendOrReceive);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
