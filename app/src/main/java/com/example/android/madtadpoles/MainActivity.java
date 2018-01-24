/*
Tadpoles variable names changed from KM ("Kijanka miecz") and KT ("Kijanka topór") to KL
("Kijanka Left") and KR ("Kijanka Rigth") respectively
*/


package com.example.android.madtadpoles;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.os.Handler; // Ola's new code

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Dialog.DialogListener{

    private boolean isAttackHitted = false;
    private boolean isBackBtnPressed = false;
    private int attackValue = 0;
    public int activePlayer=1;
    private CountDownTimer countDownTimer; // Ola's new code
    private MediaPlayer attackSound;
    Vibrator vibe;
    private final Gun miecz = new Gun(3,R.drawable.ic_miecz);
    private final Gun arc = new Gun(6,R.drawable.ic_arc);
    private final Gun sickle = new Gun(15, R.drawable.ic_sickle);
    private final Gun axe = new Gun(10,R.drawable.ic_axe);
    private final Gun baseball = new Gun(5,R.drawable.ic_baseball);
    private final Gun bomb = new Gun(30,R.drawable.ic_bomb);
    private final Gun bigBomb = new Gun(50,R.drawable.ic_bigbomb);
    private final Gun recovery = new Gun(0,R.drawable.ic_bigbomb); // TEMPORARY ICON
    private final Gun[] guns = {miecz, arc, sickle, axe, baseball, bomb, bigBomb, recovery};

    private boolean recovered;

    private boolean mIsBound = false;
    private BackgroundMusic mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((BackgroundMusic.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,BackgroundMusic.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;

        }
    }


    // creating tadpoles
    private Tadpole KL = new Tadpole(100, 4, 0);
    private Tadpole KR = new Tadpole(100, 4, 1);
    private Tadpole[] players = {KL, KR};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tadpoles);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Assign Views corresponding to tadpoles
        KL.setHealthPoints((TextView) findViewById(R.id.kijankaLewaHP));
        KL.setAttackButton((ImageButton) findViewById(R.id.KLBtnAttack));
        KL.setStartCount((Button) findViewById(R.id.startCountKL));
        KL.setName((TextView) findViewById(R.id.KLName));
        KL.setLabelCounter((TextView) findViewById(R.id.labelCounterKL));
        KL.setAttackPoints((TextView) findViewById(R.id.kijankaLewaPts));
        KL.setProgressBar((ProgressBar) findViewById(R.id.progressA));
        KL.setHealthPoints((TextView) findViewById(R.id.kijankaLewaHP));
        KL.setAttackSound(R.raw.kinja_01);
        KL.setSkinView((ImageView) findViewById(R.id.kijankaLewa));

        KR.setHealthPoints((TextView) findViewById(R.id.kijankaPrawaHP));
        KR.setSkinView((ImageView) findViewById(R.id.kijankaPrawa));
        KR.setAttackButton((ImageButton) findViewById(R.id.KRBtnAttack));
        KR.setStartCount((Button) findViewById(R.id.startCountKR));
        KR.setName((TextView) findViewById(R.id.KRName));
        KR.setLabelCounter((TextView) findViewById(R.id.labelCounterKR));
        KR.setAttackPoints((TextView) findViewById(R.id.kijankaPrawaPts));
        KR.setProgressBar((ProgressBar) findViewById(R.id.progressB));
        KR.setHealthPoints((TextView) findViewById(R.id.kijankaPrawaHP));
        KR.setAttackSound(R.raw.kinja_02);

        if(!mIsBound) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, BackgroundMusic.class);
            startService(music);
        }
        // ******************************************************
        // ********************************** Damian's code start
        // ******************************************************


        // After starting activity take previous winner  and switch player to start new game
        Bundle extras = getIntent().getExtras(); // --> Ola's new code
        if (extras != null){
            activePlayer = extras.getInt("winner");
            switchPlayers();
        }else switchPlayers();

        updateLabels();
        progressbar(KL);  // --> Ola's new code
        progressbar(KR);  // --> Ola's new code


        // Changeroom on click listener
        ImageButton changeroomIcon = (ImageButton) findViewById(R.id.changeroom);
        changeroomIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent openChangeroom = new Intent(MainActivity.this, TadpoleChangeroomActivity.class);

                Player changeroomUser = new Player(activePlayer, players[activePlayer].getName().getText().toString());

                //change so it will add player that is currently active
                openChangeroom.putExtra("UserInfo", changeroomUser);
                startActivity(openChangeroom);

            }
        });

       // KL Attack button ClickListener
        // On attack button click set isAttackHitted = true
        KL.getAttackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAttackHitted = true;
            }
        });

        // KR Attack button ClickListener
        // On attack button click set isAttackHitted = true
        KR.getAttackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAttackHitted = true;
            }
        });

        //************************ Main counter for KL tadpole (left)
        // On Start button enable Attack button and disable other players buttons
        // Create guns
        KL.getStartCount().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
             activatePlayer(KL);
            }
        });

        //********************* Main counter for KR tadpole
        // On Start button enable Attack button and disable other players buttons
        // Create guns
        KR.getStartCount().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
               activatePlayer(KR);
            }
        });

    }

    private int nextPLayer(int player){
        return(player + 1)%2;
    }

    private void switchPlayers(){

        disabledCounterStart(players[activePlayer], true);
        disabledBtnAttack(players[activePlayer], true);
        activePlayer = nextPLayer(activePlayer);
        disabledCounterStart(players[activePlayer], false);
        disabledBtnAttack(players[activePlayer], true);
        changePlayerColors(activePlayer);
    }

    private void activatePlayer(final Tadpole tadpole){


                updateLabels();
                disabledCounterStart(tadpole, true);
                disabledBtnAttack(tadpole, false);

                countDownTimer = new CountDownTimer(tadpole.getMainCounter()*1000,100){
                    int i =0;
                    @Override
                    // On each counter tick..
                    public void onTick(long millisUntilFinished) {
                        // .. display remaining time
                        int counter = (int)(millisUntilFinished / 1000);
                        updateLabels(tadpole, counter);
                        // .. display gun
                        tadpole.getAttackButton().setImageResource(guns[i].icon);
                        // Check if Attack button is pressed
                        // If yes, disable it, show chosen gun, update progress bar, reset counter
                        if (isAttackHitted) {
                            disabledBtnAttack(tadpole, true);
                            tadpole.getAttackButton().setImageResource(guns[i].icon);
                            // Recovery
                            if (i == 7){
                                // Open new activity - Recovery
                                openRecoveryActivity();
                            // Attack
                            }else {
                                attackValue = guns[i].damage;

                                players[nextPLayer(tadpole.getId())].getHealthPoints().setText(String.valueOf(tadpole.attack(players[nextPLayer(tadpole.getId())], guns[i])));
                                if (players[nextPLayer(tadpole.getId())].getHealth() <= 0) {
                                    winner(tadpole);
                                }

                                afterAttack(tadpole);
                            }
                        }
                        // .. increment index to show new gun
                        i++;
                        // Health recovery possible only for tadpole with health < 100
                        if (tadpole.getHealth() == 100) {
                            if (i > 6)
                                i = 0;
                        } else {
                            if (i > 7)
                                i = 0;
                        }
                    }
                    @Override
                    // Attack not pressed and countdown finished - reset counter
                    public void onFinish() {
                        attackValue=0;
                        afterAttack(tadpole);

                    }
                }.start();

    }


    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        if(isBackBtnPressed){
            finish();
        }else {
            isBackBtnPressed=true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackBtnPressed=false;
                }
            }, 2000);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusic.class);
        stopService(music);
        doUnbindService();
    }

    @Override
    protected void onPause() {
        super.onPause();



        if(mServ!=null)
        mServ.pauseMusic();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mIsBound){
            doBindService();
            Intent music = new Intent();
            music.setClass(this, BackgroundMusic.class);
            startService(music);
        }else if (mServ!=null){
            mServ.resumeMusic();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusic.class);
        stopService(music);
        doUnbindService();
    }

    /*
            * Finish KL countdown - Attack button pressed or countdown is finished
            * give attacked tadpole as parameter
            */

    private void afterAttack(final Tadpole tadpole ){
        // Reset countdown timer
        cancelTimer();

        // If Attack button was pressed introduce 1s delay and display attack points
        if (isAttackHitted) { // Ola's new code
            attackSound = MediaPlayer.create(this, tadpole.getAttackSound());
            attackSound.start();
            vibe.vibrate(100);
            players[nextPLayer(tadpole.getId())].getAttackPoints().setVisibility(View.VISIBLE);
            players[nextPLayer(tadpole.getId())].getAttackPoints().setAlpha(0f); // Damian
            players[nextPLayer(tadpole.getId())].getAttackPoints().animate().alpha(1f).setDuration(300); // Damian
            players[nextPLayer(tadpole.getId())].getAttackPoints().setText("-"+ String.valueOf(attackValue));
            progressbar(players[nextPLayer(tadpole.getId())]);
            // Ola's new code ..
            // Update labels, enable 2nd player Start button, disable this player attack button..
            // .. change player, reset isAttackHitted
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateLabels();
                    attackSound.stop();
                    attackSound.reset();
                    attackSound.release();
                    attackSound = null;
                    isAttackHitted = false;
                    players[nextPLayer(tadpole.getId())].getAttackPoints().setVisibility(View.INVISIBLE);
                    players[nextPLayer(tadpole.getId())].getAttackPoints().setAlpha(0f); // Damian
                    if(players[nextPLayer(tadpole.getId())].getHealth()>0)
                        switchPlayers();
                }
            }, 1000);

        }else
        switchPlayers();


    }

    // Ola's code
    /*
    *  Countdown timer reset
    */
    private void cancelTimer() {
        if(countDownTimer!=null)
            countDownTimer.cancel();
        countDownTimer = null;

    }


    /*
    * Display KL and KR counters value
    */
    private void updateLabels(){

        KL.getLabelCounter().setText(String.valueOf(KL.getMainCounter()));
        KR.getLabelCounter().setText(String.valueOf(KR.getMainCounter()));
    }
    @SuppressLint("SetTextI18n")
    private void updateLabels(Tadpole tadpole, int counter){
        tadpole.getLabelCounter().setText(""+counter);
    }

    /*
    * Open change name dialog
    */
    public void openDialog(View view){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    /*
    Start HelpActivity
     */
    public void openHelpActivity(View view){
        Intent helpActivity = new Intent (getApplicationContext(), HelpActivity.class);
        startActivity(helpActivity);
    }
    /*
    Start InfoActivity
     */
    public void openInfoActivity(View view){
        Intent infoActivity = new Intent (getApplicationContext(), InfoActivity.class);
        startActivity(infoActivity);
    }

    /*
    Start RecoveryActivity
    */
    public void openRecoveryActivity(){
        cancelTimer();
        Intent recoveryActivity = new Intent (getApplicationContext(), RecoveryActivity.class);
        recoveryActivity.putExtra("currentPlayerHealth", players[activePlayer].getHealth());
        recoveryActivity.putExtra("currentPlayerName", players[activePlayer].getName().getText());
        startActivityForResult(recoveryActivity,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                recovered = data.getBooleanExtra("healthRecovered", false);
                afterRecovery(players[activePlayer]);
            }
        }
    }

    private void afterRecovery(final Tadpole tadpole ){
        // Reset countdown timer
        cancelTimer();
        int delay = 0;
        // If health recovery is successfully introduce 1s delay and display attack points
        if (recovered) {
            delay = 1000;
            guns[2].damage = -(100 - tadpole.getHealth());
            attackValue = guns[2].damage;
            players[activePlayer].getAttackPoints().setVisibility(View.VISIBLE);
            players[activePlayer].getAttackPoints().setAlpha(0f); // Damian
            players[activePlayer].getAttackPoints().animate().alpha(1f).setDuration(300); // Damian
            players[activePlayer].getAttackPoints().setText("+"+ String.valueOf(-attackValue));
            players[activePlayer].getAttackPoints().setBackgroundResource(R.drawable.ic_bam_green);
            players[activePlayer].getHealthPoints().setText(String.valueOf(tadpole.attack(players[activePlayer], guns[2])));
            progressbar(players[activePlayer]);
            Toast.makeText(getApplicationContext(), players[activePlayer].getName().getText() + getString(R.string.RecoveryOk), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), players[activePlayer].getName().getText() + getString(R.string.RecoveryNOk), Toast.LENGTH_SHORT).show();


        // Update labels, enable 2nd player Start button, disable this player attack button..
        // .. change player, reset isAttackHitted
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLabels();
                isAttackHitted = false;
                recovered = false;
                players[activePlayer].getAttackPoints().setVisibility(View.INVISIBLE);
                players[activePlayer].getAttackPoints().setAlpha(0f); // Damian
                players[activePlayer].getAttackPoints().setBackgroundResource(R.drawable.ic_bam);
                switchPlayers();
            }
        }, delay);
        // .. Ola's new code

    }

    /**
     * Change player name
     * @param km is 1st (left) player's name
     * @param kt is 1st (left) player's name
     */
    @Override
    public void applyTexts(String km, String kt) {
        KL.getName().setText(km);
        KR.getName().setText(kt);

       // KL.getName().getText();
    }


    /**
     * Enabling/disabling KL Attack button, change icon
     * @param disabled true false
     */
    private void disabledBtnAttack(Tadpole tadpole, boolean disabled){

        if (disabled){
            tadpole.getAttackButton().setBackgroundResource(R.drawable.my_button_grey);
            tadpole.getAttackButton().setEnabled(false);
            tadpole.getAttackButton().setImageResource(R.drawable.ic_unnactive_miecz);
        }else {
            tadpole.getAttackButton().setEnabled(true);
            tadpole.getAttackButton().setBackgroundResource(R.drawable.my_button);
            tadpole.getAttackButton().setImageResource(R.drawable.ic_miecz);
        }
    }

    /**
     * Enabling/disabling KL Start button, change icon
     * @param disabled true false
     */
    private void disabledCounterStart(Tadpole tadpole, boolean disabled){
            if (disabled){
            tadpole.getStartCount().setBackgroundResource(R.drawable.my_button_grey);
            tadpole.getStartCount().setEnabled(false);
            tadpole.getStartCount().setTextColor(getResources().getColor(R.color.unactive_white_icon));
        } else {
            tadpole.getStartCount().setBackgroundResource(R.drawable.my_button);
            tadpole.getStartCount().setEnabled(true);
            tadpole.getStartCount().setTextColor(getResources().getColor(R.color.creme_text));
        }
    }

    // ********************************** Damian's code end

    // *******************************************************
    // ********************************** MisQLak's code start
    // *******************************************************
    // Alert about winner, game restart or finish
    private void winner(final Tadpole tadpole) {
        changePlayerColors(3);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setTitle(getString(R.string.winnerIs) + " " + tadpole.getName().getText() + "!");

        alertDialogBuilder.setMessage(R.string.playAgain);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, R.string.newGameToast, Toast.LENGTH_LONG).show();
                        Intent startIntent = new Intent(MainActivity.this, MainActivity.class);  // --> Ola's new code
                        startIntent.putExtra("winner", tadpole.getId());
                        startActivity(startIntent);
                        finish();
                    }
                });

        /*if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);

            startActivity(intent);
            overridePendingTransition(0, 0);
        }*/


        alertDialogBuilder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

       // ********************************** MisQLak's code end

    // *******************************************************
    // ********************************** Mateusz's code start
    // *******************************************************

    /**
     * Set progress bar max
     * @param pb progressbar
     * @param max maximum value
     */
    private void setProgressMax(ProgressBar pb, int max) {
        pb.setMax(max * 100);
    }

    /**
     * Set progress bar animation values
     * @param pb progressbar
     * @param progressTo progress bar animation value
     */
    private void setProgressAnimate(ProgressBar pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    /**
     * Left progress bar animation
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void progressbar(Tadpole tadpole) {
        // Progress bar animation
        setProgressMax(tadpole.getProgressBar(),tadpole.getHitPoints());
        setProgressAnimate(tadpole.getProgressBar(),tadpole.getHealth());
        tadpole.getProgressBar().setProgress(tadpole.getHealth());

        // Animation color changing
        if (tadpole.getHealth() >= tadpole.getHitPoints()*0.6){
            tadpole.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        } else if (tadpole.getHealth() >= tadpole.getHitPoints()*0.3) {
            tadpole.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        } else {
            tadpole.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
    }


    // ********************************** Mateusz's code end


    // ******************************************************
    // ********************************** Cezary's code start
    // ******************************************************

//    public void enterChangeroom(){
//
//    }


    /**
     * Display/hide player depending on turn
     * @param parameter 0-3
     */
    private void changePlayerColors(int parameter) {
        // Left KL player's turn
        if (parameter == 0){
            // Display left KL player
            leftTadpoleVisibility(1);
            // Hide right KR player
            rightTadpoleVisibility(0);
        }
        // Right KR player's turn
        else if (parameter == 1){
            // Hide left KL player
            leftTadpoleVisibility(0);
            // Display right KR player
            rightTadpoleVisibility(1);
        }
        // Display both players
        else if (parameter == 2){
            // Display left KL player
            leftTadpoleVisibility(1);
            // Display right KR player
            rightTadpoleVisibility(1);
        }
        // Hide both players
        else if (parameter == 3){
            // Hide left KL player
            leftTadpoleVisibility(0);
            // Hide right KR player
            rightTadpoleVisibility(0);
        }
    }

    /**
     * Left KL player visibility
     * @param value hide = 0, display = 1
     */
    private void leftTadpoleVisibility(int value) {

        ProgressBar progressSword = findViewById(R.id.progressA);     // Progress bar
        View swordBack = findViewById(R.id.swordTadBack);                           // Icon background
        ImageView swordTadPole = findViewById(R.id.kijankaLewa);       // Player's icon
        TextView turnDisplaySword = findViewById(R.id.KLRound);          // Turn display text
        Button countDownStartSword = findViewById(R.id.startCountKL);      // Start button
        TextView powerAttackSword = findViewById(R.id.labelCounterKL);   // Countdown value
        ImageButton btnAttackSword = findViewById(R.id.KLBtnAttack);  // Attack button
        TextView nameSword = findViewById(R.id.KLName);                  // Player's name

        // Hide left KL player
        if (value == 0) {
            // Change player's icon
            swordTadPole.setImageResource(R.drawable.left_tadpole_1_non_active);

            // Change turnDisplay TextView text, color and font color
            turnDisplaySword.setText(R.string.notYourTurn);
            turnDisplaySword.setBackgroundColor(getResources().getColor(R.color.unactive_green));
            turnDisplaySword.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Disable Start button, change color and font color
//            countDownStartSword.setEnabled(false);
//            countDownStartSword.setBackgroundResource(R.drawable.my_button_grey);
//            countDownStartSword.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Change countdown value color
            powerAttackSword.setTextColor(getResources().getColor(R.color.unactive_green));

            // Disable Attack button, change color and icon
//            btnAttackSword.setEnabled(false);
//            btnAttackSword.setBackgroundResource(R.drawable.my_button_grey);
//            btnAttackSword.setImageResource(R.drawable.ic_unnactive_miecz);

            // Change name color
            nameSword.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Change background of player's main icon
            swordBack.setVisibility(View.VISIBLE);
            swordBack.setBackgroundResource(R.drawable.background_unnactive_view);

            // Change progress bar background color
            progressSword.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal_unactive));
        }
        // Display left KL player
        else if (value == 1) {
            // Change player's icon
            swordTadPole.setImageResource(R.drawable.left_tadpole_1);

            // Change turnDisplay TextView text, color and font color
            turnDisplaySword.setText(R.string.YourTurn);
            turnDisplaySword.setBackgroundColor(getResources().getColor(R.color.main_green));
            turnDisplaySword.setTextColor(getResources().getColor(R.color.creme_text));

            // Enable Start button, change color and font color
//            countDownStartSword.setEnabled(true);
//            countDownStartSword.setBackgroundResource(R.drawable.my_button);
//            countDownStartSword.setTextColor(getResources().getColor(R.color.creme_text));

            // Change countdown value color
            powerAttackSword.setTextColor(getResources().getColor(R.color.creme_text));

            // Enable Attack button, change color and icon
//            btnAttackSword.setEnabled(true);
//            btnAttackSword.setBackgroundResource(R.drawable.my_button);
//            btnAttackSword.setImageResource(R.drawable.ic_miecz);

            // Change name color
            nameSword.setTextColor(getResources().getColor(R.color.creme_text));

            // Change background of player's main icon
            swordBack.setVisibility(View.INVISIBLE);

            // Change progress bar background color
            progressSword.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal));
        }
    }
    /**
     * Right KR player visibility
     * @param value hide = 0, display = 1
     */
    private void rightTadpoleVisibility(int value) {
        ProgressBar progressAxe = findViewById(R.id.progressB);    // Progress bar
        View axeBack = findViewById(R.id.axeTadBack);                            // Icon background
        ImageView axeTadPole = findViewById(R.id.kijankaPrawa);      // Player's icon
        TextView turnDisplayAxe = findViewById(R.id.KRRound);         // Turn display text
        Button countDownStartAxe = findViewById(R.id.startCountKR);     // Start button
        TextView powerAttackAxe = findViewById(R.id.labelCounterKR);  // Countdown value
        ImageButton btnAttackAxe = findViewById(R.id.KRBtnAttack); // Attack button
        TextView nameAxe = findViewById(R.id.KRName);                 // Player's name

        // Hide right KR player
        if (value == 0) {
            // Change player's icon
            axeTadPole.setImageResource(R.drawable.right_tadpole_2_non_active);

            // Change turnDisplay TextView text, color and font color
            turnDisplayAxe.setText(R.string.notYourTurn);
            turnDisplayAxe.setBackgroundColor(getResources().getColor(R.color.unactive_green));
            turnDisplayAxe.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Disable Start button, change color and font color
//            countDownStartAxe.setEnabled(false);
//            countDownStartAxe.setBackgroundResource(R.drawable.my_button_grey);
//            countDownStartAxe.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Change countdown value color
            powerAttackAxe.setTextColor(getResources().getColor(R.color.unactive_green));

            // Disable Attack button, change color and icon
//            btnAttackAxe.setEnabled(false);
//            btnAttackAxe.setBackgroundResource(R.drawable.my_button_grey);
//            btnAttackAxe.setImageResource(R.drawable.ic_unnactive_miecz);

            // Change name color
            nameAxe.setTextColor(getResources().getColor(R.color.unactive_white_icon));

            // Change background of player's main icon
            axeBack.setVisibility(View.VISIBLE);
            axeBack.setBackgroundResource(R.drawable.background_unnactive_view);

            // Change progress bar background color
            progressAxe.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal_unactive));

        }
        // Display right KR player
        else if (value == 1) {
            // Change player's icon
            axeTadPole.setImageResource(R.drawable.right_tadpole_2);

            // Change turnDisplay TextView text, color and font color
            turnDisplayAxe.setText(R.string.YourTurn);
            turnDisplayAxe.setBackgroundColor(getResources().getColor(R.color.main_green));
            turnDisplayAxe.setTextColor(getResources().getColor(R.color.creme_text));

            // Enable Start button, change color and font color
//            countDownStartAxe.setEnabled(true);
//            countDownStartAxe.setBackgroundResource(R.drawable.my_button);
//            countDownStartAxe.setTextColor(getResources().getColor(R.color.creme_text));

            // Change countdown value color
            powerAttackAxe.setTextColor(getResources().getColor(R.color.creme_text));

            // Enable Attack button, change color and icon
//            btnAttackAxe.setEnabled(true);
//            btnAttackAxe.setBackgroundResource(R.drawable.my_button);
//            btnAttackAxe.setImageResource(R.drawable.ic_miecz);

            // Change countdown value color
            nameAxe.setTextColor(getResources().getColor(R.color.creme_text));

            // Change background of player's main icon
            axeBack.setVisibility(View.INVISIBLE);

            // Change progress bar background color
            progressAxe.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal));
        }
    }

    // ********************************** Cezary's code end
}
