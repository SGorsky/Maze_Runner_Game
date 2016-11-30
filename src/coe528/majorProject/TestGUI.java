package coe528.majorProject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Annie
 */
public class TestGUI extends Application {

    // The size of the maze, preset and hardcoded into the program (must be an odd number)
    public static final int MAZE_SIZE = 51;
    // Initial health given to the player and distributed along the body parts
    public static final int PLAYER_HEALTH = 100;
    // A Random used to generate random numbers to make the game different every time
    static Random random = new Random();
    // Chance of getting into a battle with an enemy
    static double enemyChance = 0.05;
    // Chance of getting into a battle with the final boss (scales by 2 after each enemy dies)
    static double bossChance = 0.01;
    // The probability of finding a new weapon, piece of armor or a potion in a corner (used during maze generation)
    static double itemCornerProbability = 1;// 0.4
    static int displayMazeSize = 10;

    @Override
    public void start(Stage primaryStage) {
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
            System.out.println(e.getMessage());
        }
        // TextArea to replace System.out
        TextArea ta = TextAreaBuilder.create().prefWidth(666).prefHeight(666).wrapText(true).build();
        ta.setEditable(false);
        ta.setFont(new Font("Consolas", 12));
        TestGUI.Console console = new TestGUI.Console(ta);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
        // Text that displays the map
        Text map = new Text();
        map.setFont(new Font("Consolas", 15));
        map.setTextAlignment(TextAlignment.JUSTIFY);
        map.setLineSpacing(-6);
        map.setUserData(Maze.getInstance());
        // BorderPane holds everything
        BorderPane root = new BorderPane();
        // FlowPane for information on the sidebar
        VBox infoBar = new VBox();
        Label commands = new Label();
        infoBar.setAlignment(Pos.TOP_CENTER);
        infoBar.setSpacing(10);
        infoBar.setMaxWidth(200);
        infoBar.setMinWidth(200);
        infoBar.getChildren().add(map);
        root.setCenter(ta);
        root.setRight(infoBar);
        // Labels to display equipped armor and weapon
        Text equippedArmor = new Text();
        Text equippedWeap = new Text();
        Label divider1 = new Label();
        divider1.setFont(new Font("Consolas", 12));
        equippedArmor.setFont(new Font("Consolas", 12));
        equippedWeap.setFont(new Font("Consolas", 12));
        commands.setText("Commands");
        commands.setFont(new Font("Consolas", 14));
        // Movement Buttons
        Button upButton = new Button();
        Button downButton = new Button();
        Button rightButton = new Button();
        Button leftButton = new Button();
        upButton.setText("Up");
        downButton.setText("Down");
        rightButton.setText("Right");
        leftButton.setText("Left");
        // Movement Holder
        BorderPane direction = new BorderPane();
        direction.setTop(upButton);
        direction.setBottom(downButton);
        direction.setRight(rightButton);
        direction.setLeft(leftButton);
        direction.setPadding(new Insets(0, 10, 20, 10));
        BorderPane.setAlignment(upButton, Pos.CENTER);
        BorderPane.setAlignment(downButton, Pos.CENTER);
        BorderPane.setAlignment(rightButton, Pos.CENTER_LEFT);
        BorderPane.setAlignment(leftButton, Pos.CENTER_RIGHT);
        // Label that holds the yes/no choice data
        Label yn = new Label();
        // Label that shows description of the item picked up
        Label desc = new Label();
        desc.setFont(new Font("Consolas", 12));
        desc.setText("");
        yn.setText("");
        FlowPane ynButtons = new FlowPane();
        HBox buttonHolder = new HBox();
        buttonHolder.setSpacing(15);
        Scene equipCheck = new Scene(ynButtons);
        // Yes and No Buttons
        Button yes = new Button();
        yes.setText("Yes");
        Button no = new Button();
        no.setText("No");
        Stage equipWindow = new Stage();
        ynButtons.getChildren().add(desc);
        buttonHolder.getChildren().add(yes);
        buttonHolder.getChildren().add(no);
        ynButtons.getChildren().add(buttonHolder);
        ynButtons.setAlignment(Pos.CENTER);
        ynButtons.setPadding(new Insets(10));
        equipWindow.setScene(equipCheck);
        equipWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        equipWindow.setTitle("Item Found.");
        // New item window
        EventHandler ynfilter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Object source = event.getSource();
                if (source instanceof Button) {
                    yn.setText(((Button) source).getText());
                    direction.setDisable(false);
                    equipWindow.close();
                }
                event.consume();
            }
        };
        no.addEventFilter(MouseEvent.MOUSE_CLICKED, ynfilter);
        yes.addEventFilter(MouseEvent.MOUSE_CLICKED, ynfilter);
        System.out.println("Welcome player, to COOL GAME TITLE (this was done on purpose)\n" + "Your mission, should you choose to accept it (if "
                + "you don't accept it, I don't know why you're playing this game, just turn this off now) is to kill "
                + "the dangerous SCARY ENEMY NAME, a INSERT A NUMBER headed INSERT SCARY FANTASY SPECIES and then "
                + "escape the maze. You cannot escape the maze without killing the beast but he won't come out and fight "
                + "you right away. Kill enough of his minions to bait him into revealing himself and then kill him." + "\n\n"
                + "Click one of the buttons to the right to select it. "
                + "So if you wanted to play you'd type click the Play button or if you wanted to read the instructions"
                + " you'd click the Instructions button and read some more of my static but motivational and possibly heartwarming"
                + " words. Of course if you're already bored and want to exit, click the Exit button and go do something else.\n");
        System.out.println("\nPlay\nInstructions\nExit");
        Button b1 = new Button();
        Button b3 = new Button();
        Button b2 = new Button();
        Button b4 = new Button();
        b1.setText("Play");
        b2.setText("Instructions");
        b3.setText("Exit");
        b4.setText("Continue");
        TextField nameGet = new TextField();
        nameGet.setPromptText("Enter a name here");
        nameGet.setMaxWidth(150);
        b1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ta.clear();
                System.out.println("Welcome to the game\nPlease enter your name in the field to the right (the first "
                        + "letter of your name will be used to identify your player in the maze. It is recommended "
                        + "that you do not use the letter 'X' as that is what the walls of the maze look like: ");
                infoBar.getChildren().remove(b1);
                infoBar.getChildren().remove(b2);
                infoBar.getChildren().remove(b3);
                infoBar.getChildren().add(nameGet);
                infoBar.getChildren().add(b4);
            }
        });
        b2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ta.clear();
                System.out.println("\nINSTRUCTIONS\n============\nThe game is simple, you're in a maze and you're "
                        + "trying to get out. To do that, you need to kill a boss and find the exit. "
                        + "\nHow do you do that? Simple. You'll be shown part of the maze around you and you need to find "
                        + "your way around the maze. But don't worry, you'll somehow know the cardinal directions to "
                        + "anything you want to find (except the exit). Don't ask why. Somethings are better left unknown. "
                        + "Like if if Wile E. Coyote had enough money to buy all that stuff from ACME Corp, why didn't "
                        + "he just pay someone to catch the roadrunner or buy his dinner?\nAnyway, the Up, Down, Left and Right "
                        + "buttons will let you control your player in the game: "
                        + "\nSimple enough right? Your game view will include a seciton of the maze "
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
            }
        });
        b3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Platform.exit();
            }
        });
        b4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String name = nameGet.getText();
                Maze.getInstance().generateNewMaze();
                if (name.length() == 0 || name.length() > 20 || !Character.isLetter(name.charAt(0))) {
                    System.out.println("I'm sorry. I don't understand what you want me to call you. I'll just call you Bob. "
                            + "Finish the game like a good little boy (yes, I just assumed your gender) and I'll give you some cake.");
                    name = "Bob";
                }
                infoBar.getChildren().remove(nameGet);
                infoBar.getChildren().remove(b4);
                Player player = null;
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
                equippedArmor.setText("");
                equippedWeap.setText("");
                divider1.setText("====================");
                infoBar.getChildren().add(commands);
                infoBar.getChildren().add(direction);
                infoBar.getChildren().add(equippedArmor);
                infoBar.getChildren().add(divider1);
                infoBar.getChildren().add(equippedWeap);
                Maze.getInstance().setPlayerTile(start);
                player = new Player(start, PLAYER_HEALTH, name, weaponsList.get(8), armorList.get(9), 1);
                b1.setUserData(player);
                equippedWeap.setText("Equipped Weapon:\n" + player.getWeapon() + "\n");
                equippedArmor.setText("Equipped Armor:\n" + player.getArmor() + "\n");
                map.setText(Maze.getInstance().displayPlayerLocation(player.getLocation(), displayMazeSize));
                EventHandler filter = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Button) {
                            NumField getOption = new NumField();
                            getOption.setPromptText("Attack Type");
                            NumField getLocation = new NumField();
                            getLocation.setPromptText("Attack Target");
                            Button atkButton = new Button();
                            atkButton.setText("Attack");
                            VBox combatWindow = new VBox();
                            HBox combatInfo = new HBox();
                            HBox combatUI = new HBox();
                            combatInfo.minHeight(500);
                            Text atkOptionInfo = new Text();
                            Text atkLocInfo = new Text();
                            atkLocInfo.setWrappingWidth(300);
                            atkOptionInfo.setWrappingWidth(350);
                            combatUI.setSpacing(5);
                            combatUI.setAlignment(Pos.CENTER);
                            combatUI.getChildren().add(getOption);
                            combatUI.getChildren().add(getLocation);
                            combatUI.getChildren().add(atkButton);
                            combatInfo.setAlignment(Pos.TOP_LEFT);
                            combatInfo.getChildren().add(atkOptionInfo);
                            combatInfo.getChildren().add(atkLocInfo);
                            combatInfo.setSpacing(10);
                            combatWindow.getChildren().add(combatInfo);
                            combatWindow.getChildren().add(combatUI);
                            combatWindow.setPadding(new Insets(15));
                            Scene battleScene = new Scene(combatWindow);
                            Stage battleStage = new Stage();
                            battleStage.setScene(battleScene);
                            battleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    event.consume();
                                }
                            });
                            atkButton.setUserData(null);
                            atkButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    int result;
                                    if (getOption.getText().equals("") || getLocation.getText().equals("")) {
                                        result = 4;
                                    } else {
                                        result = TestGUI.Combat(((Player) b1.getUserData()), ((Enemy) atkButton.getUserData()),
                                                Integer.parseInt(getOption.getText()), Integer.parseInt(getLocation.getText()));
                                    }
                                    switch (result) {
                                        case 1:
                                            List<BodyPart> bodyParts = ((Enemy) atkButton.getUserData()).getBodyParts();
                                            atkOptionInfo.setText("\n" + ((Player) b1.getUserData()).getAttacksString());
                                            String bodyPartList = "\nTarget Options\n==============\n";
                                            for (int i = 1; i <= bodyParts.size(); ++i) {
                                                bodyPartList += i + " " + bodyParts.get(i - 1).toString() + "\n";
                                            }
                                            atkLocInfo.setText(bodyPartList);
                                            break;
                                        case 2:
                                            System.out.println("The monster has been defeated!");
                                            Maze.getInstance().setEmptyTile(((Player) b1.getUserData()).getLocation());
                                            direction.setDisable(false);
                                            battleStage.close();
                                            break;
                                        case 3:
                                            // ta.clear();
                                            System.out.println("Try Again");
                                            infoBar.getChildren().clear();
                                            infoBar.getChildren().add(commands);
                                            b1.setText("Restart");
                                            infoBar.getChildren().add(b3);
                                            battleStage.close();
                                            break;
                                        case 4:
                                            System.out.println("Invalid Input");
                                            break;
                                        case 5:
                                            atkOptionInfo.setText("\nThere's nothing you can do anymore.");
                                            combatUI.getChildren().remove(getOption);
                                            combatUI.getChildren().remove(getLocation);
                                            atkButton.setText("Continue");
                                    }
                                }
                            });
                            Point dir = Movement(((Button) source).getText());
                            if (Maze.getInstance()
                                    .getTileType(Point.add2Points(dir, ((Player) b1.getUserData()).getLocation())) != Tile.WALL_TILE) {
                                Maze.getInstance().setEmptyTile(((Player) b1.getUserData()).getLocation());
                                ((Player) b1.getUserData()).getLocation().incrementX(dir.getX());
                                ((Player) b1.getUserData()).getLocation().incrementY(dir.getY());
                                switch (Maze.getInstance().getTileType(((Player) b1.getUserData()).getLocation())) {
                                    case Tile.EMPTY_TILE:
                                        double chance = random.nextDouble();
                                        if (chance < enemyChance) {
                                            int weaponIndex = random.nextInt(weaponsList.size() - 1);
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
                                            atkButton.setUserData(enemy);
                                            // TestGUI.Combat(((Player)b1.getUserData()), enemy);
                                            System.out.println("You've come across a creature in the maze.\n" + enemy.getName() + ": "
                                                    + FightIntro(enemy.getName()));
                                            List<BodyPart> bodyParts = ((Enemy) atkButton.getUserData()).getBodyParts();
                                            atkOptionInfo.setText("\n" + ((Player) b1.getUserData()).getAttacksString());
                                            String bodyPartList = "\nTarget Options\n==============\n";
                                            for (int i = 1; i <= bodyParts.size(); ++i) {
                                                bodyPartList += i + " " + bodyParts.get(i - 1).toString() + "\n";
                                            }
                                            atkLocInfo.setText(bodyPartList);
                                            direction.setDisable(true);
                                            battleStage.sizeToScene();
                                            battleStage.showAndWait();
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
                                            Enemy enemy = new Enemy(
                                                    health, enemyName, weaponsList.get(weaponIndex), armorList.get(armourIndex), numHeads);
                                            atkButton.setUserData(enemy);
                                            List<BodyPart> bodyParts = ((Enemy) atkButton.getUserData()).getBodyParts();
                                            atkOptionInfo.setText("\n" + ((Player) b1.getUserData()).getAttacksString());
                                            String bodyPartList = "\nTarget Options\n==============\n";
                                            for (int i = 1; i <= bodyParts.size(); ++i) {
                                                bodyPartList += i + " " + bodyParts.get(i - 1).toString() + "\n";
                                            }
                                            atkLocInfo.setText(bodyPartList);
                                            direction.setDisable(true);
                                            battleStage.showAndWait();
                                            if (!b1.getText().equalsIgnoreCase("Restart")) {
                                                enemyChance = 0;
                                                bossChance = 0;
                                                List<Point> mazeCorners = new ArrayList<Point>(4);
                                                double distance = 1000000000;
                                                mazeCorners.add(new Point(1, 1));
                                                mazeCorners.add(new Point(1, MAZE_SIZE - 2));
                                                mazeCorners.add(new Point(MAZE_SIZE - 2, 1));
                                                mazeCorners.add(new Point(MAZE_SIZE - 2, MAZE_SIZE - 2));
                                                int index = -1;
                                                for (int i = 0; i < mazeCorners.size(); i++) {
                                                    if (Maze.getInstance().getTileType(mazeCorners.get(i)) != Tile.WALL_TILE) {
                                                        double tempD = getDistance(((Player) b1.getUserData()).getLocation(),
                                                                mazeCorners.get(i));
                                                        if (distance > tempD) {
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
                                                Maze.getInstance().setPlayerTile(((Player) b1.getUserData()).getLocation());
                                                System.out.println(Maze.getInstance().toString());
                                                displayMazeSize = 20;
                                            }
                                        }
                                        //else {
                                        // Moved without getting into a fight
                                        //}
                                        break;
                                    case Tile.ARMOR_TILE:
                                        int index = random.nextInt(armorList.size() - 1);// Don't allow naked option
                                        desc.setText("You've come across a new piece of armor while traveling through the maze.\n\n"
                                                + "Armor Stats\n===========\n" + armorList.get(index).toString()
                                                + "\nComparison\n==========\n"
                                                + Armor.Compare(armorList.get(index), ((Player) b1.getUserData()).getArmor())
                                                + "\nWould you like " + "to replace your current armor ("
                                                + ((Player) b1.getUserData()).getArmor().getName() + ") with it?\n");
                                        direction.setDisable(true);
                                        equipWindow.sizeToScene();
                                        equipWindow.showAndWait();
                                        if (yn.getText().equalsIgnoreCase("yes")) {
                                            ((Player) b1.getUserData()).setArmor(armorList.get(index));
                                            equippedArmor.setText("Equipped Armor:\n" + ((Player) b1.getUserData()).getArmor() + "\n");
                                        }
                                        Maze.getInstance().setEmptyTile(((Player) b1.getUserData()).getLocation());
                                        break;
                                    case Tile.WEAPON_TILE:
                                        index = random.nextInt(weaponsList.size() - 2);// Don't allow fist or foot option
                                        desc.setText("You've come across a new weapon while traveling through the maze.\n"
                                                + "Weapon Stats\n============\n" + weaponsList.get(index).toString() + "\nComparison\n"
                                                + "=========="
                                                + Weapon.compare(weaponsList.get(index), ((Player) b1.getUserData()).getWeapon())
                                                + "\nWould you like to replace your current weapon ("
                                                + ((Player) b1.getUserData()).getWeapon().getName() + ") with it?\n\n");
                                        direction.setDisable(true);
                                        equipWindow.sizeToScene();
                                        equipWindow.showAndWait();
                                        if (yn.getText().equalsIgnoreCase("yes")) {
                                            if (((Player) b1.getUserData()).equipWeapon(weaponsList.get(index))) {
                                                equippedWeap.setText("Equipped Weapon:\n" + ((Player) b1.getUserData()).getWeapon() + "\n");
                                            } else if (weaponsList.get(index).isOneHanded()) {
                                                System.out.println("Unable to equip " + weaponsList.get(index).getName()
                                                        + ". You don't have any arms.");
                                            } else {
                                                System.out.println("Unable to equip " + weaponsList.get(index).getName()
                                                        + ". You need two arms to use this weapon.");
                                            }
                                        }
                                        Maze.getInstance().setEmptyTile(((Player) b1.getUserData()).getLocation());
                                        break;
                                    case Tile.POTION_TILE:
                                        System.out.println("You found a strange vial of liquid in a corner. Not being the brightest "
                                                + "of your species, you decide to drink it without any thought of the consequences. Luckily, its a "
                                                + "healing potion and you gain some health as well as regrow any missing limbs you lost in combat. "
                                                + "You feel stronger than ever.\n\n");
                                        b1.setUserData(new Player(
                                                ((Player) b1.getUserData()).getLocation(), ((Player) b1.getUserData()).getHealth() + 25,
                                                ((Player) b1.getUserData()).getName(), ((Player) b1.getUserData()).getWeapon(),
                                                ((Player) b1.getUserData()).getArmor(), 1));
                                        break;
                                    case Tile.EXIT_TILE:
                                        System.out.println("Congratulations. You've finished the game. There's no prize. No parade. "
                                                + "No celebration. Sorry. Don't have the funding for that kind of thing, you know."
                                                + "\nBut you won.\nSo thats it. Goodbye. Farewell. Maybe you'll play me again soon. "
                                                + "Press the Exit to exit.");
                                        infoBar.getChildren().clear();
                                        infoBar.getChildren().add(b3);
                                        break;
                                }
                                Maze.getInstance().setPlayerTile(((Player) b1.getUserData()).getLocation());
                                map.setText(Maze.getInstance().displayPlayerLocation(((Player) b1.getUserData()).getLocation(),
                                        displayMazeSize));
                            } else {
                                System.out.println("The walls of the maze prevent you from moving in that direction.");
                            }
                        }
                        event.consume();
                    }
                };
                upButton.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                downButton.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                leftButton.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                rightButton.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
            }
        });
        infoBar.getChildren().add(b1);
        infoBar.getChildren().add(b2);
        infoBar.getChildren().add(b3);
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("TEST");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static String FightIntro(String enemyName) {
        switch (random.nextInt(12)) {
            case 0:
                return "My name is " + enemyName + ". Prepare to die!";
            case 1:
                return "I am " + enemyName + ". Prepare to meet your maker.";
            case 2:
                return "What makes you think you can win against me? Do you even " + "know who I am? I am " + enemyName;
            case 3:
                return "Must I remind you of my superiority?";
            case 4:
                return "Do not underestimate me, or it will be your doom. I guarantee it.";
            case 5:
                return "You have no chance against me. I am " + enemyName;
            case 6:
                return "I promise you, your death will be slow and painful.";
            case 7:
                return "Surrender now and I promise you a quick death.";
            case 8:
                return "Run while you can. You know how this will end.";
            case 9:
                return "Pray to your gods. You'll be seeing them soon.";
            case 10:
                return "When you meet your Gods, tell them " + enemyName + " sent you.";
            case 11:
                return "You are wise to be afraid.";
            default:
                return "";
        }
    }

    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }

    // Combat between the user and an enemy (works whether it is a boss or minion)
    private static int Combat(Player player, Enemy enemy, int attackOP, int attackLoc) {
        List<BodyPart> bodyParts = enemy.getBodyParts();
        List<Attack> attacks = player.getAttacks(false);
        int limbless = 1;
        int attackIndex = attackOP - 1;
        int bodyPartIndex = attackLoc - 1;
        if (attacks.size() > 0) {
            if (attackIndex >= attacks.size() || attackIndex < 0) {
                return 4;
            }
            if (bodyPartIndex >= bodyParts.size() || bodyPartIndex < 0) {
                return 4;
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
                            switch (attacks.get(attackIndex).getName().toLowerCase()) {
                                case "slap with the flat end":
                                    System.out.print("You slap " + enemy.getName() + "'s "
                                            + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the flat end of your "
                                            + player.getWeapon().getName());
                                    break;
                                case "strike with pommel":
                                    System.out.print("You strike " + enemy.getName() + "'s "
                                            + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the pommel of your "
                                            + player.getWeapon().getName());
                                    break;
                                case "bash with shaft":
                                    System.out.print("You bash " + enemy.getName() + " in the "
                                            + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the shaft of your "
                                            + player.getWeapon().getName());
                                    break;
                                case "bash":
                                    System.out.print("You bash " + enemy.getName() + " in the "
                                            + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with the end of your "
                                            + player.getWeapon().getName());
                                    break;
                                case "punch":
                                    System.out.print("You punch " + enemy.getName() + " in the "
                                            + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with your fist");
                                    break;
                                case "kick":
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
                            System.out.print("You " + attacks.get(attackIndex).getName().toLowerCase() + " " + enemy.getName() + " in the "
                                    + bodyParts.get(bodyPartIndex).getDescription().toLowerCase() + " with your "
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
                            return 2;
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
            System.out.println("Your body is heavily injured. You can no longer attack (Press Continue)");
            limbless = 5;
        }
        System.out.print("\n");
        // Enemy Combat Turn ====================================================================================================
        // Choose random attack and random target and execute the attack
        List<BodyPart> bodyPartsP = player.getBodyParts();
        List<Attack> attacksE = enemy.getAttacks(false);
        if (attacksE.size() > 0) {
            attackIndex = random.nextInt(attacksE.size());
            bodyPartIndex = random.nextInt(bodyPartsP.size());
            double dodgeChance = 10 + enemy.getWeight() - player.getWeight();
            if (100 * random.nextDouble() > dodgeChance) {
                if (100 * random.nextDouble() < bodyPartsP.get(bodyPartIndex).getAttackChance()) {
                    double damage = 1;
                    switch (attacksE.get(attackIndex).getType()) {
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
                            switch (attacksE.get(attackIndex).getName().toLowerCase()) {
                                case "slap with the flat end":
                                    System.out.print(
                                            enemy.getName() + " slaps your " + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase()
                                            + " with the flat end of his " + enemy.getWeapon().getName());
                                    break;
                                case "strike with pommel":
                                    System.out.print(enemy.getName() + " strikes your "
                                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with the pommel of his "
                                            + enemy.getWeapon().getName());
                                    break;
                                case "bash with shaft":
                                    System.out.print(enemy.getName() + " bashes you in the "
                                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with the shaft of his "
                                            + enemy.getWeapon().getName());
                                    break;
                                case "bash":
                                    System.out.print(enemy.getName() + " bashes you in the "
                                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with the end of his "
                                            + enemy.getWeapon().getName());
                                    break;
                                case "punch":
                                    System.out.print(enemy.getName() + " punches you in the "
                                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with his fist");
                                    break;
                                case "kick":
                                    System.out.print(enemy.getName() + " kicks you in the "
                                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with his foot");
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
                            System.out.print(enemy.getName() + "'s " + enemy.getWeapon().getName() + " pierces your "
                                    + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + ".");
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
                            System.out.print(enemy.getName() + " " + attacksE.get(attackIndex).getName().toLowerCase());
                            switch (attacksE.get(attackIndex).getName().toLowerCase()) {
                                case "hack":
                                    System.out.print("s ");
                                    break;
                                case "slash":
                                    System.out.print("es ");
                                    break;
                            }
                            System.out.print("you in the " + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " with his "
                                    + enemy.getWeapon().getName());
                            if (random.nextDouble() < removeLimbChance) {
                                bodyPartsP.get(bodyPartIndex).lowerHealth(player.getHealth());
                                System.out.print(" and the severed part sails off in a arc leaving a bloody trail on the floor");
                            }
                            System.out.print(".");
                            break;
                    }
                    damage *= attacksE.get(attackIndex).getDamage() * (1.0 - (double) player.getArmor().getLevel() / 10);
                    bodyPartsP.get(bodyPartIndex).lowerHealth(damage);
                    if (bodyPartsP.get(bodyPartIndex).getHealth() == 0) {
                        String parts = "";
                        List<BodyPart> children = bodyPartsP.get(bodyPartIndex).getChildren();
                        System.out.print(" The " + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase()
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
                        // System.out.println("\n"+bodyPartIndex+"\n");
                        // String bodyPartList = "";
                        // for (int i = 1; i <= bodyPartsP.size(); ++i) {
                        // bodyPartList += i + " " + bodyPartsP.get(i - 1).toString() +"\n";
                        // }
                        // System.out.println(bodyPartList);
                        player.removeBodyPart(bodyPartsP.get(bodyPartIndex));
                        // bodyPartList = "";
                        // for (int i = 1; i <= bodyPartsP.size(); ++i) {
                        // bodyPartList += i + " " + bodyPartsP.get(i - 1).toString() +"\n";
                        // }
                        // System.out.println(bodyPartList);
                        if (!player.isAlive()) {
                            System.out.println("\nYou died!");
                            return 3;
                        }
                    }
                } else {
                    System.out.print(enemy.getName() + " attempts to strike you in the "
                            + bodyPartsP.get(bodyPartIndex).getDescription().toLowerCase() + " but the shot is blocked.");
                }
            } else {
                System.out.print(enemy.getName() + " attacks you, but you manage to jump away.");
            }
        } else {
            System.out.print(enemy.getName() + "'s body is heavily injured and can no "
                    + "longer attack you (feel free to pummel on him with no remorse)");
        }
        System.out.print("\n");
        return limbless;
    }

    private static double getDistance(Point x1, Point x2) {
        return Math.sqrt(Math.pow(x2.getX() - x1.getX(), 2) + Math.pow(x2.getY() - x1.getY(), 2));
    }

    public class NumField extends TextField {

        @Override
        public void replaceText(int start, int end, String text) {
            if (validate(text)) {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text) {
            if (validate(text)) {
                super.replaceSelection(text);
            }
        }

        private boolean validate(String text) {
            return text.matches("[0-9]*");
        }
    }
}
