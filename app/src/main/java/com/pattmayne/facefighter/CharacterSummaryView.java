package com.pattmayne.facefighter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * This class displays information about the FaceCharacter which is saved to the database.
 * Created by Matt on 2014-10-27.
 */
public class CharacterSummaryView extends View {

    private FaceCharacter character;
    private FaceFactory faceFactory;
    private Context context;
    private Paint paint;
    private ArrayList<FacePiece> facePieces;

    private int centerX;
    private int centerY;


    public CharacterSummaryView(Context context) {
        super(context);
        instantiateVariables(context);
    }

    public CharacterSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instantiateVariables(context);
    }

    public CharacterSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        instantiateVariables(context);
    }

    /**
     * This method creates a FaceFactory and requests for a new FaceCharacter to be created,
     * with information that is saved to the database.
     * @param context
     */
    private void instantiateVariables(Context context)
    {
        paint = new Paint();
        this.context = context;
        faceFactory = new FaceFactory(this, context);
        character = faceFactory.reviveCharacter();
        facePieces = character.getPieces();
    }

    /**
     * This method draws each Bitmap image for each FacePiece object in the ArrayList of FacePieces.
     * @param canvas
     */
    @Override public void onDraw(Canvas canvas)
    {
        centerX = canvas.getWidth()/3;
        centerY = canvas.getHeight()/3;

        for(int i=0; i<facePieces.size(); i++)
        {
            canvas.drawBitmap(facePieces.get(i).getPic(), facePieces.get(i).getPicLocation()[0], facePieces.get(i).getPicLocation()[1], paint);
        }
    }

    public FaceCharacter getCharacter()
    {
        return character;
    }
}
