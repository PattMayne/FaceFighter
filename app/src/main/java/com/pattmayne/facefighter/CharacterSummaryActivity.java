package com.pattmayne.facefighter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This Activity is called after a FaceCharacter has been created,
 * and called again just after a Fight is finished.
 *
 * This Activity displays information about the FaceCharacter which is saved to the database.
 */
public class CharacterSummaryActivity extends Activity {

    //variables

    private FaceCharacter character;
    private CharacterSummaryView characterSummaryView;

    private boolean gameOver = false;
    private String heroStatus = "winner";

    public boolean playMusic = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_summary);

        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", playMusic);
        gameOver = getIntent().getBooleanExtra("GameOver", false);
        characterSummaryView = (CharacterSummaryView)findViewById(R.id.characterSummaryView);

        instantiateVariables();
    }

    /**
     * This method makes the call to display information about the FaceCharacter,
     * plays the appropriate music,
     * and also decides which button needs to be displayed:
     * (either the button which starts the Fight, or the button which leads back to the start screen).
     */
    private void instantiateVariables()
    {
        character = characterSummaryView.getCharacter();
        setupInfoScreen();
        Button launchButton = (Button) findViewById(R.id.character_view_variable_launch_button);

        if (gameOver)
        {
            launchButton.setText("Okay");

            heroStatus = getIntent().getStringExtra("HeroStatus");
            Intent musicIntent = new Intent();
            musicIntent.putExtra("TYPE", heroStatus);
            musicIntent.putExtra("PLAY_MUSIC", playMusic);
            musicIntent.setAction("com.pattmayne.MUSICPLAYER");
            startService(musicIntent);
        }
        else
        {
            launchButton.setText("Fight!");
        }
    }

    /**
     * This method retrieves a string of information about the FaceCharacter,
     * and displays that information on the screen.
     */
    private void setupInfoScreen()
    {
        TextView characterNameLabel = (TextView) findViewById(R.id.character_name_label);
        characterNameLabel.setText(character.getName());

        TextView characterInfoLabel = (TextView) findViewById(R.id.character_info_text_label);
        characterInfoLabel.setText(character.makeStatsString());
    }

    public void launchNextActivity(View view)
    {
        if (gameOver)
        {
            backToStartScreen();
        }
        else
        {
            prepareToFight();
        }
    }

    public void backToStartScreen()
    {
        Intent finishingIntent = new Intent(this, MainActivity.class);
        finishingIntent.putExtra("PLAY_MUSIC", playMusic);
        startActivity(finishingIntent);
        finish();
    }

    public void prepareToFight()
    {
        Intent fightIntent = new Intent(this, FightPrepActivity.class);
        fightIntent.putExtra("PLAY_MUSIC", playMusic);
        startActivity(fightIntent);
        finish();
    }

     @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backToStartScreen();
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

}
