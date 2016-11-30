package coe528.majorProject;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    private static final Maze maze = new Maze();
    private Tile[][] mazeGrid;

    private Maze() {
        generateNewMaze();
    }

    public static Maze getInstance() {
        return maze;
    }

    public void generateNewMaze() {
        mazeGrid = new Tile[MainClass.MAZE_SIZE][MainClass.MAZE_SIZE];
        for (int y = 0; y < MainClass.MAZE_SIZE; ++y) {
            for (int x = 0; x < MainClass.MAZE_SIZE; ++x) {
                mazeGrid[y][x] = new Tile();
            }
        }
        Point current = new Point(1, 1);
        mazeGrid[current.getY()][current.getX()].setEmpty();
        List<Point> moves = new ArrayList<Point>();
        moves.add(current);
        while (!moves.isEmpty()) {
            List<Point> neighbours = getFrontierNeighbours(current);
            if (!neighbours.isEmpty()) {
                int index = MainClass.random.nextInt(neighbours.size());
                Point dir = new Point(neighbours.get(index).getX() - current.getX(), neighbours.get(index).getY() - current.getY());
                mazeGrid[current.getY() + dir.getY()][current.getX() + dir.getX()].setEmpty();
                mazeGrid[current.getY() + dir.getY() / 2][current.getX() + dir.getX() / 2].setEmpty();
                current.incrementX(dir.getX());
                current.incrementY(dir.getY());
                moves.add(new Point(current.getX(), current.getY()));
            } else {
                current = moves.get(moves.size() - 1);
                moves.remove(current);
            }
        }

        List<Point> corners = getCorners();

        // Given the itemCornerProbability and the number of corners in the maze, populate that exact number of corners with items
        while (true) {
            int count = (int) (corners.size() * MainClass.itemCornerProbability);
            for (int i = 0; i < count; i++) {
                int index = MainClass.random.nextInt(corners.size());
                switch (MainClass.random.nextInt(3)) {
                    case 0:
                        setArmorTile(corners.get(index));
                        break;
                    case 1:
                        setWeaponTile(corners.get(index));
                        break;
                    case 2:
                        setPotionTile(corners.get(index));
                        break;
                }
                corners.remove(index);
            }
            break;
        }
    }

    private List<Point> getFrontierNeighbours(Point loc) {
        List<Point> neighbours = new ArrayList<Point>();
        if (loc.getY() > 1 && mazeGrid[loc.getY() - 2][loc.getX()].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX(), loc.getY() - 2));
        }
        if (loc.getY() < MainClass.MAZE_SIZE - 3 && mazeGrid[loc.getY() + 2][loc.getX()].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX(), loc.getY() + 2));
        }
        if (loc.getX() > 1 && mazeGrid[loc.getY()][loc.getX() - 2].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX() - 2, loc.getY()));
        }
        if (loc.getX() < MainClass.MAZE_SIZE - 3 && mazeGrid[loc.getY()][loc.getX() + 2].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX() + 2, loc.getY()));
        }
        return neighbours;
    }

    private List<Point> getNeighbours(int x, int y) {
        Point loc = new Point(x, y);
        List<Point> neighbours = new ArrayList<Point>();
        if (loc.getY() > 0 && mazeGrid[loc.getY() - 1][loc.getX()].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX(), loc.getY() - 1));
        }
        if (loc.getY() < MainClass.MAZE_SIZE - 1 && mazeGrid[loc.getY() + 1][loc.getX()].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX(), loc.getY() + 1));
        }
        if (loc.getX() > 0 && mazeGrid[loc.getY()][loc.getX() - 1].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX() - 1, loc.getY()));
        }
        if (loc.getX() < MainClass.MAZE_SIZE - 1 && mazeGrid[loc.getY()][loc.getX() + 1].getTileType() == Tile.WALL_TILE) {
            neighbours.add(new Point(loc.getX() + 1, loc.getY()));
        }
        return neighbours;
    }

    private List<Point> getCorners() {
        List<Point> corners = new ArrayList<Point>();
        for (int i = 0; i < MainClass.MAZE_SIZE; ++i) {
            for (int j = 0; j < MainClass.MAZE_SIZE; ++j) {
                if (mazeGrid[i][j].getTileType() == Tile.EMPTY_TILE && getNeighbours(j, i).size() == 3) {
                    corners.add(new Point(j, i));
                }
            }
        }
        return corners;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < MainClass.MAZE_SIZE; ++i) {
            for (int j = 0; j < MainClass.MAZE_SIZE; ++j) {
                switch (mazeGrid[i][j].getTileType()) {
                    case Tile.EMPTY_TILE:
                        output += ' ';
                        break;
                    case Tile.WALL_TILE:
                        output += 'X';
                        break;
                    case Tile.PLAYER_TILE:
                        output += Player.firstChar;
                        break;
                    case Tile.WEAPON_TILE:
                    case Tile.ARMOR_TILE:
                    case Tile.POTION_TILE:
                        output += '*';
                        break;
                    case Tile.EXIT_TILE:
                        output += '#';
                        break;
                }
            }
            output += "\n";
        }
        return output + "\n";
    }

    /**
     * @param playerLoc is the players location in the maze
     * @param sizeView is the size of the maze to display to the user
     * @return a String displaying the maze around the player
     */
    public String displayPlayerLocation(Point playerLoc, int sizeView) {
        String output = "";
        int startingY = Math.max(playerLoc.getY() - sizeView / 2, 0);
        int startingX = Math.max(playerLoc.getX() - sizeView / 2, 0);
        for (int i = startingY; i < sizeView + startingY; ++i) {
            for (int j = startingX; j < sizeView + startingX; ++j) {
                try {
                    switch (mazeGrid[i][j].getTileType()) {
                        case Tile.EMPTY_TILE:
                            output += ' ';
                            break;
                        case Tile.WALL_TILE:
                            output += 'X';
                            break;
                        case Tile.PLAYER_TILE:
                            output += Player.firstChar;
                            break;
                        case Tile.WEAPON_TILE:
                        case Tile.ARMOR_TILE:
                        case Tile.POTION_TILE:
                            output += '*';
                            break;
                        case Tile.EXIT_TILE:
                            output += '#';
                            break;
                    }
                } catch (Exception e) {
                }
            }
            output += "\n";
        }
        return output;
    }

    public int getTileType(Point loc) {
        return mazeGrid[loc.getY()][loc.getX()].getTileType();
    }

    public void setEmptyTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setEmpty();
    }

    public void setPlayerTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setPlayer();
    }

    public void setArmorTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setArmor();
    }

    public void setWeaponTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setWeapon();
    }

    public void setPotionTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setPotion();
    }

    public void setExitTile(Point loc) {
        mazeGrid[loc.getY()][loc.getX()].setExit();
    }
}
