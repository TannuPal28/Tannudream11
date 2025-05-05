package com.elevendreamer.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elevendreamer.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<String> contactsList= new ArrayList<>();
    private static final int REQUEST_READ_CONTACTS= 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_contacts, container, false);

       recyclerView= view.findViewById(R.id.recyclerViewContacts);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       adapter= new ContactsAdapter(contactsList);
       recyclerView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS
            );
        }


       return view;
    }

    private void loadContacts() {

        contactsList.clear();
        ContentResolver contentResolver= getContext().getContentResolver();
        Cursor cursor= contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        if (cursor!= null && cursor.moveToFirst()){
            do {
                String name= cursor.getString(cursor.getColumnIndexOrThrow(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));
                contactsList.add(name);
            }while (cursor.moveToNext());
            cursor.close();

        }
        adapter.notifyDataSetChanged();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void filterContacts(String query) {
        adapter.filter(query);
    }


    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
        private List<String> originalList;
        private List<String> filteredList;

        public ContactsAdapter(List<String> contactList) {
            this.originalList = contactList;
            this.filteredList = new ArrayList<>(contactList);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(android.R.id.text1);
            }
        }

        @NonNull
        @Override
        public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
            holder.name.setText(filteredList.get(position));
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        public void filter(String query) {
            filteredList.clear();
            if (query.isEmpty()) {
                filteredList.addAll(originalList);
            } else {
                for (String name : originalList) {
                    if (name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(name);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}