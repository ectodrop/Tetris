import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Text here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Text extends Actor
{
    /**
     * Act - do whatever the Text wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public Text(String text, Color color){
        setImage(new GreenfootImage(16*12, 32));
        getImage().setColor(color);
        getImage().drawString(text, 20,20);
    }
    public void update(String text){
        getImage().clear();
        getImage().drawString(text, 20,20);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
