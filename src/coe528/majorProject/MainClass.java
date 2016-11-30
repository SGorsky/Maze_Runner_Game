package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class MainClass {

    // The size of the maze, preset and hardcoded into the program (must be an odd number)
    public static final int MAZE_SIZE = 51;
    // Initial health given to the player and distributed along the body parts
    public static final int PLAYER_HEALTH = 100;
    // A Random used to generate random numbers to make the game different every time
    public static Random random = new Random();
    // The probability of finding a new weapon, piece of armor or a potion in a corner (used during maze generation)
    public static double itemCornerProbability = 1;// 0.4
    // Chance of getting into a battle with an enemy
    private static double enemyChance = 0.05;
    // Chance of getting into a battle with the final boss (scales by 2 after each enemy dies)
    private static double bossChance = 0.01;
    // The dimensions of the maze to display to the player during the game
    private static int displayMazeSize = 10;

    public static void main(String[] args) {
        String controls = "Controls\n========\n" + "Move Up:\tu, up, n, north\nMove Down:\td, down, s, south\n"
                + "Move Left:\tl, left, w, west\nMove Right:\tr, right, e, east";
        // Each is a list of all the possible items available in the game
        List<Weapon> weaponsList = new ArrayList<Weapon>();
        List<Armor> armorList = new ArrayList<Armor>();
        List<String> firstName = new ArrayList<String>();
        List<String> secondNameShort = new ArrayList<String>();
        List<String> secondNameLong = new ArrayList<String>();
        List<String> thirdNameLong = new ArrayList<String>();
        // Read in the data from Weapons.txt, Armor.txt, First Names.txt, Second Names I.txt, Second Names II.txt and Third Names.txt
        try {
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
                    attacks.add(new Attack(weaponInput[4 + j * 3], weaponInput[4 + j * 3 + 1], 
                            Double.valueOf(weaponInput[4 + j * 3 + 2]), baseDamage));
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
            p = Paths.get("First Names.txt");
            input = Files.readAllLines(p);
            for (int i = 0; i < input.size(); ++i) {
                firstName.addAll(input);
            }
            p = Paths.get("Second Names I.txt");
            input = Files.readAllLines(p);
            for (int i = 0; i < input.size(); ++i) {
                secondNameShort.addAll(input);
            }
            p = Paths.get("Second Names II.txt");
            input = Files.readAllLines(p);
            for (int i = 0; i < input.size(); ++i) {
                secondNameLong.addAll(input);
            }
            p = Paths.get("Third Names.txt");
            input = Files.readAllLines(p);
            for (int i = 0; i < input.size(); ++i) {
                thirdNameLong.addAll(input);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        Scanner scanner = new Scanner(System.in);
        Player player = null;
        // Display main menu prompt
        System.out.println("Welcome player, to INSERT GAME TITLE (this was done on purpose)\n" + "Your mission, should you choose to accept it (if "
                + "you don't accept it, I don't know why you're playing this game, just turn this off now) is to kill "
                + "the dangerous INSERT SCARY ENEMY NAME, a INSERT A NUMBER headed INSERT SCARY FANTASY SPECIES and then "
                + "escape the maze. You cannot escape the maze without killing the beast but he won't come out and fight "
                + "you right away. Kill enough of his minions to bait him into revealing himself and then kill him." + "\n\n"
                + "Type the number next to the menu option you want to choose to select it. "
                + "For example, it says under here \"1 Play\", \"2 Instructions\" and \"3 Exit\".\n"
                + "So if you wanted to play you'd type 1 and press Enter or if you wanted to read the instructions"
                + " you'd type 2 and read some more of my static but motivational and possibly heartwarming"
                + " words. Of course if you're already bored and want to exit, type 3 and go do something else.\n");
        while (true) {
            // Display and handle main menu options
            System.out.println("\n1 Play\n2 Instructions\n3 Exit");
            String menuInput = scanner.nextLine();
            switch (menuInput) {
                case "1":
                    Maze.getInstance().generateNewMaze();
                    // Get player's name (or set one for them if they enter something weird)
                    System.out.println("Welcome to the game\nPlease enter your name below (the first "
                            + "letter of your name will be used to identify your player in the maze. It is recommended "
                            + "that you do not use the letter 'X' as that is what the walls of the maze look like): ");
                    String name = scanner.nextLine().trim();
                    if (name.length() == 0 || name.length() > 20 || !Character.isLetter(name.charAt(0))) {
                        System.out.println("I'm sorry. I don't understand what you want me to call you. I'll just call you Bob. "
                                + "Finish the game like a good little boy (yes, I just assumed your gender) and I'll give you some cake.");
                        name = "Bob";
                    }
                    // Set start location as the center of the maze or as close as possible to the center if the center tile is a wall
                    Point start = new Point(MAZE_SIZE / 2, MAZE_SIZE / 2);
                    while (true) {
                        if (Maze.getInstance().getTileType(start) == Tile.EMPTY_TILE) {
                            break;
                        } else {
                            switch (random.nextInt(4)) {
                                case 0:
                                    start.incrementX(1);
                                    break;
                                case 1:
                                    start.incrementX(-1);
                                    break;
                                case 2:
                                    start.incrementY(1);
                                    break;
                                case 3:
                                    start.incrementY(-1);
                                    break;
                            }
                        }
                    }
                    // Create new player at determined location. They will be naked and defenseless (no weapons)
                    player = new Player(start, PLAYER_HEALTH, name, weaponsList.get(8), armorList.get(9), 1);
                    Maze.getInstance().setPlayerTile(start);
                    System.out.println("\n" + controls + "\n");
                    boolean dead_DoneOrExit = false;
                    while (!dead_DoneOrExit) {
                        System.out.print(Maze.getInstance().displayPlayerLocation(player.getLocation(), displayMazeSize));
                        String movement;
                        while (true) {
                            movement = scanner.nextLine();
                            if (movement.length() > 0) {
                                break;
                            }
                        }
                        // Get direction based off user input
                        Point dir = Movement(movement);
                        // Check if destination tile is not a wall
                        if (Maze.getInstance().getTileType(Point.add2Points(start, dir)) != Tile.WALL_TILE) {
                            Maze.getInstance().setEmptyTile(player.getLocation());
                            player.getLocation().incrementX(dir.getX());
                            player.getLocation().incrementY(dir.getY());
                            // Do something different based on whether the tile is empty or has an item on it
                            switch (Maze.getInstance().getTileType(player.getLocation())) {
                                case Tile.EMPTY_TILE:
                                    double chance = random.nextDouble();
                                    if (chance < enemyChance) {
                                        int weaponIndex = random.nextInt(weaponsList.size());
                                        int armourIndex = random.nextInt(armorList.size());
                                        int numHeads = 1 + random.nextInt(3);
                                        double health = random.nextDouble() * 100;
                                        String enemyName = firstName.get(random.nextInt(firstName.size()));
                                        if (random.nextBoolean()) {
                                            enemyName += " The " + secondNameShort.get(random.nextInt(secondNameShort.size()));
                                        } else {
                                            enemyName += " " + secondNameLong.get(random.nextInt(secondNameLong.size())) + " "
                                                    + thirdNameLong.get(random.nextInt(thirdNameLong.size()));
                                        }
                                        Enemy enemy = new Enemy(
                                                health, enemyName, weaponsList.get(weaponIndex), armorList.get(armourIndex), numHeads);
                                        dead_DoneOrExit = !MainClass.Combat(player, enemy, scanner);
                                        if (dead_DoneOrExit) {
                                            Maze.getInstance().setEmptyTile(player.getLocation());
                                        }
                                        bossChance *= 2;
                                    } else if (chance < enemyChance + bossChance) {
                                        // Choose a weapon from Longsword, Greatsword, Battleaxe, Spear and Great Axe
                                        int weaponIndex = random.nextInt(5);
                                        // Choose one of the level 3 armors
                                        int armourIndex = random.nextInt(3) * 3 + 2;
                                        int numHeads = 3;
                                        System.out.println("Boss fight!");
                                        double health = random.nextDouble() * 50 + 100.0;
                                        String enemyName = firstName.get(random.nextInt(firstName.size()));
                                        enemyName += " " + secondNameLong.get(random.nextInt(secondNameLong.size())) + " "
                                                + thirdNameLong.get(random.nextInt(thirdNameLong.size()));
                                        Enemy enemy = new Enemy(
                                                health, enemyName, weaponsList.get(weaponIndex), armorList.get(armourIndex), numHeads);
                                        dead_DoneOrExit = !MainClass.Combat(player, enemy, scanner);
                                        if (dead_DoneOrExit) {
                                            Maze.getInstance().setEmptyTile(player.getLocation());
                                        } else {
                                            enemyChance = 0;
                                            bossChance = 0;
                                            List<Point> mazeCorners = new ArrayList<Point>(4);
                                            double distance = -1;
                                            mazeCorners.add(new Point(1, 1));
                                            mazeCorners.add(new Point(1, MAZE_SIZE - 2));
                                            mazeCorners.add(new Point(MAZE_SIZE - 2, 1));
                                            mazeCorners.add(new Point(MAZE_SIZE - 2, MAZE_SIZE - 2));
                                            int index = -1;
                                            for (int i = 0; i < mazeCorners.size(); i++) {
                                                if (Maze.getInstance().getTileType(mazeCorners.get(i)) != Tile.WALL_TILE) {
                                                    double tempD = getDistance(player.getLocation(), mazeCorners.get(i));
                                                    if (distance < tempD) {
                                                        index = i;
                                                        distance = tempD;
                                                    }
                                                }
                                            }
                                            Maze.getInstance().setExitTile(mazeCorners.get(index));
                                            switch (index) {
                                                case 0:
                                                    System.out.println("The exit is located at the North West corner of the maze.");
                                                    break;
                                                case 1:
                                                    System.out.println("The exit is located at the South West corner of the maze.");
                                                    break;
                                                case 2:
                                                    System.out.println("The exit is located at the North East corner of the maze.");
                                                    break;
                                                case 3:
                                                    System.out.println("The exit is located at the South East corner of the maze.");
                                                    break;
                                            }
                                            displayMazeSize = 20;
                                        }
                                    }
                                    // else {
                                    // Moved without getting into a fight
                                    // }
                                    break;
                                case Tile.ARMOR_TILE:
                                    // Choose a ranodm piece of armor and display its stats and compare them to the user's
                                    // armor. Then offer them the choice to use swap armors
                                    int index = random.nextInt(armorList.size() - 1);// Don't allow no armor option
                                    System.out.println("You've come across a new piece of armor while traveling through the maze.\n"
                                            + "Armor Stats\n===========\n" + armorList.get(index).toString() + "\nComparison\n==========\n"
                                            + Armor.Compare(armorList.get(index), player.getArmor()) + "\nWould you like "
                                            + "to replace your current armor (" + player.getArmor().getName() + ") with it? (y/n)");
                                    if (scanner.nextLine().toLowerCase().equals("y")) {
                                        player.setArmor(armorList.get(index));
                                        System.out.println(player.getArmor().getName() + " equipped.");
                                    }
                                    break;
                                case Tile.WEAPON_TILE:
                                    // Choose a ranodm weapon and display its stats and compare them to the user's
                                    // weapon. Then offer them the choice to use the weapon
                                    index = random.nextInt(weaponsList.size() - 2);// Don't allow fist or feet option
                                    System.out.println("You've come across a new weapon while traveling through the maze.\n"
                                            + "Weapon Stats\n============\n" + weaponsList.get(index).toString() + "\nComparison\n"
                                            + "==========" + Weapon.compare(weaponsList.get(index), player.getWeapon())
                                            + "\nWould you like to replace your current weapon (" + player.getWeapon().getName()
                                            + ") with it? (y/n)");
                                    if (scanner.nextLine().toLowerCase().equals("y")) {
                                        if (player.equipWeapon(weaponsList.get(index))) {
                                            System.out.println(player.getWeapon().getName() + " equipped.");
                                        } else if (weaponsList.get(index).isOneHanded()) {
                                            System.out.println("Unable to equip " + weaponsList.get(index).getName()
                                                    + ". You don't have any arms.");
                                        } else {
                                            System.out.println("Unable to equip " + weaponsList.get(index).getName()
                                                    + ". You need two arms to use this weapon.");
                                        }
                                    }
                                    break;
                                case Tile.POTION_TILE:
                                    // Tell the user they found a potion. Restore their limbs and give them an additional 25 health
                                    System.out.println("You found a strange vial of liquid in a corner. Not being the brightest "
                                            + "of your species, you decide to drink it without any thought of the consequences. Luckily, its a "
                                            + "healing potion and you gain some health as well as regrow any missing limbs you lost in combat. "
                                            + "You feel stronger than ever.");
                                    player = new Player(
                                            player.getLocation(), player.getHealth() + 25, player.getName(), player.getWeapon(),
                                            player.getArmor(), 1);
                                    break;
                                case Tile.EXIT_TILE:
                                    System.out.println("Congratulations. You've finished the game. There's no prize. No parade. "
                                            + "No celebration. Sorry. Don't have the funding for that kind of thing, you know."
                                            + "\nBut you won.\nSo thats it. Goodbye. Farewell. Maybe you'll play me again soon. "
                                            + "Press Enter to exit.");
                                    scanner.nextLine();
                                    dead_DoneOrExit = true;
                                    break;
                            }
                            Maze.getInstance().setPlayerTile(player.getLocation());
                        } else {
                            System.out.println("The walls of the maze prevent you from moving in that direction.");
                        }
                    }
                    break;
                case "2":
                    // Display instructions
                    System.out.println("\nINSTRUCTIONS\n============\nThe game is simple, you're in a maze and you're "
                            + "trying to get out. To do that, you need to kill a boss and find the exit. "
                            + "\nHow do you do that? Simple. You'll be shown part of the maze around you and you need to find "
                            + "your way around the maze. But don't worry, you'll somehow know the cardinal directions to "
                            + "anything you want to find (except the exit). Don't ask why. Somethings are better left unknown. "
                            + "Like if if Wile E. Coyote had enough money to buy all that stuff from ACME Corp, why didn't "
                            + "he just pay someone to catch the roadrunner or buy his dinner?\nAnyway, the following commands "
                            + "will let you control your player in the game (case insensitive): \n\n" + controls
                            + "\n\nSimple enough right? Your game view will include a seciton of the maze "
                            + "(see below)\n X X      \nXX X XXXXX\n X X      \n X X XXXXX\n X X     X\n X XX XXXX\n X    *   \n XXXXX XXX"
                            + "\n     X   X\nXXXX XXX X\n\nX = Wall\n  = Empty Space\nFirst letter of your name = You\n"
                            + "* = Item (Weapon, Armor, Healing Potion)\n# = Exit\n\nMovement\n========\nI'm going to blow "
                            + "your mind. Your character, believe it or not...CAN'T WALK THROUGH WALLS. Yeah. Crazy. I know right?\nSo in "
                            + "this scenario (below), your character (P) can't move down. But if they take 1 step to the left, they can"
                            + "\n\n    P      \nXXX XXXXXX\n\n                                        X\nVertical walls look like "
                            + "this           X\n(they have a bit of space in            X\n" + "between cause of line spacing)          X"
                            + "\nA passage through a verical wall        X\n" + "looks like this --------------->\n"
                            + "                                        X\n" + "                                        X\n\n"
                            + "Horizontal walls look like this\nwith a passage through the wall\nthat looks like this ↓\n"
                            + "                     ↓\nXXXXXXXXXXXXXXXXXXXXX XXXX");
                    break;
                case "3":
                    // Quit the game after a Pirates of the Carribean reference
                    System.out.println("\nWell you want to exit do ya? I am disinclined to acquiesce to your request...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Means no");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Just kidding :) Press enter one more time to exit.");
                    scanner.nextLine();
                    scanner.close();
                    System.exit(0);
                    break;
                case "33":
                    // Exit immediately
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    // User entered something else. Respond seriously or not...
                    String[] randRemark = new String[5];
                    randRemark[0] = "I have no idea what you want from me. ¯\\_(ツ)_/¯";
                    randRemark[1] = "ENGLISH M*TH*R F**K*R! DO YOU SPEAK IT! ONLY TYPE THE NUMBERS YOU SEE UP THERE.";
                    randRemark[2] = "I believe we've reached a point in our relationship where I feel like neither of "
                            + "us understands each other. Its not you, its me. I need to sort some things out and I "
                            + "think we should start seeing other people...unless of course you start listening to what I say.";
                    randRemark[3] = "I'm sorry. My responses are limited. You must ask the right questions.";
                    randRemark[4] = "Invalid Input. Please try again. Only enter the numbers next to the menu options above";
                    System.out.println(randRemark[random.nextInt(randRemark.length)]);
                    break;
            }
        }
    }

    // Based on the user's input, return the direction they want to go
    private static Point Movement(String input) {
        String controls = "Controls\n========\n" + "Move Up:\tu, up, n, north\nMove Down:\td, down, s, south\n"
                + "Move Left:\tl, left, w, west\nMove Right:\tr, right, e, east";
        switch (input.toLowerCase()) {
            case "u":
            case "up":
            case "n":
            case "north":
                return new Point(0, -1);
            case "r":
            case "right":
            case "e":
            case "east":
                return new Point(1, 0);
            case "d":
            case "down":
            case "s":
            case "south":
                return new Point(0, 1);
            case "l":
            case "left":
            case "w":
            case "west":
                return new Point(-1, 0);
        }
        System.out.println("Sorry. That's not one of the valid movement controls\nPlease try again\n" + controls);
        return new Point(0, 0);
    }

    // Generate a random introduction statement for the enemy
    private static String FightIntro(String enemyName) {
        switch (random.nextInt(10)) {
            case 0:
                return "My name is " + enemyName + ". Prepare to die!";
            case 1:
                return "I am judge, jury and executioner. But you can call me " + enemyName;
            case 2:
                return "Must I remind you of my superiority?";
            case 3:
                return "Do not underestimate me, or it will be your doom. I guarantee it.";
            case 4:
                return "I promise you, your death will be slow and painful.";
            case 5:
                return "Surrender now and I promise you a quick death.";
            case 6:
                return "Run while you can. You know how this will end.";
            case 7:
                return "Pray to your gods. You'll be seeing them soon.";
            case 8:
                return "When you meet your Gods, tell them " + enemyName + " sent you.";
            case 9:
                return "You are wise to be afraid.";
            default:
                return "";
        }
    }

    // Combat between the user and an enemy (works whether it is a boss or minion)
    private static boolean Combat(Player player, Enemy enemy, Scanner scanner) {
        System.out.println("You've come across a creature in the maze.\n" + enemy.getName() + ": " + FightIntro(enemy.getName()));
        while (true) {
            List<BodyPart> bodyParts = enemy.getBodyParts();
            List<Attack> attacks = player.getAttacks(false);
            boolean properInput = false;
            int attackIndex = 0;
            int bodyPartIndex = 0;
            if (attacks.size() > 0) {
                while (!properInput) {
                    String input = "";
                    try {
                        System.out.print("Choose your attack: ");
                        input = scanner.nextLine();
                        attackIndex = Integer.valueOf(input) - 1;
                    } catch (Exception e) {
                        System.out.println("\nInvalid input: A random attack has been chosen for you.");
                        attackIndex = random.nextInt(attacks.size());
                    }
                    if (attackIndex < attacks.size() && attackIndex >= 0) {
                        System.out.println("\nTarget Options\n==============");
                        for (int i = 1; i <= bodyParts.size(); ++i) {
                            System.out.println(i + " " + bodyParts.get(i - 1).toString());
                        }
                        try {
                            System.out.print("Choose your target: ");
                            input = scanner.nextLine();
                            bodyPartIndex = Integer.valueOf(input) - 1;
                        } catch (Exception e) {
                            System.out.println("\nInvalid input: A random target has been chosen for you.\n");
                            bodyPartIndex = random.nextInt(bodyParts.size());
                        }
                        if (bodyPartIndex < bodyParts.size() && bodyPartIndex >= 0) {
                            properInput = true;
                        }
                    }
                }
                double dodgeChance = 10.0 + player.getWeight() - enemy.getWeight();
                if (100 * random.nextDouble() > dodgeChance) {
                    if (100 * random.nextDouble() < bodyParts.get(bodyPartIndex).getAttackChance()) {
                        double damage = 1;
                        switch (attacks.get(attackIndex).getType()) {
                            case Attack.BLUDGEONING_ATTACK:
                                double dentChance = 0;
                                switch (enemy.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        damage = 1.5;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        dentChance = 0.3;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        dentChance = 0.1;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        dentChance = 0.5;
                                        damage = 1.5;
                                        break;
                                }
                                switch (attacks.get(attackIndex).getName()) {
                                    case "Slap with the flat end":
                                        System.out.print("You slap " + enemy.getName() + "'s "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase()
                                                + " with the flat end of your " + player.getWeapon().getName());
                                        break;
                                    case "Strike with pommel":
                                        System.out.print("You strike " + enemy.getName() + "'s "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the pommel of your "
                                                + player.getWeapon().getName());
                                        break;
                                    case "Bash with shaft":
                                        System.out.print("You bash " + enemy.getName() + " in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the shaft of your "
                                                + player.getWeapon().getName());
                                        break;
                                    case "Bash":
                                        System.out.print("You bash " + enemy.getName() + " in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the end of your "
                                                + player.getWeapon().getName());
                                        break;
                                    case "Punch":
                                        System.out.print("You punch " + enemy.getName() + " in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with your fist");
                                        break;
                                    case "Kick":
                                        System.out.print("You kick " + enemy.getName() + " in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with your foot");
                                        break;
                                }
                                if (random.nextDouble() < dentChance) {
                                    System.out.print(" denting the " + enemy.getArmor().getName() + ", reducing its effectiveness");
                                    enemy.reduceArmorLevel();
                                }
                                System.out.print(".");
                                break;
                            case Attack.PIERCING_ATTACK:
                                double criticalChance = 0;
                                switch (enemy.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        criticalChance = 0.7;
                                        damage = 1.5;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        criticalChance = 0.5;
                                        damage = 1.5;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        criticalChance = 0.3;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        criticalChance = 0.1;
                                        break;
                                }
                                System.out.print("Your " + player.getWeapon().getName() + " pierces " + enemy.getName() + "'s "
                                        + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + ".");
                                if (random.nextDouble() <= criticalChance) {
                                    damage *= 2;
                                    System.out.print(" A major artery has been opened by the strike.");
                                }
                                break;
                            case Attack.SLASHING_ATTACK:
                                double removeLimbChance = 0;
                                switch (enemy.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        removeLimbChance = 0.2;
                                        damage = 1.5;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        removeLimbChance = 0.1;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        removeLimbChance = 0.2;
                                        damage = 1.5;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        removeLimbChance = 0.05;
                                        break;
                                }
                                System.out.print("You " + attacks.get(attackIndex).getName().toLowerCase() + " " + enemy.getName()
                                        + " in the " + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with your "
                                        + player.getWeapon().getName());
                                if (random.nextDouble() < removeLimbChance) {
                                    bodyParts.get(bodyPartIndex).lowerHealth(enemy.getHealth());
                                    System.out.print(" and the severed part sails off in a arc leaving a bloody trail on the floor");
                                }
                                System.out.print(".");
                                break;
                        }
                        damage *= attacks.get(attackIndex).getDamage() * (1.0 - (double) enemy.getArmor().getLevel() / 10);
                        bodyParts.get(bodyPartIndex).lowerHealth(damage);
                        if (bodyParts.get(bodyPartIndex).getHealth() == 0) {
                            String parts = "";
                            List<BodyPart> children = bodyParts.get(bodyPartIndex).getChildren();
                            System.out.print(" The " + bodyParts.get(bodyPartIndex).getDescription().toLowerCase()
                                    + " has been heavily damaged and can no longer be used");
                            if (children != null && children.size() > 0) {
                                if (children.size() > 1) {
                                    for (int i = 0; i < children.size(); ++i) {
                                        if (i == children.size() - 1) {
                                            parts += "and the " + children.get(i).getDescription().toLowerCase();
                                        } else {
                                            parts += "the " + children.get(i).getDescription().toLowerCase() + ", ";
                                        }
                                    }
                                } else {
                                    parts = "the " + children.get(0).getDescription().toLowerCase();
                                }
                                System.out.print(" along with " + parts);
                            }
                            System.out.print(".");
                            enemy.removeBodyPart(bodyParts.get(bodyPartIndex));
                            if (!enemy.isAlive()) {
                                System.out.println(" You are victorious! " + enemy.getName() + " is dead.\n");
                                return true;
                            }
                        }
                    } else {
                        System.out.print("You attempt to strike " + enemy.getName() + " in the "
                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " but the shot is blocked.");
                    }
                } else {
                    System.out.print("You attack " + enemy.getName() + " but he jumps away avoiding the strike.");
                }
            } else {
                System.out.print("Your body is heavily injured. You can no longer attack (Press Enter)");
                scanner.nextLine();
            }
            System.out.print("\n");
            // Enemy Combat Turn ====================================================================================================
            // Choose random attack and random target and execute the attack
            bodyParts = player.getBodyParts();
            attacks = enemy.getAttacks(false);
            if (attacks.size() > 0) {
                attackIndex = random.nextInt(attacks.size());
                bodyPartIndex = random.nextInt(bodyParts.size());
                double dodgeChance = 10.0 + enemy.getWeight() - player.getWeight();
                if (100 * random.nextDouble() > dodgeChance) {
                    if (100 * random.nextDouble() < bodyParts.get(bodyPartIndex).getAttackChance()) {
                        double damage = 1;
                        switch (attacks.get(attackIndex).getType()) {
                            case Attack.BLUDGEONING_ATTACK:
                                double dentChance = 0;
                                switch (player.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        damage = 1.5;
                                        dentChance = 0;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        dentChance = 0.3;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        dentChance = 0.1;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        dentChance = 0.5;
                                        damage = 1.5;
                                        break;
                                }
                                switch (attacks.get(attackIndex).getName()) {
                                    case "Slap with the flat end":
                                        System.out.print(enemy.getName() + " slaps your "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the flat end of his "
                                                + enemy.getWeapon().getName());
                                        break;
                                    case "Strike with pommel":
                                        System.out.print(enemy.getName() + " strikes your "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the pommel of his "
                                                + enemy.getWeapon().getName());
                                        break;
                                    case "Bash with shaft":
                                        System.out.print(enemy.getName() + " bashes you in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the shaft of his "
                                                + enemy.getWeapon().getName());
                                        break;
                                    case "Bash":
                                        System.out.print(enemy.getName() + " bashes you in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the end of his "
                                                + enemy.getWeapon().getName());
                                        break;
                                    case "Punch":
                                        System.out.print(enemy.getName() + " punches you in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with his fist");
                                        break;
                                    case "Kick":
                                        System.out.print(enemy.getName() + " kicks you in the "
                                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with his foot");
                                        break;
                                }
                                if (random.nextDouble() < dentChance) {
                                    System.out.print(" denting the " + player.getArmor().getName() + ", reducing its effectiveness");
                                    player.reduceArmorLevel();
                                }
                                System.out.print(".");
                                break;
                            case Attack.PIERCING_ATTACK:
                                double criticalChance = 0;
                                switch (player.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        criticalChance = 0.7;
                                        damage = 1.5;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        criticalChance = 0.5;
                                        damage = 1.5;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        criticalChance = 0.3;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        criticalChance = 0.1;
                                        break;
                                }
                                System.out.print(enemy.getName() + "'s " + enemy.getWeapon().getName() + " pierces your "
                                        + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + ".");
                                if (random.nextDouble() <= criticalChance) {
                                    damage *= 2;
                                    System.out.print(" A major artery has been opened by the strike.");
                                }
                                break;
                            case Attack.SLASHING_ATTACK:
                                double removeLimbChance = 0;
                                switch (player.getArmor().getType()) {
                                    case Armor.NO_ARMOR:
                                        removeLimbChance = 0.2;
                                        damage = 1.5;
                                        break;
                                    case Armor.LIGHT_ARMOR:
                                        removeLimbChance = 0.1;
                                        break;
                                    case Armor.MEDIUM_ARMOR:
                                        removeLimbChance = 0.2;
                                        damage = 1.5;
                                        break;
                                    case Armor.HEAVY_ARMOR:
                                        removeLimbChance = 0.05;
                                        break;
                                }
                                System.out.print(enemy.getName() + " " + attacks.get(attackIndex).getName().toLowerCase());
                                switch (attacks.get(attackIndex).getName()) {
                                    case "Hack":
                                        System.out.print("s ");
                                        break;
                                    case "Slash":
                                        System.out.print("es ");
                                        break;
                                }
                                System.out.print("you in the " + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with his "
                                        + enemy.getWeapon().getName());
                                if (random.nextDouble() < removeLimbChance) {
                                    bodyParts.get(bodyPartIndex).lowerHealth(player.getHealth());
                                    System.out.print(" and the severed part sails off in a arc leaving a bloody trail on the floor.");
                                }
                                break;
                        }
                        damage *= attacks.get(attackIndex).getDamage() * (1.0 - (double) player.getArmor().getLevel() / 10);
                        bodyParts.get(bodyPartIndex).lowerHealth(damage);
                        if (bodyParts.get(bodyPartIndex).getHealth() == 0) {
                            String parts = "";
                            List<BodyPart> children = bodyParts.get(bodyPartIndex).getChildren();
                            System.out.print(" The " + bodyParts.get(bodyPartIndex).getDescription().toLowerCase()
                                    + " has been heavily damaged and can no longer be used");
                            if (children != null && children.size() > 0) {
                                if (children.size() > 1) {
                                    for (int i = 0; i < children.size(); ++i) {
                                        if (i == children.size() - 1) {
                                            parts += "and the " + children.get(i).getDescription().toLowerCase();
                                        } else {
                                            parts += "the " + children.get(i).getDescription().toLowerCase() + ", ";
                                        }
                                    }
                                } else {
                                    parts = "the " + children.get(0).getDescription().toLowerCase();
                                }
                                System.out.print(" along with " + parts);
                            }
                            System.out.print(".");
                            player.removeBodyPart(bodyParts.get(bodyPartIndex));
                            if (!player.isAlive()) {
                                System.out.println(" You died!");
                                return false;
                            }
                        }
                    } else {
                        System.out.print(enemy.getName() + " attempts to strike you in the "
                                + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " but the shot is blocked.");
                    }
                } else {
                    System.out.print(enemy.getName() + " attacks you, but you manage to jump away.");
                }
            } else {
                System.out.print(enemy.getName() + "'s body is heavily injured and can no "
                        + "longer attack you (feel free to pummel on him with no remorse)");
            }
            System.out.print("\n");
        }
    }

    private static double getDistance(Point x1, Point x2) {
        return Math.sqrt(Math.pow(x2.getX() - x1.getX(), 2) + Math.pow(x2.getY() - x1.getY(), 2));
    }
}
