package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class UpperArm extends BodyPart {

    // BodyPart upperChest;
    private BodyPart lowerArm;

    public UpperArm(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public LowerArm getLowerArm() {
        return (LowerArm) lowerArm;
    }

    public void setLowerArm(BodyPart lowerArm) {
        this.lowerArm = lowerArm;
    }

    @Override
    public int getNumberOfChildren() {
        if (lowerArm != null) {
            return 1 + lowerArm.getNumberOfChildren();
        } else {
            return 0;
        }
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (lowerArm != null) {
            children.add(lowerArm);
            children.addAll(lowerArm.getChildren());
        }
        return children;
    }

    public void removeLowerArm() {
        lowerArm = null;
    }
}
