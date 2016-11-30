package coe528.majorProject;

public class Point {
    /* OVERVIEW: The Point class is a single point on a coordinate plane. It has an x and y coordinate
     * This class is mutable
     * 
     * ABSTRACTION FUNCTION
     * AF(c) = a Point A
     *      A.X = c.X where { c.X is an integer }
     *      A.Y = c.Y where { c.Y is an integer }
     *      
     * REP INVARIANT
     * c.X is an integer
     * c.Y is an integer
     */
    
    private int X;
    private int Y;

    public Point(int x, int y) {
        X = x;
        Y = y;
    }

    public Point(Point newPoint) {
        if (newPoint == null){
            throw new IllegalArgumentException("Input arguments cannot be null");
        }
        X = newPoint.X;
        Y = newPoint.Y;
    }

    
    /**
     * REQUIRES
     * @param p1 a valid Point object
     * @param p2 a valid Point object
     * @return a new Point who's x and y coordinate is the sum of the x and y coordinates of p1 and p2
     * 
     * @Effects Takes p1.X and p2.X and adds them together to get a new value and does the same with p1.Y and p2.Y
     * Returns a Point who's X and Y coordinates are the sum found from p1 and p2's coordinate components
     */
    public static Point add2Points(Point p1, Point p2) {
        return new Point(p1.X + p2.X, p1.Y + p2.Y);
    }

    /**
     * EFFECTS: Returns string representation of the object
     * @see java.lang.Object#toString()
     * @return the String representation of the Point
     */
    @Override
    public String toString() {
        return "{ X = " + X + ", Y = " + Y + " }";
    }

    /**
     * @return the x coordinate
     */
    public int getX() {
        return X;
    }

    /**
     * @return the y coordinate
     */
    public int getY() {
        return Y;
    }

    /**
     * @param value is the value to increment X by. If the value is negative
     * then X will be decremented
     * @EFFECTS adds value to the x coordinate
     */
    public void incrementX(int value) {
        X += value;
    }

    /**
     * @param value is the value to increment Y by. If the value is negative
     * then Y will be decremented
     * @EFFECTS adds value to the Y coordinate
     */
    public void incrementY(int value) {
        Y += value;
    }
    
    /**
     * @return a boolean describing whether the rep is valid
     */
    public boolean repOK(){
        if ((Integer)X instanceof Integer && (Integer)Y instanceof Integer){
            return true;
        }
        else {
            return false;
        }
    }
}
