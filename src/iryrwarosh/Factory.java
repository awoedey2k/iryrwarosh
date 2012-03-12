package iryrwarosh;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import asciiPanel.AsciiPanel;

public class Factory {
	private HashMap<Tile,List<CreatureTrait>> monsterTraits;
	
	public Factory(){
		setMonsterTraits();
	}
	
	private void setMonsterTraits(){
		monsterTraits = new HashMap<Tile,List<CreatureTrait>>();
		
		for (Tile biome : new Tile[]{ Tile.GREEN_TREE1, Tile.PINE_TREE1, Tile.BROWN_TREE1, Tile.BROWN_TREE4, Tile.WHITE_TREE1, 
				Tile.GREEN_ROCK, Tile.BROWN_ROCK, Tile.WHITE_ROCK, Tile.DESERT_SAND1, Tile.WATER1 }){
			
			List<CreatureTrait> traits = new ArrayList<CreatureTrait>();
			
			if (Math.random() < 0.05)
				traits.add(CreatureTrait.FLYER);
			
			while (traits.size() < 3){
				CreatureTrait trait = CreatureTrait.getRandom();
				if (!traits.contains(trait))
					traits.add(trait);
			}
			
			monsterTraits.put(biome, traits);
		}
	}
	
	public Weapon knuckles(){
		Weapon w = new Weapon("Knuckes", ')', Tile.WHITE_ROCK.background());
		w.comboAttackPercent = 33;
		return w;
	}
	
	public Weapon knife(){
		Weapon w = new Weapon("Knife", ')', Tile.WHITE_ROCK.background());
		w.evadeAttackPercent = 75;
		return w;
	}
	
	public Weapon club(){
		Weapon w = new Weapon("Club", ')', Tile.BROWN_ROCK.background());
		w.circleAttackPercent = 75;
		return w;
	}
	
	public Weapon sword(){
		Weapon w = new Weapon("Sword", ')', AsciiPanel.white);
		w.finishingAttackPercent = 75;
		return w;
	}
	
	public Weapon spear(){
		Weapon w = new Weapon("Spear", ')', Tile.BROWN_ROCK.background());
		w.distantAttackPercent = 25;
		w.isImuneToSpikes = true;
		return w;
	}
	
	public Weapon staff(){
		Weapon w = new Weapon("Staff", ')', Tile.BROWN_ROCK.background());
		w.counterAttackPercent = 25;
		w.isImuneToSpikes = true;
		return w;
	}

	public Weapon weapon() {
		switch ((int)(Math.random() * 6)){
		case 0: return knuckles();
		case 1: return knife();
		case 2: return club();
		case 3: return sword();
		case 4: return spear();
		default: return staff();
		}
	}
	
	public Creature goblin(final World world){
		int hue = 30 + (int)(Math.random() * 90);
		Creature goblin = new Creature("goblin", 'g', Tile.hsv(hue, 50, 50), 2){
			public void update(World world){
				super.update(world);
				wander(world);
			}
		};
		goblin.addTrait(CreatureTrait.WALKER);
		
		world.add(goblin);
		goblin.equip(world, weapon());
		return goblin;
	}
	
	private int monstersCreated = 0;
	public Creature monster(final World world, Tile biome){
		Color color = null;
		List<Point> candidates = null;
		Point candidate = null;
		String name = null;
		
		switch (biome) {
		case GREEN_TREE1:
			name = "evergreen monster";
			color = biome.color();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case PINE_TREE1:
			name = "alpine monster";
			color = biome.color().darker();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case BROWN_TREE1:
			name = "boreal monster";
			color = biome.color().darker();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case BROWN_TREE4:
			name = "forest monster";
			color = biome.color();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case WHITE_TREE1:
			name = "pale monster";
			color = biome.color().brighter();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case GREEN_ROCK:
			name = "hill monster";
			color = biome.background().brighter();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case BROWN_ROCK:
			name = "mountan monster";
			color = biome.background().brighter();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case WHITE_ROCK:
			name = "snow monster";
			color = biome.background().brighter();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case DESERT_SAND1:
			name = "desert monster";
			color = biome.color();
			candidates = world.screensOfType(biome);
			if (candidates.size() == 0)
				return null;
			candidate = candidates.get((int)(Math.random() * candidates.size()));
			break;
		case WATER1:
			name = "water monster";
			color = biome.color().brighter();
			break;
		}
		
		boolean isBigMonster = Math.random() * 1000 < (monstersCreated - 200);
		
		Creature monster = new Creature(name, isBigMonster ? 'M' : 'm', color, 3){
			public void update(World world){
				super.update(world);
				wander(world);
			}
		};
		
		if (biome == Tile.WATER1){
			monster.addTrait(CreatureTrait.SWIMMER);
		} else {
			monster.addTrait(CreatureTrait.WALKER);
		}
		
		for (CreatureTrait trait : monsterTraits.get(biome))
			monster.addTrait(trait);
		
		if (isBigMonster){
			monster.addTrait(CreatureTrait.EXTRA_HP);
			
			CreatureTrait trait = CreatureTrait.getRandom();
			while (monster.hasTrait(trait))
				trait = CreatureTrait.getRandom();
			monster.addTrait(trait);
		}
		
		if (candidate == null)
			world.add(monster);
		else
			world.addToScreen(monster, candidate.x, candidate.y);
		
		monstersCreated++;
		
		return monster;
	}

	public Creature player(World world) {
		Creature player = new Creature("player", '@', AsciiPanel.brightWhite, 10);
		player.addTrait(CreatureTrait.WALKER);
		world.add(player);
		return player;
	}
	
	public Creature zora(World world){
		Creature zora = new Creature("zora", 'z', Tile.WATER1.color(), 1);
		zora.addTrait(CreatureTrait.SWIMMER);
		zora.addTrait(CreatureTrait.HIDER);
		zora.addTrait(CreatureTrait.TERRITORIAL);
		zora.addTrait(CreatureTrait.ROCK_SPITTER);
		world.add(zora);
		return zora;
	}

	public Armor heavyArmor() {
		Armor heavy = new Armor("heavy armor", '[', AsciiPanel.white);
		return heavy;
	}

	public Armor greenTunic() {
		Armor tunic = new Armor("green tunic", '[', Tile.GREEN_ROCK.background());
		return tunic;
	}

	public Armor wizardRobe() {
		Armor robes = new Armor("wizard robes", '[', AsciiPanel.white);
		return robes;
	}

	public Armor cloak() {
		Armor cloak = new Armor("dark cloak", '[', AsciiPanel.brightBlack);
		return cloak;
	}

	public Armor gillsuit() {
		Armor gillsuit = new Armor("gillsuit", '[', Tile.WATER1.color());
		return gillsuit;
	}

	public Armor vestments() {
		Armor vestments = new Armor("holy vestments", '[', AsciiPanel.white);
		return vestments;
	}
}
