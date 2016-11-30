package coe528.majorProject;

import java.util.List;

public class Head extends BodyPart {

    //BodyPart neck;
    public Head(double health, double attackChance, String description) {
        super(health, attackChance, description);
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
