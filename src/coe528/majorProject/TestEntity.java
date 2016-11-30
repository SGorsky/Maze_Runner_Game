package coe528.majorProject;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class TestEntity {
    private static List<Weapon> weaponsList = new ArrayList<Weapon>();
    private static List<Armor> armorList = new ArrayList<Armor>();
    
    private void readWeaponAndArmorFiles(){
        try{
        List<String> input;
        Path p = Paths.get("Weapons.txt");
        input = Files.readAllLines(p);
        for (int i = 0; i < input.size(); ++i) {
            String[] weaponInput = input.get(i).split(",");
            String name = weaponInput[0];
            int hands = Integer.valueOf(weaponInput[1]);
            double baseDamage = Double.valueOf(weaponInput[2]);
            double weight = Double.valueOf(weaponInput[3]);
            int count = (weaponInput.length - 4) / 3;
            List<Attack> attacks = new ArrayList<Attack>(4);
            for (int j = 0; j < count; ++j) {
                attacks.add(new Attack(
                        weaponInput[4 + j * 3], weaponInput[4 + j * 3 + 1], Double.valueOf(weaponInput[4 + j * 3 + 2]), baseDamage));
            }
            weaponsList.add(new Weapon(baseDamage, weight, name, attacks, hands));
        }
        Weapon.FIST = weaponsList.get(8);
        p = Paths.get("Armor.txt");
        input = Files.readAllLines(p);
        for (int i = 0; i < input.size(); ++i) {
            String[] armorInput = input.get(i).split(",");
            String type = armorInput[0];
            String name = armorInput[1];
            int level = Integer.valueOf(armorInput[2]);
            armorList.add(new Armor(type, level, name));
        }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void case1TestValidConstructor() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(armorList.size() - 1);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        assertTrue(entity.repOK());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void case2TestInvalidConstructor1() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(armorList.size() - 1);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(0, "Test Entity", weapon, armor, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void case3TestInvalidConstructor2() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(armorList.size() - 1);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(100, null, weapon, armor, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void case4TestInvalidConstructor3() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(armorList.size() - 1);
        Entity entity= new Enemy(100, "Test Entity", null, armor, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void case5TestInvalidConstructor4() {
        readWeaponAndArmorFiles();
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity= new Enemy(100, "Test Entity", weapon, null, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void case6TestInvalidConstructor5() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(armorList.size() - 1);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity= new Enemy(100, "Test Entity", weapon, armor, 0);
    }
    
    @Test
    public void case7TestReduceArmorLevel() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(0);//Get Padded armor (Level 1)
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        entity.reduceArmorLevel();
        assertEquals(0, entity.getArmor().getLevel());
    }
    
    @Test
    public void case8TestIsAlive() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(0);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        assertTrue(entity.isAlive());
        List<BodyPart> bodyParts = entity.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription() == "Head"){
                entity.removeBodyPart(bodyPart);
                assertFalse(entity.isAlive());
            }
        }
        assertTrue(entity.repOK());
        
        entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        assertTrue(entity.isAlive());
        bodyParts = entity.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription() == "Upper Chest"){
                entity.removeBodyPart(bodyPart);
                assertFalse(entity.isAlive());
            }
        }
        assertTrue(entity.repOK());
        
        entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        assertTrue(entity.isAlive());
        bodyParts = entity.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription() == "Neck"){
                entity.removeBodyPart(bodyPart);
                assertFalse(entity.isAlive());
            }
        }
        assertTrue(entity.repOK());
        
        entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        assertTrue(entity.isAlive());
        bodyParts = entity.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription() == "Lower Arm"){
                entity.removeBodyPart(bodyPart);
            }
        }
        assertTrue(entity.isAlive());
        assertTrue(entity.repOK());
    }
    
    @Test
    public void case9TestRemoveBodyParts1ValidArguments() {
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(0);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        List<BodyPart> bodyParts = entity.getBodyParts();
        
        int count = 0;
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription().contains("Upper Arm")){
                count += bodyPart.getNumberOfChildren() + 1;
                entity.removeBodyPart(bodyPart);
            }
        }
        assertEquals(bodyParts.size() - count, entity.getBodyParts().size());
        assertTrue(entity.repOK());
        
        entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        bodyParts = entity.getBodyParts();
        count = 0;
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription().contains("Lower Chest")){
                count += bodyPart.getNumberOfChildren() + 1;
                entity.removeBodyPart(bodyPart);
            }
        }
        assertEquals(bodyParts.size() - count, entity.getBodyParts().size());
        assertTrue(entity.repOK());
        
        entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        bodyParts = entity.getBodyParts();
        count = 0;
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.getDescription().contains("Neck")){
                count += bodyPart.getNumberOfChildren() + 1;
                entity.removeBodyPart(bodyPart);
            }
        }
        assertEquals(bodyParts.size() - count, entity.getBodyParts().size());
        assertTrue(entity.repOK());
    }

    @Test(expected = IllegalArgumentException.class)
    public void case9TestRemoveBodyPartsInvalidArguments(){
        readWeaponAndArmorFiles();
        Armor armor = armorList.get(0);
        Weapon weapon = weaponsList.get(weaponsList.size() - 1);
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        entity.removeBodyPart(null);
    }
    
    @Test
    public void case10TestGetAttacks(){
        readWeaponAndArmorFiles();
        Random random = new Random();
        Armor armor = armorList.get(0);
        int index= random.nextInt(weaponsList.size() - 1);
        Weapon weapon = weaponsList.get(index);
        Entity entity = new Enemy(100, "Test Entity", weapon, armor, 1);
        
        if (weapon.isOneHanded()){
            assertEquals(3 + weapon.getAttackList().size(), entity.getAttacks(false).size());
        }
        else {
            assertEquals(2 + weapon.getAttackList().size(), entity.getAttacks(false).size());
        }
    }
}