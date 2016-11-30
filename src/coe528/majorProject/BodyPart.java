package coe528.majorProject;

import java.util.List;

public abstract class BodyPart {

    private double health;
    private double attackChance;
    private String description;

    public BodyPart(double health, double attackChance, String description) {
        this.health = health;
        this.attackChance = attackChance;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public abstract int getNumberOfChildren();

    public abstract List<BodyPart> getChildren();

    public double getAttackChance() {
        return attackChance;
    }

    public void setAttackChance(double attackChance) {
        this.attackChance = attackChance;
    }

    public double getHealth() {
        return health;
    }

    public void lowerHealth(double amount) {
        health -= amount;

        if (health < 0) {
            health = 0;
        }
    }

    public String getDescription() {
        return description;
    }
}
