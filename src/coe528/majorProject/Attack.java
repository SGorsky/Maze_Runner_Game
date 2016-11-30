package coe528.majorProject;

public class Attack {
    /* OVERVIEW: The Attack class is a single attack that can be performed with a specific weapon. It has a type 
     * (see the constants for options), name and damage
     * This class is immutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = an Attack A
     *      A.damage = c.damage where { c.damage is an double | 0 < c.damage }
     *      A.name = c.name
     *      A.type = c.type where { c.type is an integer | 0 <= c.type <= 2 }
     *      
     * REP INVARIANT
     * c.damage is a positive double
     * c.name is not null
     * c.type is either 0, 1 or 2
     */
    
    public static final int PIERCING_ATTACK = 0;
    public static final int SLASHING_ATTACK = 1;
    public static final int BLUDGEONING_ATTACK = 2;
    private int type; // 0 = Piercing 1 = Slashing 2 = Bludgeoning
    private String name;
    private double damage;

    public Attack(String type, String name, double multiplier, double baseDamage) {
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (baseDamage <= 0){
            throw new IllegalArgumentException("Base Damage must be greater than 0");
        }
        switch (type) {
            case "Piercing":
                this.type = PIERCING_ATTACK;
                break;
            case "Slashing":
                this.type = SLASHING_ATTACK;
                break;
            case "Bludgeoning":
                this.type = BLUDGEONING_ATTACK;
                break;
            default:
                throw new IllegalArgumentException("Type must be either \"Piercing\", \"Slashing\" or \"Bludgeoning\"");
        }
        this.name = name;
        damage = multiplier * baseDamage;

    }

    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Attack
     */
    @Override
    public String toString() {
        switch (type) {
            case PIERCING_ATTACK:
                return name + "\nPiercing Damage\nDamage = " + damage;
            case SLASHING_ATTACK:
                return name + "\nSlashing Damage\nDamage = " + damage;
            case BLUDGEONING_ATTACK:
                return name + "\nBludgeoning Damage\nDamage = " + damage;
            default:
                return "";
        }
    }

    /**
     * @return the type damage the attack inflicts
     */
    public int getType() {
        return type;
    }

    /**
     * @return the name of the attack
     */
    public String getName() {
        return name;
    }

    /**
     * @return the damage the attack inflicts
     */
    public double getDamage() {
        return damage;
    }
    
    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        if (damage > 0){
            return false;
        }
        if (name != null){
            return false;
        }
        if (type != 0 && type != 1 && type != 2){
            return false;
        }
        
        return true;
    }
}
