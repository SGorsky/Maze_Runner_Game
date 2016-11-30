package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class LowerChest extends BodyPart {

    private List<BodyPart> upperLegs;
    // BodyPart upperChest;

    public LowerChest(double health, double attackChance, String description) {
        super(health, attackChance, description);
        upperLegs = new ArrayList<BodyPart>(2);
    }

    public void addUpperLeg(BodyPart upperLeg) {
        upperLegs.add(upperLeg);
    }

    public int getLegsCount() {
        return upperLegs.size();
    }

    public UpperLeg getUpperLeg(int index) {
        if (index < upperLegs.size()) {
            return (UpperLeg) upperLegs.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfChildren() {
        int count = 0;
        for (BodyPart bodyPart : upperLegs) {
            count += 1 + bodyPart.getNumberOfChildren();
        }
        return count;
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (upperLegs.size() > 0) {
            children.addAll(upperLegs);
            for (BodyPart bodyPart : upperLegs) {
                children.addAll(bodyPart.getChildren());
            }
        }
        return children;
    }

    public void removeUpperLeg(int index) {
        if (index < upperLegs.size()) {
            upperLegs.remove(index);
        }
    }
}
