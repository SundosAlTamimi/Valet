package com.example.valet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainValetActivity extends AppCompatActivity {
   GridView listItemGrid;
    ItemListAdapter itemListAdapter;
   List< DriveInfo>drivenList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.valet_captin);
    initialization();


    }

    private void initialization() {
        drivenList=new ArrayList<>();
         DriveInfo driveInfo=new  DriveInfo();
        driveInfo.setDriverName("ahmad");
        drivenList.add(driveInfo);
        driveInfo.setDriverName("amer");
        drivenList.add(driveInfo);
        driveInfo.setDriverName("ali");
        drivenList.add(driveInfo);
        driveInfo.setDriverName("alaa");
        drivenList.add(driveInfo);
        driveInfo.setDriverName("maher");
        drivenList.add(driveInfo);


        listItemGrid=findViewById(R.id.listItemGrid);
        itemListAdapter = new  ItemListAdapter( MainValetActivity.this, drivenList);

        listItemGrid.setAdapter(itemListAdapter);
    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logInButton:
                    Intent intent=new Intent( MainValetActivity.this,  MapsActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
