package coe528.majorProject;

public class Armor {
    /* OVERVIEW: The Armor class is the armor that the entity will wear. It has a weight, type 
     * (see the constants for options), level (how effectively it reduces incoming damage) and name
     * This class is mutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = a piece of Armor A
     *      A.level = c.level
     *      A.name = c.name
     *      A.type = { c.type is an integer | 0 <= c.type <= 3 }
     *      A.weight = c.weight { c.weight is an integer | 0 < c.weight }
     *      
     * REP INVARIANT
     * c.level is an integer
     * c.name is not null
     * c.type is either 0, 1, 2 or 3
     * c.weight is a positive integer
     */
    
    public static final int NO_ARMOR = 0;
    public static final int LIGHT_ARMOR = 1;
    public static final int MEDIUM_ARMOR = 2;
    public static final int HEAVY_ARMOR = 3;
    private int weight;
    private int type;
    private int level;
    private String name;

    public Armor(String type, int level, String name) {
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        switch (type) {
            case "None":
                this.type = NO_ARMOR;
                weight = 1;
                break;
            case "Light":
                this.type = LIGHT_ARMOR;
                weight = this.type * 2;
                break;
            case "Medium":
                this.type = MEDIUM_ARMOR;
                weight = this.type * 2;
                break;
            case "Heavy":
                this.type = HEAVY_ARMOR;
                weight = this.type * 2;
                break;
            default:
                throw new IllegalArgumentException("Type must be either \"None\", \"Light\", \"Medium\" or \"Heavy\"");
        }
        this.level = level;
        this.name = name;
    }

    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Armor
     */
    @Override
    public String toString() {
        switch (type) {
            case NO_ARMOR:
                return "No Armor\nWeight = " + weight;
            case LIGHT_ARMOR:
                return "Light Armor\n" + name + "\nLevel = " + level + "\nWeight = " + weight;
            case MEDIUM_ARMOR:
                return "Medium Armor\n" + name + "\nLevel = " + level + "\nWeight = " + weight;
            case HEAVY_ARMOR:
                return "Heavy Armor\n" + name + "\nLevel = " + level + "\nWeight = " + weight;
        }
        return "";
    }

    
    /**
     * @return the name of the armor
     */
    public String getName() {
        return name;
    }

    /**
     * @return the weight of the armor
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the armor type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the Armor's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * REQUIRES
     * @param armor1 the armor to compare the armor2 against
     * @param armor2 the armor that is being compared
     * @return a String describing the difference between the two armors
     * 
     * @Effects
     * Looks at the difference between the armors and returns a String describing the differences if any
     */
    public static String Compare(Armor armor1, Armor armor2) {
        if (armor1 == null || armor2 == null){
            throw new IllegalArgumentException("Input arguments cannot be null");
        }
        
        int difWeight = armor1.weight - armor2.weight;
        int difType = armor1.type - armor2.type;
        int difLevel = armor1.level - armor2.level;
        String output = "";
        if (difLevel != 0) {
            output += "Level Difference = ";
            if (difLevel > 0) {
                output += "+";
            }
            output += difLevel;
        }
        if (difType != 0) {
            output += "\nType Difference = ";
            switch (armor2.type) {
                case NO_ARMOR:
                    output += "No Armour -> ";
                    break;
                case LIGHT_ARMOR:
                    output += "Light Armour -> ";
                    break;
                case MEDIUM_ARMOR:
                    output += "Medium Armour -> ";
                    break;
                case HEAVY_ARMOR:
                    output += "Heavy Armour -> ";
                    break;
            }
            switch (armor1.type) {
                case NO_ARMOR:
                    output += "No Armour";
                    break;
                case LIGHT_ARMOR:
                    output += "Light Armour";
                    break;
                case MEDIUM_ARMOR:
                    output += "Medium Armour";
                    break;
                case HEAVY_ARMOR:
                    output += "Heavy Armour";
                    break;
            }
        }
        if (difWeight != 0) {
            output += "\nWeight Difference = ";
            if (difWeight > 0) {
                output += "+";
            }
            output += difWeight;
        }
        return output;
    }

    
    /**
     * EFFECTS:
     * Reduces the level of the armor by 1
     */
    public void reduceLevel() {
        --level;
    }

    
    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        if (name == null){
            return false;
        }
        if (type != 0 && type != 1 && type != 2 && type != 3){
            return false;
        }
        if (weight > 0){
            return false;
        }
        
        return true;
    }
}
