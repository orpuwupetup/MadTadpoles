package com.example.android.madtadpoles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TadpoleChangeroomActivity extends AppCompatActivity {

    public Player currentUser;
    public Changeroom currentChangeroom;
    int centerSkinIndex;
    public ImageView left;
    public ImageView right;
    public EditText changeUserNameField;
    public Intent userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tadpole_changeroom);

        // get intent with Player object containing data about current player and save
        //this data in another Player object
        userInfo = getIntent();
        currentUser = (Player)userInfo.getSerializableExtra("UserInfo");

        // instantiate new Changeroom
        currentChangeroom = new Changeroom(currentUser);

        // set changeroom Views
        // center
        currentChangeroom.setCenter((ImageView) findViewById(R.id.center_tad));
        // left
        currentChangeroom.setLeft((ImageView) findViewById(R.id.left_tad));
        // right
        currentChangeroom.setRight((ImageView) findViewById(R.id.right_tad));
        // name
        currentChangeroom.setNameView((EditText) findViewById(R.id.change_user_name));
        // left/right text View
        currentChangeroom.setLeftRightView((TextView) findViewById(R.id.left_right));

        // set all Views accordingly to info data about user
        currentChangeroom.populateChangeroom();

        ImageButton next = (ImageButton) findViewById(R.id.next_skin);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentChangeroom.getWhichSkin() + 2 > currentChangeroom.getWardrobe().length - 1) {
                    currentChangeroom.setWhichSkin(0);
                } else {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWhichSkin() + 2);
                }
                currentChangeroom.populateChangeroom();
            }
        });
        ImageButton previous = (ImageButton) findViewById(R.id.previous_skin);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentChangeroom.getWhichSkin() - 2 < 0) {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWardrobe().length - 2);
                } else {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWhichSkin() - 2);
                }
                currentChangeroom.populateChangeroom();
            }
        });







    }
}
