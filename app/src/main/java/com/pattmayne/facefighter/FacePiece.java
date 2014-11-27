package com.pattmayne.facefighter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * A FaceCharacter is a collection of FacePiece objects.
 * Each FacePiece can be a weapon, or responsive, or neutral.
 *
 * Each FacePiece has an image, a name, and information about where it is to be displayed on the screen.
 * Information about its usefulness in battle, is optional.
 * Created by Matt on 2014-10-19.
 */
public class FacePiece implements Serializable {

    //variables

    private int damage = 0;
    private int armour = 1;
    private int hp = 1;
    private int rechargeTime = 1;
    private boolean isWeapon = false;
    private boolean respondsToAttack = false;
    private boolean reflects = false;
    private boolean absorbs = false;
    private int cost = 1;
    private boolean alive = true;
    private boolean kamikaze = false;

    private String name;
    private String characterName;
    private String battleMove = "none";
    private String stats;
    private Bitmap pic;
    private int[] picLocation = {0,0};
    private int[] picSize = {0,0};


    //layerPlacement will become this object's index(status) in the FaceCharacter's ArrayList of FacePiece objects.
    //That decides which pieces will be drawn on top or on bottom.
    private int layerPlacement = 0;

    /**
     * Constructor. A name and picture are required.
     * @param newName
     * @param newPic
     */
    public FacePiece(String newName, Bitmap newPic)
    {
        pic = newPic;
        name = newName;
        picSize[0] = pic.getWidth();
        picSize[1] = pic.getHeight();
    }

    /**
     * This method provides a String of information about the FacePiece.
     * @return
     */
    public String makeStatsString()
    {
        stats = "\n";
        stats = stats + "\n" + "Battle Move: " + battleMove;
        stats = stats + "\n" + "Damage: " + damage;
        stats = stats + "\n" + "Armour: " + armour;
        stats = stats + "\n" + "HP: " + hp;
        stats = stats + "\n" + "Recharge Time: " + rechargeTime;

        return stats;
    }

    /**
     * This method completely recreates this object and sends the clone to whoever called this method.
     * This is for when a Player object is created and it needs copies of a FaceCharacter's FacePieces.
     * We can freely manipulate and alter the cloned FacePiece without altering this one.
     * @return
     */
    public FacePiece copy()
    {
        FacePiece copy = new FacePiece(name, pic);
        copy.setHP(hp);
        copy.setArmour(armour);
        copy.setBattleMove(battleMove);
        copy.setLayerPlacement(layerPlacement);
        copy.setPicLocation(picLocation[0],picLocation[1]);
        copy.setDamage(damage);
        copy.setRechargeTime(rechargeTime);
        copy.setCost(cost);

        if (isWeapon())
        {
            copy.makeWeapon();
        }

        if (isReflective())
        {
            copy.makeReflective();
        }

        if (isResponsive())
        {
            copy.makeResponsive();
        }

        if (isAbsorbent())
        {
            copy.makeAbsorbent();
        }

        return copy;
    }

    /**
     * During the battle (Fight), if a FacePiece receives damage, the damage will be transmitted by calling this method.
     * (That will only happen to a Player object's FacePiece clones, never to the original FaceCharacter's FacePiece).
     * @param wound
     */
    public void sufferWound(int wound)
    {
        hp = hp-wound;

        //If a FacePiece loses all its HP and dies,
        // the Fight's game logic will notice the !alive status,
        // and remove the dead FacePiece from the Player's ArrayList.
        if (hp <= 0)
        {
            die();
        }
    }

    /**
     * During the battle (Fight), if a FacePiece receives HP, the bonus HP will be transmitted by calling this method.
     * (That will only happen to a Player object's FacePiece clones, never to the original FaceCharacter's FacePiece).
     * @param health
     */
    public void receiveHP(int health)
    {
        hp = hp + health;
    }

    private void die()
    {
        alive = false;
    }


    //Setters:

    public void setDamage(int newDamage)
    {
        damage = newDamage;
        if (damage>0)
        {
            isWeapon = true;
        }
    }

    public void setCharacterName(String characterName)
    {
        this.characterName = characterName;
    }

    public void setArmour(int newArmour)
    {
        armour = newArmour;
    }

    public void setHP(int newHP)
    {
        hp = newHP;
    }

    public void setRechargeTime(int newRechargeTime)
    {
        rechargeTime = newRechargeTime;
    }

    public void setCost(int newCost)
    {
        cost = newCost;
    }

    public void setPicLocation(int x, int y)
    {
        picLocation[0] = x;
        picLocation[1] = y;
    }

    public void setLayerPlacement(int l)
    {
        layerPlacement = l;
    }

    public void setBattleMove(String move)
    {
        battleMove = move;
    }

    public void makeWeapon()
    {
        isWeapon = true;
    }

    public void makeKamikaze()
    {
        kamikaze = true;
        reflects = false;
        absorbs = false;
        respondsToAttack = false;
        damage = hp + 5;
    }

    public void makeResponsive()
    {
        respondsToAttack = true;
    }

    public void makeReflective() {
        reflects = true;
    }

    public void makeAbsorbent() {
        absorbs = true;
    }

    //Getters:

    public Bitmap getPic()
    {
        return pic;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getArmour()
    {
        return armour;
    }

    public int getHP()
    {
        return hp;
    }

    public int getRechargeTime()
    {
        return  rechargeTime;
    }

    public int getCost()
    {
        return cost;
    }

    public int[] getPicLocation()
    {
        return picLocation;
    }

    public boolean isWeapon()
    {
        return isWeapon;
    }

    public boolean isResponsive()
    {
        return respondsToAttack;
    }

    public boolean isReflective()
    {
        return reflects;
    }

    public boolean isAbsorbent()
    {
        return absorbs;
    }

    public boolean isKamikaze()
    {
        return kamikaze;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public int getLayerPlacement()
    {
        return layerPlacement;
    }

    public String getBattleMove()
    {
        return battleMove;
    }

    public String getName()
    {
        return name;
    }

    /**
     * When the FaceCharacter is saved to a database, each FacePiece must be individually stored to a different database.
     * This method performs that function.
     * @param db
     * @param values
     */
    public void savePieceToDB(SQLiteDatabase db, ContentValues values)
    {
        values.clear();

        values.put("damage", damage);
        values.put("armour", armour);
        values.put("cost", cost);
        values.put("hp", hp);
        values.put("recharge_time", rechargeTime);
        values.put("responds_to_attack", respondsToAttack);
        values.put("absorbent", absorbs);
        values.put("reflective", reflects);
        values.put("layer_placement", layerPlacement);
        values.put("pic_location_x", picLocation[0]);
        values.put("pic_location_y", picLocation[1]);
        values.put("pic_size_x", picSize[0]);
        values.put("pic_size_y", picSize[1]);

        values.put("battle_move", battleMove);
        values.put("name", name);
        values.put("character_name", characterName);

        db.insert("face_pieces_table", null, values);

        values.clear();
    }

}
