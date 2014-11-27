package com.pattmayne.facefighter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This Activity displays three main panels (Views) which the user can manipulate to create a FaceCharacter.
 * One side of the screen holds a changeable list of FacePiece images for the user to select,
 * the middle holds a canvas image (from FaceBuilderView) of the FaceCharacter that is being created,
 * and the other side displays information about the last selected FacePiece object.
 *
 * This Activity creates a multi-stage process where the user chooses an item for each category of FacePiece:
 * a face, eyes, mouth, upper head piece, lower head piece, and brows, in sequence.
 *
 * This Activity also allows the user to choose a name for their FaceCharacter.
 */
public class FaceBuilderActivity extends ActionBarActivity {

    private FaceBuilderView faceBuilderView;
    private String chosenName;
    private FaceFactory faceFactory;

    //The featureTicker determines which category of FacePiece is currently active
    private int featureTicker = 0;

    //A list of all the lists
    private ArrayList<ArrayList> listList;

    private ArrayList<View> faceButtonList;
    private ArrayList<View> eyesButtonList;
    private ArrayList<View> mouthButtonList;
    private ArrayList<View> headButtonList;
    private ArrayList<View> beardButtonList;
    private ArrayList<View> browButtonList;

    public boolean playMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_builder);

        playMusic = getIntent().getBooleanExtra("PLAY_MUSIC", true);
        faceBuilderView = (FaceBuilderView)findViewById(R.id.faceBuildView);
        faceFactory = faceBuilderView.getFaceFactory();
        createButtonLists();
        updateButtonVisibility();
        findViewById(R.id.button_save_face).setVisibility(View.GONE);
    }

    /**
     * We have a button for each FacePiece already defined in the XML layout file for this Activity.
     * We need an ArrayList of buttons for each category or FacePiece.
     *
     * This method creates those ArrayLists, and then creates a master ArrayList which holds all the ArrayLists of Buttons.
     */
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
     * This method makes all the buttons invisible,
     * except the ones that are relevant right now based on the status of the featureTicker.
     */
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


    /**
     * This method reads the current status of the featureTicker,
     * and uses that information to decide which ArrayList of Buttons to display.
     */
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
                findViewById(R.id.button_next_feature).setVisibility(View.GONE);

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
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle("Name Your Character");

            //setup the input
            final EditText inputName = new EditText(this);

            inputName.setInputType(InputType.TYPE_CLASS_TEXT);
            dialogBuilder.setView(inputName);

            dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    chosenName = inputName.getText().toString();
                    if (chosenName == " " || chosenName.length() == 0)
                    {
                        chosenName = "Player One";
                    }
                    faceFactory.setName(chosenName);
                }
            });

            dialogBuilder.show();

            findViewById(R.id.button_next_feature).setVisibility(View.GONE);
            findViewById(R.id.button_save_face).setVisibility(View.VISIBLE);
        }
    }


    /**
     * This method increments the featureTicker
     * so the user can select an item from the next category of FacePiece.
     * @param view
     */
    public void nextFeature(View view)
    {
        featureTicker++;
        updateButtonVisibility();
        findViewById(R.id.button_next_feature).setVisibility(View.GONE);
    }


    /**
     * This method retrieves a string of information about the selected FacePiece,
     * and sets that info String into a label on the screen.
     * @param activePiece
     */
    private void setupInfoScreen(FacePiece activePiece)
    {
        findViewById(R.id.button_next_feature).setVisibility(View.VISIBLE);

        TextView pieceTitleLabel = (TextView) findViewById(R.id.piece_title_text_label);
        pieceTitleLabel.setText(activePiece.getName());

        TextView facePieceInfoLabel = (TextView) findViewById(R.id.selected_piece_text_label);
        facePieceInfoLabel.setText(activePiece.makeStatsString());
    }


    //The following methods create individual FacePiece objects,
    //and display their images on the faceBuilderView canvas.


    public void placeFaceOne(View view)
    {
        FacePiece tempPiece = faceFactory.makeFaceOne(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeFaceTwo(View view) {
        FacePiece tempPiece = faceFactory.makeFaceTwo(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeEyesLaser(View view) {
        FacePiece tempPiece = faceFactory.makeEyesLaser(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeEyesCat(View view) {
        FacePiece tempPiece = faceFactory.makeEyesCat(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeEyesShades(View view) {
        FacePiece tempPiece = faceFactory.makeEyesShades(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeMouthBigTeeth(View view) {
        FacePiece tempPiece = faceFactory.makeMouthBigTeeth(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeMouthMetalTongue(View view) {
        FacePiece tempPiece = faceFactory.makeMouthMetalTongue(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeHeadHelmet(View view)
    {
        FacePiece tempPiece = faceFactory.makeHeadHelmet(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeHeadSpikedHair(View view)
    {
        FacePiece tempPiece = faceFactory.makeHeadSpikedHair(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeHeadTopHat(View view)
    {
        FacePiece tempPiece = faceFactory.makeHeadTopHat(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeBeardAbsorbent(View view) {
        FacePiece tempPiece = faceFactory.makeBeardAbsorbent(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeBeardSonic(View view) {
        FacePiece tempPiece = faceFactory.makeBeardSonic(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    public void placeBeardSpikedTie(View view) {
        FacePiece tempPiece = faceFactory.makeBeardSpikedTie(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
        tempPiece.setLayerPlacement(featureTicker);
    }

    public void placeBrowMean(View view) {
        FacePiece tempPiece = faceFactory.makeBrowMean(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
     }

    public void placeBrowCurious(View view) {
        FacePiece tempPiece = faceFactory.makeBrowCurious(featureTicker);
        faceBuilderView.addPiece(tempPiece);

        setupInfoScreen(tempPiece);
    }

    /**
     * This method calls another method which saves the final FaceCharacter image as a PNG,
     * but it also calls another method which saves all the info about the FaceCharacter and its FacePieces.
     * Then it calls the next Activity.
     * @param view
     */
    public void saveFace(View view)
    {
        faceBuilderView.saveFaceImage(this);

        //save the important information about the face character to a database
        faceFactory.getCharacter().saveFaceToDB(this);

        Intent intent = new Intent(this, CharacterSummaryActivity.class);
        intent.putExtra("GameOver", false);
        intent.putExtra("PLAY_MUSIC", playMusic);
        startActivity(intent);
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
