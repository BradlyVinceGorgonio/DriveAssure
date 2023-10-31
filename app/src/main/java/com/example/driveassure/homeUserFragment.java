package com.example.driveassure;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class homeUserFragment extends Fragment {
    ImageButton chatMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);

        chatMsg = view.findViewById(R.id.chatoMsg);
        chatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the ChatList fragment
                ChatList chatListFragment = new ChatList();

                // Navigate to the ChatList fragment when the button is clicked
                Fragment ChatList = new Fragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_chat_list, ChatList)
                        .commit();
            }
        });

        return view;
    }
}

