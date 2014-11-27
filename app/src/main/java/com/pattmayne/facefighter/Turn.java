package com.pattmayne.facefighter;

import java.util.Random;

/**
 * Created by Matt on 2014-11-04.
 * This is a turn-based game, and this class is the "Turn" class to resolve those turns.
 * During a Fight between two Player objects (each of which is based on a FaceCharacter object), the fight will proceed with a sequence of Turn objects.
 * An object of this class receives information about whose turn it is and which BattleMoves (attacks and defenses) have been chosen by the players.
 * After receiving the information, an object of this class does the computation for who gains and loses HP, and then lets the Fight initiate the next Turn.
 */
public class Turn {

    //variables

    private Fight fight;
    private Player currentPlayer;
    private Player opponent;
    private BattleMove attackMove;
    private BattleMove defendMove;
    private boolean firstMove = true;

    private FacePiece attackingPiece;
    private FacePiece defendingPiece;

    private int damageToDefendingPiece = 0;
    private int damageToAttackingPiece = 0;
    private int defendingAbsorbentBenefit = 0;
    private int attackingAbsorbentBenefit = 0;

    private final int[] effectModifierArray = {-2, -1, 0, 1, 2};
    private Random randomizer;

    /**
     * Create a new Turn and do nothing else until a BattleMove has been made.
     * @param player
     */
    public Turn(Player player, Fight fight)
    {
        randomizer = new Random();
        this.fight = fight;
        currentPlayer = player;
        opponent = currentPlayer.getOpponent();
    }

    /**
     * Each turn can have a first move and a responding/defensive move.
     * Both of those moves come through this public method, which directs them to either "firstMove" or "defendMove."
     * @param move
     */
    public void makeMove(BattleMove move) {
        if (firstMove) {
            firstMove = false;
            makeFirstMove(move);
        } else
        {
            makeDefendMove(move);
        }
    }

    /**
     * The first move in a turn is always an attack, so this method sets the damage to the defending (victim) piece, though we don't yet know which piece will be attacked.
     * A first move can be absorbent, where the attacking piece will leech HP from its victim.
     * @param move
     */
    private void makeFirstMove(BattleMove move)
    {
        attackMove = move;
        attackingPiece = attackMove.getPiece();
        damageToDefendingPiece = attackingPiece.getDamage() + effectModifier();

        if (attackingPiece.isAbsorbent())
        {
            resolveAbsorbentAttack();
        } else if (attackingPiece.isKamikaze())
        {
            resolveKamikazeAttack();
        }
    }

    /**
     * This method is called when the initial attack is an absorbent attack.
     * Here we decide how much damage to inflict on the defending (victim) piece, and how much HP to absorb.
     */
    private void resolveAbsorbentAttack()
    {
        attackingAbsorbentBenefit = (damageToDefendingPiece / 2) + 1;
    }

    private void resolveKamikazeAttack()
    {
        damageToAttackingPiece = (damageToDefendingPiece / 2);
    }

    /**
     * The second and final move of a turn may not happen at all.
     * If it does occur it will either be absorbent (to transmute damage into bonus HP) or reflective (to redirect the damage back at the attacking piece).
     * @param move
     */
    private void makeDefendMove(BattleMove move)
    {
        defendingPiece = move.getPiece();

        if (defendingPiece.isAbsorbent())
        {
            resolveAbsorbentDefense();
        }
        else if (defendingPiece.isReflective())
        {
            resolveReflectiveEffect();
        }
    }

    /**
     * This method is called when the responsive second BattleMove is an absorbent defense.
     * This method decides how much damage the defending (victim) piece receives, and how much HP will be absorbed by the defending piece.
     */
    private void resolveAbsorbentDefense()
    {
        damageToDefendingPiece = resolveArmourEffect(damageToDefendingPiece, defendingPiece.getArmour());
        defendingAbsorbentBenefit = (attackingPiece.getDamage() - damageToDefendingPiece);
    }

    /**
     * This method is called when the responsive second BattleMove is a reflective defense.
     * This method decides how much damage the defending (victim) piece receives, and how much damage will be redirected back at the attacking piece.
     */
    private void resolveReflectiveEffect()
    {
        damageToDefendingPiece = resolveArmourEffect(damageToDefendingPiece, defendingPiece.getArmour()) / 2;
        damageToAttackingPiece = damageToAttackingPiece + (attackingPiece.getDamage() - damageToDefendingPiece);
    }

    /**
     * This method calculates a new amount of damage based on the amount of armour.
     * @param damage
     * @param armour
     * @return
     */
    private int resolveArmourEffect(int damage, int armour)
    {
        int resolvedDamage = (damage - armour) + effectModifier();

        if (resolvedDamage < 2)
        {
            resolvedDamage = 2;
        }

        return resolvedDamage;
    }

    /**
     * Once all the BattleMoves have been made, and the Turn object has calculated how much HP to subtract or add to different FacePiece objects,
     * this method is called to perform the actual addition and subtraction of HP.
     * Then it tells the Fight object to finish the turn and prepare the next Turn object.
     */
    public void resolveBattleSequence()
    {
        //if there was only one BattleMove (attack) during this turn then no defending (victim) FacePiece has yet been chosen.
        //This conditional statement covers that possibility by assigning a randomly-chosen FacePiece from the CurrentPlayer's opponent.
        if (defendingPiece == null)
        {
            setDefendingPiece();
            damageToDefendingPiece = resolveArmourEffect(damageToDefendingPiece, defendingPiece.getArmour());
        }

        //the defender receives HP from the absorbent piece... but the HP goes to a random piece rather than the defending piece.
        if (defendingAbsorbentBenefit != 0)
        {
                opponent.getRandomPiece().receiveHP(defendingAbsorbentBenefit);
        }

        if (attackingAbsorbentBenefit != 0) {
            currentPlayer.getRandomPiece().receiveHP(attackingAbsorbentBenefit);
        }

            defendingPiece.sufferWound(damageToDefendingPiece);
            attackingPiece.sufferWound(damageToAttackingPiece);

        //These statements initiate an announcement to tell the user how much HP has been gained and lost for each Player.
            fight.announceDamage(damageToDefendingPiece, damageToAttackingPiece, defendingPiece, attackingPiece);
            removeDestroyedParts();

            if (attackingAbsorbentBenefit > 0 || defendingAbsorbentBenefit > 0) {
                fight.announceHealthBenefit(attackingAbsorbentBenefit, defendingAbsorbentBenefit);
        }

        defendingAbsorbentBenefit = 0;
        attackingAbsorbentBenefit = 0;

        damageToAttackingPiece = 0;
        damageToDefendingPiece = 0;

        fight.finishTurn();
    }

    /**
     * If there was only one BattleMove (attack) during this turn then no defending (victim) FacePiece has yet been chosen.
     * This conditional statement covers that possibility by assigning a randomly-chosen FacePiece from the CurrentPlayer's opponent.
     */
    private void setDefendingPiece()
    {
        int pieceIndex = randomizer.nextInt(opponent.getPieces().size());
        defendingPiece = opponent.getPieces().get(pieceIndex);
    }

    /**
     * After FacePiece objects have lost HP, this method tells each Player object to remove any FacePieces with zero or less HP
     * Only the Player must lose pieces, not the FaceCharacter.
     */
    private void removeDestroyedParts()
    {
        currentPlayer.removeDestroyedPieces(fight);
        opponent.removeDestroyedPieces(fight);

        //if any FacePieces have been removed from the user (heroPlayer), associated BattleMoves must be removed from the buttons lists.
        fight.resetBattleButtons();
    }

    /**
     * To introduce an element of randomness to the game, this method is called to add or subtract a small number to calculations which effect HP.
     * @return int modifier
     */
    private int effectModifier()
    {
        return effectModifierArray[randomizer.nextInt(effectModifierArray.length - 1)];
    }

}
