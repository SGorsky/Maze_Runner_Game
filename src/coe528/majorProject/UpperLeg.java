package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class UpperLeg extends BodyPart {
    private BodyPart lowerLeg;
    // BodyPart lowerChest;

    public UpperLeg(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public LowerLeg getLowerLeg() {
        return (LowerLeg) lowerLeg;
    }

    public void setLowerLeg(BodyPart lowerLeg) {
        this.lowerLeg = lowerLeg;
    }

    @Override
    public int getNumberOfChildren() {
        if (lowerLeg != null) {
            return 1 + lowerLeg.getNumberOfChildren();
        }
        else {
            return 0;
        }
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (lowerLeg != null) {
            children.add(lowerLeg);
            children.addAll(lowerLeg.getChildren());
        }
        return children;
    }

    public void removeLowerLeg() {
        lowerLeg = null;
    }
}
