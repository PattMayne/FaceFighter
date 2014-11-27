package com.pattmayne.facefighter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * The first Activity to be called, and the first thing that the user sees.
 * This Activity is also called again after every Fight is completed.
 * This class starts the music and displays the initial options for the user:
 * The user can either LOAD their previously saved FaceCharacter, or CREATE a new FaceCharacter.
 *
 * There are also hidden options to allow a developer to access a screen which can create new opponents for a user to fight.
 */
public class MainActivity extends ActionBarActivity {

    //variables
    Intent musicIntent;
    public boolean playMusic = true;

    /**
     * Setup the display and prepare any necessary music.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", playMusic);

        //This hides a button Devs can use to make new Opponent faces.
        findViewById(R.id.button_create_badguy_face).setVisibility(View.GONE);

        startMusic();
    }

    /**
     * An Intent is used to call the MusicPlayer Service, if music should be played.
     */
    private void startMusic()
    {
        //If the user has turned off the music, each Activity is responsible for informing and being informed about that decision.
        //That information is transmitted via the intents that call new activities.
        if (playMusic) {
            musicIntent = new Intent();
            musicIntent.setAction("com.pattmayne.MUSICPLAYER");
            musicIntent.putExtra("TYPE", "fightMusic");
            musicIntent.putExtra("PLAY_MUSIC", playMusic);
            startService(musicIntent);
        }
    }

    /**
     * This method starts an activity which allows the user to create a new FaceCharacter.
     * A button, defined in the XML layout, calls this method.
     * @param view
     */
    public void createFace(View view)
    {
        Intent intent = new Intent(this, FaceBuilderActivity.class);
        intent.putExtra("PLAY_MUSIC", playMusic);
        startActivityForResult(intent, 1);
        finish();
    }

    /**
     * This method starts an activity which loads the previously saved FaceCharacter.
     * First we must check the database to see if it is empty, or if it holds a FaceCharacter.
     * A button, defined in the XML layout, calls this method.
     * @param view
     */
    public void loadFace(View view)
    {
        //call up the database and see if it has any information
        FaceDBHelper helper = new FaceDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String count = "SELECT count(*) FROM face_table";
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int tableCount = cursor.getInt(0);

        if (tableCount == 0)
        {
            //If there are no FaceCharacters saved, inform the user and do nothing else.
            Toast.makeText(this, "No Saved Face", Toast.LENGTH_LONG).show();
        } else {
            //If there IS a FaceCharacter saved to the database, start an Activity to display information about the FaceCharacter.
            Intent intent = new Intent(this, CharacterSummaryActivity.class);
            intent.putExtra("PLAY_MUSIC", playMusic);
            startActivityForResult(intent, 1);
            finish();
        }
    }

    /**
     * This method starts an activity which allows the user to create a new, opponent FaceCharacter.
     * A button, defined in the XML layout, calls this method.
     * The button should be hidden (GONE), so only a developer can access it by altering the code.
     * @param view
     */
    public void createBadguy(View view)
    {
        Intent intent = new Intent(this, BadguyBuilderActivity.class);
        startActivityForResult(intent, 1);
        intent.putExtra("PLAY_MUSIC", playMusic);
        finish();
    }


    /**
     * This method handles selections from the menu bar.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            //Call the "About" screen.
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);

            return true;
        } else if (id == R.id.action_toggle_music)
        {
            //Toggle switch for playing music. This decision should persist while any Activities are active.
            playMusic = !playMusic;
            Intent toggleMusicIntent = new Intent();
            toggleMusicIntent.setAction("com.pattmayne.MUSICPLAYER");
            toggleMusicIntent.putExtra("PLAY_MUSIC", playMusic);
            toggleMusicIntent.putExtra("TYPE", "fightMusic");
            startService(toggleMusicIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method STOPS the music and closes the Activity.
     * It pretty much shuts down the entire app.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        playMusic = false;
        Intent stopMusicIntent = new Intent();
        stopMusicIntent.setAction("com.pattmayne.MUSICPLAYER");
        stopMusicIntent.putExtra("PLAY_MUSIC", playMusic);
        stopMusicIntent.putExtra("TYPE", "no music");
        startService(stopMusicIntent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
