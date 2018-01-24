package com.example.android.madtadpoles;

import java.io.Serializable;

/**
 * Created by cezar on 24.01.2018.
 */

public class Player implements Serializable{

    private int mPlayerId;
    private String mPlayerName;
    private int mWhichSkin;


public Player (int player_id, String player_name) {
    mPlayerId = player_id;
    mPlayerName = player_name;
    // set default skin accordingly to which player wardrobe it is
    if (player_id == 0){
        mWhichSkin = 0;
    } else if (player_id == 1) {
        mWhichSkin = 2;
    }
}

public void setPlayerId (int player_id){
    mPlayerId = player_id;
}

public void setPlayerName (String player_name){
    mPlayerName = player_name;
}

public void setWhichSkin (int which_skin){
    mWhichSkin = which_skin;
}

public String getPlayerName(){
    return mPlayerName;
}

public int getPlayerId(){
    return mPlayerId;
}

public int getWhichSkin(){
    return mWhichSkin;
}

}
