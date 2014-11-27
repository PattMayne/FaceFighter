package com.pattmayne.facefighter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * This class displays the canvas where the user builds their FaceCharacter.
 * One by one, FacePiece Bitmap images are added to the screen and placed where the user chooses.
 *
 * The images are displayed by the FaceBuilderActivity.
 * Created by Matt on 2014-10-19.
 */
public class FaceBuilderView extends View {

    //variables

    private FaceCharacter character;
    private FaceFactory faceFactory;

    private Context context;
    private Paint paint;
    private Resources res;

    private int centerX;
    private int centerY;

    private FacePiece activePiece;

    private ArrayList<FacePiece> facePieces;

    private FileOutputStream fileStream;
    private String filePath;
    private Bitmap savedBitmap;
    private File faceFile;

    //constructors

    public FaceBuilderView(Context sentContext)
    {
        super(sentContext);
        this.context = sentContext;
        setDrawingCacheEnabled(true);
        instantiateVariables();
    }

    public FaceBuilderView(Context sentContext, AttributeSet attrs) {
        super(sentContext, attrs);
        this.context = sentContext;
        this.setDrawingCacheEnabled(true);
        instantiateVariables();
    }

    public FaceBuilderView(Context sentContext, AttributeSet attrs, int defStyleAttr) {
        super(sentContext, attrs, defStyleAttr);
        this.context = sentContext;
        this.setDrawingCacheEnabled(true);
        instantiateVariables();
    }


    private void instantiateVariables()
    {
        paint = new Paint();
        res = getResources();
        faceFactory = new FaceFactory(this, context);
        character = faceFactory.getCharacter();
        facePieces = faceFactory.getPieces();
    }

    /**
     * The Thread calls this method every time invalidate() is invoked.
     * It draws all the FacePiece Bitmap images in their appointed locations.
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

    /**
     * When the user selects a new FacePiece from the menu,
     * the selected FacePiece is fed to this method and displayed on the screen.
     * @param piece
     */
    public void addPiece(FacePiece piece)
    {
        activePiece = piece;
        invalidate();
    }

    /**
     * When the user touches the screen,
     * this method redraws the FacePiece, activePiece, to the location the user touched.
     *
     * It does this by changing the FacePiece object's picLocation variables to the touched location.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (activePiece!=null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
            }
            invalidate();
            activePiece.setPicLocation(x, y);

            return true;
        }
        else {return false;}
    }

    /**
     * This method saves a picture of the completed FaceCharacter to disk.
     * @param context
     */
    public void saveFaceImage(Context context)
    {
        savedBitmap = getDrawingCache();
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        faceFile = new File(filePath+"/facePicture.png");

        try {

        faceFile.createNewFile();
        fileStream = new FileOutputStream(faceFile);
        savedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);

        } catch (Exception e) {}

        activePiece = null;
    }

    public FaceCharacter getCharacter()
    {
        return character;
    }

    public FaceFactory getFaceFactory()
    {
        return faceFactory;
    }

}
