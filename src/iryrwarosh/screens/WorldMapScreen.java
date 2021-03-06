package iryrwarosh.screens;

import iryrwarosh.Point;
import iryrwarosh.Tile;
import iryrwarosh.WorldMap;
import iryrwarosh.WorldScreen;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class WorldMapScreen implements Screen {
	private Screen previous;
	private WorldMap map;
	private Point playerPosition;
	private boolean showAll;
	
	public WorldMapScreen(Screen previous, WorldMap map, Point playerPosition){
		this(previous, map, playerPosition, false);
	}

	public WorldMapScreen(Screen previous, WorldMap map, Point playerPosition, boolean showAll){
		this.previous = previous;
		this.map = map;
		this.playerPosition = playerPosition;
		this.showAll = showAll;
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.setDefaultBackgroundColor(AsciiPanel.black);
		terminal.clear();
		
		
		for (int x = 0; x < map.width(); x++)
		for (int y = 0; y < map.height(); y++){
			if (showAll)
				displayExploredScreen(x*3+1, y*3+1, map.screen(x, y), terminal);
			else
			{
				switch (map.explorationStatus(x, y)){
				case 0: break;
				case 1: displayUnexploredScreen(x*3+1, y*3+1, map.screen(x, y), terminal); break;
				case 2: displayExploredScreen(x*3+1, y*3+1, map.screen(x, y), terminal); break;
				}
			}
		}
		terminal.write('@', 
				playerPosition.x / 19 * 3 + 1, playerPosition.y / 9 * 3 + 1, 
				
				AsciiPanel.brightWhite, 
				map.screen(playerPosition.x / 19, playerPosition.y / 9).defaultGround.background());
	}

	private void displayUnexploredScreen(int x, int y, WorldScreen screen, AsciiPanel terminal) {
		int wall = WorldScreen.WALL;

		displayDarkTile(terminal, x-1, y-1, screen.nwWater ? Tile.WATER1 : screen.defaultWall);
		displayDarkTile(terminal, x,   y-1, screen.nWater ? Tile.WATER1 : (screen.nEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayDarkTile(terminal, x+1, y-1, screen.neWater ? Tile.WATER1 : screen.defaultWall);

		displayDarkTile(terminal, x-1, y,   screen.wWater ? Tile.WATER1 : (screen.wEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayDarkTile(terminal, x,   y,   screen.defaultGround);
		displayDarkTile(terminal, x+1, y,   screen.eWater ? Tile.WATER1 : (screen.eEdge==wall ? screen.defaultWall : screen.defaultGround));

		displayDarkTile(terminal, x-1, y+1, screen.swWater ? Tile.WATER1 : screen.defaultWall);
		displayDarkTile(terminal, x,   y+1, screen.sWater ? Tile.WATER1 : (screen.sEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayDarkTile(terminal, x+1, y+1, screen.seWater ? Tile.WATER1 : screen.defaultWall);
	}
	
	private void displayExploredScreen(int x, int y, WorldScreen screen, AsciiPanel terminal) {
		int wall = WorldScreen.WALL;

		displayTile(terminal, x-1, y-1, screen.nwWater ? Tile.WATER1 : screen.defaultWall);
		displayTile(terminal, x,   y-1, screen.nWater ? Tile.WATER1 : (screen.nEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayTile(terminal, x+1, y-1, screen.neWater ? Tile.WATER1 : screen.defaultWall);

		displayTile(terminal, x-1, y,   screen.wWater ? Tile.WATER1 : (screen.wEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayTile(terminal, x,   y,   screen.defaultGround);
		displayTile(terminal, x+1, y,   screen.eWater ? Tile.WATER1 : (screen.eEdge==wall ? screen.defaultWall : screen.defaultGround));

		displayTile(terminal, x-1, y+1, screen.swWater ? Tile.WATER1 : screen.defaultWall);
		displayTile(terminal, x,   y+1, screen.sWater ? Tile.WATER1 : (screen.sEdge==wall ? screen.defaultWall : screen.defaultGround));
		displayTile(terminal, x+1, y+1, screen.seWater ? Tile.WATER1 : screen.defaultWall);
	}
	
	private void displayDarkTile(AsciiPanel terminal, int x, int y, Tile t){
		terminal.write(t.glyph(), x, y, t.color().darker().darker().darker(), t.background().darker().darker().darker());
	}
	
	private void displayTile(AsciiPanel terminal, int x, int y, Tile t){
		terminal.write(t.glyph(), x, y, t.color(), t.background());
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return previous;
	}
}
