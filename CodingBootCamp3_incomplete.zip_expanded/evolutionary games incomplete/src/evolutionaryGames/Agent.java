package evolutionaryGames;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Agent implements Steppable {
	public Stoppable event;//event.stop() is used to remove the agent from the schedule
	long id;//this is the agent's id
	Strategy strategy; //this is the agent's strategy
	double resources;
	boolean played = false;
	Memory memory = null;
	int x; //location on the x-axis
	int y; //location on the y-axis
	int xdir; //x direction of change
	int ydir; //y direction of change
	int age; //current age of agent
	int maxAge;//age at which agent dies

	/**
	 * Agent constructor method.
	 * @param state
	 * @param id
	 * @param strategy
	 * @param resources
	 * @param x
	 * @param y
	 * @param xdir
	 * @param ydir
	 * @param startup
	 */
	public Agent(Environment state, long id, Strategy strategy, double resources, int x, int y, int xdir, int ydir, boolean startup) {
		super();
		this.id = id;
		this.strategy = strategy;
		this.resources = resources;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		maxAge = (int)(state.averageAge + state.random.nextGaussian()*state.sdAge);
		if(startup) {
			age = state.random.nextInt((int)state.averageAge);
		}
		else {
			age = 0;
		}
		
		switch(strategy) {
		case TFTM: ; case TFTS:; case PAVLOVM:; case PAVLOVS: memory = new Memory(state.memorySize);
		}
		
	}
	
	/**
	 * Returns the strategy of an opponent.  
	 * @param opponent
	 * @return
	 */
	public Strategy getStrategy(Agent opponent) {
		switch(strategy) {
		case COOPERATOR:
			return Strategy.COOPERATOR;
		case DEFECTOR:
			return Strategy.DEFECTOR;
		case WALKAWAY:
			return Strategy.COOPERATOR;
		case WALKAWAYD:
			return Strategy.DEFECTOR; 
		case TFTM:; case TFTS:
			return getStrategyTFT(opponent);
		case PAVLOVM:; case PAVLOVS:
			return getStrategyPAVLOV(opponent);
		default:
			return Strategy.COOPERATOR;
		}
	}
	
	public Strategy getStrategyTFT(Agent opponent) {
		Triple m = memory.getLastMemory(opponent);
		if (m == null) {
			return Strategy.COOPERATOR;
		}
		switch(m.opponentStrategy) {
		
		case COOPERATOR:
			return Strategy.COOPERATOR;
		case DEFECTOR:
			return Strategy.DEFECTOR;
		default:
			return Strategy.COOPERATOR;
		}
	}
	
	public Strategy getStrategyPAVLOV(Agent opponent) {
		Triple m = memory.getLastMemory(opponent);
		if (m == null) {
			return Strategy.COOPERATOR;
		}
		switch(m.opponentStrategy) {
		case COOPERATOR:
			return m.myStrategy;
		case DEFECTOR:
			if (m.myStrategy == Strategy.COOPERATOR) {
				return Strategy.DEFECTOR;
			} else {
				return Strategy.COOPERATOR;
			}
		default:
			return Strategy.COOPERATOR;
		}
	}

	/**
	 * Calculates an agent's payoff given the strategy it played and the strategy of its opponent.
	 * @param state
	 * @param opponent
	 * @return
	 */
	public Strategy play(Environment state, Agent opponent) {
		Strategy myStrategy = getStrategy(opponent);
		Strategy myOpponentStrategy = opponent.getStrategy(this);
		switch(myOpponentStrategy) {
		case COOPERATOR:
			switch(myStrategy) {
			case COOPERATOR:
				resources += state.cooperate_cooperator;
				break;
			case DEFECTOR:
				resources += state.defect_cooperator;
				break;
			}
			break;
		case DEFECTOR:
			switch(myStrategy) {
			case COOPERATOR:
				resources += state.cooperate_defector;
				break;
			case DEFECTOR:
				resources += state.defect_defector;
				break;
			}
			break;
		}
		this.played = true;
		switch(strategy) {
		case TFTM: case TFTS: case PAVLOVM: case PAVLOVS:
			//Good place to add code for remembering an opponent. An agent that
	        //is TFT or PAVLOV must remember the opponent it played.
	        memory.storeMemory(opponent, myOpponentStrategy, myStrategy);
		}
		
		return myOpponentStrategy;
	}

	/**
	 * An agent using a mobile strategy first searches for agents in its search radius.  If there are none, then
	 * it randomly moves then serches again for an opponent to play.  If so they play.  
	 * @param state
	 */
	public void mobileStrategy(Environment state) {
		Bag agents = search(state);
		if(agents == null || agents.numObjs == 0) 
		{
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
			agents = search(state);
			if(agents == null) {return;}
			Agent opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
		else {
			Agent opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
	}
	/**
	 * A walkaway strategy is just like a mobile strategy except that if a defection is encountered, it moves randomly.
	 * @param state
	 */
	public void walkawayStrategy(Environment state) {
		Bag agents = search(state);
		Agent opponent;
		Strategy opponentStrategy;
		if(agents == null || agents.numObjs == 0) {
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
			agents = search(state);
			if(agents == null) {return;}
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			opponentStrategy = play(state, opponent);
			opponent.play(state, this);
		}
		else {
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			opponentStrategy = play(state, opponent);
			opponent.play(state, this);
		}
		if(opponentStrategy == Strategy.DEFECTOR) { //walk away
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
		}
	}
	
	/**
	 * This is the stationary strategy.
	 * @param state
	 */
	
	public void stationaryStrategy(Environment state) {
		Bag agents = search(state);
		Agent opponent;
		if(agents == null || agents.numObjs == 0) {
			return;
		}
		else {
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
	}
	
	/**
	 * This is the play method. Notice that strategy in the switch statement is the agent's strategy.
	 * @param state
	 */

	public void play(Environment state) {
		switch(strategy) {
		case COOPERATOR:;case DEFECTOR:
			mobileStrategy(state);
			break;
		case WALKAWAY:; case WALKAWAYD:
			walkawayStrategy(state);
			break;
		case TFTS:; case PAVLOVS:
			stationaryStrategy(state);
			break;
		case TFTM:; case PAVLOVM:
			mobileStrategy(state);
		}
	}
	
	/**
	 * This method finds an opponent to play from a bag of agents supplied to it.
	 * @param state
	 * @param agents
	 * @return
	 */
	public Agent findOpponent(Environment state, Bag agents) {
		if(agents == null || agents.numObjs == 0) return null;
		Agent a = (Agent)agents.objs[state.random.nextInt(agents.numObjs)];//get a random agent
		while(a.played && a.equals(this)) {
			agents.remove(a);
			if(agents.numObjs == 0) {
				return null; //none to be found
			}
			a = (Agent)agents.objs[state.random.nextInt(agents.numObjs)];//get another random agent
		}
		return a;
	}

	/**
	 * When an agent reproduces, this method can return a mutant strategy from the mutationList if the
	 * mutations rate is > 0.0
	 * @param state
	 * @return
	 */

	public Strategy mutation(Environment state) {
		Strategy newStrategy;
		if(state.mutationRange) {
			if(state.random.nextBoolean(state.mutationRate)) {
				newStrategy = state.mutationList.get(state.random.nextInt(state.mutationList.size()));
				while(newStrategy == this.strategy) { //find a strategy different from the parent
					newStrategy = state.mutationList.get(state.random.nextInt(state.mutationList.size()));
				}
				return newStrategy;
			}
			else {
				return this.strategy; //the current agent
			}
		}
		else {
			if(state.random.nextBoolean(state.mutationRate)) {
				newStrategy = Strategy.values()[state.random.nextInt(Strategy.values().length)];
				while(newStrategy == this.strategy) { //find a strategy different from the parent
					newStrategy = Strategy.values()[state.random.nextInt(Strategy.values().length)];
				}
				return newStrategy;
			}
			else {
				return this.strategy; //the current agent
			}
		}
	}

	/**
	 * This method finds a random and empty location in space, if an empty location exists within the
	 * reproductionRadius
	 * @param state
	 * @return
	 */
	public Int2D findLocation(Environment state) {
		if(!state.groups) {
			Int2D location = state.sparseSpace.getRandomEmptyMooreLocation(state, x, y, state.reproductionRadius, state.sparseSpace.TOROIDAL, false);
			return location;
		}
		else {
			if(state.reproductionRadius == 0) {
				return new Int2D(x,y);
			}
			else {
				int xch = state.random.nextInt(state.reproductionRadius+1);
				int ych = state.random.nextInt(state.reproductionRadius+1);
				int newx = state.sparseSpace.stx(x+xch);
				int newy = state.sparseSpace.sty(y+ych);
				return new Int2D(newx,newy);
			}
		}
	}

	/**
	 * The replication or reproduction method for agents.
	 * @param state
	 * @return
	 */
	public Agent replicate(Environment state) {
		Int2D location;
		if(state.localReproduction) { //find a location in the reproduction radius
			location = this.findLocation(state);
		}
		else {//fine a location anywhere
			if(!state.groups)
				location = state.uniqueXY();
			else
				location = state.locationXY();
		}
		if(location == null) {
			resources = 0; //reproduction failed
			return null; //reproduction cannot proceed because there is not place for the agent
		}
		if(carryingCapacityExceeded(state)) {
			resources = 0; //reproduction failed
			return null; //exceeds 
		}
		int xdir = state.random.nextInt(3)-1;
		int ydir = state.random.nextInt(3)-1;
		Strategy newStrategy = mutation(state);
		long newId = state.id++;
		Agent a = new Agent(state,newId, newStrategy,0,location.x,location.y,xdir,ydir, false); //create a new agent
		a.event = state.schedule.scheduleRepeating(a);//schedule it
		state.sparseSpace.setObjectLocation(a, location.x, location.y);//put it in space
		colorByStrategy(a.strategy, state,  a);//color it by strategy
		return a;
	}
	
	/**
	 * Color by strategy method: 
	 * state.gui.setOvalPortrayal2DColor(agent, red, green, blue, opacity);
	 * @param strategy
	 * @param state
	 * @param a
	 */

	public void colorByStrategy(Strategy strategy, Environment state, Agent a) {
		switch(strategy) {
		case COOPERATOR: 
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)1);
			break;
		case DEFECTOR:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)1);
			break;
		case WALKAWAY:
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)1, (float)0, (float)1);
			break;
		case WALKAWAYD:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0.6, (float)0, (float)1);
			break;
		case TFTM:
			state.gui.setOvalPortrayal2DColor(a, (float)0.5, (float)0.6, (float)0, (float)1);
			break;
		case TFTS:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0.5, (float)1);
			break;
		case PAVLOVM:
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0.6, (float)0.6, (float)1);
			break;
		case PAVLOVS:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0.6, (float)1);
			break;
		default:
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)0.5, (float)0.5);
			break;
		}
	}
	/**
	 * Places an agent in space.
	 * @param state
	 */
	public void placeAgent(Environment state) {
		if(!state.groups) {
			int tempx = state.sparseSpace.stx(x + xdir);
			int tempy = state.sparseSpace.sty(y + ydir);
			Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
			if(b == null || b.numObjs == 0){
				x = tempx;
				y = tempy;
				state.sparseSpace.setObjectLocation(this, x, y);
			}
		}
		else {
			x = state.sparseSpace.stx(x + xdir);
			y = state.sparseSpace.sty(y + ydir);
			state.sparseSpace.setObjectLocation(this, x, y);
		}
	}
	
	/**
	 * Searches locally in it search radius for agents and returns a bag of them. If none, it returns null
	 * @param state
	 * @return
	 */
	public Bag search(Environment state) {
		Bag agents = null;
		if(state.groups) {//For Aktipis model
			Bag temp = state.sparseSpace.getObjectsAtLocation(x,y);
			if(temp == null) {
				return null;
			}
			agents = new Bag(temp.numObjs);//size of temp
			agents.addAll(temp); //have to make a copy
			agents.remove(this);
			return agents;
		}
		else {
			agents = state.sparseSpace.getMooreNeighbors(x, y, state.searchOpponent, state.sparseSpace.TOROIDAL,false);
			return agents;
		}
	}
	
	/**
	 * When an agent is removed from the simulation, it's die method is called
	 * @param state
	 */
	public void die(Environment state) {
		state.sparseSpace.remove(this);//remove from space
		event.stop();//remove it from the schedule
	}

	/**
	 * Determines whether the carrying capacity of the population is exceeded.  
	 * @param state
	 * @return
	 */
	public boolean carryingCapacityExceeded(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		if(agents.numObjs<=state.carryingCapacity) {
			return false;
		}
		else {
			return true;
		}
	}

	public void step(SimState state) {
		Environment eState = (Environment)state;
		if(age > maxAge) { //if too old, it dies
			die(eState);
			return;
		}
		if(resources >= eState.resoucesToReproduce) { //enough resources to reproduce?
			replicate(eState);//create a baby agent
		}
		age++; //aging
		if(played) {
			return;
			} //if played, do not play again
		play(eState);
		
	}

}

