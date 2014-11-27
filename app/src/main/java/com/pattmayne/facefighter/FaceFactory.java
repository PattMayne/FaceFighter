package com.pattmayne.facefighter;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import java.util.ArrayList;

/**
 * This class assists in the creation of a FaceCharacter.
 * As the user chooses and places FacePieces on the canvas, and chooses a name,
 * the FaceFactory receives that information and create a new FaceCharacter from it.
 *
 * It can also reconstruct a FaceCharacter that has been saved to a database.
 *
 * This class stores information about the pre-defined FacePiece objects that are made available to the user.
 * Created by Matt on 2014-10-31.
 */
public class FaceFactory{

    //variables

    private Context context;
    private View view;
    private Resources res;

    private FaceCharacter character;
    //The activePiece is the FacePiece which is being manipulated "right now" on the screen.
    private FacePiece activePiece;
    private ArrayList<FacePiece> facePieces;

    private Bitmap pieceBitmap;

    //SQLite variables
    private SQLiteDatabase db;
    private FaceDBHelper helper;
    private ContentValues values;
    private Cursor cursor;

    /**
     * The constructor creates a new, blank FaceCharacter,
     * then waits for further input from the user.
     * @param view
     * @param context
     */
    public FaceFactory(View view, Context context)
    {
        this.view = view;
        this.context = context;

        character = new FaceCharacter();
        res = view.getResources();
        facePieces = character.getPieces();
    }

    public void setName(String name)
    {
        character.setName(name);
    }

    public FaceCharacter getCharacter()
    {
        return character;
    }

    public ArrayList<FacePiece> getPieces()
    {
        return facePieces;
    }

    /**
     * This method is used whenever a class wants to make a custom FacePiece,
     * with a custom name and Bitmap image.
     *
     * Currently, the BadguyBuilderActivity calls this method to create FacePiece objects
     * for the computer-controlled opponent FaceCharacters.
     * @param pieceName
     * @param pieceImage
     * @param layer
     * @return
     */
    public FacePiece makeExtraPiece(String pieceName, Bitmap pieceImage, int layer)
    {
        FacePiece tempPiece = new FacePiece(pieceName, pieceImage);
        character.addPiece(tempPiece, layer);
        return tempPiece;
    }

    //These methods create specific FacePiece objects with pre-defined properties.
    //Each method receives an int value which decides their layerPlacement (who gets drawn on top of who),
    //and automatically adds that piece to the FaceCharacter's ArrayList of FacePieces.

    public FacePiece makeFaceOne(int layer)
    {
        FacePiece tempPiece = new FacePiece("Blue Face", BitmapFactory.decodeResource(res, R.drawable.face_one));
        tempPiece.setHP(27);
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeFaceTwo(int layer) {
        FacePiece tempPiece = new FacePiece("Green Face", BitmapFactory.decodeResource(res, R.drawable.face_two));
        tempPiece.setHP(27);
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeEyesLaser(int layer) {
        FacePiece tempPiece = new FacePiece("Laser Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_laser));
        tempPiece.makeWeapon();
        tempPiece.setDamage(11);
        tempPiece.setHP(16);
        tempPiece.setBattleMove("Laser Burn");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeEyesCat(int layer) {
        FacePiece tempPiece = new FacePiece("Cat Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_cat));
        tempPiece.makeWeapon();
        tempPiece.setDamage(9);
        tempPiece.setHP(18);
        tempPiece.setBattleMove("Scratch");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeEyesShades(int layer) {
        FacePiece tempPiece = new FacePiece("Cool Shades", BitmapFactory.decodeResource(res, R.drawable.eyes_shades));
        tempPiece.setArmour(6);
        tempPiece.setHP(19);
        tempPiece.makeResponsive();
        tempPiece.makeReflective();
        tempPiece.setBattleMove("Reflect Damage");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeMouthBigTeeth(int layer) {
        FacePiece tempPiece = new FacePiece("Big Teeth", BitmapFactory.decodeResource(res, R.drawable.mouth_bigteeth));
        tempPiece.makeWeapon();
        tempPiece.setHP(11);
        tempPiece.setArmour(1);
        tempPiece.setDamage(13);
        tempPiece.setRechargeTime(3);
        tempPiece.setBattleMove("Bite");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeMouthMetalTongue(int layer) {
        FacePiece tempPiece = new FacePiece("Heavy Metal Tongue", BitmapFactory.decodeResource(res, R.drawable.mouth_metaltongue));
        tempPiece.makeWeapon();
        tempPiece.setHP(27);
        tempPiece.setDamage(7);
        tempPiece.setBattleMove("Tongue Lash");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeHeadHelmet(int layer)
    {
        FacePiece tempPiece = new FacePiece("Spiked Helmet", BitmapFactory.decodeResource(res, R.drawable.head_helmet));
        tempPiece.setArmour(6);
        tempPiece.setHP(19);
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeHeadSpikedHair(int layer)
    {
        FacePiece tempPiece = new FacePiece("Spiked Hair", BitmapFactory.decodeResource(res, R.drawable.head_spikedhair));
        tempPiece.setArmour(3);
        tempPiece.setHP(24);
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeHeadTopHat(int layer)
    {
        FacePiece tempPiece = new FacePiece("Stylish Top Hat", BitmapFactory.decodeResource(res, R.drawable.head_tophat));
        tempPiece.setArmour(4);
        tempPiece.setHP(21);
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeBeardAbsorbent(int layer) {
        FacePiece tempPiece = new FacePiece("Mighty Absorbent Beard", BitmapFactory.decodeResource(res, R.drawable.beard_absorbent));
        tempPiece.setHP(19);
        tempPiece.setArmour(7);
        tempPiece.makeResponsive();
        tempPiece.makeAbsorbent();
        tempPiece.setBattleMove("Absorb Damage");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeBeardSonic(int layer) {
        FacePiece tempPiece = new FacePiece("Sonic Moustache", BitmapFactory.decodeResource(res, R.drawable.beard_sonic));
        tempPiece.makeWeapon();
        tempPiece.setHP(13);
        tempPiece.setDamage(13);
        tempPiece.setRechargeTime(2);
        tempPiece.setBattleMove("Sonic Slice");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeBeardSpikedTie(int layer) {
        FacePiece tempPiece = new FacePiece("Spiked Tie", BitmapFactory.decodeResource(res, R.drawable.beard_spikedtie));
        tempPiece.makeWeapon();
        tempPiece.setDamage(8);
        tempPiece.setHP(22);
        tempPiece.setBattleMove("Stab");
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeBrowMean(int layer) {
        FacePiece tempPiece = new FacePiece("Mean Brows", BitmapFactory.decodeResource(res, R.drawable.brow_mean));
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }

    public FacePiece makeBrowCurious(int layer) {
        FacePiece tempPiece = new FacePiece("Curious Brows", BitmapFactory.decodeResource(res, R.drawable.brow_curious));
        tempPiece.setLayerPlacement(layer);
        character.addPiece(tempPiece, layer);

        return tempPiece;
    }



    /**
     * This method allows you to retrieve FaceCharacter information from the database and recreate your saved character.
     * @return
     */
    public FaceCharacter reviveCharacter()
    {
        helper = new FaceDBHelper(context);
        db = helper.getReadableDatabase();
        cursor = db.query("face_table", null, null, null, null, null, null);

        cursor.moveToFirst();
        character.setName(cursor.getString(1));
        character.setWins(cursor.getInt(2));
        character.setLosses(cursor.getInt(3));

        cursor.close();

        cursor = db.query("face_pieces_table", null, null, null, null, null, null);

        for (int i=0; i<cursor.getCount(); i++)
        {
            if (i==0)
            {
                cursor.moveToFirst();
            }
            else {cursor.moveToNext();}

            FacePiece tempPiece = new FacePiece(cursor.getString(9),choosePieceBitmap(cursor.getString(9)));
            tempPiece.setLayerPlacement(cursor.getInt(16));
            tempPiece.setHP(cursor.getInt(3));
            tempPiece.setCharacterName(cursor.getString(10));
            tempPiece.setDamage(cursor.getInt(1));
            tempPiece.setRechargeTime(cursor.getInt(4));
            tempPiece.setArmour(cursor.getInt(2));
            tempPiece.setBattleMove(cursor.getString(11));
            tempPiece.setCost(cursor.getInt(7));
            tempPiece.setPicLocation(cursor.getInt(12),cursor.getInt(13));

            //check to see if the piece should respondToAttack, and if so, if it should absorb or reflect the damage
            if(cursor.getInt(6)==1)
            {
                tempPiece.makeResponsive();

                if(cursor.getInt(17)==1)
                {
                    tempPiece.makeReflective();
                } else if (cursor.getInt(18)==1)
                {
                    tempPiece.makeAbsorbent();
                }

            }

            if(cursor.getInt(5)==1)
            {
                tempPiece.makeWeapon();
            }

            character.addPiece(tempPiece,cursor.getInt(16));
        }

        return character;
    }


    /**
     * This method will be used to choose the Bitmap image based on the stored name of the FacePiece object.
     * It will be used to revive FacePiece objects which have been stored to the database.
     *
     * Other FacePiece properties can be stored as numbers or text directly in a database,
     * but these Bitmap images cannot be stored in a database.
     * So we must store their names in a database, then use this method to retrieve the corresponding Bitmap.
     * @param name
     * @return
     */
    private Bitmap choosePieceBitmap(String name)
    {

        if (name.contains("Blue Face"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.face_one);
        } else if (name.contains("Green Face"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.face_two);
        } else if (name.contains("Curious Brows"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.brow_curious);
        } else if (name.contains("Mean Brows"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.brow_mean);
        } else if (name.contains("Laser Eyes"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.eyes_laser);
        } else if (name.contains("Cool Shades"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.eyes_shades);
        } else if (name.contains("Cat Eyes"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.eyes_cat);
        } else if (name.contains("Spiked Helmet"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.head_helmet);
        } else if (name.contains("Stylish Top Hat"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.head_tophat);
        } else if (name.contains("Spiked Hair"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.head_spikedhair);
        } else if (name.contains("Heavy Metal Tongue"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.mouth_metaltongue);
        } else if (name.contains("Big Teeth"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.mouth_bigteeth);
        } else if (name.contains("Mighty Absorbent Beard"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.beard_absorbent);
        } else if (name.contains("Sonic Moustache"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.beard_sonic);
        } else if (name.contains("Spiked Tie"))
        {
            pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.beard_spikedtie);
        } else {pieceBitmap = BitmapFactory.decodeResource(res, R.drawable.beard_red);}

        return pieceBitmap;
    }

}
