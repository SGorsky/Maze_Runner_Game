package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class LowerArm extends BodyPart {

    private BodyPart hand;
    // BodyPart upperArm;

    public LowerArm(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public Hand getHand() {
        return (Hand) hand;
    }

    public void setHand(BodyPart hand) {
        this.hand = hand;
    }

    @Override
    public int getNumberOfChildren() {
        if (hand != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (hand != null) {
            children.add(hand);
        }
        return children;
    }

    public void removeHand() {
        hand = null;
    }
}
