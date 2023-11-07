package com.example.driveassure;

// homeUserFragment
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class homeUserFragment extends Fragment {
    ImageButton chatMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);

        chatMsg = view.findViewById(R.id.chatMsg);
        chatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ChatListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
