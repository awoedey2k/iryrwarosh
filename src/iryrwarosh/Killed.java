package iryrwarosh;

public class Killed extends Message {
	public Creature attacker;
	public Creature attacked;
	
	public Killed(World world, Creature attacker, Creature attacked){
		super(world, addArticle("The", attacker.name()) + " killed " + addArticle("the", attacked.name()));
		this.attacker = attacker;
		this.attacked = attacked;
	}

	@Override
	public boolean involves(Creature player) {
		return attacker == player || attacked == player;
	}
}
