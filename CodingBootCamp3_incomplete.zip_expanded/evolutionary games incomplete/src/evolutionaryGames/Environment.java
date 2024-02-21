package evolutionaryGames;

import java.util.ArrayList;

import sim.util.Int2D;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	public static long id = 0;//start id.  for each agent created, increment by 1
	public int _cooperators = 500;//variables _cooperators to are the initial numbers of each strategy
	public int _defectors = 500;
	public int _walkaways = 0;
	public int _walkawayD = 0;
	public int _tftm = 0;
	public int _tfts = 0;
	public int _pavlovm = 0;
	public int _pavlovs = 0;
	public int memorySize = 7; //size of an agent's memory of TFT and PAVLOV strategies
	public double active = 1.0; //probability of random movement
	public boolean groups = true; //Multiple agents can be in the same location or what we will call group
	public double averageAge = 150;//this is how long in steps on average an agent lives
	public double sdAge = 25; //this is the standard deviation in the variation lifespan of agents relative to averageAge
	public double mutationRate = 0.0;//mutation rate from one strategy to the next
	public double resoucesToReproduce = 100;//total resource required to reproduce an agent
	public double minResourcesStart = 0; //this and the next variable specifies a range of resources to start a simulation
	public double maxResourcesStart = 50;
	public double carryingCapacity = 1000;//the maximum total number of agents in a population
	public int searchOpponent = 1; //search radius for an opponent if groups is false
	public double cooperate_cooperator = 3; //payoff for cooperation
	public double defect_defector = 0;//defector payoff
	public double cooperate_defector = -1;//suckers payoff
	public double defect_cooperator = 5; //temptation payoff
	public int reproductionRadius = 1;//the distance from parent to offspring at birth
	public boolean localReproduction = true;//reproduce offspring close to parents i.e., within the reproductionRadius
	public ArrayList<Strategy> mutationList = new ArrayList();//strategies that can be mutants
	public boolean mutationRange = true;//if true, use the mutationList
	



	/**
	 * Constructor method
	 * @param seed
	 * @param observer
	 */
	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}	

	/**
	 * Find a unique x,y location
	 * @return
	 */
	public Int2D uniqueXY() {
		int x = random.nextInt(gridWidth);
		int y = random.nextInt(gridHeight);
		while(sparseSpace.getObjectsAtLocation(x, y)!= null) {
			x = random.nextInt(gridWidth);
			y = random.nextInt(gridHeight);
		}
		return new Int2D(x,y);
	}
	
	/**
	 * Find a random x,y location, it is not necessarily unique
	 * @return
	 */
	
	public Int2D locationXY() {
		int x = random.nextInt(gridWidth);
		int y = random.nextInt(gridHeight);
		return new Int2D(x,y);
	}
	
	/**
	 * This is the list of mutations that can occur in a simulation.  We only allow mutations back and forth for
	 * populations in which at least one agent initially exists.  So this list is created at the start of the simulation
	 * and the strategy types are determined by whether there are more than 0 agents in the initial population.
	 */
	public void makeMutationList() {
		mutationList.clear();//clear the mutation list from previous simulations
		if(_cooperators > 0) {//we fill the list will allowable mutations.
			mutationList.add(Strategy.COOPERATOR);
		}
		if(_defectors > 0) {
			mutationList.add(Strategy.DEFECTOR);
		}
		if(_walkaways > 0) {
			mutationList.add(Strategy.WALKAWAY);
		}
		if(_walkawayD > 0) {
			mutationList.add(Strategy.WALKAWAYD);
		}
		if(_tftm > 0) {
			mutationList.add(Strategy.TFTM);
		}
		if(_tfts > 0) {
			mutationList.add(Strategy.TFTS);
		}
		if(_pavlovm > 0) {
			mutationList.add(Strategy.PAVLOVM);
		}
		if(_pavlovs > 0) {
			mutationList.add(Strategy.PAVLOVS);
		}
		if(_walkawayD > 0) {
			mutationList.add(Strategy.WALKAWAYD);
		}
	}
	
	public void makeAgents() {
		if(!groups) {
			int total =  _cooperators + _defectors + _walkaways + _tftm + _tfts + _pavlovm + _pavlovs + _walkawayD;
			int total2 = gridWidth * gridHeight;
			if(total > total2) {
				System.out.println("Too many agents");
				return;
			}
		}
		for(int i=0;i<_cooperators;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent =  new Agent(this,id,Strategy.COOPERATOR,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_defectors;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.DEFECTOR,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_walkaways;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.WALKAWAY,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_tftm;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.TFTM,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_tfts;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.TFTS,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_pavlovm;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.PAVLOVM,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		for(int i=0;i<_pavlovs;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.PAVLOVS,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		
		for(int i=0;i<_walkawayD;i++) {
			Int2D location;
			if(!groups)
				location = uniqueXY();
			else
				location = locationXY();
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			double resources = (maxResourcesStart-minResourcesStart)*random.nextDouble()+minResourcesStart;
			long id = this.id++;
			Agent agent = new Agent(this,id,Strategy.WALKAWAYD,resources,location.x,location.y,xdir,ydir,true);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent,random.nextInt(gridWidth), random.nextInt(gridHeight));
			agent.colorByStrategy(agent.strategy, this, agent);
		}
		makeMutationList();
	}

	public void start() {
		super.start();
		makeSpace(gridWidth,gridHeight);
		makeAgents();
		if(observer != null)
			observer.initialize(space, spaces);
	}
	
	/*
	 * 
	 * Getters and Setter begin
	 */

	public boolean isGroups() {
		return groups;
	}

	public void setGroups(boolean groups) {
		this.groups = groups;
	}

	public int get_cooperators() {
		return _cooperators;
	}

	public void set_cooperators(int _cooperators) {
		this._cooperators = _cooperators;
	}

	public int get_defectors() {
		return _defectors;
	}

	public void set_defectors(int _defectors) {
		this._defectors = _defectors;
	}

	public int get_walkaways() {
		return _walkaways;
	}

	public void set_walkaways(int _walkaways) {
		this._walkaways = _walkaways;
	}

	public int get_walkawayD() {
		return _walkawayD;
	}

	public void set_walkawayD(int _walkawayD) {
		this._walkawayD = _walkawayD;
	}
	
	public int get_tftm() {
		return _tftm;
	}

	public void set_tftm(int _tftm) {
		this._tftm = _tftm;
	}

	public int get_tfts() {
		return _tfts;
	}

	public void set_tfts(int _tfts) {
		this._tfts = _tfts;
	}

	public int get_pavlovm() {
		return _pavlovm;
	}

	public void set_pavlovm(int _pavlovm) {
		this._pavlovm = _pavlovm;
	}

	public int get_pavlovs() {
		return _pavlovs;
	}

	public void set_pavlovs(int _pavlovs) {
		this._pavlovs = _pavlovs;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}

	public double getAverageAge() {
		return averageAge;
	}

	public void setAverageAge(double averageAge) {
		this.averageAge = averageAge;
	}

	public double getSdAge() {
		return sdAge;
	}

	public void setSdAge(double sdAge) {
		this.sdAge = sdAge;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public double getResoucesToReproduce() {
		return resoucesToReproduce;
	}

	public void setResoucesToReproduce(double resoucesToReproduce) {
		this.resoucesToReproduce = resoucesToReproduce;
	}

	public double getMinResourcesStart() {
		return minResourcesStart;
	}

	public void setMinResourcesStart(double minResourcesStart) {
		this.minResourcesStart = minResourcesStart;
	}

	public double getMaxResourcesStart() {
		return maxResourcesStart;
	}

	public void setMaxResourcesStart(double maxResourcesStart) {
		this.maxResourcesStart = maxResourcesStart;
	}

	public double getCarryingCapacity() {
		return carryingCapacity;
	}

	public void setCarryingCapacity(double carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}

	public int getSearchOpponent() {
		return searchOpponent;
	}

	public void setSearchOpponent(int searchOpponent) {
		this.searchOpponent = searchOpponent;
	}

	public double getCooperate_cooperator() {
		return cooperate_cooperator;
	}

	public void setCooperate_cooperator(double cooperate_cooperator) {
		this.cooperate_cooperator = cooperate_cooperator;
	}

	public double getDefect_defector() {
		return defect_defector;
	}

	public void setDefect_defector(double defect_defector) {
		this.defect_defector = defect_defector;
	}

	public double getCooperate_defector() {
		return cooperate_defector;
	}

	public void setCooperate_defector(double cooperate_defector) {
		this.cooperate_defector = cooperate_defector;
	}

	public double getDefect_cooperator() {
		return defect_cooperator;
	}

	public void setDefect_cooperator(double defect_cooperator) {
		this.defect_cooperator = defect_cooperator;
	}

	public int getReproductionRadius() {
		return reproductionRadius;
	}

	public void setReproductionRadius(int reproductionRadius) {
		this.reproductionRadius = reproductionRadius;
	}

	public boolean isLocalReproduction() {
		return localReproduction;
	}

	public void setLocalReproduction(boolean localReproduction) {
		this.localReproduction = localReproduction;
	}


	public boolean isMutationRange() {
		return mutationRange;
	}

	public void setMutationRange(boolean mutationRange) {
		this.mutationRange = mutationRange;
	}


	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}
	
	/*
	 * 
	 * Getters and Setter end
	 */
}