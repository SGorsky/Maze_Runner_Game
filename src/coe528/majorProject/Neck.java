package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class Neck extends BodyPart {

    // BodyPart upperChest;
    private BodyPart head;

    public Neck(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public Head getHead() {
        return (Head) head;
    }

    public void setHead(BodyPart head) {
        this.head = head;
    }

    @Override
    public int getNumberOfChildren() {
        if (head != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<BodyPart> getChildren() {
        List<BodyPart> children = new ArrayList<BodyPart>();
        if (head != null) {
            children.add(head);
        }
        return children;
    }

    public void removeHead() {
        head = null;
    }
}
