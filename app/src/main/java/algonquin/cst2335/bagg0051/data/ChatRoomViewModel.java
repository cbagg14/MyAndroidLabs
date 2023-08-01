package algonquin.cst2335.bagg0051.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.bagg0051.ChatMessage;

public class ChatRoomViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();
    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData<>();

}