import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Start here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Start extends World
{

    /**
     * Constructor for objects of class Start.
     * 
     */
    public Start()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(18, 22, 16, false);
        setBackground("startbackground.png");
        for(int i = 0; i < 18; i++){
            for(int j = 15; j < 22; j++){
                Tile t = new Tile();
                t.setImage(Greenfoot.getRandomNumber(7) + ".png");
                addObject(t, i , j);
            }
        }
        if(Grid.music.isPlaying()){
            Grid.music.stop();
        }
    }
    public void act(){
        if(Greenfoot.isKeyDown("Enter")){
            Greenfoot.setWorld(new Grid());
        }
    }
}
