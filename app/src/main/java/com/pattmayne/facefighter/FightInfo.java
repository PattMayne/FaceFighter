package com.pattmayne.facefighter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Matt on 2014-11-03.
 * This class stores universal options for each Fight (battle).
 * This class also creates opponents, and their FacePieces, and attributes.
 *
 * This class is called once by the FightPrepActivity, and once by the FightActivity, every time there is a Fight.
 * The FightPrepActivity retrieves String information, while the FightActivity retrieves a location image and a "bad guy" antagonist FaceCharacter object.
 */
public class FightInfo {

    private String[] locationNames;
    private String[] antagonistNames;

    private ArrayList<Drawable> locations;
    private ArrayList<FaceCharacter> antagonists;

    private Resources res;
    private Context context;

    /**
     * Sole Constructor.
     * Creates lists and nothing else.
     * @param context
     */
    public FightInfo(Context context)
    {
        res = context.getResources();
        this.context = context;

        locationNames = new String[]{"Junk Yard", "Stonehenge", "Newfoundland"};
        antagonistNames = new String[]{"Junk Yard Robot", "Satan", "Fish Monster"};

        locations = new ArrayList<Drawable>();
        antagonists = new ArrayList<FaceCharacter>();
    }

    /**
     * This should only be called by the FightActivity.
     * This method creates FaceCharacter objects for each available antagonist, and retrieves the three location images.
     * @param view
     */
    public void createAntagonistsAndLocations(View view)
    {
        createAntagonists(view);
        createLocations();
    }

    /**
     *  This method retrieves the background image for each location and adds them to an ArrayList.
     */
    private void createLocations()
    {
        locations.clear();

        Drawable junkYard = res.getDrawable(R.drawable.junkyard);
        Drawable stonehenge = res.getDrawable(R.drawable.stonehenge);
        Drawable newfoundland = res.getDrawable(R.drawable.greenspond);
        locations.add(junkYard);
        locations.add(stonehenge);
        locations.add(newfoundland);
    }

    /**
     * This method creates the three available antagnoist FaceCharacters.
     * @param view
     */
    private void createAntagonists(View view)
    {
        createRobot(view);
        createSatan(view);
        createFishMonster(view);
    }

    /**
     * This method creates all the FacePiece objects for an antagonist FaceCharacter.
     * It fills in all the information for the FaceCharacter, and adds the FaceCharacter to the ArrayList of antagonistCharacters.
     * @param view
     */
    private void createSatan(View view)
    {
        FaceFactory tempFactory = new FaceFactory(view, context);
        FaceCharacter tempCharacter = tempFactory.getCharacter();

        tempCharacter.setName("Satan");

        FacePiece face = new FacePiece("Face", BitmapFactory.decodeResource(res, R.drawable.face_devil));
        FacePiece eyes = new FacePiece("Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_devil));
        FacePiece mouth = new FacePiece("Mouth", BitmapFactory.decodeResource(res, R.drawable.mouth_devil));
        FacePiece beard = new FacePiece("Beard", BitmapFactory.decodeResource(res, R.drawable.beard_devil));
        FacePiece head = new FacePiece("Horns", BitmapFactory.decodeResource(res, R.drawable.head_devil));
        FacePiece brow = new FacePiece("Brow", BitmapFactory.decodeResource(res, R.drawable.brow_mean));

        face.setHP(25);
        eyes.setHP(18);
        mouth.setHP(13);
        head.setHP(19);
        brow.setHP(1);
        beard.setHP(13);

        mouth.makeWeapon();
        mouth.setDamage(7);
        mouth.setBattleMove("Devour Soul");
        mouth.makeAbsorbent();
        mouth.setArmour(5);

        head.makeWeapon();
        head.setDamage(14);
        head.setBattleMove("Horn Ram");

        beard.makeResponsive();
        beard.makeAbsorbent();
        beard.setBattleMove("Absorb Damage");
        beard.setArmour(8);

        eyes.setArmour(2);
        eyes.makeWeapon();
        eyes.setBattleMove("Fiery Glare");
        eyes.setDamage(8);

        face.setPicLocation(201,202);
        eyes.setPicLocation(193,342);
        mouth.setPicLocation(263,462);
        beard.setPicLocation(205,532);
        head.setPicLocation(196,106);
        brow.setPicLocation(205,334);

        tempCharacter.addPiece(face, 0);
        tempCharacter.addPiece(eyes, 1);
        tempCharacter.addPiece(mouth, 2);
        tempCharacter.addPiece(beard, 3);
        tempCharacter.addPiece(head, 4);
        tempCharacter.addPiece(brow, 5);

        antagonists.add(tempCharacter);

    }

    /**
     * This method creates all the FacePiece objects for an antagonist FaceCharacter.
     * It fills in all the information for the FaceCharacter, and adds the FaceCharacter to the ArrayList of antagonistCharacters.
     * @param view
     */
    private void createFishMonster(View view)
    {
        FaceFactory tempFactory = new FaceFactory(view, context);
        FaceCharacter tempCharacter = tempFactory.getCharacter();

        tempCharacter.setName("Fish Monster");

        FacePiece face = new FacePiece("Face", BitmapFactory.decodeResource(res, R.drawable.face_fish));
        FacePiece eyes = new FacePiece("Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_fish));
        FacePiece mouth = new FacePiece("Beak", BitmapFactory.decodeResource(res, R.drawable.mouth_fish));
        FacePiece beard = new FacePiece("Beard", BitmapFactory.decodeResource(res, R.drawable.beard_fish));
        FacePiece head = new FacePiece("Antlers", BitmapFactory.decodeResource(res, R.drawable.head_fish));
        FacePiece brow = new FacePiece("Brow", BitmapFactory.decodeResource(res, R.drawable.brow_curious));

        face.setHP(31);
        eyes.setHP(14);
        mouth.setHP(23);
        head.setHP(19);
        brow.setHP(4);
        beard.setHP(15);

        mouth.makeWeapon();
        mouth.setDamage(6);
        mouth.setBattleMove("Peck");

        head.makeResponsive();
        head.makeReflective();
        head.setBattleMove("Throw Back");
        head.setArmour(6);

        eyes.setArmour(7);

        beard.makeWeapon();
        beard.setDamage(10);
        beard.setArmour(4);
        beard.makeAbsorbent();
        beard.setBattleMove("Beardy Tangle!");

        face.setPicLocation(143,195);
        eyes.setPicLocation(68,330);
        mouth.setPicLocation(102,407);
        beard.setPicLocation(113,426);
        head.setPicLocation(149,172);
        brow.setPicLocation(80,313);

        tempCharacter.addPiece(face, 0);
        tempCharacter.addPiece(eyes, 1);
        tempCharacter.addPiece(mouth, 2);
        tempCharacter.addPiece(beard, 3);
        tempCharacter.addPiece(head, 4);
        tempCharacter.addPiece(brow, 5);

        antagonists.add(tempCharacter);
    }

    /**
     * This method creates all the FacePiece objects for an antagonist FaceCharacter.
     * It fills in all the information for the FaceCharacter, and adds the FaceCharacter to the ArrayList of antagonistCharacters.
     * @param view
     */
    private void createRobot(View view)
    {
        FaceFactory tempFactory = new FaceFactory(view, context);
        FaceCharacter tempCharacter = tempFactory.getCharacter();

        tempCharacter.setName("Junk Yard Robot");

        FacePiece face = new FacePiece("Metal Face", BitmapFactory.decodeResource(res, R.drawable.face_robot));
        FacePiece eyes = new FacePiece("Glowing Eyes", BitmapFactory.decodeResource(res, R.drawable.eyes_robot));
        FacePiece mouth = new FacePiece("Speaker Mouth", BitmapFactory.decodeResource(res, R.drawable.mouth_robot));
        FacePiece beard = new FacePiece("Rocket Beard", BitmapFactory.decodeResource(res, R.drawable.beard_robot));
        FacePiece head = new FacePiece("Wire Hair", BitmapFactory.decodeResource(res, R.drawable.head_robot));
        FacePiece brow = new FacePiece("Brow", BitmapFactory.decodeResource(res, R.drawable.brow_mean));

        face.setHP(37);
        eyes.setHP(24);
        mouth.setHP(29);
        head.setHP(17);
        brow.setHP(2);
        beard.setHP(7);

        eyes.makeWeapon();
        eyes.setDamage(6);
        eyes.setBattleMove("Annoying Lights");

        mouth.makeWeapon();
        mouth.setDamage(5);
        mouth.setBattleMove("Sonic Screech");

        beard.makeWeapon();
        beard.setDamage(15);
        beard.setBattleMove("Rocket Blast");

        head.setArmour(6);
        head.makeResponsive();
        head.makeReflective();
        head.setBattleMove("Ping Damage");

        face.setPicLocation(210,250);
        eyes.setPicLocation(194,368);
        mouth.setPicLocation(267,548);
        beard.setPicLocation(244,595);
        head.setPicLocation(178,184);
        brow.setPicLocation(196,345);

        tempCharacter.addPiece(face, 0);
        tempCharacter.addPiece(eyes, 1);
        tempCharacter.addPiece(mouth, 2);
        tempCharacter.addPiece(beard, 3);
        tempCharacter.addPiece(head, 4);
        tempCharacter.addPiece(brow, 5);

        antagonists.add(tempCharacter);
    }

    //Getter methods

    public String[] getLocationNames()
    {
        return locationNames;
    }

    public String[] getAntagonistNames()
    {
        return antagonistNames;
    }

    /**
     * Provide the caller with a Drawable Location Image, based on an index selection provided as an integer.
     * @param selection
     * @return
     */
    public Drawable getLocation(int selection)
    {
        return locations.get(selection);
    }

    /**
     * Provide the caller with a FaceCharacter to use as an antagonist,
     * chosen from the ArrayList of antagonist FaceCharacters based on an index provided to the method as an integer.
     * @param selection
     * @return
     */
    public FaceCharacter getAntagonist(int selection)
    {
        return antagonists.get(selection);
    }
}
