package com.pattmayne.facefighter;

/**
 * A battle (Fight) consists of a sequence of Turns, which consist of a sequence of BattleMoves.
 * This class holds information about specific BattleMoves.
 *
 * Each FacePiece may have only one BattleMove.
 * Created by Matt on 2014-11-04.
 */
public class BattleMove {

    //variables:
    private Player player;
    private FacePiece piece;
    private String name;
    private int damage;
    private Turn currentTurn;

    public BattleMove(FacePiece piece, Player player)
    {
        this.player = player;
        currentTurn = player.getTurn();

        this.piece = piece;
        setupBattleMove();
    }

    private void setupBattleMove()
    {
        name = piece.getBattleMove();
        damage = piece.getDamage();
    }

    public FacePiece getPiece()
    {
        return piece;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
