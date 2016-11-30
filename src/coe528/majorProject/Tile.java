package coe528.majorProject;

public class Tile {
    /* OVERVIEW: The Tile class is a single space that the user can walk on. It only has a type (see the constants for options)
     * This class is mutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = a Tile A
     *      A.tileType = c.tileType where { c.tileType is an integer | 0 <= c.tileType <= 6}
     *      
     * REP INVARIANT
     * c.tileType is either 0, 1, 2, 3, 4, 5 or 6
     */
    
    public static final int EMPTY_TILE = 0;
    public static final int WALL_TILE = 1;
    public static final int PLAYER_TILE = 2;
    public static final int ARMOR_TILE = 3;
    public static final int WEAPON_TILE = 4;
    public static final int POTION_TILE = 5;
    public static final int EXIT_TILE = 6;

    private int tileType;

    public Tile() {
        tileType = 1;
    }

    /*
     * @return the tile type
     */
    public int getTileType() {
        return tileType;
    }

    
    /**
     * EFFECTS: set the tileType to EMPTY_TILE
     */
    public void setEmpty() {
        tileType = EMPTY_TILE;
    }

    /**
     * EFFECTS: set the tileType to PLAYER_TILE
     */
    public void setPlayer() {
        tileType = PLAYER_TILE;
    }

    /**
     * EFFECTS: set the tileType to ARMOR_TILE
     */
    public void setArmor() {
        tileType = ARMOR_TILE;
    }

    /**
     * EFFECTS: set the tileType to WEAPON_TILE
     */
    public void setWeapon() {
        tileType = WEAPON_TILE;
    }

    /**
     * EFFECTS: set the tileType to POTION_TILE
     */
    public void setPotion() {
        tileType = POTION_TILE;
    }

    /**
     * EFFECTS: set the tileType to EXIT_TILE
     */
    public void setExit() {
        tileType = EXIT_TILE;
    }
    
    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Tile
     */
    @Override
    public String toString() {
        switch (tileType) {
            case 0:
                return "Empty";
            case 1:
                return "Wall";
            case 2:
                return "Player";
            case 3:
                return "Armor";
            case 4:
                return "Weapon";
            case 5:
                return "Potion";
            case 6:
                return "Exit";
            default:
                return "";
        }
    }

    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        switch (tileType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }
}
