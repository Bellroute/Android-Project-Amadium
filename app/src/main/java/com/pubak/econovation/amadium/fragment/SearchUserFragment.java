package com.pubak.econovation.amadium.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.ListViewAdapter;

public class SearchUserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        ListViewAdapter adapter;

        adapter = new ListViewAdapter();

        ListView listView = (ListView) view.findViewById(R.id.listview_search_user);
        listView.setAdapter(adapter);

        // 추가
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user), "bell", "광주");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user), "column", "광주");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user), "gordon", "광주");

        return view;
    }
}
