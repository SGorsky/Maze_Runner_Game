package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
    /* OVERVIEW: The Weapon class is the weapon that the entity will use to attack. It has a base damage, weight, 
     * name, list of attacks and a boolean describing if its a one handed weapon
     * This class is immutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = a Weapon A
     *      A.attacks = c.attacks where { c.attacks = List<Attack> | 0 < c.attack.size() }
     *      A.baseDamage = c.baseDamage where { c.baseDamage is a double | 0 < c.baseDamage }
     *      A.name = c.name
     *      A.oneHanded = c.oneHanded where { c.oneHanded is a boolean | c.oneHanded = true || c.oneHanded = false }
     *      A.weight = c.weight where { c.weight is a double | 0 < c.weight }
     *      
     * REP INVARIANT
     * A.attacks is not null and has at least 1 element
     * A.baseDamage is a positive double 
     * A.name is not null
     * A.oneHanded is a boolean
     * A.weight is a positive double    
     */
    
    public static Weapon FIST;
    private double baseDamage;
    private double weight;
    private String name;
    private List<Attack> attacks = new ArrayList<Attack>();
    private boolean oneHanded;

    public Weapon(double baseDamage, double weight, String name, List<Attack> attacks, int hands) {
        if (attacks == null || attacks.size() == 0){
            throw new IllegalArgumentException("Attacks cannot be null or have no elements inside it");
        }
        if (baseDamage <= 0){
            throw new IllegalArgumentException("Base Damage must be greater than 0");
        }
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (weight <= 0){
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        this.baseDamage = baseDamage;
        this.weight = weight;
        this.name = name;
        this.attacks = attacks;
        switch (hands) {
            case 1:
                oneHanded = true;
                break;
            case 2:
                oneHanded = false;
                break;
        }
    }

    
    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Weapon
     */
    @Override
    public String toString() {
        String output = name + "\nBase Damage = " + baseDamage + "\nWeight = " + weight + "\nAttacks\n=======\n";
        for (Attack attack : attacks) {
            output += attack.toString() + "\n";
        }
        return output;
    }

    /**
     * @return the name of the Weapon
     */
    public String getName() {
        return name;
    }

    /**
     * @return the weight of the Weapon
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return a list of attacks for the weapon
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Attack> getAttackList() {
        return (ArrayList<Attack>) ((ArrayList<Attack>) attacks).clone();
    }

    /**
     * @return a boolean describing if the weapon can be wielded in one handed
     */
    public boolean isOneHanded() {
        return oneHanded;
    }

    /**
     * REQUIRES
     * @param weapon1 the weapon to compare the weapon2 against
     * @param weapon2 the weapon that is being compared
     * @return a String describing the difference between the two weapons
     * 
     * @EFFECTS
     * Looks at the difference between the weapons and returns a String describing the differences if any
     */
    public static String compare(Weapon weapon1, Weapon weapon2) {
        double difWeight = weapon1.weight - weapon2.weight;
        double difDamage = weapon1.baseDamage - weapon2.baseDamage;
        String output = "";
        if (difDamage != 0) {
            output += "\nBase Damage Difference = " + difDamage;
        }
        if (difWeight != 0) {
            output += "\nWeight Difference = " + difWeight;
        }
        return output;
    }

    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        if (attacks == null || attacks.size() == 0){
            return false;
        }
        if (baseDamage <= 0){
            return false;
        }
        if (name == null){
            return false;
        }
        if (oneHanded != true && oneHanded != false){
            return false;
        }
        if (weight <= 0){
            return false;
        }
        
        return true;
    }
}
