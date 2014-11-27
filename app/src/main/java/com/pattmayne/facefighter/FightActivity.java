package com.pattmayne.facefighter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * As the Fight (battle) is taking place, this Activity displays the FightView (which displays the animations and Fight-related information)
 * This Activity also shows a sidebar View where the user can press buttons to initiate attacks (BattleMoves).
 * This Activity needs to retrieve the antagonist and location from the FightInfo class, and re-create the heroPlayer from a database.
 */
public class FightActivity extends ActionBarActivity {

    //variables
    private FightInfo fightInfo;
    private FightView fightView;
    private Fight fight;

    private FaceCharacter antagonist;
    private FaceCharacter hero;

    private Player heroPlayer;
    private Player antagonistPlayer;

    private Drawable location;
    private ArrayList<Button> battleButtons;
    private ArrayList<Button> responsiveButtons;

    public boolean playMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        fightView = (FightView) findViewById(R.id.fight_view);
        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", true);
        setupFight();
    }

    /**
     * This method retrieves the selections from the users (stored and sent as integers),
     * and uses those integer-selections to retrieve the appropriate antagonist and location from the fightInfo object.
     */
    private void setupFight()
    {
        FaceFactory faceFactory = new FaceFactory(fightView, this);
        fightInfo = new FightInfo(this);
        fightInfo.createAntagonistsAndLocations(fightView);

        int[] locationAndOpponent = getIntent().getExtras().getIntArray("location_and_opponent");

        //retrieve and set the location and both FaceCharacters.
        location = fightInfo.getLocation(locationAndOpponent[0]);
        antagonist = fightInfo.getAntagonist(locationAndOpponent[1]);
        hero = faceFactory.reviveCharacter();

        battleButtons = new ArrayList<Button>();
        responsiveButtons = new ArrayList<Button>();

        //Create two Player objects based on the FaceCharacter objects.
        heroPlayer = new Player(hero);
        antagonistPlayer = new Player(antagonist);

        fight = new Fight(heroPlayer, antagonistPlayer, fightView, this);

        fightView.setHeroPlayer(heroPlayer);
        fightView.setAntagonistPlayer(antagonistPlayer);
        fightView.setLocation(location);

        setupBattleButtons();
    }

    /**
     *Initiates the actual fight, after the soundEffectPlayer presses the button.
     */
    public void startFight(View view)
    {
        Button fightButton = (Button) findViewById(R.id.start_fight_button);
        fightButton.setVisibility(View.GONE);
        fight.createFirstTurn();
    }

    /**
     * This method creates a list of available Buttons,
     * then finds out how many offensive and defensive BattleMoves the user has available,
     * then assigns a Button to each BattleMove.
     *
     * This method creates an ArrayList for offensive attack Buttons (battleButtons),
     * and a second ArrayList for defensive Buttons (responsiveButtons).
     */
    public void setupBattleButtons()
    {
        battleButtons.clear();
        responsiveButtons.clear();
        //Create a temporary ArrayList with all the available buttons
        ArrayList<Button> allButtons = new ArrayList<Button>();
        allButtons.add((Button) findViewById(R.id.battle_button_1));
        allButtons.add((Button) findViewById(R.id.battle_button_2));
        allButtons.add((Button) findViewById(R.id.battle_button_3));
        allButtons.add((Button) findViewById(R.id.battle_button_4));
        allButtons.add((Button) findViewById(R.id.battle_button_5));
        allButtons.add((Button) findViewById(R.id.battle_button_6));

        for (int i=0; i<allButtons.size(); i++)
        {
            allButtons.get(i).setVisibility(View.GONE);
        }

        //Any FacePiece with a BattleMove is either added to the battleButtons list, or the responsiveButtons list.
        for (int i=0; i<heroPlayer.getPieces().size(); i++)
        {
            FacePiece piece = heroPlayer.getPieces().get(i);

            if(!piece.getBattleMove().contains("none") && !piece.isResponsive())
            {
                battleButtons.add(allButtons.get(i));
                allButtons.get(i).setText(piece.getBattleMove());
            }
            else if (!piece.getBattleMove().contains("none") && piece.isResponsive())
            {
                responsiveButtons.add(allButtons.get(i));
                allButtons.get(i).setText(heroPlayer.getPieces().get(i).getBattleMove());
            }
        }

        //If there are no offensive attacks available to the user, a non-offensice FacePiece is converted into a "Kamikaze Piece."
        if (battleButtons.size() == 0 && heroPlayer == fight.currentPlayer)
        {
            FacePiece smashPiece = heroPlayer.makeKamikazePiece();
            if (smashPiece != null)
            {
                Button smashButton = allButtons.get(allButtons.size() - 1);
                smashButton.setText(smashPiece.getBattleMove());
                battleButtons.add(smashButton);
            } else
            {
                fightView.makeAnnouncement(heroPlayer.getName(), "", "Cannot Attack!");
                fight.finishTurn();
            }
        }
    }

    /**
     * Displays the buttons for each available BattleMove(when it's the soundEffectPlayer's turn).
     */
    public void displayBattleButtons()
    {
        setupBattleButtons();
        displayButtons(battleButtons);
    }

    /**
     * Displays the buttons for each available responsive BattleMove(when the soundEffectPlayer has been attacked)
     */
    public void displayResponsiveButtons()
    {
        setupBattleButtons();
        displayButtons(responsiveButtons);
    }

    /**
     * Displays whichever list of Buttons has been provided.
     * @param buttonList
     */
    private void displayButtons(ArrayList<Button> buttonList)
    {
        for (int i=0; i< buttonList.size(); i++)
        {
            buttonList.get(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hides ALL the buttons!
     */
    public void hideButtons()
    {
        for (int i=0; i< battleButtons.size(); i++)
        {
            battleButtons.get(i).setVisibility(View.GONE);
        }

        for (int i=0; i< responsiveButtons.size(); i++)
        {
            responsiveButtons.get(i).setVisibility(View.GONE);
        }
    }

    /**
     * When the user selects a BattleMove from the list of Buttons on the screen,
     * this method is called with information about which BattleMove the user selected.
     * Then this method tells the Fight object to perform the selected BattleMove.
     * @param callingButton
     */
    public void performBattleMove(View callingButton)
    {
        Button button = (Button) callingButton;
        String buttonText = button.getText().toString();
        fight.performHeroBattleMove(buttonText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fight, menu);
        return true;
    }

    /**
     * Menu options.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_toggle_music)
        {
            //Turns the music on or off.
            playMusic = !playMusic;
            Intent toggleMusicIntent = new Intent();
            toggleMusicIntent.setAction("com.pattmayne.MUSICPLAYER");
            toggleMusicIntent.putExtra("PLAY_MUSIC", playMusic);
            toggleMusicIntent.putExtra("TYPE", "fightMusic");
            startService(toggleMusicIntent);

            return true;
        } else if (id == R.id.action_toggle_sound)
        {
            //Turns the in-game sounds on or off.
            fight.toggleSound();
        }

        return super.onOptionsItemSelected(item);
    }

    public Fight getFight()
    {
        return fight;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startScreenIntent = new Intent(this, MainActivity.class);
        startScreenIntent.putExtra("PLAY_MUSIC", playMusic);
        startActivity(startScreenIntent);
        finish();
    }

}
