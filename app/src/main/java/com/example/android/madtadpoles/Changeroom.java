package com.example.android.madtadpoles;

import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by cezar on 24.01.2018.
 */

public class Changeroom {

    // users info
    private String mUserName;
    private int mUserId;
    private ImageView skinView;
    private ImageView changeroomActiveSkinView;
    private ImageView changeroomNonActiveSkinLeft;
    private ImageView changeroomNonActiveSkinRight;

    // skin info and storage
    private int mWhichSkin;
    private int[] wardrobe;

    public Changeroom(int active_player, String active_player_name, int which_skin) {
        mUserId = active_player;
        mUserName = active_player_name;

        mWhichSkin = which_skin;

        wardrobe = new int[4];
        setWardrobe(mUserId);
    }

    // method for putting on new skin
    public void putOnNewSkin (int which_skin){
        skinView.setImageResource(this.getWardrobe()[which_skin]);
        mWhichSkin = which_skin;
    }


    //set method to get "wardrobe" from Changeroom class
    public int[] getWardrobe(){
        return wardrobe;
    }

    // set method to get ID of current user of the Changeroom
    public int getUserID(){
        return mUserId;
    }

    // set method to get name of current user of the Changeroom
    public String getUserName(){
        return mUserName;
    }

    // set method to get which skin is currently in usage
    public int getWhichSkin (){
        return mWhichSkin;
    }

    //method for setting parameter telling which skin is currently used by player
    public void setWhichSkin(int which_skin){
        mWhichSkin = which_skin;
    }
    // method for preparing Changeroom for current user (personalized wardrobe)
    public void setWardrobe (int user_id){

        if (user_id == 0){
            wardrobe[0] = R.drawable.left_tadpole_1;
            wardrobe[1] = R.drawable.left_tadpole_1_non_active;
            wardrobe[2] = R.drawable.left_tadpole_2;
            wardrobe[3] = R.drawable.left_tadpole_2_non_active;
        } else if (user_id == 1) {
            wardrobe[0] = R.drawable.right_tadpole_1;
            wardrobe[1] = R.drawable.right_tadpole_1_non_active;
            wardrobe[2] = R.drawable.right_tadpole_2;
            wardrobe[3] = R.drawable.right_tadpole_2_non_active;
        }
    }

    //method for setting changeroom user name
    public void setUserName(String user_name){
        mUserName = user_name;
    }
}