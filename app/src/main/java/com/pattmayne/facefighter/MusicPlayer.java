package com.pattmayne.facefighter;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class extends the Service class and is responsible for playing in-game music.
 * A Service runs in the background and its lifecycle does not depend on any single Activity.
 * Created by Matt on 2014-11-18.
 */
public class MusicPlayer extends Service{

    //variables

    Intent intentThatCalled;
    String musicType = "No Music";
    boolean playMusic = true;

    int[] battleTracks = {0,0,0};
    int currentTrack = 0;

    //When we want to create a new music soundEffectPlayer,
    // we call the MediaPlayer class's static create() method, instead of a regular constructor.

    MediaPlayer player = null;
    Random randomizer;

    /**
     * This method is called when the MusicPlayer Service is created.
     * It may also be called whenever an Activity wants to change the music.
     *
     * The Intent which calls this method should also send extra information about which kind of music to play,
     * and whether to play any music at all.
     * (For instance, if the user wants to STOP all music, the program must call this method and say: { playMusic = false; } .
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        randomizer = new Random();
        prepareBattleTracks();

        //receive information from the calling intent about which music (if any) to play.
        intentThatCalled = intent;
        if (intentThatCalled != null) {
            musicType = intentThatCalled.getStringExtra("TYPE");
            playMusic = intentThatCalled.getBooleanExtra("PLAY_MUSIC", true);
        }

        stopMusic();

        if (playMusic)
        {
            chooseMusic();
        } else {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * This method creates an integer array,
     * where each integer refers to a music resource file.
     *
     * The first track will be randomly selected.
     */
    private void prepareBattleTracks()
    {
        currentTrack = randomizer.nextInt(3);

        battleTracks[0] = R.raw.battle_1;
        battleTracks[1] = R.raw.battle_2;
        battleTracks[2] = R.raw.battle_3;
    }

    /**
     * This method uses the musicType string to select which kind of music to play.
     * We have three tracks for background music, plus a celebration track for when the user wins,
     * and a sad track for when the user loses.
     */
    private void chooseMusic()
    {
        if (musicType.contains("fightMusic"))
        {
            playBattleMusic();
        } else if (musicType.contains("winner"))
        {
            playWinnerMusic();
        } else if (musicType.contains("loser"))
        {
            playLoserMusic();
        } else
        {
            //Since the default value for musicType is "No Music," this Toast should tell the user that there is "No Music."
            //However, I have written the process so this should never be called...
            Toast.makeText(this, musicType, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method uses the currentTrack variable to decide which battleTrack to play.
     */
    public void playBattleMusic()
    {
        if (currentTrack >= (battleTracks.length) || currentTrack < 0)
        {
            currentTrack = 0;
        }

        player = MediaPlayer.create(this, battleTracks[currentTrack]);

        currentTrack++;

        //When the track is over I want the OnCompletionListener to play another track.
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                player.release();
                chooseMusic();
            };
        });

        player.setVolume(0.031f, 0.031f);
        player.setLooping(false);
        player.start();
    }

    public void playLoserMusic()
    {
        stopMusic();
        player = MediaPlayer.create(this, R.raw.loser_music);
        player.setVolume(0.031f, 0.031f);
        player.setLooping(true);
        player.start();
    }

    public void playWinnerMusic()
    {
        stopMusic();
        player = MediaPlayer.create(this, R.raw.winning_music);
        player.setVolume(0.031f, 0.031f);
        player.setLooping(true);
        player.start();
    }


    /**
     * If there is a MediaPlayer object, and it is currently playing,
     * this method will make it stop.
     */
    public void stopMusic()
    {
        if (player != null)
        {
            if (player.isPlaying())
            {
                player.stop();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
