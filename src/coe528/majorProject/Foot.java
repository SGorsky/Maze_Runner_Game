package coe528.majorProject;

import java.util.List;

public class Foot extends BodyPart {

    // private BodyPart lowerLeg;
    private Weapon weapon;

    public Foot(double health, double attackChance, String description) {
        super(health, attackChance, description);
        java.util.List<Attack> attacks = new java.util.ArrayList<Attack>(1);
        attacks.add(new Attack("Bludgeoning", "Kick", 1.2, 5));
        weapon = new Weapon(5, 1, "Foot", attacks, 1);
    }

    public Weapon getWeapon() {
        return weapon;
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
