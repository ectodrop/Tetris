import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Grid extends World
{
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public static GreenfootSound music = new GreenfootSound("Tetris_theme.wav");
    GreenfootSound line = new GreenfootSound("selection.wav");
    GreenfootSound clear = new GreenfootSound("clear.wav");
    GreenfootSound hit = new GreenfootSound("fall.wav");
    private int[] framesPerRow = {
        53, 49, 45, 41, 37, 33, 28, 22, 17, 11, 10, 9 ,8, 7, 6, 6, 5, 5, 4, 4, 3};
        
    private int[] counter = new int[3];
    
    private int leftBoundary = 4;
    private int rightBoundary = 13;
    private int floor = 19;
    private int width = (rightBoundary-leftBoundary)+1;
    private int height = floor+1;
    private int horizontalDelay = 8;
    private int stepDelay = 3;
    
    private Vector spawnPosition = new Vector(8, 1);
    
    private ControlledTile[] tiles = new ControlledTile[4];
    private ControlledTile[] ghostTiles = new ControlledTile[4];
    private List<Tile> garbagePool = new ArrayList<Tile>();
    private List<Integer> queuedTiles= new ArrayList<Integer>();
    
    
    private int curFrame = 0;
    private int currentPiece;
    private int queuedPiece;
    private int storedPiece = -1;
    private int rotationIndex = 0;
    private int lockDelay = 0;
    private int level = 0;
    private int score = 0;
    private int linesCleared = 0;
    
    private boolean canStore = true;
    private boolean lockReady = false;
    
    
    private Text levelText = new Text("Level: 0", Color.BLACK);
    private Text scoreText = new Text("Score: 0", Color.BLACK);
    private Box hold = new Box("Hold");
    private Box next = new Box("Next");
    
    public Vector[][] JLSTZ_OFFSET_DATA = new Vector[5][4];
    public Vector[][] O_OFFSET_DATA = new Vector[1][4];
    public Vector[][] I_OFFSET_DATA = new Vector[5][4];
    public Grid()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(18, 22, 16, false);
        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new ControlledTile();
            ghostTiles[i] = new ControlledTile();
            addObject(tiles[i],0,0);
            addObject(ghostTiles[i],0,0);
        }
        addObject(hold, 2, 2);
        addObject(next, 15, 2);
        addObject(levelText, 10, 20);
        addObject(scoreText, 14, 20);
        for(int i = 0; i < width*height; i++){
            garbagePool.add(new Tile());
        }
        
        setBackground("background.png");
        getBackground().setColor(Color.BLACK);
        getBackground().drawRect(16*leftBoundary,0,16*10,16*20);
        for(int i = 0; i <= floor; i++){
            for(int j  = leftBoundary; j <= rightBoundary; j++){
                getBackground().drawImage(new GreenfootImage("gridcell.png"), j*16, i*16);
            }
        }
        
        
        
        spawnPiece(Greenfoot.getRandomNumber(7));
        nextQueue();
        music.setVolume(30);
        hit.setVolume(80);
        clear.setVolume(90);
        line.setVolume(80);
        
        music.playLoop();
        
        //srs offset data
        JLSTZ_OFFSET_DATA[0][0] = Vector.Zero;
        JLSTZ_OFFSET_DATA[0][1] = Vector.Zero;
        JLSTZ_OFFSET_DATA[0][2] = Vector.Zero;
        JLSTZ_OFFSET_DATA[0][3] = Vector.Zero;
    
        JLSTZ_OFFSET_DATA[1][0] = Vector.Zero;
        JLSTZ_OFFSET_DATA[1][1] = new Vector(1,0);
        JLSTZ_OFFSET_DATA[1][2] = Vector.Zero;
        JLSTZ_OFFSET_DATA[1][3] = new Vector(-1, 0);
    
        JLSTZ_OFFSET_DATA[2][0] = Vector.Zero;
        JLSTZ_OFFSET_DATA[2][1] = new Vector(1, 1);
        JLSTZ_OFFSET_DATA[2][2] = Vector.Zero;
        JLSTZ_OFFSET_DATA[2][3] = new Vector(-1, 1);
    
        JLSTZ_OFFSET_DATA[3][0] = Vector.Zero;
        JLSTZ_OFFSET_DATA[3][1] = new Vector(0, -2);
        JLSTZ_OFFSET_DATA[3][2] = Vector.Zero;
        JLSTZ_OFFSET_DATA[3][3] = new Vector(0, -2);
    
        JLSTZ_OFFSET_DATA[4][0] = Vector.Zero;
        JLSTZ_OFFSET_DATA[4][1] = new Vector(1, -2);
        JLSTZ_OFFSET_DATA[4][2] = Vector.Zero;
        JLSTZ_OFFSET_DATA[4][3] = new Vector(-1, 2);
    
        
        I_OFFSET_DATA[0][0] = Vector.Zero;
        I_OFFSET_DATA[0][1] = new Vector(-1, 0);
        I_OFFSET_DATA[0][2] = new Vector(-1, -1);
        I_OFFSET_DATA[0][3] = new Vector(0, -1);
    
        I_OFFSET_DATA[1][0] = new Vector(-1, 0);
        I_OFFSET_DATA[1][1] = Vector.Zero;
        I_OFFSET_DATA[1][2] = new Vector(1, -1);
        I_OFFSET_DATA[1][3] = new Vector(0, -1);
    
        I_OFFSET_DATA[2][0] = new Vector(2, 0);
        I_OFFSET_DATA[2][1] = Vector.Zero;
        I_OFFSET_DATA[2][2] = new Vector(-2, -1);
        I_OFFSET_DATA[2][3] = new Vector(0, -1);
    
        I_OFFSET_DATA[3][0] = new Vector(-1, 0);
        I_OFFSET_DATA[3][1] = new Vector(0, -1);
        I_OFFSET_DATA[3][2] = new Vector(1, 0);
        I_OFFSET_DATA[3][3] = new Vector(0, 1);
    
        I_OFFSET_DATA[4][0] = new Vector(2, 0);
        I_OFFSET_DATA[4][1] = new Vector(0, 2);
        I_OFFSET_DATA[4][2] = new Vector(-2, 0);
        I_OFFSET_DATA[4][3] = new Vector(0, -2);
    
        
        O_OFFSET_DATA[0][0] = Vector.Zero;
        O_OFFSET_DATA[0][1] = Vector.Down;
        O_OFFSET_DATA[0][2] = new Vector(-1, 1);
        O_OFFSET_DATA[0][3] = Vector.Left;
    }
    public void stopped()
    {
         music.pause();
    }
     
    public void started()
    {
        music.playLoop();
    }
    public void act(){
        checkInputs();
        
        curFrame++;
        
        if(curFrame % framesPerRow[clamp(level, 0 ,20)] == 0 && canMoveTile(Vector.Down, tiles)){
            moveTile(Vector.Down);
        }
        if(!lockReady && !canMoveTile(Vector.Down, tiles)){
            lockReady = true;
        }
        if(lockReady){
            lockDelay++;
            if(lockDelay >= 60 && !canMoveTile(Vector.Down, tiles)){
                lockPiece();
                lockReady = false;
                lockDelay = 0;
            }
        }
        
        /*
         * 
        
         */
    }
    private int clamp(int value, int low, int high){
        if(value < low){
            return low;
        }
        else if(value > high){
            return high;
        }
        else return value;
    }
    private void storePiece(){
        if(storedPiece != -1){
            int temp = currentPiece;
            currentPiece = storedPiece;
            storedPiece = temp;
            hold.setPiece(storedPiece);
            spawnPiece(currentPiece);
        }else{
            storedPiece = currentPiece;
            hold.setPiece(storedPiece);
            
            spawnPiece(queuedPiece);
            nextQueue();
        }
    }
    private void updateGhost(){
        for(int i = 0; i < ghostTiles.length; i++){
            ghostTiles[i].setLocation(tiles[i].getX(), tiles[i].getY());
            
        }
        while(canMoveTile(Vector.Down, ghostTiles)){
            for(ControlledTile t: ghostTiles){
                t.setLocation(t.getX()+Vector.Down.x,t.getY()+Vector.Down.y);
            }
        }
    }
    private boolean canMoveTile(Vector direction, ControlledTile[] tileList){
        for(ControlledTile t: tileList){
            Vector target = direction.add(new Vector(t.getX(), t.getY()));
            List<Tile> block = getObjectsAt(target.x, target.y, Tile.class);
            if(target.x < leftBoundary || target.x > rightBoundary ||
            (block.size() > 0 && direction.x != 0)){
                return false;
            }
            else if((block.size() > 0 && direction.x == 0)|| target.y > floor){
                return false;
            }
        }
        return true;
    }
    private void moveTile(Vector movement){
        for(ControlledTile t: tiles){
            t.setLocation(t.getX()+movement.x,t.getY()+movement.y);
        }
        updateGhost();
    }
    private void moveGarbageTilesDown(int row, int times){
        Vector direction = Vector.Down;
        boolean canMove = true;
        for(int k = 0; k < times; k++){
            for(int i = row; i >= 0; i--){
                for(int g = leftBoundary; g <= rightBoundary;g++){
                    List<Tile> t = getObjectsAt(g,i, Tile.class);
                    if(t.size() > 0){
                        Vector target = direction.add(new Vector(t.get(0).getX(), t.get(0).getY()));
                        if(getObjectsAt(target.x,target.y, Tile.class).size() > 0){
                            canMove = false;
                            break;
                        }
                    }
                }
                if(!canMove) continue;
                for(int j = leftBoundary;  j <= rightBoundary; j++){
                    List<Tile> occupied = getObjectsAt(j,i, Tile.class);
                    if(occupied.size() > 0){
                        occupied.get(0).setLocation(
                            occupied.get(0).getX()+direction.x,
                            occupied.get(0).getY()+direction.y
                            );
                    }
                }
            }
        }
    }
    private void changeTileColor(int type){
        for(ControlledTile t: tiles){
            t.setImage(type+".png");
        }
        for(ControlledTile gt: ghostTiles){
            gt.setImage(type+".png");
            gt.getImage().setTransparency(100);
        }
    }
    private void lockPiece(){
        canStore = true;
        for(int i = 0; i < tiles.length; i++){
            Tile t = getPooledTile();
            t.setImage(currentPiece + ".png");
            addObject(t, tiles[i].getX(), tiles[i].getY());
        }
        hit.play();
        //check for line clears
        int combo = 0;
        int lastRow = 0;
        List<Tile> clearedLines = new ArrayList<Tile>();
        for(int i = 0; i <= floor; i++){
            List<Tile> line = new ArrayList<Tile>();
            
            for(int j = leftBoundary;  j <= rightBoundary; j++){
                List<Tile> occupied = getObjectsAt(j,i, Tile.class);
                if(occupied.size() > 0){
                    line.addAll(occupied);
                }
            }
            
            if(line.size() == (rightBoundary-leftBoundary) +1){
                linesCleared++;
                if(linesCleared % 10 == 0) level++;
                combo++;
                levelText.update("Level: " + level);
                clearedLines.addAll(line);
                lastRow = i;
            }
        }
        if(combo > 0){
            for(Tile t : clearedLines){
                t.setImage("white.png");
            }
            if(combo == 4){
                clear.play();
            }else{
                line.play();
            }
            Greenfoot.delay(30);
            removeObjects(clearedLines);
            clearedLines.clear();
            moveGarbageTilesDown(lastRow-1, combo);
            
        }
        
        for(int j = leftBoundary;  j <= rightBoundary; j++){
            List<Tile> occupied = getObjectsAt(j,-1, Tile.class);
            if(occupied.size() > 0){
                Greenfoot.setWorld(new GameOver(score));
            }
        }
            
        //printGrid();
        
        switch(combo){
            case 1:
               score+= 40*(level+1);
               scoreText.update("Score: " + score);
               break;
            case 2:
               score+= 100*(level+1);
               scoreText.update("Score: " + score);
               break;
            case 3:
               score+= 300*(level+1);
               scoreText.update("Score: " + score);
               break;
            case 4:
               score+= 1200*(level+1);
               scoreText.update("Score: " + score);
               break;
        }
        spawnPiece(queuedPiece);
        nextQueue();
    }
    public Tile getPooledTile(){
        for(int i = 0; i < garbagePool.size(); i++){
            if(!getObjects(null).contains(garbagePool.get(i))){//check if the tile is already added to the world
                return garbagePool.get(i);
            }
        }
        return null;
    }
    public void printObjects(){
        for(int i = 0; i <= floor; i++){
            for(int j = leftBoundary;  j <= rightBoundary; j++){
                System.out.print(getObjectsAt(j,i, Tile.class).size() + " ");
                
            }
            System.out.println();
            
        }
    }
    private void rotatePiece(boolean cw, boolean offset){
        
        int oldRotIndex = rotationIndex;
        rotationIndex += cw ? 1:-1;
        rotationIndex = (rotationIndex % 4 + 4) % 4;
        
        //System.out.println("Rotation (" + oldRotIndex + ", " + rotationIndex +")");
        
        Vector center = new Vector(tiles[0].getX(), tiles[0].getY());
        for(int i = 1; i < 4; i++){
            Vector dir = center.sub(new Vector(tiles[i].getX(), tiles[i].getY()));
            dir.rotate90(cw);
            Vector newPos = dir.add(center);
            tiles[i].setLocation(newPos.x, newPos.y);
        }
        if(!offset) return;
        boolean canOffset = canOffset(oldRotIndex, rotationIndex);
        if(!canOffset){
            rotatePiece(!cw, false);
        }
        updateGhost();
        /*
        
        
        */
    }
    private boolean canOffset(int oldRotation, int newRotation){
        Vector[][] offsetData;
        
        if(currentPiece == 0){
            offsetData = I_OFFSET_DATA;
        }
        else if(currentPiece == 1){
            offsetData = O_OFFSET_DATA;
        }else{
            offsetData = JLSTZ_OFFSET_DATA;
        }
        boolean canMove = false;
        Vector end = Vector.Zero;
        for(int i = 0; i < offsetData.length; i++){
            Vector offset1 = offsetData[i][oldRotation];
            Vector offset2 = offsetData[i][newRotation];
            
            end = offset2.sub(offset1);
            
            if(canMoveTile(end, tiles)){
                canMove = true;
                break;
            }
        }
        //System.out.println(end.toString());
        if(canMove){
            moveTile(end);
        }
        return canMove;
    }
    private void nextQueue(){
        if(queuedTiles.size() == 0){
            for(int i = 0; i < 7; i++){
                queuedTiles.add(i);
            }
        }
        int random = Greenfoot.getRandomNumber(queuedTiles.size());
        queuedPiece = queuedTiles.get(random);
        queuedTiles.remove(random);
        next.setPiece(queuedPiece);
    }
    private void hardDrop(){
        int distance = 0;
        while(canMoveTile(Vector.Down, tiles)){
            moveTile(Vector.Down);
            
            distance++;
        }
        
        score += distance*(level+1);
        scoreText.update("Score: " + score);
        lockPiece();
        lockReady = false;
        lockDelay = 0;
    }
    private void spawnPiece(int type){
        //System.out.println(type);
        Vector spawnPos = spawnPosition;
        while(getObjectsAt(spawnPos.x,spawnPos.y, Tile.class).size() > 0){
            spawnPos = spawnPos.add(Vector.Up);
        }
        currentPiece = type;
        rotationIndex = 0;
        lockDelay = 0;
        changeTileColor(type);
        tiles[0].setLocation(spawnPos.x, spawnPos.y);
        
        switch(type){
            case 0://I piece
                tiles[1].setLocation(spawnPos.x-1, spawnPos.y);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y);
                tiles[3].setLocation(spawnPos.x+2, spawnPos.y);
                break;
            case 1://O piece
                tiles[1].setLocation(spawnPos.x, spawnPos.y-1);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y-1);
                tiles[3].setLocation(spawnPos.x+1, spawnPos.y);
                break;
            case 2://T piece
                tiles[1].setLocation(spawnPos.x-1, spawnPos.y);
                tiles[2].setLocation(spawnPos.x, spawnPos.y-1);
                tiles[3].setLocation(spawnPos.x+1, spawnPos.y);
                break;
            case 3://L piece
                tiles[1].setLocation(spawnPos.x-1, spawnPos.y);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y);
                tiles[3].setLocation(spawnPos.x+1, spawnPos.y-1);
                break;
            case 4://J piece
                tiles[1].setLocation(spawnPos.x-1, spawnPos.y);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y);
                tiles[3].setLocation(spawnPos.x-1, spawnPos.y-1);
                break;
            case 5://Z piece
                tiles[1].setLocation(spawnPos.x, spawnPos.y-1);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y);
                tiles[3].setLocation(spawnPos.x-1, spawnPos.y-1);
                break;
            case 6://S piece
                tiles[1].setLocation(spawnPos.x-1, spawnPos.y);
                tiles[2].setLocation(spawnPos.x+1, spawnPos.y-1);
                tiles[3].setLocation(spawnPos.x, spawnPos.y-1);
                break;
            default:
                System.out.println("Error invalid block type");
                break;
        }
        
        updateGhost();
    }
    public void checkInputs(){
        String key = Greenfoot.getKey();
        if(key != null){
            
            if(key.equals("space")) hardDrop();
            if(key.equals("c") && canStore){
                storePiece();
                canStore = false;
            }
            if(key.equals("x")|| key.equals("up")) rotatePiece(true, true);
            if(key.equals("z")) rotatePiece(false, true);
            
        }
        if(clamp(counter[1], 0, horizontalDelay) % horizontalDelay == 0 //slight delay until you can move the tile fastly horizontally
        && canMoveTile(Vector.Right, tiles)&& 
        counter[1] % stepDelay == 0 && 
        Greenfoot.isKeyDown("Right")){
            moveTile(Vector.Right);
        }
        
        if(clamp(counter[2], 0, horizontalDelay) % horizontalDelay == 0 && canMoveTile(Vector.Left, tiles) && 
        counter[2] % stepDelay == 0 && 
        Greenfoot.isKeyDown("Left")){
            moveTile(Vector.Left);
        }
        if(Greenfoot.isKeyDown("Right")){
            counter[1]++;
        }
        else{
            counter[1] = 0;
        }
        
        if(Greenfoot.isKeyDown("Left")){
            counter[2]++;
        }
        else{
            counter[2] = 0;
        }
        if(Greenfoot.isKeyDown("Down")) counter[0]++;
        if(counter[0] % 3 == 0 && canMoveTile(Vector.Down, tiles)){
            moveTile(Vector.Down);
            counter[0] = 1;
            score++;
            scoreText.update("Score: " + score);
        }
        
    }
}

