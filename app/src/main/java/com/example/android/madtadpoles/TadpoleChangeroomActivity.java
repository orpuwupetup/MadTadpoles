package com.example.android.madtadpoles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class TadpoleChangeroomActivity extends AppCompatActivity {

    public Player currentUser;
    public Changeroom currentChangeroom;
    int centerSkinNum;
    public ImageView center;
    public ImageView left;
    public ImageView right;
    public EditText changeUserNameField;
    public Intent userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tadpole_changeroom);

        // get intent with
        userInfo = getIntent();
        currentUser = (Player)userInfo.getSerializableExtra("UserInfo");

        currentChangeroom = new Changeroom(currentUser.getPlayerId(),
                currentUser.getPlayerName(), currentUser.getWhichSkin());

        centerSkinNum = currentUser.getWhichSkin();

        center = (ImageView) findViewById(R.id.center_tad);
        center.setImageResource(currentChangeroom.getWardrobe()[centerSkinNum]);


        left = (ImageView) findViewById(R.id.left_tad);
        if (centerSkinNum - 1 < 0) {
            left.setImageResource(currentChangeroom.getWardrobe()[currentChangeroom.getWardrobe().length - 1]);
        }else{
            left.setImageResource(currentChangeroom.getWardrobe()[centerSkinNum - 1]);
        }


        right = (ImageView) findViewById(R.id.right_tad);
        if (centerSkinNum + 1 > currentChangeroom.getWardrobe().length - 1) {
            right.setImageResource(currentChangeroom.getWardrobe()[0]);
        }else{
            right.setImageResource(currentChangeroom.getWardrobe()[centerSkinNum+1]);
        }



        changeUserNameField = (EditText) findViewById(R.id.change_user_name);
        changeUserNameField.setHint(currentUser.getPlayerName());


    }

}
