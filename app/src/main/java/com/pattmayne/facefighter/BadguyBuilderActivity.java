package com.pattmayne.facefighter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This Activity is for developers to create new "bad guys" for the user to fight.
 * It's very similar to the FaceBuilderActivity,
 * but creates FaceCharacters with alternative "bad guy" FacePiece objects,
 * which are defined in the FightInfo class.
 *
 * If a developer wants to add more "bad guy" opponents,
 * they will have to supply new Bitmap images and define properties in the FightInfo class.
 *
 * They will also have to adjust the code in this file to access the new information that they supplied.
 *
 * Some of the methods herein have been copied from the FaceBuilderActivity class, but are not currently used.
 * They have been retained for reference, and for possible alteration.
 *
 * This class is never called during a game.
 * It is only called when a developer makes it available in the MainActivity.
 *
 * This class is not available to users.
 */
public class BadguyBuilderActivity extends ActionBarActivity {

    private FaceBuilderView faceBuilderView;
    private String chosenName;
    private FaceFactory faceFactory;
    private Resources res;

    private int featureTicker = 0;

    //A list of all the lists
    private ArrayList<ArrayList> listList;

    private ArrayList<View> faceButtonList;
    private ArrayList<View> eyesButtonList;
    private ArrayList<View> mouthButtonList;
    private ArrayList<View> headButtonList;
    private ArrayList<View> beardButtonList;
    private ArrayList<View> browButtonList;

    private ArrayList<View> devilList;
    private ArrayList<View> fishList;
    private ArrayList<View> robotList;

    public boolean playMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badguy_layout);

        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", true);
        faceBuilderView = (FaceBuilderView)findViewById(R.id.faceBuildView);
        faceFactory = faceBuilderView.getFaceFactory();

        createButtonLists();
        updateButtonVisibility();
        makeAllButtonsGone();

        findViewById(R.id.button_save_face).setVisibility(View.GONE);
        res = faceBuilderView.getResources();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This Activity has no menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This Activity has no menu
        return super.onOptionsItemSelected(item);
    }

    //These ("placePIECE") methods are currently only useful for reference.
    //A developer can copy/alter them, designing them to access new Bitmap resources and give them new names.
    //It would be necessary to also add corresponding information to the FightInfo class,
    // to define properties for the new FacePiece objects.

    public void placeDevilFace(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Face", BitmapFactory.decodeResource(res, R.drawable.face_fish), featureTicker);
        featureTicker++;
        faceBuilderView.addPiece(tempPiece);
    }

    public void placeDevilEyes(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_fish), featureTicker);
        faceBuilderView.addPiece(tempPiece);
        featureTicker++;
    }

    public void placeDevilMouth(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Mouth", BitmapFactory.decodeResource(res, R.drawable.mouth_fish), featureTicker);
        faceBuilderView.addPiece(tempPiece);
        featureTicker++;
    }

    public void placeDevilHead(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Head", BitmapFactory.decodeResource(res, R.drawable.head_fish), featureTicker);
        faceBuilderView.addPiece(tempPiece);
        featureTicker++;
    }

    public void placeDevilBeard(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Beard", BitmapFactory.decodeResource(res, R.drawable.beard_fish), featureTicker);
        faceBuilderView.addPiece(tempPiece);
        featureTicker++;
    }

    public void placeDevilBrow(View view)
    {
        FacePiece tempPiece = faceFactory.makeExtraPiece("Devil Brows", BitmapFactory.decodeResource(res, R.drawable.brow_mean), featureTicker);
        faceBuilderView.addPiece(tempPiece);
        featureTicker++;
    }

    //makes all the buttons invisible, except the ones that are relevant right now based on the status of the featureTicker
    private void updateButtonVisibility()
    {
        makeAllButtonsGone();
        chooseButtonSet();
    }

    private void makeAllButtonsGone()
    {
        findViewById(R.id.button_save_face).setVisibility(View.GONE);

        for (int i=0; i<listList.size(); i++)
        {
            ArrayList<View> tempViewList = listList.get(i);

            for (int j = 0; j<tempViewList.size(); j++)
            {
                View tempView = tempViewList.get(j);
                tempView.setVisibility(View.GONE);
            }
        }
    }

    private void chooseButtonSet()
    {
        if (featureTicker==0)
        {
            for (int i = 0; i<faceButtonList.size(); i++)
            {
                View tempButton = faceButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);

                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick a Face");

            }
        } else if (featureTicker==1)
        {
            for (int i = 0; i<eyesButtonList.size(); i++)
            {
                View tempButton = eyesButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);

                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick Some Eyes");
            }
        } else if (featureTicker==2)
        {
            for (int i = 0; i<mouthButtonList.size(); i++)
            {
                View tempButton = mouthButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);

                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick a Mouth");
            }
        } else if (featureTicker==3)
        {
            for (int i = 0; i<headButtonList.size(); i++)
            {
                View tempButton = headButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);

                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick a Headpiece");
            }
        } else if (featureTicker==4)
        {
            for (int i = 0; i<beardButtonList.size(); i++)
            {
                View tempButton = beardButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);
                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick a Beard?");
            }
        } else if (featureTicker==5)
        {
            for (int i = 0; i<browButtonList.size(); i++)
            {
                View tempButton = browButtonList.get(i);
                tempButton.setVisibility(View.VISIBLE);
                TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
                featurePickerLabel.setText("Pick Some Eyebrows");
            }
        } else
        {

            TextView featurePickerLabel = (TextView) findViewById(R.id.feature_picker_label_text);
            featurePickerLabel.setText("Choose a Name!");

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Name Your Character");

            //setup the input
            final EditText inputName = new EditText(this);

            inputName.setInputType(InputType.TYPE_CLASS_TEXT);
            dialogBuilder.setView(inputName);

            dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    chosenName = inputName.getText().toString();
                    faceFactory.setName(chosenName);
                }
            });

            dialogBuilder.show();

            findViewById(R.id.button_next_feature).setVisibility(View.GONE);
            findViewById(R.id.button_save_face).setVisibility(View.VISIBLE);
        }
    }

    private void createButtonLists()
    {
        //Each ArrayList of buttons is for a specific facial feature
        //Each list of buttons will be visible at a different time, in sequence.
        faceButtonList = new ArrayList<View>();
        eyesButtonList = new ArrayList<View>();
        mouthButtonList = new ArrayList<View>();
        headButtonList = new ArrayList<View>();
        beardButtonList = new ArrayList<View>();
        browButtonList = new ArrayList<View>();

        listList = new ArrayList<ArrayList>();

        faceButtonList.add(findViewById(R.id.button_face_one));
        faceButtonList.add(findViewById(R.id.button_face_two));

        eyesButtonList.add(findViewById(R.id.button_eyes_laser));
        eyesButtonList.add(findViewById(R.id.button_eyes_cat));
        eyesButtonList.add(findViewById(R.id.button_eyes_shades));

        mouthButtonList.add(findViewById(R.id.button_mouth_bigteeth));
        mouthButtonList.add(findViewById(R.id.button_mouth_metaltongue));

        headButtonList.add(findViewById(R.id.button_head_helmet));
        headButtonList.add(findViewById(R.id.button_head_spikedhair));
        headButtonList.add(findViewById(R.id.button_head_tophat));

        beardButtonList.add(findViewById(R.id.button_beard_absorbent));
        beardButtonList.add(findViewById(R.id.button_beard_sonic));
        beardButtonList.add(findViewById(R.id.button_beard_spikedtie));

        browButtonList.add(findViewById(R.id.button_brow_curious));
        browButtonList.add(findViewById(R.id.button_brow_mean));

        listList.add(browButtonList);
        listList.add(faceButtonList);
        listList.add(eyesButtonList);
        listList.add(beardButtonList);
        listList.add(headButtonList);
        listList.add(mouthButtonList);
    }

    /**
     * This method saves the image as a PNG, but also saves all the info about the FaceCharacter and its FacePieces
     * @param view
     */
    public void saveFace(View view)
    {

        Toast toast = Toast.makeText(this, "No Saving Badguys!", Toast.LENGTH_LONG);
        toast.show();

    }

    public void nextFeature(View view)
    {
        featureTicker++;

        updateButtonVisibility();
    }



    @Override
    protected void onStop() {
        super.onStop();
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
