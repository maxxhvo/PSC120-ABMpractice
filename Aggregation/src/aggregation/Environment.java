package aggregation;
// sim state sweep

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
	
	public void makeAgents() {
		if (this.oneAgentPerCell) {
			int size = gridWidth * gridHeight;
			if (n > size) {
				n = size;
				System.out.println("change the number of agents to " + n);
			}
		}
		
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			
			if (this.oneAgentPerCell) {
				Bag b = sparseSpace.getObjectsAtLocation(x,y);
				while(b != null) {
					x = random.nextInt(gridWidth);
					y = random.nextInt(gridHeight);
					b = sparseSpace.getObjectsAtLocation(x,y);
				}
			}
			int xdir = random.nextInt(3)-1; // -1, 0, 1
			int ydir = random.nextInt(3)-1;
			
			Agent a = new Agent(x, y, xdir, ydir);
			sparseSpace.setObjectLocation(a,  x, y);
			schedule.scheduleRepeating(a);
		}
	}

	public void start() {
		super.start();  // calls all the function of its parent
		spaces = Spaces.SPARSE;
		make2DSpace(spaces, gridWidth, gridHeight);
		
		makeAgents();
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
