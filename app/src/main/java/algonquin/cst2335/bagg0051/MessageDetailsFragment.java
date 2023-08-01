package algonquin.cst2335.bagg0051;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.bagg0051.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
        ChatMessage selected;
        public MessageDetailsFragment(ChatMessage m) {
            selected = m;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
            return binding.getRoot();
        }
    }

