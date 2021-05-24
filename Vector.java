/**
 * Write a description of class Vector here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Vector{
    public int x, y;
    public static Vector Up = new Vector(0, -1);
    public static Vector Down = new Vector(0, 1);
    public static Vector Right = new Vector(1, 0);
    public static Vector Left = new Vector(-1, 0);
    public static Vector Zero = new Vector(0, 0);
    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Vector add(Vector other){
        int newX = other.x + this.x;
        int newY = other.y + this.y;
        
        return new Vector(newX, newY);
    }
    public Vector sub(Vector other){
        int newX = other.x - this.x;
        int newY = other.y - this.y;
        
        return new Vector(newX, newY);
    }
    public void update(int newX, int newY){
        x = newX;
        y = newY;
    }
    public String toString(){
        return "Vector ("+x +"," + y + ")" ;
    }
    public void rotate90(boolean clockwise){
        Vector[] ccw = { new Vector(0,-1), new Vector(1,0)};
        Vector[] cw = { new Vector (0,1), new Vector(-1,0)};
        Vector[] rotation = clockwise? cw:ccw;
        
        int xRot = (rotation[0].x * x) + (rotation[1].x * y);
        int yRot = (rotation[0].y * x) + (rotation[1].y * y);
        
        update(xRot, yRot);
    }
}
