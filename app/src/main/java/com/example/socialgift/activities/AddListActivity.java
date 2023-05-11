package com.example.socialgift.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.socialgift.R;
import com.example.socialgift.fragments.AddListFragment;
import com.example.socialgift.fragments.ListFragment;
import com.example.socialgift.fragments.ListInfoFragment;
import com.example.socialgift.model.Wishlist;

public class AddListActivity extends AppCompatActivity {
    private Toolbar toolbarAddList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        syncronizeViewToolbar();
        Intent intent = getIntent();
        Wishlist wishlist = (Wishlist) intent.getSerializableExtra("wishlist");
        if (wishlist == null){
            replace(new AddListFragment());
        }else {
            replace(new ListInfoFragment());
        }
    }

    private void syncronizeViewToolbar(){
        toolbarAddList = (Toolbar) findViewById(R.id.toolbarAddList);
        setSupportActionBar(toolbarAddList);
    }

    private void replace(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameAddList, fragment)
                .commit();
    }

}