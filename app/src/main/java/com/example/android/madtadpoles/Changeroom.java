package com.example.android.madtadpoles;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cezar on 24.01.2018.
 */

public class Changeroom {

    // users info and corresponding views
    private EditText nameView;
    private String mUserName;
    private int mUserId;
    private TextView leftRight;

    // skin Views
    private ImageView changeroomActiveSkinView;
    private ImageView changeroomNonActiveSkinLeft;
    private ImageView changeroomNonActiveSkinRight;

    // skin info and storage
    private int centerSkinIndex;
    private int[] wardrobe;

    // Changeroom class constructor
    public Changeroom(Player player) {
        // load info about changeroom user
        mUserId = player.getPlayerId();
        mUserName = player.getPlayerName();
        centerSkinIndex = player.getWhichSkin();

        //create new wardrobe and put skins inside it
        setWardrobe(mUserId);
    }

    /*
    method for loading all of the skins available for current player into "wardrobe".
    If user_id == 0, all skins are "left" (they are facing right side of the screen), and if
    user_id == 1, skins are "right" (they are facing left side of the screen). Skins in wardrobe
    with even number( + 0) as an index are skins with active colors, while those with odd number
    as an index are nonactive. Every skin consist of one active and one non active skin at indexes
    of respectively "any odd number" (for active skin) and "this number + 1" (for non active skin)
    */
    public void setWardrobe (int user_id){

        /*
        when new skin is added, programmer has to increase this number by 2 for each new skin
        pair (active, nonactive)
        */
        wardrobe = new int[6];

        // Skins for left tadpole
        if (user_id == 0){
            wardrobe[0] = R.drawable.left_tadpole_1;
            wardrobe[1] = R.drawable.left_tadpole_1_non_active;
            wardrobe[2] = R.drawable.left_tadpole_2;
            wardrobe[3] = R.drawable.left_tadpole_2_non_active;
            wardrobe[4] = R.drawable.left_tadpole_3;
            wardrobe[5] = R.drawable.left_tadpole_3_non_active;

        // Skins for right tadpole
        } else if (user_id == 1) {
            wardrobe[0] = R.drawable.right_tadpole_1;
            wardrobe[1] = R.drawable.right_tadpole_1_non_active;
            wardrobe[2] = R.drawable.right_tadpole_2;
            wardrobe[3] = R.drawable.right_tadpole_2_non_active;
            wardrobe[4] = R.drawable.right_tadpole_3;
            wardrobe[5] = R.drawable.right_tadpole_3_non_active;
        }
    }
    //get method to get "wardrobe" from Changeroom class
    public int[] getWardrobe(){
        return wardrobe;
    }

    // define private method used for populating Changeroom views
    public void populateChangeroom (){

        // change name hint on EditText
        this.getNameView().setHint(this.getUserName());

        //set center Tadpole Skin
        this.getCenter().setImageResource(this.getWardrobe()[centerSkinIndex]);

        // set right Tadpole skin
        if (centerSkinIndex + 1 > this.getWardrobe().length - 1) {
            this.getRight().setImageResource(this.getWardrobe()[1]);
        }else if(centerSkinIndex + 3 > wardrobe.length - 1){
            this.getRight().setImageResource(this.getWardrobe()[1]);
        }else{
            this.getRight().setImageResource(this.getWardrobe()[centerSkinIndex + 3]);
        }

        // set left Tadpole skin
        if (centerSkinIndex - 1 < 0) {
            this.getLeft().setImageResource(this.getWardrobe()[this.getWardrobe().length - 1]);
        }else{
            this.getLeft().setImageResource(this.getWardrobe()[centerSkinIndex - 1]);
        }

        // set left/right View to indicate on which side current player is playing
        if (mUserId == 0){
            leftRight.setText("Left");
        }else{
            leftRight.setText("Right");
        }

    }


    // set changeroom views methods
    public void setLeftRightView (TextView left_right){
        leftRight = left_right;
    }
    public void setNameView(EditText name_view){
        nameView = name_view;
    }
    public void setCenter(ImageView center){
        changeroomActiveSkinView = center;
    }
    public void setLeft(ImageView left){
        changeroomNonActiveSkinLeft = left;
    }
    public void setRight(ImageView right){
        changeroomNonActiveSkinRight = right;
    }

    // get changeroom views methods
    public TextView getLeftRightView (){
        return leftRight;
    }
    public ImageView getCenter(){
        return changeroomActiveSkinView;
    }
    public ImageView getLeft(){
        return changeroomNonActiveSkinLeft;
    }
    public ImageView getRight(){
        return changeroomNonActiveSkinRight;
    }
    public EditText getNameView() {
        return nameView;
    }



    // setter and getter method to get ID of current user of the Changeroom
    public int getUserID(){
        return mUserId;
    }
    public void setUserID(int user_id){
        mUserId = user_id;
    }


    // setter and getter method to get name of current user of the Changeroom
    public String getUserName(){
        return mUserName;
    }
    public void setUserName(String user_name){
        mUserName = user_name;
    }

    // setter and getter method to get which skin is currently in usage
    public int getWhichSkin (){
        return centerSkinIndex;
    }
    public void setWhichSkin(int which_skin){
        centerSkinIndex = which_skin;
    }

    //method for creating Player object with info of current changeroom user
    public Player getUser (){
        Player userOfChangeroom = new Player(mUserId, mUserName);
        userOfChangeroom.setWhichSkin(centerSkinIndex);
        return userOfChangeroom;
    }
    // method for updating changeroom when Player object is provided
    public void updateChangeroomInfo(Player player){
        mUserId = player.getPlayerId();
        mUserName = player.getPlayerName();
        centerSkinIndex = player.getWhichSkin();

        //create new wardrobe and put skins inside it
        setWardrobe(mUserId);
    }

}