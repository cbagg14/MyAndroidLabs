package algonquin.cst2335.bagg0051.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.bagg0051.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public ArrayList<ChatMessage> messages = new ArrayList<>();
    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData< >();

}
