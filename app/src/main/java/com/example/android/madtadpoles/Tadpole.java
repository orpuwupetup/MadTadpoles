package com.example.android.madtadpoles;




import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Michał Jura on 11.12.2017.
 *
 * class definig tadpoles
 */


class Tadpole {
    //Tadpole views
   private ImageButton attackButton;
   private ImageView skinView;
   private int skinId;
   private int unactiveSkinId;
   private Button startCount;
   private TextView name;
   private TextView AttackPoints;
   private ProgressBar progressBar;
   private TextView healthPoints;
   private TextView labelCounter;
   private int attackSound;
    // Tadpole stats
    private int hitPoints;
    private int health;
    private int mainCounter;
    private int id;


    Tadpole(int hitPoints, int mainCounter, int id) {
        this.hitPoints = hitPoints;
        this.mainCounter = mainCounter;
        this.id = id;
        this.health = hitPoints;
        Player tad = new Player(this.id, "Bob");
        this.skinId = tad.getWhichSkin();
        this.unactiveSkinId = this.skinId + 1;
    }
    public void setSkinId(int skinIndex){
        this.skinId = skinIndex;
        this.unactiveSkinId = skinIndex + 1;
    }
    public int getSkinId(){
        return this.skinId;
    }

     int attack(Tadpole tadpole, Gun gun) {

        int attackValue;
        int health;
        attackValue = gun.damage;
        health = tadpole.takeDamage(attackValue);
        return health;
    }

     private int takeDamage(int damage) {
        if (health >= 0)
            this.health -= damage;
        return health;
    }

    void setSkinView (ImageView view_id){
        skinView = view_id;
    }

    ImageView getSkinView (){
        return skinView;
    }

    int getAttackSound() {
        return attackSound;
    }

    void setAttackSound(int attackSound) {
        this.attackSound = attackSound;
    }

    void setAttackButton(ImageButton attackButton) {
        this.attackButton = attackButton;
    }

     void setStartCount(Button startCount) {
        this.startCount = startCount;
    }

     void setName(TextView name) {
        this.name = name;
    }

     void setAttackPoints(TextView attackPoints) {
        AttackPoints = attackPoints;
    }

     void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

     int getHealth() {
        return health;
    }

     int getId() {
        return id;
    }

     int getHitPoints() {
        return hitPoints;
    }

     void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

     int getMainCounter() {
        return mainCounter;
    }

     void setMainCounter(int mainCounter) {
        this.mainCounter = mainCounter;
    }

     TextView getHealthPoints() {
        return healthPoints;
    }

     void setHealthPoints(TextView healthPoints) {
        this.healthPoints = healthPoints;
    }

     ImageButton getAttackButton() {
        return attackButton;
    }

     Button getStartCount() {
        return startCount;
    }

     TextView getName() {
        return name;
    }

     TextView getAttackPoints() {
        return AttackPoints;
    }

     ProgressBar getProgressBar() {
        return progressBar;

    }

     TextView getLabelCounter() {
        return labelCounter;
    }

     void setLabelCounter(TextView labelCounter) {
        this.labelCounter = labelCounter;
    }
}
