package com.pattmayne.facefighter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A FaceCharacter object represents a character who will fight in battles,
 * including the user's character and the computer's "bad guy" opponents.
 *
 * A FaceCharacter consists mostly of statistics and an ArrayList of FacePiece objects.
 * (FacePiece objects are more complex, with Bitmap images and properties that are useful in battles).
 *
 * Note that a FaceCharacter actually doesn't fight in a battle. Instead, we create a Player object,
 * which is based on a FaceCharacter object, so we can manipulate the Player without altering the FaceCharacter.
 * Created by Matt on 2014-10-24.
 */
public class FaceCharacter implements Serializable {
    //variables
    private ArrayList<FacePiece> facePieces;
    private String name;

    //stats variables
    private String stats;
    private int totalHP;
    private int totalArmour;
    private String totalBattleMoves;

    private int wins = 0;
    private int losses = 0;

    //database variables
    private FaceDBHelper helper;
    private SQLiteDatabase db;
    private ContentValues values;

    /**
     * The constructor is totally blank.
     * This allows the FaceFactory object to do all the work to fill in the blanks for the FaceCharacter,
     * using the public methods we create in this class.
     */
    public FaceCharacter()
    {
        facePieces = new ArrayList<FacePiece>();
    }

    /**
     * The FaceFactory calls this method to add pieces to the FaceCharacter's ArrayList of FacePieces.
     * @param piece
     * @param index
     */
    public void addPiece(FacePiece piece, int index)
    {
        if(facePieces.size() == index)
        {
            facePieces.add(piece);
        }
        else {
            facePieces.set(index, piece);
        }
    }

    /**
     * This method is called after the user chooses a name in the FaceBuilderActivity.
     * It sets the character's name and also applies that name to a variable for each of the facePieces, as a handle, for easy retrieval from a database.
     * @param newName
     */
    public void setName(String newName)
    {
        name = newName;

        for (int i=0; i<facePieces.size(); i++)
        {
            facePieces.get(i).setCharacterName(name);
        }
    }

    public void win()
    {
        wins++;
    }

    public void lose()
    {
        losses++;
    }


    //These two methods are called when the FaceFactory revives a FaceCharacter whose information has been saved to the database.
    //We need to "set" the number of wins and losses that have previously been accumulated and saved.

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public void setLosses(int losses)
    {
        this.losses = losses;
    }


    /**
     * When an Activity needs a String of information to display the FaceCharacter's properties,
     * that Activity will call this method.
     * @return
     */
    public String makeStatsString()
    {
        totalBattleMoves = "\n";
        totalArmour = 0;
        totalHP = 0;

        for (int i=0; i<facePieces.size(); i++)
        {
            FacePiece tempPiece = facePieces.get(i);
            if(!tempPiece.getBattleMove().contains("none")) {
                totalBattleMoves = totalBattleMoves + tempPiece.getBattleMove() + "\n";
            }
            totalArmour = totalArmour + tempPiece.getArmour();
            totalHP = totalHP + tempPiece.getHP();
        }

        stats = "\n";
        stats = stats + "\n" + "BATTLE MOVES: " + "\n" + totalBattleMoves + "\n";
        stats = stats + "\n" + "ARMOUR: " + totalArmour;
        stats = stats + "\n" + "HP: " + totalHP;
        stats = stats + "\n" + "WINS: " + wins;
        stats = stats + "\n" + "LOSSES: " + losses;

        return stats;
    }

    /**
     * After creating or altering a FaceCharacter,
     * this method should be called to persistently save the FaceCharacter object's properties.
     * @param context
     */
    public void saveFaceToDB(Context context)
    {
        helper = new FaceDBHelper(context);
        db = helper.getWritableDatabase();
        values = new ContentValues();

        db.delete("face_table",null,null);
        db.delete("face_pieces_table",null,null);

        for (int i=0; i<facePieces.size(); i++)
        {
            facePieces.get(i).savePieceToDB(db, values);
        }

        values.clear();

        values.put("name", name);
        values.put("losses", losses);
        values.put("wins", wins);

        db.insert("face_table", null, values);

        values.clear();
    }

    public ArrayList<FacePiece> getPieces()
    {
        return facePieces;
    }

    public String getName()
    {
        return name;
    }
}
