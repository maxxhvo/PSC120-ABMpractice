package freezing_aggregation;

import freezing_aggregation.Agent;

import sim.util.Bag;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	
	int gridWidth = 50;
	int gridHeight = 50;
	int n = 50;
	boolean oneAgentPerCell = false;
	double pActive = 1;
	double p = 1;
	boolean aggregate = false;
	boolean toroidal = false;
	boolean broadrule = true; // Default rule is the broad rule
	int searchRadius = 1;

	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}

	public void start() {
		super.start();  // calls all the function of its parent
		spaces = Spaces.SPARSE;
		make2DSpace(spaces, gridWidth, gridHeight);
		
		makeAgents();
	}
	
	public void makeAgents() {
		if (this.oneAgentPerCell) {
			int size = gridWidth * gridHeight;
			if (n > size) {
				n = size;
				System.out.println("change the number of agents to " + n);
			}
		}
		int x = gridWidth / 2;
	    int y = gridHeight / 2;
	    int xdir = 0; // Agent does not move in the x direction
	    int ydir = 0; // Agent does not move in the y direction

	    Agent a = new Agent(x, y, xdir, ydir);
	    sparseSpace.setObjectLocation(a, x, y);
	    schedule.scheduleRepeating(a);

	    // If oneAgentPerCell is true, you can skip the loop and return
	    if (this.oneAgentPerCell) {
	        return;
	    }
		
		for (int i = 1; i < n; i++) {
			
			x = random.nextInt(gridWidth);
			y = random.nextInt(gridHeight);
			
			if (toroidal) {
	            // Wrap around toroidal space
	            x = (x + gridWidth) % gridWidth;
	            y = (y + gridHeight) % gridHeight;
	        } else {
	            // Bounded space, keep within the bounds
	            x = Math.max(0, Math.min(x, gridWidth - 1));
	            y = Math.max(0, Math.min(y, gridHeight - 1));
	        }
			
			if (this.oneAgentPerCell) {
				Bag b = sparseSpace.getObjectsAtLocation(x,y);
				while(b != null) {
					x = random.nextInt(gridWidth);
					y = random.nextInt(gridHeight);
					b = sparseSpace.getObjectsAtLocation(x,y);
				}
			}
			int xdirRandom = random.nextInt(3)-1; // -1, 0, 1
			int ydirRandom = random.nextInt(3)-1;
			
			Agent aRandom = new Agent(x, y, xdirRandom, ydirRandom);
			sparseSpace.setObjectLocation(aRandom,  x, y);
			schedule.scheduleRepeating(aRandom);
		}
	}

	// Getters and Setters
	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public boolean isOneAgentPerCell() {
		return oneAgentPerCell;
	}

	public void setOneAgentPerCell(boolean oneAgentPerCell) {
		this.oneAgentPerCell = oneAgentPerCell;
	}

	public double getpActive() {
		return pActive;
	}

	public void setpActive(double pActive) {
		this.pActive = pActive;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
	public boolean isToroidal() {
		return toroidal;
	}
	
	public void setToroidal(boolean toroidal) {
		this.toroidal = toroidal;
	}
	
	public boolean isUseBroadRule() {
        return broadrule;
    }

    public void setUseBroadRule(boolean useBroadRule) {
        this.broadrule = useBroadRule;
    }

    public boolean shouldAgentFreeze(Agent agent, int x, int y) {
        if (broadrule) {
            // Use broad rule
            Bag neighbors = sparseSpace.getMooreNeighbors(x, y, 1, toroidal ? 1 : 0, false);
            for (Object obj : neighbors) {
                if (obj instanceof Agent) {
                    Agent neighbor = (Agent) obj;
                    if (neighbor.isFrozen()) {
                        return true;
                    }
                }
            }
        } else {
            // Use narrow rule
            Object occupant = sparseSpace.getObjectsAtLocation(x, y);
            if (occupant instanceof Agent && ((Agent) occupant).isFrozen()) {
                return true;
            }
        }
        return false;
    }
	
	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}
	
	public int getSearchRadius() {
		return searchRadius;
	}
	
	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}
	
}
