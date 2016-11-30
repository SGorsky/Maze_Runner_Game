package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class LowerLeg extends BodyPart {

    private BodyPart foot;
    // BodyPart upperLeg;

    public LowerLeg(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public Foot getFoot() {
        return (Foot) foot;
    }

    public void setFoot(BodyPart foot) {
        this.foot = foot;
    }

    @Override
    public int getNumberOfChildren() {
        if (foot != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (foot != null) {
            children.add(foot);
        }
        return children;
    }

    public void removeFoot() {
        foot = null;
    }
}
