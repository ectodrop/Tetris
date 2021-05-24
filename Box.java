import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Box here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Box extends Actor
{
    /**
     * Act - do whatever the Box wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    String label;
    public Box(String label){
        this.label = label;
        setImage("box.png");
        getImage().setColor(Color.WHITE);
        getImage().drawString(label, 18, 15);
    }
    public void setPiece(int type){
        getImage().clear();
        setImage("box.png");
        getImage().setColor(Color.WHITE);
        getImage().drawString(label, 18, 15);
        getImage().drawImage(new GreenfootImage(type+"_piece.png"),10, 20);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
