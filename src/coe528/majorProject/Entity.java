package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    /* OVERVIEW: The Entity class is the abstract parent class of Player and Enemy. It has a health, name, Weapon
     * armor and body
     * This class is mutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = an Entity A
     *      A.health = c.health { c.health is a double | 0 < health }
     *      A.name = c.name
     *      A.weapon = c.weapon
     *      A.armor = c.armor
     *      A.body = c.body
     *      
     * REP INVARIANT
     * c.health is a positive double
     * c.name is not null
     * c.weapon is not null
     * c.armor is not null
     * c.body is an instance of Body
     */

    private double health;
    private String name;
    private Weapon weapon;
    private Armor armor;
    private BodyPart body;

    public Entity(double health, String name, Weapon weapon, Armor armor, int numHeads) {
        if (health <= 0){
            throw new IllegalArgumentException("Health must be greater than 0");
        }
        if (name == null || name.length() == 0){
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (weapon == null){
            throw new IllegalArgumentException("Weapon cannot be null");
        }
        if (armor == null){
            throw new IllegalArgumentException("Armor cannot be null");
        }
        if (numHeads <= 0){
            throw new IllegalArgumentException("numHeads must be greater than 0");
        }
        this.health = health;
        this.name = name;
        this.weapon = weapon;
        this.armor = armor;
        body = new Body(health, 0, "Body");
        BodyPart lH = new Hand(0.1 * health, 40, "Left Hand");
        BodyPart rH = new Hand(0.1 * health, 40, "Right Hand");
        if (weapon.isOneHanded()) {
            if (MainClass.random.nextBoolean()) {
                ((Hand) lH).setWeapon(this.weapon);
            } else {
                ((Hand) rH).setWeapon(this.weapon);
            }
        } else {
            ((Hand) lH).setWeapon(this.weapon);
            ((Hand) rH).setWeapon(this.weapon);
        }
        BodyPart lLA = new LowerArm(0.15 * health, 60, "Left Lower Arm");
        // ((Hand)lH).lowerArm = lLA;
        ((LowerArm) lLA).setHand(lH);
        BodyPart lUA = new UpperArm(0.2 * health, 60, "Left Upper Arm");
        // ((LowerArm)lLA).upperArm = lUA;
        ((UpperArm) lUA).setLowerArm(lLA);
        BodyPart rLA = new LowerArm(0.15 * health, 60, "Right Lower Arm");
        // ((Hand)rH).lowerArm = rLA;
        ((LowerArm) rLA).setHand(rH);
        BodyPart rUA = new UpperArm(0.2 * health, 60, "Right Upper Arm");
        // ((LowerArm)rLA).upperArm = rUA;
        ((UpperArm) rUA).setLowerArm(rLA);
        BodyPart lF = new Foot(0.1 * health, 40, "Left Foot");
        BodyPart lLL = new LowerLeg(0.2 * health, 60, "Left Lower Leg");
        // ((Foot)lF).lowerLeg = lLL;
        ((LowerLeg) lLL).setFoot(lF);
        BodyPart lUL = new UpperLeg(0.25 * health, 50, "Left Upper Leg");
        // ((LowerLeg)lLL).upperLeg = lUL;
        ((UpperLeg) lUL).setLowerLeg(lLL);
        BodyPart rF = new Foot(0.1 * health, 40, "Right Foot");
        BodyPart rLL = new LowerLeg(0.2 * health, 60, "Right Lower Leg");
        // ((Foot)rF).lowerLeg = rLL;
        ((LowerLeg) rLL).setFoot(rF);
        BodyPart rUL = new UpperLeg(0.25 * health, 50, "Right Upper Leg");
        // ((LowerLeg)rLL).upperLeg = rUL;
        ((UpperLeg) rUL).setLowerLeg(rLL);
        BodyPart lowerChest = new LowerChest(0.4 * health, 50, "Lower Chest");
        // ((UpperLeg)rUL).lowerChest = lowerChest;
        // ((UpperLeg)lUL).lowerChest = lowerChest;
        ((LowerChest) lowerChest).addUpperLeg(lUL);
        ((LowerChest) lowerChest).addUpperLeg(rUL);
        BodyPart upperChest = new UpperChest(health, getUpperChestHitChance(13), "Upper Chest");
        // ((UpperArm)rUA).upperChest = upperChest;
        // ((UpperArm)lUA).upperChest = upperChest;
        ((UpperChest) upperChest).addUpperArm(lUA);
        ((UpperChest) upperChest).addUpperArm(rUA);
        // ((LowerChest)lowerChest).upperChest = upperChest;
        ((UpperChest) upperChest).setLowerChest(lowerChest);
        if (numHeads > 1) {
            for (int i = 0; i < numHeads; ++i) {
                BodyPart neck = new Neck(0.1 * health, 20, "Neck #" + (i + 1));
                BodyPart head = new Head(0.1 * health, 20, "Head #" + (i + 1));
                ((Neck) neck).setHead(head);
                // ((Head)head).neck = neck;
                // ((Neck) neck).upperChest = upperChest;
                ((UpperChest) upperChest).addNeck(neck);
            }
        } else {
            BodyPart neck = new Neck(0.1 * health, 20, "Neck");
            BodyPart head = new Head(0.1 * health, 20, "Head");
            ((Neck) neck).setHead(head);
            // ((Head)head).neck = neck;
            // ((Neck) neck).upperChest = upperChest;
            ((UpperChest) upperChest).addNeck(neck);
        }
        ((Body) body).setUpperChest(upperChest);
    }

    /**
     * REQUIRES: body is not null and body.getUpperChest() is not null
     *
     * @EFFECTS
     * @return a List<BodyPart> which is a list of BodyPart that are in the body
     */
    public List<BodyPart> getBodyParts() {
        List<BodyPart> bodyParts = new ArrayList<BodyPart>();
        UpperChest upperChest = getBody().getUpperChest();
        bodyParts.add(upperChest);
        for (int i = 0; i < getBody().getUpperChest().getNeckCount(); ++i) {
            bodyParts.add(upperChest.getNeck(i));
            if (upperChest.getNeck(i).getHead() != null) {
                bodyParts.add(upperChest.getNeck(i).getHead());
            }
        }
        for (int i = 0; i < getBody().getUpperChest().getArmsCount(); ++i) {
            bodyParts.add(upperChest.getUpperArm(i));
            if (upperChest.getUpperArm(i).getLowerArm() != null) {
                bodyParts.add(upperChest.getUpperArm(i).getLowerArm());
                if (upperChest.getUpperArm(i).getLowerArm().getHand() != null) {
                    bodyParts.add(upperChest.getUpperArm(i).getLowerArm().getHand());
                }
            }
        }
        if (upperChest.getLowerChest() != null) {
            bodyParts.add(upperChest.getLowerChest());
            for (int i = 0; i < getBody().getUpperChest().getLegsCount(); ++i) {
                bodyParts.add(upperChest.getLowerChest().getUpperLeg(i));
                if (upperChest.getLowerChest().getUpperLeg(i).getLowerLeg() != null) {
                    bodyParts.add(upperChest.getLowerChest().getUpperLeg(i).getLowerLeg());
                    if (upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot() != null) {
                        bodyParts.add(upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot());
                    }
                }
            }
        }
        while (bodyParts.contains(null)) {
            bodyParts.remove(null);
        }
        return bodyParts;
    }

    
    
    /**
     * REQUIRES:
     * @param outputOptions whether the attack options should be output to the console
     * @return a List<Attack> containing all possible attacks that can be done by the entity
     * EFFECT: returns a List<Attack> containing all possible attacks and potentially outputting them to the console
     */
    public List<Attack> getAttacks(boolean outputOptions) {
        String output = "";
        List<Attack> attacks = new ArrayList<Attack>();
        UpperChest upperChest = ((Body) body).getUpperChest();
        if (weapon.isOneHanded()) {
            for (int i = 0; i < upperChest.getArmsCount(); ++i) {
                if (upperChest.getUpperArm(i).getLowerArm() != null && upperChest.getUpperArm(i).getLowerArm().getHand() != null) {
                    List<Attack> tempAttacks = upperChest.getUpperArm(i).getLowerArm().getHand().getWeapon().getAttackList();
                    for (int j = 0; j < tempAttacks.size(); ++j) {
                        attacks.add(tempAttacks.get(j));
                        if (outputOptions) {
                            output += attacks.size() + " " + tempAttacks.get(j).getName() + " -> "
                                    + upperChest.getUpperArm(i).getLowerArm().getHand().getWeapon().getName() + " -> "
                                    + upperChest.getUpperArm(i).getLowerArm().getHand().toString() + "\n";
                        }
                    }
                }
            }
        } else if (upperChest.getArmsCount() > 0 && upperChest.getUpperArm(0).getLowerArm() != null
                && upperChest.getUpperArm(0).getLowerArm().getHand() != null) {
            List<Attack> tempAttacks = upperChest.getUpperArm(0).getLowerArm().getHand().getWeapon().getAttackList();
            for (int i = 0; i < tempAttacks.size(); ++i) {
                attacks.add(tempAttacks.get(i));
                if (outputOptions) {
                    output += attacks.size() + " " + tempAttacks.get(i).getName() + " -> " + weapon.getName() + " -> " + "Both Hands\n";
                }
            }
        }
        for (int i = 0; i < upperChest.getLegsCount(); ++i) {
            if (upperChest.getLowerChest().getUpperLeg(i).getLowerLeg() != null
                    && upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot() != null) {
                List<Attack> tempAttacks = upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot().getWeapon().getAttackList();
                for (int j = 0; j < tempAttacks.size(); ++j) {
                    attacks.add(tempAttacks.get(j));
                    if (outputOptions) {
                        output += attacks.size() + " " + tempAttacks.get(j).getName() + " -> "
                                + upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot().toString().split("-")[0] + "\n";
                    }
                }
            }
        }
        if (output.length() != 0) {
            output.substring(0, output.length() - 2);
            System.out.println("Attack Options\n==============\n" + output);
        }
        return attacks;
    }

    
    /**
     * @return the String representation of all possible Attacks
     */
    public String getAttacksString() {
        String output = "";
        List<Attack> attacks = new ArrayList<Attack>();
        UpperChest upperChest = ((Body) body).getUpperChest();
        if (weapon.isOneHanded()) {
            for (int i = 0; i < upperChest.getArmsCount(); ++i) {
                if (upperChest.getUpperArm(i).getLowerArm() != null && upperChest.getUpperArm(i).getLowerArm().getHand() != null) {
                    List<Attack> tempAttacks = upperChest.getUpperArm(i).getLowerArm().getHand().getWeapon().getAttackList();
                    for (int j = 0; j < tempAttacks.size(); ++j) {
                        attacks.add(tempAttacks.get(j));
                        output += attacks.size() + " " + tempAttacks.get(j).getName() + " -> "
                                + upperChest.getUpperArm(i).getLowerArm().getHand().getWeapon().getName() + " -> "
                                + upperChest.getUpperArm(i).getLowerArm().getHand().toString() + "\n";
                    }
                }
            }
        } else if (upperChest.getArmsCount() > 0 && upperChest.getUpperArm(0).getLowerArm() != null
                && upperChest.getUpperArm(0).getLowerArm().getHand() != null) {
            List<Attack> tempAttacks = upperChest.getUpperArm(0).getLowerArm().getHand().getWeapon().getAttackList();
            for (int i = 0; i < tempAttacks.size(); ++i) {
                attacks.add(tempAttacks.get(i));
                output += attacks.size() + " " + tempAttacks.get(i).getName() + " -> " + weapon.getName() + " -> " + "Both Hands\n";
            }
        }
        for (int i = 0; i < upperChest.getLegsCount(); ++i) {
            if (upperChest.getLowerChest().getUpperLeg(i).getLowerLeg() != null
                    && upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot() != null) {
                List<Attack> tempAttacks = upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot().getWeapon().getAttackList();
                for (int j = 0; j < tempAttacks.size(); ++j) {
                    attacks.add(tempAttacks.get(j));
                    output += attacks.size() + " " + tempAttacks.get(j).getName() + " -> "
                            + upperChest.getLowerChest().getUpperLeg(i).getLowerLeg().getFoot().toString().split("-")[0] + "\n";
                }
            }
        }
        if (output.length() != 0) {
            output.substring(0, output.length() - 2);
            output = "Attack Options\n==============\n" + output;
        }
        return output;
    }

    
    /**
     * @return the Entity's health
     */
    public double getHealth() {
        return health;
    }

    /**
     * @return the Entity's name
     */
    public String getName() {
        return name;
    }

    
    /**
     * @return the Entity's weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * @return the Entity's armor
     */
    public Armor getArmor() {
        return armor;
    }

    
    /**
     * @param armor the armor that the Entity will equip (replacing the previously equipped armor)
     */
    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    
    /**
     * EFFECT: Reduces the current equipped armor's level by 1
     */
    public void reduceArmorLevel() {
        armor.reduceLevel();
    }

    /**
     * @return the Entity's weight
     */
    public double getWeight() {
        return weapon.getWeight() * armor.getWeight();
    }

    /**
     * @return the Entity's body
     */
    protected Body getBody() {
        return (Body) body;
    }

    
    /**
     * @param numParts is the number of child BodyParts (not including neck and heads) connected to the UpperChest
     * @return the attack chance as a decimal where 1.0 is a 100% hit chance and 0.0 is a 0% hit chance
     */
    private double getUpperChestHitChance(int numParts) {
        return 0.1623376623 * numParts * numParts - 10.43206793 * numParts + 115.4895105;
    }

    /**
     * @param bodyPart is the bodyPart that will be removed from the body along with all of bodyPart's children
     * EFFECT: Remove bodyPart from the body and if bodyPart is an instance of UpperArm, LowerArm or Hand and
     *         the equipped weapon is two handed, the player drops the weapon. Also update the upperchest hit chance
     */
    public void removeBodyPart(BodyPart bodyPart) {
        if (bodyPart != null){
            ((Body) body).removeLimb(bodyPart);
            if (bodyPart instanceof UpperArm || bodyPart instanceof LowerArm || bodyPart instanceof Hand) {
                if (!weapon.isOneHanded()) {
                    weapon = Weapon.FIST;
                }
            }
            if (getBody().getUpperChest() != null) {
                int numPartsLeft = getBody().getUpperChest().getNumberOfChildren();
                for (int i = 0; i < getBody().getUpperChest().getNeckCount(); ++i) {
                    numPartsLeft -= 1 + getBody().getUpperChest().getNeck(i).getNumberOfChildren();
                }
                ((Body) body).getUpperChest().setAttackChance(getUpperChestHitChance(numPartsLeft));
            }
        }
        else {
            throw new IllegalArgumentException("bodyPart cannot be null");
        }
    }

    
    /**
     * @return a boolean describing whether the Entity is alive
     */
    public boolean isAlive() {
        if (getBody().getUpperChest() == null) {
            return false;
        }
        if (getBody().getUpperChest().getNeckCount() > 0) {
            int neckCount = getBody().getUpperChest().getNeckCount();
            boolean hasHead = false;
            for (int i = 0; i < neckCount; ++i) {
                if (getBody().getUpperChest().getNeck(i).getHead() != null) {
                    hasHead = true;
                    break;
                }
            }
            return hasHead;
        }
        return false;
    }

    
    /**
     * @param weapon is the new weapon that will be equipped
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        if (health <= 0){
            return false;
        }
        if (weapon == null){
            return false;
        }
        if (armor == null){
            return false;
        }
        if (body instanceof Body == false){
            return false;
        }
        return true;
    }

    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Entity
     */
    @Override
    public String toString() {
        return "Entity======\nName: " + name + "\nWeapon: " + weapon + "\nArmor: " + armor;
    }
}
