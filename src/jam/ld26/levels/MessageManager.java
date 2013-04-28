package jam.ld26.levels;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class MessageManager implements KeyListener {
    static final int ANNOUNCE_MAX_TIME = 1000;
    private String announce = null, inputMsg = null;
    private String userInput = "";
    private float announceTimer = 0;
    private Input input = null;
    private boolean inputFinished;
    
    public MessageManager() {
        
    }
    
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        if(announce != null) {
           g.drawString(announce, 10, 10);
        } else if(inputMsg != null) {
           g.drawString(inputMsg + " " + userInput, 10, 10);
        }
    }
    
    public void update(GameContainer gc, int delta) {
        if(announce != null) {
            announceTimer += delta;
            if(announceTimer > ANNOUNCE_MAX_TIME) {
                announceTimer = 0;
                announce = null;
            }
        }
        this.input = gc.getInput();
        this.input.addKeyListener(this);
    }
    
    public void announce(String msg) {
        announce = msg;
    }
    
    public void input(String msg) {
        inputMsg = msg;
        inputFinished = false;
    }
    
    public String getUserInput() {
        return userInput;
    }
    
    public void finishInput() {
        inputMsg = null;
        inputFinished = true;
    }
    
    public boolean isInputFinished() {
        return inputFinished;
    }

    @Override
    public void keyPressed(int i, char c) {
        System.out.println(i);
        if (inputMsg != null) {
            if (i == 14 && userInput.length() > 0) { //backspace
                userInput = userInput.substring(0, userInput.length() - 1);
            } else if(i == 28 && userInput.length() > 0) {
                finishInput();
            } else {
                userInput += c;
            }
        }
    }

    @Override
    public void keyReleased(int i, char c) {
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }
}
