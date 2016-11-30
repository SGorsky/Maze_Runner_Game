package coe528.majorProject;

import java.util.List;

public class Body extends BodyPart {

    private BodyPart upperChest;

    public Body(double health, double attackChance, String description) {
        super(health, attackChance, description);
    }

    public UpperChest getUpperChest() {
        return (UpperChest) upperChest;
    }

    public void setUpperChest(BodyPart upperChest) {
        this.upperChest = upperChest;
    }

    public void removeLimb(BodyPart bodyPart) {
        if (upperChest.getChildren().contains(bodyPart)) {
            if (bodyPart instanceof Foot) {
                for (int i = 0; i < getUpperChest().getLegsCount(); ++i) {
                    if (bodyPart.getDescription() == getUpperChest().getLowerChest().getUpperLeg(i).getLowerLeg().getFoot()
                            .getDescription()) {
                        ((UpperChest) upperChest).getLowerChest().getUpperLeg(i).getLowerLeg().removeFoot();
                        break;
                    }
                }
            } else if (bodyPart instanceof Hand) {
                for (int i = 0; i < getUpperChest().getArmsCount(); ++i) {
                    if (bodyPart.getDescription() == getUpperChest().getUpperArm(i).getLowerArm().getHand().getDescription()) {
                        if (!getUpperChest().getUpperArm(i).getLowerArm().getHand().getWeapon().isOneHanded()) {
                            // Using 2 handed weapon: Lost a hand -> drop weapon
                            ((UpperChest) upperChest).getUpperArm((i + 1) % 2).getLowerArm().getHand().setWeapon(Weapon.FIST);
                        }
                        ((UpperChest) upperChest).getUpperArm(i).getLowerArm().removeHand();
                        break;
                    }
                }
            } else if (bodyPart instanceof Head) {
                for (int i = 0; i < getUpperChest().getNeckCount(); ++i) {
                    if (getUpperChest().getNeck(i).getHead() != null) {
                        if (bodyPart.getDescription() == getUpperChest().getNeck(i).getHead().getDescription()) {
                            ((UpperChest) upperChest).removeNeck(i);
                            break;
                        }
                    }
                }
            } else if (bodyPart instanceof LowerArm) {
                for (int i = 0; i < getUpperChest().getArmsCount(); ++i) {
                    if (getUpperChest().getUpperArm(i).getLowerArm() != null) {
                        if (bodyPart.getDescription() == getUpperChest().getUpperArm(i).getLowerArm().getDescription()) {
                            if (getUpperChest().getUpperArm(i).getLowerArm().getHand() != null
                                    && !getUpperChest().getUpperArm(i).getLowerArm().getHand().getWeapon().isOneHanded()) {
                                // Using 2 handed weapon: Lost a hand -> drop weapon
                                ((UpperChest) upperChest).getUpperArm((i + 1) % 2).getLowerArm().getHand().setWeapon(Weapon.FIST);
                            }
                            ((UpperChest) upperChest).getUpperArm(i).removeLowerArm();
                            break;
                        }
                    }
                }
            } else if (bodyPart instanceof LowerChest) {
                ((UpperChest) upperChest).removeLowerChest();
            } else if (bodyPart instanceof LowerLeg) {
                for (int i = 0; i < getUpperChest().getLegsCount(); ++i) {
                    if (getUpperChest().getLowerChest().getUpperLeg(i).getLowerLeg() != null) {
                        if (bodyPart.getDescription() == getUpperChest().getLowerChest().getUpperLeg(i).getLowerLeg().getDescription()) {
                            ((UpperChest) upperChest).getLowerChest().getUpperLeg(i).removeLowerLeg();
                            break;
                        }
                    }
                }
            } else if (bodyPart instanceof Neck) {
                for (int i = 0; i < getUpperChest().getNeckCount(); ++i) {
                    if (bodyPart.getDescription() == getUpperChest().getNeck(i).getDescription()) {
                        ((UpperChest) upperChest).removeNeck(i);
                        break;
                    }
                }
            } else if (bodyPart instanceof UpperArm) {
                for (int i = 0; i < getUpperChest().getArmsCount(); ++i) {
                    if (bodyPart.getDescription() == getUpperChest().getUpperArm(i).getDescription()) {
                        if (getUpperChest().getUpperArm(i).getLowerArm() != null
                                && getUpperChest().getUpperArm(i).getLowerArm().getHand() != null) {
                            if (!getUpperChest().getUpperArm(i).getLowerArm().getHand().getWeapon().isOneHanded()) {
                                // Using 2 handed weapon: Lost a hand -> drop weapon
                                ((UpperChest) upperChest).getUpperArm((i + 1) % 2).getLowerArm().getHand().setWeapon(Weapon.FIST);
                            }
                        }
                        ((UpperChest) upperChest).removeUpperArm(i);
                        break;
                    }
                }
            } else if (bodyPart instanceof UpperLeg) {
                for (int i = 0; i < getUpperChest().getLegsCount(); ++i) {
                    if (bodyPart.getDescription() == getUpperChest().getLowerChest().getUpperLeg(i).getDescription()) {
                        ((UpperChest) upperChest).getLowerChest().removeUpperLeg(i);
                        break;
                    }
                }
            }
        } else if (bodyPart instanceof UpperChest) {
            upperChest = null;
        }
    }

    @Override
    public int getNumberOfChildren() {
        return upperChest.getNumberOfChildren();
    }

    @Override
    public List<BodyPart> getChildren() {
        return upperChest.getChildren();
    }
}
