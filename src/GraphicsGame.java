import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Timer;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class GraphicsGame extends GraphicsPane implements ActionListener {
	// you will use program to get access to all of the GraphicsProgram calls
	private MainApplication program;
	private Level level = new Level();
	public static final int WINDOW_WIDTH = 1550;
	public static final int WINDOW_HEIGHT = 800;
	private static final int LIFE_WIDTH = 40;
	private static final int LIFE_HEIGHT = 40;
	
	private GLabel lives = new GLabel("LIVES: ", 10, 30);
	private GLabel score = new GLabel("SCORE: 0", 1250, 25);
	private GOval lifeOne = new GOval(100, 5, LIFE_WIDTH, LIFE_HEIGHT);
	private GOval lifeTwo = new GOval(150, 5, LIFE_WIDTH, LIFE_HEIGHT);
	private GOval lifeThree = new GOval(200, 5, LIFE_WIDTH, LIFE_HEIGHT);
	
	//used in keyEvents
	//row == y, column == x
	private double x;
	private double y;
	private int row;
	private int col;
	
	//character and map selections
	private int monkey = 0;
	private String s = "";
	
	//Vectors to hold entity values
	//(row, column, type)
	Vector<Entity> barrels = new Vector<Entity>();
	Vector<Entity> walls = new Vector<Entity>();
	Vector<Entity> bananas = new Vector<Entity>();
	Vector<Entity> cherries = new Vector<Entity>();
	Vector<Entity> mangos = new Vector<Entity>();
	
	//vectors to store spot on screen for adding/removing
	private Vector<GImage> barrelImages = new Vector<GImage>();
	private Vector<GImage> bananaImages = new Vector<GImage>();
	private Vector<GImage> mangoImages = new Vector<GImage>();
	private Vector<GImage> cherryImages = new Vector<GImage>();
	private Vector<GRect> wallImages = new Vector<GRect>();
	private Vector<GLine> gridLines = new Vector<GLine>();
	private GImage character = null;
	private GImage entity = null;
	private GRect winSpace = null;
	private GRect wall = null;
	
	private Timer timer = new Timer(1000, this);
	Vector<Boolean> switcher = null;
	Vector<Boolean> vertic = null;

	
	public GraphicsGame(MainApplication app) {
		this.program = app;
		
		lives.setFont("Arial-26");
		score.setFont("Arial-26");
		lives.setColor(Color.RED);
		score.setColor(Color.RED);
		
		switcher = level.getSwitcher();
		vertic = level.getVertic();
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
	
	public void resize()
	{
		if(s=="easy")
		{
			character.scale(.2);
			program.add(character);
			
			for (GImage temp:bananaImages) {
				temp.scale(1.4);
				program.add(temp);
			}
			for (GImage temp:cherryImages) {
				temp.scale(1.4);
				program.add(temp);
			}
			for (GImage temp:mangoImages) {
				temp.scale(1.4);
				program.add(temp);
			}
			for (GImage temp:barrelImages) {
				temp.scale(1.0);
				program.add(temp);
			}
		}
		else if(s=="medium")
		{
			character.scale(.15);
			program.add(character);
			
			for (GImage temp:bananaImages) {
				temp.scale(.8);
				program.add(temp);
			}
			for (GImage temp:cherryImages) {
				temp.scale(.8);
				program.add(temp);
			}
			for (GImage temp:mangoImages) {
				temp.scale(.8);
				program.add(temp);
			}
			for (GImage temp:barrelImages) {
				temp.scale(.6);
				program.add(temp);
			}
		}
		else
		{
			character.scale(.11);
			program.add(character);
			
			for (GImage temp:bananaImages) {
				temp.scale(.4);
				program.add(temp);
			}
			for (GImage temp:cherryImages) {
				temp.scale(.4);
				program.add(temp);
			}
			for (GImage temp:mangoImages) {
				temp.scale(.4);
				program.add(temp);
			}
			for (GImage temp:barrelImages) {
				temp.scale(.37);
				program.add(temp);
			}
		}
			
	}
	
	private void drawEntities() {
		//entities will be reused in this method for barrels, and fruits
		
		walls = level.getWalls();
		barrels = level.getBarrels();
		bananas = level.getBananas();
		cherries = level.getCherries();
		mangos = level.getMangos();
		
		int startRow = level.map.getStartSpace().getRow();
		int startCol = level.map.getStartSpace().getCol();
		if(monkey == 1)
		{
			character = new GImage("Chimp_Cartoon.jpg", startCol * spaceWidth(), startRow * spaceHeight());
		}
		else if(monkey==2)
		{
			character = new GImage("Gorilla_Cartoon.jpg", startCol * spaceWidth(), startRow * spaceHeight());
		}
		else
		{
			character = new GImage("Orangutan_Cartoon.jpg", startCol * spaceWidth(), startRow * spaceHeight());
		}
		
		for (Entity temp:barrels) {
			entity = new GImage("barrel.png", temp.getCol() * spaceWidth(), temp.getRow() * spaceHeight());
			barrelImages.add(entity);
		}
		
		for (Entity temp:bananas) {
			entity = new GImage("Banana.png", temp.getCol() * spaceWidth(), temp.getRow() * spaceHeight());
			bananaImages.add(entity);
		}
		
		for (Entity temp:cherries) {
			entity = new GImage("Cherry.png", temp.getCol() * spaceWidth(), temp.getRow() * spaceHeight());
			cherryImages.add(entity);
		}
		
		for (Entity temp:mangos) {
			entity = new GImage("Mango.png", temp.getCol() * spaceWidth(), temp.getRow() * spaceHeight());
			mangoImages.add(entity);
		}
		
		resize();
		
		for (Entity temp:walls) {
			wall = new GRect(temp.getCol() * spaceWidth(), temp.getRow() * spaceHeight(), spaceWidth(), spaceHeight());
			wall.setFillColor(Color.BLACK);
			wall.setFilled(true);
			program.add(wall);
			wallImages.add(wall);
		}
		
		program.add(lives);
		program.add(score);
		drawGridLines(s);
		drawWinningSpace();
		drawLives();
	}
	
	private void drawLives() {
		
		
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
		
		winSpace = new GRect(winCol * spaceWidth(), winRow * spaceHeight(), spaceWidth(), spaceHeight());
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
	
	public void removeEntities()
	{
		program.remove(character);
		
		for(GRect l:wallImages)
		{
			program.remove(l);
		}
		for(GImage l:cherryImages)
		{
			program.remove(l);
		}
		for(GImage l:mangoImages)
		{
			program.remove(l);
		}
		for(GImage l:bananaImages)
		{
			program.remove(l);
		}
		for(GImage l:barrelImages)
		{
			program.remove(l);
		}
		program.remove(lives);
		program.remove(score);
		removeGridLines();
		program.remove(winSpace);
		program.remove(lifeOne);
		program.remove(lifeTwo);
		program.remove(lifeThree);
		
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
		timer.start();
	}

	@Override
	public void hideContents() {
		removeEntities();
		timer.stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {	
		x = character.getLocation().getX();
		y = character.getLocation().getY();
		row = (int)(y / spaceHeight());
		col = (int)(x / spaceWidth());
		Space charOldSpace = level.getCharSpace();
		Space targetSpace;
		
		if (e.getKeyChar() == 'w') {
			targetSpace = new Space(row-1,col);
			if (level.map.wallCollision(targetSpace)) {
				return;
			}
			
			if ((row-1) * spaceHeight() >= 0) {
				Entity entityCollision = level.map.getEnt(targetSpace);
				
				level.collision(targetSpace);
				
				if (entityCollision != null) {
					if (entityCollision.getType() == EntityType.BANANA) {
						GImage bananaImage = new GImage("Banana.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						bananaImages.remove(bananaImage);
						program.remove(bananaImage);
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						GImage cherryImage = new GImage ("Cherry.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						cherryImages.remove(cherryImage);
						program.remove(cherryImage);
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						GImage mangoImage = new GImage ("Mango.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						mangoImages.remove(mangoImage);
						program.remove(mangoImage);
					}
					/*
					if (entityCollision.getType() == EntityType.BANANA) {
						bananaImages = null;
						bananas = level.getBananas();
						
						for (Entity banana:bananas) {
							GImage bananaImage = new GImage("Banana.png", banana.getCol() * spaceWidth(), banana.getRow() * spaceHeight());
							bananaImages.add(bananaImage);
							program.add(bananaImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						cherryImages = null;
						cherries = level.getCherries();
						
						for (Entity cherry:cherries) {
							GImage cherryImage = new GImage("Cherry.png", cherry.getCol() * spaceWidth(), cherry.getRow() * spaceHeight());
							cherryImages.add(cherryImage);
							program.add(cherryImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						mangoImages = null;
						mangos = level.getMangos();
						
						for (Entity mango:mangos) {
							GImage mangoImage = new GImage("Mango.png", mango.getCol() * spaceWidth(), mango.getRow() * spaceHeight());
							mangoImages.add(mangoImage);
							program.add(mangoImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					*/
				}
				
				if (charOldSpace != level.getCharSpace()) {
					targetSpace = level.getCharSpace();
				}
				level.setCharSpace(targetSpace.getRow(),targetSpace.getCol());	
				character.setLocation(targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
			}
		}
		if (e.getKeyChar() == 'a') {
			targetSpace = new Space(row, col-1);
			
			if (level.map.wallCollision(targetSpace)) {
				return;
			}
			
			if ((col-1) * spaceWidth() >= 0) {
				Entity entityCollision = level.map.getEnt(targetSpace);
				
				level.collision(targetSpace);
				
				if (entityCollision != null) {
					if (entityCollision.getType() == EntityType.BANANA) {
						GImage bananaImage = new GImage("Banana.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						bananaImages.remove(bananaImage);
						program.remove(bananaImage);
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						GImage cherryImage = new GImage ("Cherry.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						cherryImages.remove(cherryImage);
						program.remove(cherryImage);
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						GImage mangoImage = new GImage ("Mango.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						mangoImages.remove(mangoImage);
						program.remove(mangoImage);
					}
					/*
					if (entityCollision.getType() == EntityType.BANANA) {
						bananaImages = null;
						bananas = level.getBananas();
						
						for (Entity banana:bananas) {
							GImage bananaImage = new GImage("Banana.png", banana.getCol() * spaceWidth(), banana.getRow() * spaceHeight());
							bananaImages.add(bananaImage);
							program.add(bananaImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						cherryImages = null;
						cherries = level.getCherries();
						
						for (Entity cherry:cherries) {
							GImage cherryImage = new GImage("Cherry.png", cherry.getCol() * spaceWidth(), cherry.getRow() * spaceHeight());
							cherryImages.add(cherryImage);
							program.add(cherryImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						mangoImages = null;
						mangos = level.getMangos();
						
						for (Entity mango:mangos) {
							GImage mangoImage = new GImage("Mango.png", mango.getCol() * spaceWidth(), mango.getRow() * spaceHeight());
							mangoImages.add(mangoImage);
							program.add(mangoImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					*/
				}
				
				if (charOldSpace != level.getCharSpace()) {
					targetSpace = level.getCharSpace();
				}
				level.setCharSpace(targetSpace.getRow(),targetSpace.getCol());	
				character.setLocation(targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
			}
		}
		if (e.getKeyChar() == 's') {
			targetSpace = new Space(row+1, col);
			
			if (level.map.wallCollision(targetSpace)) {
				return;
			}
			
			if ((row+1) * spaceHeight() < WINDOW_HEIGHT) {
				Entity entityCollision = level.map.getEnt(targetSpace);
				
				level.collision(targetSpace);
				
				if (entityCollision != null) {
					if (entityCollision.getType() == EntityType.BANANA) {
						GImage bananaImage = new GImage("Banana.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						bananaImages.remove(bananaImage);
						program.remove(bananaImage);
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						GImage cherryImage = new GImage ("Cherry.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						cherryImages.remove(cherryImage);
						program.remove(cherryImage);
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						GImage mangoImage = new GImage ("Mango.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						mangoImages.remove(mangoImage);
						program.remove(mangoImage);
					}
					/*
					if (entityCollision.getType() == EntityType.BANANA) {
						bananaImages = null;
						bananas = level.getBananas();
						
						for (Entity banana:bananas) {
							GImage bananaImage = new GImage("Banana.png", banana.getCol() * spaceWidth(), banana.getRow() * spaceHeight());
							bananaImages.add(bananaImage);
							program.add(bananaImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						cherryImages = null;
						cherries = level.getCherries();
						
						for (Entity cherry:cherries) {
							GImage cherryImage = new GImage("Cherry.png", cherry.getCol() * spaceWidth(), cherry.getRow() * spaceHeight());
							cherryImages.add(cherryImage);
							program.add(cherryImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						mangoImages = null;
						mangos = level.getMangos();
						
						for (Entity mango:mangos) {
							GImage mangoImage = new GImage("Mango.png", mango.getCol() * spaceWidth(), mango.getRow() * spaceHeight());
							mangoImages.add(mangoImage);
							program.add(mangoImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					*/
				}
				
				if (charOldSpace != level.getCharSpace()) {
					targetSpace = level.getCharSpace();
				}
				level.setCharSpace(targetSpace.getRow(),targetSpace.getCol());	
				character.setLocation(targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
			}
		}
		if (e.getKeyChar() == 'd') {
			targetSpace = new Space(row, col+1);
			
			if (level.map.wallCollision(targetSpace)) {
				return;
			}
			
			else {
				Entity entityCollision = level.map.getEnt(targetSpace);
				level.collision(targetSpace);
				
				if (entityCollision != null) {
					if (entityCollision.getType() == EntityType.BANANA) {
						GImage bananaImage = new GImage("Banana.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						bananaImages.remove(bananaImage);
						program.remove(bananaImage);
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						GImage cherryImage = new GImage ("Cherry.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						cherryImages.remove(cherryImage);
						program.remove(cherryImage);
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						GImage mangoImage = new GImage ("Mango.png", targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
						mangoImages.remove(mangoImage);
						program.remove(mangoImage);
					}
					/*
					if (entityCollision.getType() == EntityType.BANANA) {
						bananaImages = null;
						bananas = level.getBananas();
						
						for (Entity banana:bananas) {
							GImage bananaImage = new GImage("Banana.png", banana.getCol() * spaceWidth(), banana.getRow() * spaceHeight());
							bananaImages.add(bananaImage);
							program.add(bananaImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.CHERRY) {
						cherryImages = null;
						cherries = level.getCherries();
						
						for (Entity cherry:cherries) {
							GImage cherryImage = new GImage("Cherry.png", cherry.getCol() * spaceWidth(), cherry.getRow() * spaceHeight());
							cherryImages.add(cherryImage);
							program.add(cherryImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					if (entityCollision.getType() == EntityType.MANGO) {
						mangoImages = null;
						mangos = level.getMangos();
						
						for (Entity mango:mangos) {
							GImage mangoImage = new GImage("Mango.png", mango.getCol() * spaceWidth(), mango.getRow() * spaceHeight());
							mangoImages.add(mangoImage);
							program.add(mangoImage);
							
							for (GRect temp:wallImages) {
								program.add(temp);
							}
						}
					}
					*/
				}
				
				if (targetSpace != level.getCharSpace()) {
					targetSpace = level.getCharSpace();
				}
				level.setCharSpace(targetSpace.getRow(),targetSpace.getCol());	
				character.setLocation(targetSpace.getCol() * spaceWidth(), targetSpace.getRow() * spaceHeight());
			}
		}
		
		if (level.getLives() == 0) {
			hideContents();
			//GButton lose = new GButton("YOU LOSE. \"SPACE\" TO CONTINUE", 50, 50, WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
			//program.add(lose);
			
			LevelSelectPane levelSelect = new LevelSelectPane(program, monkey);
			program.pause(3000);
			program.switchToLevelSelect(levelSelect);
			
			/*
			while (e.getKeyChar() != ' ') {
				if (e.getKeyChar() == ' ') {
					program.switchToLevelSelect(null);
				}
			}
			*/
		}
		
		if(level.map.getWinSPace() == level.getCharSpace())
		{
			hideContents();
			//GParagraph win = new GParagraph("YOU Win! \"SPACE\" TO CONTINUE", 50, 50);
			//program.add(win);
			System.out.println("\nWIN\n");
			
			LevelSelectPane levelSelect = new LevelSelectPane(program, monkey);
			program.pause(3000);
			program.switchToLevelSelect(levelSelect);
		}
		score.setLabel("SCORE: " + level.getScore());
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		program.add(character);
	}
	
	public void actionPerformed(ActionEvent e) {
		GImage barrelImage = null;
		
		for (GImage barrel:barrelImages) {
			program.remove(barrel);
		}
		for (Entity barrel:barrels) {
			barrelImage = new GImage("barrel.png", 0, 0);
			barrelImage.setLocation(barrel.getCol() * spaceWidth(), barrel.getRow() * spaceHeight());
			
			barrelImages.add(barrelImage);
			program.add(barrelImage);
		}
	}
}
