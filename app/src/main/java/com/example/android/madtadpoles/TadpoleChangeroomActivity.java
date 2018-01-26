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

    public Player activeUser;
    public Player notActiveUser;
    public Changeroom currentChangeroom;
    int centerSkinIndex;
    public ImageView left;
    public ImageView right;
    public EditText changeUserNameField;
    public Intent userInfo;
    public int activeChangeroomUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tadpole_changeroom);

        // get intent with Player objects containing data about current and resting players and save
        //this data in another Player objects
        userInfo = getIntent();
        activeUser = (Player)userInfo.getSerializableExtra("ActiveUserInfo");
        notActiveUser = (Player)userInfo.getSerializableExtra("NotActiveUserInfo");

        // set activePlayer variable as id of currently active player
        activeChangeroomUser = activeUser.getPlayerId();

        // instantiate new Changeroom
        currentChangeroom = new Changeroom(activeUser);


        // set on click listeners used for changing changeroom users between active and
        //non active player
        ImageButton nextChangeroomUser = (ImageButton) findViewById(R.id.next_user);
        nextChangeroomUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChangeroom();

            }
        });

        ImageButton previousChangeroomUser = (ImageButton) findViewById(R.id.previous_user);
        previousChangeroomUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChangeroom();
            }
        });

        // set on click listener on changeroom refresh button
        ImageButton changeroomRefresh = (ImageButton) findViewById(R.id.refresh_skins);
        changeroomRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshChangeroom();
            }
        });

        // set changeroom Views
        // set playerSideView
        currentChangeroom.setPlayerSideView((TextView) findViewById(R.id.player_side));
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


        // set on click listeners on buttons changing skin and give them functionality

        ImageButton nextSkin = (ImageButton) findViewById(R.id.next_skin);
        nextSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set if this is first change of skin for player
                if (activeChangeroomUser == 0){
                    currentChangeroom.setWasLeftPlayerSkinUpdated();
                }else if (activeChangeroomUser == 1){
                    currentChangeroom.setWasRightPlayerSkinUpdated();
                }
                if (currentChangeroom.getWhichSkin() + 2 > currentChangeroom.getWardrobe().length - 1) {
                    currentChangeroom.setWhichSkin(0);
                } else {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWhichSkin() + 2);
                }
                currentChangeroom.populateChangeroom();
            }
        });
        ImageButton previousSkin = (ImageButton) findViewById(R.id.previous_skin);
        previousSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set if this is first change of skin for player
                if (activeChangeroomUser == 0){
                    currentChangeroom.setWasLeftPlayerSkinUpdated();
                }else if (activeChangeroomUser == 1){
                    currentChangeroom.setWasRightPlayerSkinUpdated();
                }
                if (currentChangeroom.getWhichSkin() - 2 < 0) {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWardrobe().length - 2);
                } else {
                    currentChangeroom.setWhichSkin(currentChangeroom.getWhichSkin() - 2);
                }
                currentChangeroom.populateChangeroom();
            }
        });
    }
    public void updateChangeroom(){
        if (activeUser.getPlayerId() == 0) {
            if (activeChangeroomUser == 0) {

                currentChangeroom.updateChangeroomInfo(notActiveUser);
                activeChangeroomUser = 1;
            } else if (activeChangeroomUser == 1) {

                currentChangeroom.updateChangeroomInfo(activeUser);
                activeChangeroomUser = 0;

            }
        }else if (activeUser.getPlayerId() == 1) {

                if (activeChangeroomUser == 0) {
                    currentChangeroom.updateChangeroomInfo(activeUser);
                    activeChangeroomUser = 1;
                } else if (activeChangeroomUser == 1) {

                    currentChangeroom.updateChangeroomInfo(notActiveUser);
                    activeChangeroomUser = 0;
                }
            }
        currentChangeroom.getNameView().setText("");
        currentChangeroom.populateChangeroom();
    }
    public void refreshChangeroom(){
        if (activeChangeroomUser == 0 && activeUser.getPlayerId() ==0){
            currentChangeroom.refreshChangeroom(activeUser);
        }else if (activeChangeroomUser == 0 && activeUser.getPlayerId() == 1){
            currentChangeroom.refreshChangeroom(notActiveUser);
        }else if (activeChangeroomUser == 1 && activeUser.getPlayerId() == 0){
            currentChangeroom.refreshChangeroom(notActiveUser);
        }else if (activeChangeroomUser == 1 && activeUser.getPlayerId() == 1){
            currentChangeroom.refreshChangeroom(activeUser);
        }
        currentChangeroom.populateChangeroom();
    }
}
