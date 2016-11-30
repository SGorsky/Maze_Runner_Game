package coe528.majorProject;

public class Player extends Entity {

    private Point location;
    public static char firstChar;

    public Player(Point location, double health, String name, Weapon weapon, Armor armor, int numHeads) {
        
        super(health, name, weapon, armor, numHeads);
        this.location = location;
        firstChar = name.charAt(0);
    }

    public Point getLocation() {
        return location;
    }

    public boolean equipWeapon(Weapon weapon) {
        UpperChest upperChest = getBody().getUpperChest();
        if (upperChest.getArmsCount() > 0) {
            if (!weapon.isOneHanded()) {
                if (upperChest.getArmsCount() == 2 && upperChest.getUpperArm(0).getNumberOfChildren() == 2
                        && upperChest.getUpperArm(1).getNumberOfChildren() == 2) {
                    for (int i = 0; i < upperChest.getArmsCount(); ++i) {
                        upperChest.getUpperArm(i).getLowerArm().getHand().setWeapon(weapon);
                    }
                } else {
                    return false;
                }
            } else if (this.getWeapon().isOneHanded() && !this.getWeapon().getName().equals("Fist")) {
                boolean equipped = false;
                for (int i = 0; i < upperChest.getArmsCount(); ++i) {
                    if (upperChest.getUpperArm(0).getLowerArm().getHand().getWeapon().getName() == this.getWeapon().getName()) {
                        upperChest.getUpperArm(i).getLowerArm().getHand().setWeapon(weapon);
                        equipped = true;
                        break;
                    }
                }
                if (!equipped) {
                    equipWeaponToRandomHand(weapon);
                }
            } else {
                equipWeaponToRandomHand(weapon);
            }
            this.setWeapon(weapon);
            return true;
        }
        return false;
    }

    private void equipWeaponToRandomHand(Weapon weapon) {
        if (MainClass.random.nextInt(getBody().getUpperChest().getArmsCount()) == 0) {
            this.getBody().getUpperChest().getUpperArm(0).getLowerArm().getHand().setWeapon(weapon);
            getBody().getUpperChest().getUpperArm(1).getLowerArm().getHand().setWeapon(Weapon.FIST);
        } else {
            getBody().getUpperChest().getUpperArm(1).getLowerArm().getHand().setWeapon(weapon);
            getBody().getUpperChest().getUpperArm(0).getLowerArm().getHand().setWeapon(Weapon.FIST);
        }
    }
}
