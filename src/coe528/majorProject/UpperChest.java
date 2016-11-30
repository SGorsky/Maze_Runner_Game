package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class UpperChest extends BodyPart {

    private BodyPart lowerChest;
    private List<BodyPart> necks;
    private List<BodyPart> upperArms;

    public UpperChest(double health, double attackChance, String description) {
        super(health, attackChance, description);
        necks = new ArrayList<BodyPart>(1);
        upperArms = new ArrayList<BodyPart>(2);
    }

    public void addUpperArm(BodyPart upperArm) {
        upperArms.add(upperArm);
    }

    public UpperArm getUpperArm(int index) {
        if (index < upperArms.size()) {
            return (UpperArm) upperArms.get(index);
        } else {
            return null;
        }
    }

    public int getArmsCount() {
        return upperArms.size();
    }

    public int getLegsCount() {
        if (lowerChest != null) {
            return getLowerChest().getLegsCount();
        } else {
            return 0;
        }
    }

    public int getNeckCount() {
        return necks.size();
    }

    public void addNeck(BodyPart neck) {
        necks.add(neck);
    }

    public Neck getNeck(int index) {
        if (index < necks.size()) {
            return (Neck) necks.get(index);
        } else {
            return null;
        }
    }

    public LowerChest getLowerChest() {
        if (lowerChest != null) {
            return (LowerChest) lowerChest;
        } else {
            return null;
        }
    }

    public void setLowerChest(BodyPart lowerChest) {
        this.lowerChest = lowerChest;
    }

    @Override
    public int getNumberOfChildren() {
        int count = 0;
        for (BodyPart neck : necks) {
            count += 1 + neck.getNumberOfChildren();
        }
        for (BodyPart upperArm : upperArms) {
            count += 1 + upperArm.getNumberOfChildren();
        }
        if (lowerChest != null) {
            count += 1 + lowerChest.getNumberOfChildren();
        }
        return count;
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        for (BodyPart neck : necks) {
            children.add(neck);
            children.addAll(neck.getChildren());
        }
        for (BodyPart upperArm : upperArms) {
            children.add(upperArm);
            children.addAll(upperArm.getChildren());
        }
        if (lowerChest != null) {
            children.add(lowerChest);
            children.addAll(lowerChest.getChildren());
        }
        return children;
    }

    public void removeLowerChest() {
        lowerChest = null;
    }

    public void removeNeck(int index) {
        if (index < necks.size()) {
            necks.remove(index);
        }
    }

    public void removeUpperArm(int index) {
        if (index < upperArms.size()) {
            upperArms.remove(index);
        }
    }
}
