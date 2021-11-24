import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class GraphicsGame extends GraphicsPane {
	// you will use program to get access to all of the GraphicsProgram calls
	private MainApplication program;
	private static final int LIFE_WIDTH = 40;
	private static final int LIFE_HEIGHT = 40;
	public static final int WINDOW_WIDTH = 1600;
	public static final int WINDOW_HEIGHT = 800;
	private Level level = new Level();
	
	private GLabel lives = new GLabel("LIVES:", 10, 30);
	private GLabel score = new GLabel("SCORE:", 1250, 25);
	private GImage entities;
	GRect winSpace;
	GRect wall;
	
	private int monkey = 0;
	private int points = 0;
	Vector<Entity> barrels = new Vector<Entity>();
	Vector<Entity> walls = new Vector<Entity>();
	Vector<Entity> bananas = new Vector<Entity>();
	Vector<Entity> cherries = new Vector<Entity>();
	Vector<Entity> mangos = new Vector<Entity>();
	private String s = "";
	
	
	private ArrayList<GLine> gridLines = new ArrayList<GLine>();

	public GraphicsGame(MainApplication app) {
		this.program = app;
		
		lives.setFont("Arial-26");
		score.setFont("Arial-26");
		lives.setColor(Color.RED);
		score.setColor(Color.RED);
	}
	
	public void setMonkey(int monkey) {
		this.monkey = monkey;
	}
	
	public int getMonkey() {
		return monkey;
	}
	
	public void setupEasy() {
		level.createLevel("small");
		s="easy";
	}
	
	public void setupMedium() {
		level.createLevel("medium");
		s="medium";
	}
	
	public void setupHard() {
		level.createLevel("large");
		s="hard";
	}
	
	private void drawEntities() {
		//entities will be reused in this method for character, barrels, and fruits
		//not working yet, need images in folder
		
		
		//int startRow = level.map.getStartSpace().getRow();
		//int startCol = level.map.getStartSpace().getCol();
		//entities = new GImage(character image name, startRow, startCol);
		//program.add(entities);
		
		walls = level.getWalls();
		
		for (Entity temp:walls) {
			wall = new GRect(temp.getRow() * spaceWidth(), temp.getCol() * spaceHeight(), spaceWidth(), spaceHeight());
			wall.setFillColor(Color.BLACK);
			wall.setFilled(true);
			program.add(wall);
		}
		
		//for (Entity temp:barrels) {
			//entities = new GImage(/*barrel image name*/, temp.getRow() * spaceWidth(), temp.getCol() * spaceHeight());
			//program.add(entities);
		//}
		
		//for (Entity temp:bananas) {
			//entities = new GImage(/*banana image name*/, temp.getRow() * spaceWidth(), temp.getCol() * spaceHeight());
			//program.add(entities);
		//}
		
		//for (Entity temp:cherries) {
			//entities = new GImage(/*cherry image name*/, temp.getRow() * spaceWidth(), temp.getCol() * spaceHeight());
			//program.add(entities);
		//}
		
		//for (Entity temp:mangos) {
			//entities = new GImage(/*mango image name*/, temp.getRow() * spaceWidth(), temp.getCol() * spaceHeight());
			//program.add(entities);
		//}
	}
	
	private void drawLives() {
		GOval lifeOne = new GOval(100, 5, LIFE_WIDTH, LIFE_HEIGHT);
		GOval lifeTwo = new GOval(150, 5, LIFE_WIDTH, LIFE_HEIGHT);
		GOval lifeThree = new GOval(200, 5, LIFE_WIDTH, LIFE_HEIGHT);
		
		lifeOne.setFilled(true);
		lifeTwo.setFilled(true);
		lifeThree.setFilled(true);
		
		lifeOne.setFillColor(Color.red);
		lifeTwo.setFillColor(Color.red);
		lifeThree.setFillColor(Color.red);
		
		program.add(lifeOne);
		program.add(lifeTwo);
		program.add(lifeThree); 
		
	}
	
	private void drawWinningSpace() {
		int winRow = level.getWin().getRow();
		int winCol = level.getWin().getCol();
		
		winSpace = new GRect(winRow * spaceWidth(), winCol * spaceHeight(), spaceWidth(), spaceHeight());
		winSpace.setFillColor(Color.RED);
		winSpace.setFilled(true);
		program.add(winSpace);
	}
	
	private void drawGridLines(String s) {
		int num = 0;
		if(s=="hard")
		{num = 25;}
		else if(s=="medium")
		{num = 15;}
		else
		{num=10;}
		for (int i = 0; i < num; ++i) {
			
			GLine gridLine = new GLine(0, spaceHeight()*i, WINDOW_WIDTH, spaceHeight()*i);
			gridLines.add(gridLine);
		}
		for (int i = 0; i < num; ++i) {
			
			GLine gridLine = new GLine(spaceWidth()*i, 0, spaceWidth()*i,WINDOW_HEIGHT );
			gridLines.add(gridLine);
		}
		for(GLine l:gridLines)
		{
			program.add(l);
		}
	}
	
	public void removeGridLines()
	{
		for(GLine l:gridLines)
		{
			program.remove(l);
		}
	}
	
	private double spaceWidth() {
		int num = 0;
		if(s=="hard")
		{num = 25;}
		else if(s=="medium")
		{num = 15;}
		else
		{num=10;}
		return WINDOW_WIDTH / num;
	}
	
	private double spaceHeight() {
		int num = 0;
		if(s=="hard")
		{num = 25;}
		else if(s=="medium")
		{num = 15;}
		else
		{num=10;}
		return WINDOW_HEIGHT / num;
	}

	@Override
	public void showContents() {
		drawEntities();
		program.add(lives);
		program.add(score);
		drawGridLines(s);
		drawWinningSpace();
		drawLives();
	}

	@Override
	public void hideContents() {
		program.remove(lives);
		program.remove(score);
		removeGridLines();
		program.remove(winSpace);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
