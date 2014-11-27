package com.pattmayne.facefighter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Matt on 2014-11-04.
 * An object of this class represents a single battle between two FaceCharacters.
 * A Fight object manages the battle-logic between Player objects (based on FaceCharacters), and initiates many sequential turns (represented as Turn objects).
 * Plus it will send info to the UI (Activity and View) to display.
 */
public class Fight {

    //variables
    private Player heroPlayer;
    private Player antagonistPlayer;
    public Player currentPlayer;
    private Player winner;
    private Player loser;

    private Turn currentTurn;
    private boolean gameOver = false;

    public FightView fightView;
    public FightActivity fightActivity;

    private String announcementTop = "null announcement";
    private String announcementMiddle = "null middle";
    private String announcementBottom = "null";

    private Random randomizer;

    MediaPlayer soundEffectPlayer;

    public boolean playSound = true;

    /**
     * The constructor receives and sets the two Player objects, as well as the FightView class which displays the visual battle.
     * Then it prepares the sound effects and waits for the first Turn to be created when the user pushes the appropriate button.
     * @param heroPlayer
     * @param antagonistPlayer
     * @param fightView
     * @param context
     */
    public Fight(Player heroPlayer, Player antagonistPlayer, FightView fightView, Context context)
    {
        randomizer = new Random();

        this.fightView = fightView;
        fightActivity = (FightActivity) context;

        this.heroPlayer = heroPlayer;
        this.antagonistPlayer = antagonistPlayer;

        heroPlayer.setOpponent(antagonistPlayer);
        antagonistPlayer.setOpponent(heroPlayer);

        prepareNextSound();
    }



    private void prepareNextSound()
    {
        int noiseResourceID;
        int noiseIndex = randomizer.nextInt(2);

        if (noiseIndex == 0)
        {
            noiseResourceID = R.raw.boom;
        }
        else
        {
            noiseResourceID = R.raw.attack;
        }

        soundEffectPlayer = MediaPlayer.create(fightActivity, noiseResourceID);

        soundEffectPlayer.setVolume(0.47f, 0.47f);
        soundEffectPlayer.setLooping(false);
    }

    /**
     * When the user starts the battle, the FightActivity calls this method.
     * This method randomly chooses which of the two Players will get the first turn,
     * then calls nextTurn, which actually creates the first Turn object.
     */
    public void createFirstTurn()
    {
        int chooser = randomizer.nextInt(2);
        if (chooser==0)
        {
            currentPlayer = heroPlayer;
            nextTurn();
        }
        else {
            currentPlayer = antagonistPlayer;
            nextTurn();
        }
    }

    /**
     * This method creates each Turn object, then tells the currentPlayer that it is their turn.
     */
    public void nextTurn()
    {
        currentPlayer = currentPlayer.getOpponent();
        fightView.setCurrentPlayerName(currentPlayer.getName());
        currentTurn = null;
        currentTurn = new Turn(currentPlayer, this);
        currentPlayer.myTurn(true, currentTurn);

        if(currentPlayer == heroPlayer)
        {
            fightActivity.displayBattleButtons();
        }
        else
        {
            fightActivity.hideButtons();
            fightView.newTurnPauseCountdown();
        }

        if (playSound) {
            soundEffectPlayer.release();
            prepareNextSound();
        }
    }

    /**
     * This method is called when the user selects a BattleMove.
     * Attacks and defenses from the user both call this method, no matter whose Turn it is.
     * @param battleMoveName
     */
    public void performHeroBattleMove(String battleMoveName)
    {
        fightActivity.hideButtons();

        //find which FacePiece will perform the BattleMove
        boolean searchForPiece = true;
        FacePiece battlePiece = null;
        for (int i=0; i<heroPlayer.getPieces().size(); i++)
        {
            if (heroPlayer.getPieces().get(i).getBattleMove().contains(battleMoveName) && searchForPiece)
            {
                //if the piece is found, set the search for false
                searchForPiece = false;
                battlePiece = heroPlayer.getPieces().get(i);
                BattleMove battleMove = new BattleMove(battlePiece, heroPlayer);
                currentTurn.makeMove(battleMove);
            }
        }

        //This will never happen but I feel like creating a contingency plan anyway.
        if (battlePiece == null)
        {
            Toast.makeText(fightActivity, "No Face Piece Selected", Toast.LENGTH_LONG).show();
            nextTurn();
        }

        //announce the heroPlayer's choice of BattleMove
        announcementTop = heroPlayer.getName();
        announcementMiddle = "Responds With";

        if (heroPlayer == currentPlayer)
        {
            announcementMiddle = "Uses";
            allowResponse();
        }

        announcementBottom =  battleMoveName + "!";

        announce(announcementTop, announcementMiddle, announcementBottom);
    }

    /**
     * This method is always called after the first BattleMove in every Turn.
     *
     * If it is the user's turn (heroPlayer's Turn), this method decides whether the antagonistPlayer will make a response (2/3 chance that the antagonist will respond).
     *
     * If it is the antagonistPlayer's Turn, this method tells the fightView to display the heroPlayer's response buttons for the user.
     */
    public void allowResponse()
    {
        if (currentPlayer == heroPlayer) {
            int opponentResponseOption = randomizer.nextInt(3);
            if (opponentResponseOption != 0) {
                fightView.opponentResponseCountdown();
            } else {
                fightView.resolveTurnCountdown();
            }
        }
        else {
            fightView.countdownToResponsiveButtonCountdown();
        }
    }

    /**
     * This method randomly chooses an attack for the Antagonist
     */
    public void chooseAntagonistAttack()
    {
        ArrayList<FacePiece> attackingPieces = new ArrayList<FacePiece>();


        FacePiece attackingPiece;
        BattleMove move;

        for (int i=0; i<antagonistPlayer.getPieces().size(); i++)
        {
            if (antagonistPlayer.getPieces().get(i).isWeapon())
            {
                attackingPieces.add(antagonistPlayer.getPieces().get(i));
            }
        }

        //if there are no weaponized pieces, make a SMashPiece to go berserk!!
        if (attackingPieces.size() == 0)
        {
            FacePiece kamikazePiece = antagonistPlayer.makeKamikazePiece();
            attackingPieces.add(kamikazePiece);
        }

        if (attackingPieces.size() > 0) {
            int pieceIndex = randomizer.nextInt(attackingPieces.size());
            attackingPiece = attackingPieces.get(pieceIndex);
            move = new BattleMove(attackingPiece, currentPlayer);

            announcementTop = antagonistPlayer.getName();
            announcementMiddle = "Uses";
            announcementBottom = attackingPiece.getBattleMove() + "!";

            currentTurn.makeMove(move);
            allowResponse();
        }
        else {
            //If there are STILL no weaponized pieces, just end the turn and display a message.
            // but I programmed the "makeKamikazePiece" method so that will never happen.
            announcementTop = antagonistPlayer.getName();
            announcementMiddle = "";
            announcementBottom = "Has No Weapons!";
            finishTurn();
        }

        announce(announcementTop, announcementMiddle, announcementBottom);
    }

    /**
     * This method makes an ArrayList out of the opponent's responsive FacePieces,
     * then randomly chooses one of those pieces for the responding BattleMove.
     */
    public void chooseAntagonistResponse()
    {
        ArrayList<FacePiece> respondingPieces = new ArrayList<FacePiece>();

        for (int i=0; i<antagonistPlayer.getPieces().size(); i++)
        {
            if (antagonistPlayer.getPieces().get(i).isResponsive())
            {
                respondingPieces.add(antagonistPlayer.getPieces().get(i));
            }
        }

        if (respondingPieces.size() > 0) {
            int pieceSelection = randomizer.nextInt(respondingPieces.size());
            FacePiece respondingPiece = respondingPieces.get(pieceSelection);
            BattleMove respondingMove = new BattleMove(respondingPiece, antagonistPlayer);
            currentTurn.makeMove(respondingMove);
            announcementTop = antagonistPlayer.getName();
            announcementMiddle =  "Responds With";
            announcementBottom =  respondingPiece.getBattleMove() + "!";
            announce(announcementTop, announcementMiddle, announcementBottom);
        }

        fightView.resolveTurnCountdown();
    }

    /**
     * This method receives information from the Turn object about which FacePieces lost HP.
     * Then this method sends that information to the FightView object to be displayed on the screen.
     * @param damageToDefendingPiece
     * @param damageToAttackingPiece
     */
    public void announceDamage(int damageToDefendingPiece, int damageToAttackingPiece, FacePiece defendingPiece, FacePiece attackingPiece)
    {
        //The MediaPlayer (soundEffectPlayer) will play a sound to accompany the damage announcement.
        //The damaged piece should flash as the sound plays for a full battle experience!
        if (playSound) {
            soundEffectPlayer.start();
        }

        //create some local variables to hold the damaged FacePieces and the amount of damage for them to receive.
        int heroDamage;
        int antagonistDamage;
        FacePiece heroPiece;
        FacePiece antagonistPiece;

        if (currentPlayer == heroPlayer)
        {
            heroDamage = damageToAttackingPiece;
            antagonistDamage = damageToDefendingPiece;

            heroPiece = attackingPiece;
            antagonistPiece = defendingPiece;
        } else {
            heroDamage = damageToDefendingPiece;
            antagonistDamage = damageToAttackingPiece;

            heroPiece = defendingPiece;
            antagonistPiece = attackingPiece;
        }

        fightView.damageAnnouncementCountdown(heroDamage, antagonistDamage, heroPiece, antagonistPiece);
    }

    /**
     * This method receives information from the Turn object about which FacePieces gained HP.
     * Then this method sends that information to the FightView object to be displayed on the screen.
     * @param attackingHealthBenefit
     * @param defendingHealthBenefit
     */
    public void announceHealthBenefit(int attackingHealthBenefit, int defendingHealthBenefit) {
        int heroHealthBenefit = 0;
        int antagonistHealthBenefit = 0;

        if (currentPlayer == heroPlayer)
        {
            heroHealthBenefit = attackingHealthBenefit;
            antagonistHealthBenefit = defendingHealthBenefit;
        } else {
            heroHealthBenefit = defendingHealthBenefit;
            antagonistHealthBenefit = attackingHealthBenefit;
        }

        fightView.healthBenefitAnnouncementCountdown(heroHealthBenefit, antagonistHealthBenefit);
    }

    /**
     * This method receives text Strings and sends those Strings to the fightView to be displayed on the screen.
     * @param announcementTop
     * @param announcementMiddle
     * @param announcementBottom
     */
    public void announce(String announcementTop, String announcementMiddle, String announcementBottom)
    {
        fightView.makeAnnouncement(announcementTop, announcementMiddle, announcementBottom);
    }


    /**
     * If a Player object has lost all its FacePieces then it calls this method
     * to let the Fight know that the Player is destroyed.
     *
     * This method also sets the gameOver boolean to TRUE,
     * so when the Turn object calls this class's finishTurn method,
     * it will initiate the endgameCountdown to end the Fight.
     * @param destroyedPlayer
     */
    public void playerDestroyed(Player destroyedPlayer)
    {
        if (!gameOver) {
            gameOver = true;
            loser = destroyedPlayer;
            winner = destroyedPlayer.getOpponent();
        }
    }

    /**
     * After the Turn object has resolved all the BattleMove effects,
     * it calls this method to end the turn so a new one can be created.
     */
    public void finishTurn()
    {
        if (!gameOver) {
            fightView.newTurnCountdown();
        } else {
            announce(loser.getOpponent().getName(), "Has", "Won!");
            fightView.endgameCountdown();
        }
    }

    /**
     * All the game logic has been performed by now,
     * and this method just wraps everything up and shuts down the fightActivity.
     */
    public void endgame()
    {
        winner.win();
        loser.lose();

        //The heroStatus is saved and sent to the next Activity, so appropriate music can be played...
        String heroStatus = "winner";
        if (heroPlayer == loser)
        {
            heroStatus = "loser";
        }

        heroPlayer.saveCharacter(fightActivity);

        Intent endGameIntent = new Intent(fightActivity, CharacterSummaryActivity.class);
        endGameIntent.putExtra("GameOver", true);
        endGameIntent.putExtra("HeroStatus", heroStatus);
        endGameIntent.putExtra("PLAY_MUSIC", fightActivity.playMusic);

        fightActivity.startActivity(endGameIntent);
        fightActivity.finish();
    }

    public void resetBattleButtons()
    {
        fightActivity.setupBattleButtons();
    }

    public void toggleSound()
    {
        playSound = !playSound;
    }

    public Turn getTurn()
    {
        return currentTurn;
    }
}
