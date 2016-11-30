package coe528.majorProject;

import java.util.List;

public class Hand extends BodyPart {

    //BodyPart lowerArm;
    private Weapon weapon;

    public Hand(double health, double attackChance, String description) {
        super(health, attackChance, description);
        weapon = Weapon.FIST;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public String toString() {
        return getDescription() + " - " + weapon.getName() + " equipped";
    }

    @Override
    public int getNumberOfChildren() {
        return 0;
    }

    @Override
    public List<BodyPart> getChildren() {
        return null;
    }
}
