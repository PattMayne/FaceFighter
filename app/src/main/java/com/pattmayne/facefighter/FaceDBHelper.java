package com.pattmayne.facefighter;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * This class defines the tables I will use to save FaceCharacter and FacePiece objects.
 * The SQLiteOpenHelper is the interface that the application uses to access the database and its tables.
 * Created by Matt on 2014-10-31.
 */
public class FaceDBHelper extends SQLiteOpenHelper {

    //class variables

    Context context;

    //Universal database variables
    static final String TAG = "DbHelper";
    static final String DB_NAME = "saved_face.db";
    static final int DB_VERSION = 5; //
    static final String FACE_TABLE = "face_table";
    static final String PIECES_TABLE = "face_pieces_table";
    static final String C_ID = BaseColumns._ID;

    //common variables to be used for both tables
    static final String C_NAME = "name";

    //FaceCharacter variables to store in columns in the face_table
    static final String C_WINS = "wins";
    static final String C_LOSSES = "losses";

    //FacePiece variables to store in columns in the face_piece_table
    static final String C_DAMAGE = "damage";
    static final String C_ARMOUR = "armour";
    static final String C_HP = "hp";
    static final String C_RECHARGE_TIME = "recharge_time";
    static final String C_IS_WEAPON = "is_weapon";
    static final String C_RESPONDS_TO_ATTACK = "responds_to_attack";
    static final String C_ABSORBENT = "absorbent";
    static final String C_REFLECTIVE = "reflective";
    static final String C_COST = "cost";
    static final String C_ALIVE = "alive";
    static final String C_BATTLE_MOVE = "battle_move";
    static final String C_PIC_LOCATION_X = "pic_location_x";
    static final String C_PIC_LOCATION_Y = "pic_location_y";
    static final String C_PIC_SIZE_X = "pic_size_x";
    static final String C_PIC_SIZE_Y = "pic_size_y";
    static final String C_LAYER_PLACEMENT = "layer_placement";
    static final String C_CHARACTER_NAME = "character_name";

    public FaceDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public FaceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public FaceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    /**
     * This method is called when the database is opened to WRITE, but not when it is opened to READ.
     * In this method we delete all old entries by dropping the tables. Then we recreate the tables so the user can save their new FaceCharacter.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("drop table if exists " + FACE_TABLE);
        db.execSQL("drop table if exists " + PIECES_TABLE);

        String sqlStatementToCreateFaceTable = "create table " + FACE_TABLE + " (" + C_ID + " int primary key, " + C_NAME + " text, " + C_WINS + " integer, " + C_LOSSES + " integer)";
        String sqlStatementToCreatePiecesTable = "create table " + PIECES_TABLE + " (" + C_ID + " int primary key, " + C_DAMAGE + " integer, " + C_ARMOUR + " integer, " + C_HP + " integer, " + C_RECHARGE_TIME + " integer, " + C_IS_WEAPON + " integer, " + C_RESPONDS_TO_ATTACK + " integer, " + C_COST + " integer, " + C_ALIVE + " integer, " + C_NAME + " text, " + C_CHARACTER_NAME + " text, " + C_BATTLE_MOVE + " text, " + C_PIC_LOCATION_X + " integer, " + C_PIC_LOCATION_Y + " integer, " + C_PIC_SIZE_X + " integer, " + C_PIC_SIZE_Y + " integer, " + C_LAYER_PLACEMENT + " integer, " + C_REFLECTIVE + " integer, " + C_ABSORBENT + " integer)";

        db.execSQL(sqlStatementToCreateFaceTable);
        db.execSQL(sqlStatementToCreatePiecesTable);
        Log.d(TAG, "onCreated sql: " + sqlStatementToCreateFaceTable + ", " + sqlStatementToCreatePiecesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + FACE_TABLE);
        db.execSQL("drop table if exists " + PIECES_TABLE);
        Log.d(TAG, "onUpdated");
        onCreate(db);

    }
}
