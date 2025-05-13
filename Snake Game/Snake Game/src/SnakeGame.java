import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //gameLogic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);


        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this); // miliseconds
        gameLoop.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //Grid
        for(int i = 0; i < boardWidth/tileSize; i++){
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize,0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize );
        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        // Snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Oh no! Game Over:", tileSize - 20,tileSize);
             g.drawString("Press R to restart!", tileSize - 20,tileSize + 25);
        }
        else{
            g.drawString("Score" + String.valueOf(snakeBody.size()), tileSize - 20, tileSize);
        }
        }

        public void placeFood() {
    while (true) {
        int foodX = random.nextInt(boardWidth / tileSize);
        int foodY = random.nextInt(boardHeight / tileSize);
        Tile newFood = new Tile(foodX, foodY);

        // Check that newFood is not on the snake
        boolean onSnake = collision(snakeHead, newFood);
        for (Tile part : snakeBody) {
            if (collision(part, newFood)) {
                onSnake = true;
                break;
            }
        }

        if (!onSnake) {
            food.x = newFood.x;
            food.y = newFood.y;
            break;
        }
    }
}
    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){

        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Snake Body movement
        for(int i = snakeBody.size()-1; i>= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else{
                Tile prevSnakePart = snakeBody.get(i -1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Game over conditions
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);

            if (collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }
        
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
             snakeHead.y*tileSize <0 || snakeHead.y * tileSize > boardHeight){
                gameOver = true;
             }
    }
    public void restartGame() {
    snakeHead = new Tile(5, 5);
    snakeBody.clear();
    placeFood();
    velocityX = 0;
    velocityY = 0;
    gameOver = false;
    gameLoop.start();
    repaint();
}
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

        @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
        velocityX = 0;
        velocityY = -1;
       }
       else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
        velocityX = 0;
        velocityY = 1;
       }
       else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
        velocityX = -1;
        velocityY = 0;
       }else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
        velocityX = 1;
        velocityY = 0;
       }

       //restarting da game
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
        restartGame();
    }
    }
      
    
     

    // do not need
    @Override
    public void keyTyped(KeyEvent e) {
       
    }



    @Override
    public void keyReleased(KeyEvent e) {
    }
}
 