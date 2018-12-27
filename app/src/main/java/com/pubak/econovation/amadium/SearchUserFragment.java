package com.pubak.econovation.amadium;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class SearchUserFragment extends ListFragment {

    ListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new ListViewAdapter();
        setListAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user),
                "User1", "Teir 3") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user),
                "User2", "Teir 5") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user),
                "User3", "Teir 8") ;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        SearchUserListViewItem item = (SearchUserListViewItem) l.getItemAtPosition(position);

        Drawable userIamge = item.getUserImage();
        String userName = item.getUserName();
        String userTier = item.getUserTier();
    }

    public void addItem(Drawable image, String name, String tier) {
        adapter.addItem(image, name, tier);
    }
}
