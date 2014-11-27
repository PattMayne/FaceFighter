package com.pattmayne.facefighter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Player object is based on a FaceCharacter object. But while a FaceCharacter can be permanent, a Player only lives within the scope of one Fight.
 * This way a Fight object is free to manipulate a Player object without effecting the permanent FaceCharacter object.
 * Created by Matt on 2014-11-04.
 */
public class Player {

    //variables
    private FaceCharacter character;
    private ArrayList<FacePiece> pieces;
    private Player opponent;
    private String name;
    private boolean myTurn;
    private Turn currentTurn;

    private Random randomizer;

    /**
     * Create a new Player object based on a FaceCharacter object.
     * @param character
     */
    public Player(FaceCharacter character)
    {
        randomizer = new Random();
        this.name = character.getName();
        this.character = character;
        pieces = new ArrayList<FacePiece>();
        copyPieceList();
    }

    /**
     * Make a copy of the Character's pieceList, so the game can remove pieces from the Player's list without removing them from the Character's list.
     * This makes NEW COPIES of every FacePiece so that their HP and other properties can be altered temporarily in the game.
     */
    private void copyPieceList()
    {
        for (int i=0; i<character.getPieces().size(); i++)
        {
            pieces.add(character.getPieces().get(i).copy());
        }
    }

    public void setOpponent(Player opponent)
    {
        this.opponent = opponent;
    }

    /**
     * This method gives the soundEffectPlayer the current Turn object, and tells the soundEffectPlayer whether it is their Turn.
     * @param myTurn
     * @param turn
     */
    public void myTurn(boolean myTurn, Turn turn)
    {
        this.myTurn = myTurn;
        currentTurn = turn;

        if (myTurn)
        {
            opponent.myTurn(false, turn);
        }
    }

    /**
     * removes any dead pieces from the Player's list of pieces.
     * This will effect which pieces are drawn, and which buttons will appear on the user's screen
     * @param fight
     */
    public void removeDestroyedPieces(Fight fight)
    {
        FacePiece destroyedPiece = null;

        for (int i=0; i<pieces.size(); i++)
        {
            if (!pieces.get(i).isAlive()) {
                destroyedPiece = pieces.get(i);

                fight.announce(opponent.getName(), "Destroyed " + name + "'s", destroyedPiece.getName());
            }
        }
        pieces.remove(destroyedPiece);

        //If the last FacePiece has been destroyed, then the Player is destroyed, so the Fight object must be notified.
        if (pieces.size() == 0)
        {
            fight.playerDestroyed(this);
        }
    }

    /**
     * This method turns a neutral (or defensive/responsive) FacePiece into a kamikaze piece.
     * It's only called when the Player has no more FacePieces that can initiate attacks.
     * This sets Smash as the new BattleMove.
     * @return FacePiece
     */
    public FacePiece makeKamikazePiece()
    {
        FacePiece kamikazePiece = null;

        //First check to see if there are any completely neutral pieces which are neither weapons or responsive/defensive.
        boolean searchForNeutralPiece = true;
        for (int i=0; i< pieces.size(); i++)
        {
            if (searchForNeutralPiece) {
                FacePiece piece = pieces.get(i);
                if (!piece.isWeapon() && !piece.isResponsive()) {
                    kamikazePiece = piece;
                    searchForNeutralPiece = false;
                }
            }
        }

        //If the only remaining FacePiece objects are responsive/defensive, just make the first piece on the list into a kamikazePiece.
        if (kamikazePiece == null && pieces.size() > 0)
        {
            kamikazePiece = pieces.get(0);
        }

        //after a piece has been chosen, set it as the kamikazePiece and give it the Smash BattleMove.
        if (kamikazePiece != null) {
            kamikazePiece.setBattleMove("Kamikaze Smash");
            kamikazePiece.makeKamikaze();
            kamikazePiece.makeWeapon();
            kamikazePiece.setDamage(7);
        }

        //At this point there is still a possibility that the kamikazePiece is null, so the calling method should account for that possibility.
        //Although I have written the process so it will never happen...
        return kamikazePiece;
    }


    //If the Player wins or loses, the original FaceCharacter must be notified so their stats can be adjusted.
    public void win()
    {
        character.win();
    }

    public void lose()
    {
        character.lose();
    }

    /**
     * Save the FaceCharacter to the persistent database along with any changes to its properties.
     * The only change should be either a win or a loss added to the FaceCharacter's stats.
     * @param context
     */
    public void saveCharacter(Context context)
    {
        character.saveFaceToDB(context);
    }

    //getter methods

    /**
     * A randomly chosen piece may be needed from a character during the fight.
     * This method provides that piece.
     * @return
     */
    public FacePiece getRandomPiece()
    {
        if (pieces.size() > 0) {
            int randomPieceIndex = randomizer.nextInt(pieces.size());
            return pieces.get(randomPieceIndex);
        }
        else return null;
    }

    public Turn getTurn()
    {
        return currentTurn;
    }

    public Player getOpponent()
    {
        return opponent;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<FacePiece> getPieces()
    {
        return pieces;
    }
}
