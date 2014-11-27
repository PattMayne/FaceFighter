package com.pattmayne.facefighter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Prepares a Fight.
 * This Activity allows a user to select an opponent and a location.
 * The user's FaceCharacter will already have been chosen.
 */
public class FightPrepActivity extends ActionBarActivity {

    //variables
    private String[] locationNames;
    private String[] opponentNames;
    private int locationSelected;
    private int opponentSelected;
    private FightInfo fightInfo;
    public boolean playMusic = true;

    ListView optionsListView;

    ListAdapter locationListAdapter;
    ListAdapter opponentListAdapter;

    /**
     * Setup the display so the user can choose who and where to Fight.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_prep);

        //Keep carrying the users' choice about whether to play music.
        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", true);
        instantiateVariables();
    }

    /**
     * Retrieve options and information from the "FightInfo" class.
     * Then setup the ArrayAdapters with the opponent and location options for the user to choose from.
     */
    private void instantiateVariables()
    {
        fightInfo = new FightInfo(this);
        locationNames = fightInfo.getLocationNames();
        opponentNames = fightInfo.getAntagonistNames();
        optionsListView = (ListView) findViewById(R.id.venue_listview);

        setupAdapters();
        setupLocationListView();
    }

    /**
     * This method sets up two ArrayAdapters which contain String objects.
     * One ArrayAdapter contains a list of Location Names, and the other contains a list of Opponent Names.
     *
     * I created a separate method for this because I needed to override a method within the ArrayAdapter class,
     * to set the text color to something readable on the dark background.
     *
     * I didn't want these lengthy chunks of code to clutter up the instantiateVariables method.
     */
    private void setupAdapters()
    {
        opponentListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opponentNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.orange));
                return view;
            }
        };

        locationListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, locationNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(getResources().getColor(R.color.orange));
                return view;
            }
        };
    }

    /**
     * Display a list of locations where the battle can take place.
     */
    private void setupLocationListView() {

        optionsListView.setAdapter(locationListAdapter);

        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                locationSelected = position;
                String locationPicked = "You selected " + String.valueOf(adapterView.getItemAtPosition(position));
                Toast.makeText(FightPrepActivity.this, locationPicked, Toast.LENGTH_SHORT).show();
                setupOpponentListView();
            }
        });
    }

    /**
     * Display a list of "bad guy" opponents to fight.
     */
    private void setupOpponentListView()
    {
        TextView fightPrepTextView = (TextView) findViewById(R.id.fight_prep_text_view);
        fightPrepTextView.setText("Choose Your Opponent!");

        optionsListView.setAdapter(opponentListAdapter);
        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                opponentSelected = position;
                String opponentPicked = "You'll fight " + String.valueOf(adapterView.getItemAtPosition(position) + "!");
                Toast.makeText(FightPrepActivity.this, opponentPicked, Toast.LENGTH_SHORT).show();
                startFight();
            }
        });
    }

    /**
     * Once all the options have been selected, this method starts the FightActivity so the actual battle can take place.
     */
    public void startFight()
    {
        int[] locationAndOpponent = {locationSelected, opponentSelected};

        Intent intentToFight = new Intent(this, FightActivity.class);
        intentToFight.putExtra("location_and_opponent", locationAndOpponent);
        intentToFight.putExtra("PLAY_MUSIC", playMusic);

        startActivity(intentToFight);
        finish();
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
