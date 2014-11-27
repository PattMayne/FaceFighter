package com.pattmayne.facefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * This class displays the visuals for every Fight.
 * It shows each Player's FaceCharacter (the user and their opponent), the location background,
 * and animates the battle.
 *
 * This View has its own built-in Thread which handles the animation.
 * That Thread is so important to the game that I decided to use this class to handle the game's timing (countdowns between sequential events).
 * Created by Matt on 2014-11-02.
 */
public class FightView extends View {

    //variables
    //the antagonistPlayer is the "bad guy" who the user will fight.
    //The user is (obviously) the heroPlayer.
    private Player antagonistPlayer;
    private Player heroPlayer;
    private FacePiece damagedHeroPiece;
    private FacePiece damagedAntagonistPiece;

    //the location is just a picture of a location.
    private Drawable location;
    private String currentPlayerName = "null";

    //character locations on the screen.
    //Each FacePiece will basically use this point as its zero-point, drawing itself relative to that point.
    //These are the default points, but we choose more dynamic ones during initialization.
    private int[] heroLocation = {14, 5};
    private int[] antagonistLocation = {704, 5};

    private boolean canvasSet = false;

    //some variables to control the animation and movement of the FaceCharacter images
    private int locationAnimationTicker = 0;
    private boolean locationAnimationDirectionSwitch = true;
    //"animationToggle" switches on/off for every thread iteration, so the animation is slower (it's too fast otherwise)
    private boolean animationToggle = false;

    //these boolean variables can be switched on to temporarily display visual information,
    //OR to allow a countdown and prepare the Fight object for the next sequence in the battle.
    private boolean timeToAnnounce = false;
    private boolean showResponseMoves = false;
    private boolean opponentResponseSwitch = false;
    private boolean resolveTurn = false;
    private boolean newTurn = false;
    private boolean pauseNewTurn = false;
    private boolean announceDamage = false;
    private boolean announceHealthBenefit = false;
    private boolean flashDamagedPiece = false;
    private boolean endgame = false;
    private boolean gameStarted = false;

    //Lots of timers to temporarily display visual information,
    //OR to countdown and prepare the Fight object for the next sequence in the battle.
    private int endgameTimer = 0;
    private int newTurnTimer = 0;
    private int pauseNewTurnTimer = 0;
    private int announcementTimer = 0;
    private int responseTimer = 0;
    private int resolveTurnTimer = 0;
    private int damageAnnouncementTimer = 0;
    private int healthBenefitAnnouncementTimer = 0;
    private int countdownToResponsiveButtonsTimer = 0;
    //When a FacePiece is damaged it will flash for a small amount of time.
    //The flashDamagedPieceTimer counts down how long a piece will flash,
    //while the Switch slows that animation down (otherwise it flashes too fast).
    private int flashDamagedPieceTimer = 0;
    private int flashDamagedPieceSwitch = 0;

    //The FightView needs to know how much damage and healthBenefit (bonus/absorbed HP) to display on-screen.
    //These variables hold that information.
    private int heroDamage = 0;
    private int antagonistDamage = 0;
    private int heroHealthBenefit = 0;
    private int antagonistHealthBenefit = 0;

    //An in-game announcement will often be necessary.
    //These three variables hold the text strings which should be displayed.
    private String announcementTop = "primary null announcement";
    private String announcementMiddle = "null middle";
    private String announcementBottom = "null";

    private Paint paint;
    private Context context;
    private FightActivity fightActivity;
    private int currentPlayerNameColor = getResources().getColor(R.color.thirdGreen);

    //These variables are specific to the drawCharacter method.
    //We declare them now so they don't have to constantly be re-declared.
    int x;
    int y;
    int damageToAnnounce = 0;
    int healthBenefitToAnnounce = 0;
    boolean drawPiece = true;
    ArrayList<FacePiece> playerPieceList;

    //I put the constructors at the bottom to avoid clutter at the top.
    //They each call the instantiateVariables() method, and set the context variable.

    /**
     * Prepare the screen for the upcoming Fight.
     */
    private void instantiateVariables() {
        paint = new Paint();
        fightActivity = (FightActivity) context;
        Typeface tf = Typeface.create("Arial", Typeface.BOLD);
        paint.setTypeface(tf);
        }

    /**
     * This method sets the user's character as the heroPlayer.
     * @param heroPlayer
     */
    public void setHeroPlayer(Player heroPlayer) {
        this.heroPlayer = heroPlayer;
        startGame();
    }

    /**
     * This method sets the "bad guy" soundEffectPlayer.
     * @param antagonistPlayer
     */
    public void setAntagonistPlayer(Player antagonistPlayer) {
        this.antagonistPlayer = antagonistPlayer;
        startGame();
    }

    /**
     * set the background image / location.
     * @param location
     */
    public void setLocation(Drawable location) {
        this.location = location;
        setBackground(location);
        startGame();
    }

    /**
     * For every subsequent Turn object there will be a different "current soundEffectPlayer."
     * This information must always be known so it can be displayed.
     * This method sets that information at the beginning of each turn.
     * @param name
     */
    public void setCurrentPlayerName(String name)
    {
        currentPlayerName = name;
        startGame();
    }

    /**
     * The Fight cannot begin until we have two players and a location.
     */
    private void startGame()
    {
        if (heroPlayer != null && antagonistPlayer != null && location != null && currentPlayerName != "null")
        {
            gameStarted = true;
        }
    }

    /**
     * This View object has a thread which constantly calls this onDraw method.
     * For every iteration of the Thread, this method decides what information must be drawn onto the Canvas.
     * That includes all the FacePieces from each Player, plus the location (background) and any necessary text.
     *
     * This method checks many boolean conditions to decide what needs to be drawn.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (gameStarted) {

            //At the top of the screen, show the user whose turn it is.
            paint.setColor(currentPlayerNameColor);
            paint.setTextSize(87);
            canvas.drawText(currentPlayerName + "'s Turn", (canvas.getWidth() / 7), 70, paint);
        }
        else if (!canvasSet)
        {
            //Before the game starts, set up the Players' locations

            heroLocation[0] = 1;
            heroLocation[1] = -5;

            antagonistLocation[0] = canvas.getWidth() - (canvas.getWidth() / 2);
            antagonistLocation[1] = -10;

            canvasSet = true;
        }

        drawCharacter(canvas, heroPlayer);
        drawCharacter(canvas, antagonistPlayer);

        //If there is information to display, display it.
        if (timeToAnnounce) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(77);
            canvas.drawText(announcementTop, (canvas.getWidth() / 3) - 4, 192, paint);
            paint.setTextSize(55);
            canvas.drawText(announcementMiddle, (canvas.getWidth()/3) + 11, 272, paint);
            paint.setTextSize(74);
            canvas.drawText(announcementBottom, (canvas.getWidth()/3) + 4, 372, paint);
            announcementTimerTicker();
        }

        //Check other conditionals.
        onDrawConditionals();

        //Move the FacePiece images to the next place in their constant animation.
        characterLocationAnimations();

        //We must invalidate so that the Thread will call onDraw again upon the next iteration.
        invalidate();
    }

    /**
     * This method contains certain calls which may or may not be used, based on boolean conditions.
     * These conditional methods decide which information must be displayed, plus when the next event of the Fight will occur.
     * I put them in this separate method, merely to maintain the readability of the onDraw class.
     */
    private void onDrawConditionals() {

        //when the opponent attacks,
        //the user has a short time period to press a button and initiate a responsive move.
        if (showResponseMoves) {
            responseTimer--;
            if (responseTimer <= 0) {
                showResponseMoves = false;
                fightActivity.hideButtons();
                fightActivity.getFight().getTurn().resolveBattleSequence();
            }
        }
            //Wait a reasonable amount of time BEFORE showing the buttons
        if (countdownToResponsiveButtonsTimer > 0)
        {
            countdownToResponsiveButtonsTimer--;
            if (countdownToResponsiveButtonsTimer == 0)
            {
                responsiveButtonsCountdown();
            }
        }

        //When the user initiates an attack on their Turn,
        //the opponent may make a response. This creates a countdown so the response happens after a reasonable amount of time.
        if (opponentResponseSwitch) {
            responseTimer--;
            if (responseTimer <= 0) {
                opponentResponseSwitch = false;
                fightActivity.getFight().chooseAntagonistResponse();
            }
        }

        //After all BattleMoves (attacks and responses) have been chosen,
        //we countdown a reasonable amount of time before resolving and displaying their effects.
        if (resolveTurn) {
            resolveTurnTimer--;
            if (resolveTurnTimer <= 0) {
                resolveTurn = false;
                fightActivity.getFight().getTurn().resolveBattleSequence();
            }
        }

        //After the current Turn has been fully resolved,
        //we wait a reasonable amount of time before beginning the next turn.
        if (newTurn) {
            newTurnTimer--;
            if (newTurnTimer <= 0) {
                newTurn = false;
                fightActivity.getFight().nextTurn();
            }
        }

        //At the BEGINNING of each new turn, we wait a reasonable amount of time
        //before the "bad guy" soundEffectPlayer can choose their attack.
        //(Only used if it is NOT the user's Turn).
        if (pauseNewTurn)
        {
            pauseNewTurnTimer--;
            if (pauseNewTurnTimer <= 0)
            {
                pauseNewTurn = false;
                fightActivity.getFight().chooseAntagonistAttack();
            }
        }

        //If the game is over and there is a winner and a loser,
        //we wait a reasonable amount of time before we actually end the game.
        if (endgame)
        {
            endgameTimer--;
            if (endgameTimer <= 0)
            {
                endgame = false;
                fightActivity.getFight().endgame();
            }
        }
    }

    /**
     * Draw each Bitmap image for each FacePiece.
     * This method calls up all FacePiece objects from the appropriate Player,
     * and paints their pictures in the right locations,
     * unless they are flashing from damage.
     * @param canvas
     * @param player
     */
    private void drawCharacter(Canvas canvas, Player player) {

        //Retrieve Player-specific information about what to draw, and where to draw it.
        setInformationToDraw(player);

        //Take all that information and draw it!
        for (int i = 0; i < playerPieceList.size(); i++) {
            FacePiece piece = playerPieceList.get(i);
            Bitmap pic = piece.getPic();
            //pieceX and pieceY take user and opponent's start location and set each of their FacePiece objects relative to that.
            int pieceX = x + piece.getPicLocation()[0];
            int pieceY = y + piece.getPicLocation()[1];

            drawPiece = true;

            //conditions for when to NOT drawPiece (so we can have flashing damaged pieces)
            //This has multiple layers of logic,
            //because when a damaged FacePiece is "flashing," it must still be drawn for some of the iterations.
            //(Otherwise it's not flashing... it would just be invisible).
            if (flashDamagedPiece)
            {
                if (piece == damagedAntagonistPiece || piece == damagedHeroPiece)
                {
                    drawPiece = false;

                    if (flashDamagedPieceSwitch < 6) {
                        drawPiece = true;
                    }
                }
            }

            //This is the most important part of the method: where we draw the actual FacePiece on the canvas.
            if (drawPiece)
            {
                canvas.drawBitmap(pic, pieceX, pieceY, paint);
            }
        }

        drawCharacterConditionals(canvas);
    }

    /**
     * Retrieve Player-specific information about what to draw, and where to draw it.
     * @param player
     */
    private void setInformationToDraw(Player player)
    {
        playerPieceList = player.getPieces();

        if (player == antagonistPlayer) {
            x = antagonistLocation[0];
            y = antagonistLocation[1];
            damageToAnnounce = antagonistDamage;
            healthBenefitToAnnounce = antagonistHealthBenefit;
        } else if (player == heroPlayer) {
            x = heroLocation[0];
            y = heroLocation[1];
            damageToAnnounce = heroDamage;
            healthBenefitToAnnounce = heroHealthBenefit;
        } else {
            x = 0;
            y = 0;
        }
    }

    /**
     * This is called on every iteration of the Thread to see if there is any Damage or HealthBenefit to display for either Player.
     * ALSO, this checks to see if a FacePiece should flash to show damage.
     * @param canvas
     */
    private void drawCharacterConditionals(Canvas canvas)
    {
        if (announceDamage && damageToAnnounce > 0)
        {
            flashDamagedPieceTimer--;

            //the flashDamagedPieceSwitch runs from 0 - 12,
            // but the FacePiece is only drawn during 0 - 5.
            //That means it is drawn slightly less than half the time,
            // with a "draw / don't draw" phase lasting several iterations.
            if (flashDamagedPieceTimer > 0) {
                flashDamagedPieceSwitch++;
            } else {flashDamagedPiece = false;}

            if (flashDamagedPieceSwitch > 12)
            {
                flashDamagedPieceSwitch = 0;
            }

            // Display visual information about damage inflicted, or HP absorbed.
            damageAnnouncementTimer--;
            paint.setTextSize(122);
            paint.setColor(Color.RED);
            canvas.drawText("-" + damageToAnnounce + " HP!", x + 5, y + 230, paint);

            if (damageAnnouncementTimer <= 0)
            {
                announceDamage = false;
                flashDamagedPiece = false;
                antagonistDamage = 0;
                heroDamage = 0;
            }
        }

        if (announceHealthBenefit && healthBenefitToAnnounce > 0)
        {
            healthBenefitAnnouncementTimer--;
            paint.setTextSize(142);
            paint.setColor(getResources().getColor(R.color.yellow));
            canvas.drawText("+" + healthBenefitToAnnounce + " HP!", x + 37, y + 650, paint);

            if (healthBenefitAnnouncementTimer <= 0)
            {
                announceHealthBenefit = false;
            }
        }
    }

    /**
     * Decide whether the FaceCharacter image moves,
     * based on a toggle-switch boolean.
     * The FaceCharacter should only move half the time, to slow the animation down.
     */
    private void characterLocationAnimations() {
        if (animationToggle) {
            changeCharacterLocations();
        }
        animationToggle = !animationToggle;
    }

    //move the FaceCharacter image up or down, for a fun and active-looking animation
    private void changeCharacterLocations() {
        if (locationAnimationDirectionSwitch) {
            locationAnimationTicker++;
        } else {
            locationAnimationTicker--;
        }

        // While the user's FaceCharacter bobs up, their opponent should bob down.
        heroLocation[1] += locationAnimationTicker;
        antagonistLocation[1] -= locationAnimationTicker;

        if (locationAnimationTicker == 5 || locationAnimationTicker == -5) {
            locationAnimationDirectionSwitch = !locationAnimationDirectionSwitch;
        }
    }

    private void announcementTimerTicker() {
        announcementTimer--;
        if (announcementTimer == 0) {
            timeToAnnounce = false;
        }
    }

    //The following Countdown methods are called by other objects to initiate different countdowns.
    //Those countdowns will either display important information to the user,
    //or allow a reasonable amount of time to pass before initiating the next sequential event in the Fight.

    /**
     * This method displays the most general announcements, with three levels of String text on top of each other.
     */
    public void makeAnnouncement(String announcementTop, String announcementMiddle, String announcementBottom) {
        this.announcementTop = announcementTop;
        this.announcementMiddle = announcementMiddle;
        this.announcementBottom = announcementBottom;
        timeToAnnounce = true;
        announcementTimer = 133;
    }

    //Wait a reasonable amount of time before the "bad guy" responds to the user's attack (BattleMove).
    public void opponentResponseCountdown() {
        responseTimer = 101;
        opponentResponseSwitch = true;
    }

    //Wait a reasonable amount of time before resolving all the effects of this Turn's BattleMoves.
    public void resolveTurnCountdown() {
        resolveTurn = true;
        resolveTurnTimer = 130;
    }

    //Wait a reasonable amount of time before starting the next Turn.
    public void newTurnCountdown() {
        newTurn = true;
        newTurnTimer = 77;
    }

    //Wait a reasonable amount of time before the user's responsive/defensive buttons are displayed.
    public void countdownToResponsiveButtonCountdown()
    {
        countdownToResponsiveButtonsTimer = 43;
    }

    //Display the user's responsive/defensive buttons for a reasonable amount of time.
    public void responsiveButtonsCountdown()
    {
        fightActivity.displayResponsiveButtons();
        showResponseMoves = true;
        responseTimer = 47;
    }

    //Wait a reasonable amount of time at the start of a Turn before the action starts.
    public void newTurnPauseCountdown()
    {
        pauseNewTurn = true;
        pauseNewTurnTimer = 169;
    }

    public void endgameCountdown()
    {
        endgameTimer = 140;
        endgame = true;
    }

    /**
     * This method allows the onDraw method to display any damage done to the characters, and animates the injured FacePieces.
     * This method is called by other objects, telling the FightView what to display.
     * @param heroDamage
     * @param antagonistDamage
     * @param heroPiece
     * @param antagonistPiece
     */
    public void damageAnnouncementCountdown(int heroDamage, int antagonistDamage, FacePiece heroPiece, FacePiece antagonistPiece)
    {
        damageAnnouncementTimer = 290;
        announceDamage = true;
        this.heroDamage = heroDamage;
        this.antagonistDamage = antagonistDamage;
        damagedAntagonistPiece = null;
        damagedHeroPiece = null;

        if (antagonistDamage > 0)
        {
            damagedAntagonistPiece = antagonistPiece;
            flashDamagedPiecesCountdown();
        }

        if (heroDamage > 0)
        {
            damagedHeroPiece = heroPiece;
            flashDamagedPiecesCountdown();
        }
    }

    public void healthBenefitAnnouncementCountdown(int heroHealthBenefit, int antagonistHealthBenefit)
    {
        healthBenefitAnnouncementTimer = 290;
        announceHealthBenefit = true;
        this.heroHealthBenefit = heroHealthBenefit;
        this.antagonistHealthBenefit = antagonistHealthBenefit;
    }

    private void flashDamagedPiecesCountdown()
    {
        flashDamagedPieceTimer = 122;
        flashDamagedPiece = true;
    }

    //These three constructor methods are at the bottom just to avoid clutter at the top.

    public FightView(Context context) {
        super(context);
        this.context = context;
        instantiateVariables();
    }

    public FightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        instantiateVariables();
    }

    public FightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        instantiateVariables();
    }
}
