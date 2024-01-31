package aggregation;
// sim state (engine) + add stoppable

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
	
	int x, y, xdir, ydir;
	
	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}
	
	public void move(Environment state) {
		if (!state.random.nextBoolean(state.getpActive())) {
			// if it doesn't move, return
			return;
		}
		if (state.random.nextBoolean(state.getP())) {
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
		}
		placeAgent(state);
	}
	
	public void placeAgent(Environment state) {
		if(state.isOneAgentPerCell()) {
			int tempx = state.sparseSpace.stx(x + xdir);
			int tempy = state.sparseSpace.sty(y + ydir);
			
			Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
			if (b != null) {
				return;
			}
			x = tempx;
			y = tempy;
		} else {
			x = state.sparseSpace.stx(x + xdir);
			y = state.sparseSpace.sty(y + ydir);
		}
		state.sparseSpace.setObjectLocation(this, x, y);
	}
	
	public void aggregate(Environment state) {
		Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.TOROIDAL, false);
		xdir = decideX(state, neighbors);
		ydir = decideY(state, neighbors);
		placeAgent(state);
	}
	
	public int decideY(Environment state, Bag neighbors) {
		int posy = 0;
		int negy = 0;
		for (int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent) neighbors.objs[i];
			
			if (a.y > this.y) {
				posy++;
			} else if (a.y < this.y) {
				negy++;
			}
		}
		
		if (posy > negy) {
			return 1;
		} else if (negy > posy) {
			return -1;
		} else {
			return state.random.nextInt(3) - 1;
		}
	}
	
	public int decideX(Environment state, Bag neighbors) {
		int posx = 0;
		int negx = 0;
		for(int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent) neighbors.objs[i];
			
			if (a.x > this.x) {
				// right side
				posx++;
			} else if (a.x < this.x) {
				negx++;
			}
		}
		
		if (posx > negx) {
			return 1;
		} else if (negx > posx) {
			return -1;
		} else {
			return state.random.nextInt(3) - 1;
		}
	}

	@Override
	public void step(SimState state) {
		Environment eState = (Environment) state;
		if (eState.random.nextBoolean()) {
			aggregate(eState);
		} else {
			move(eState);
		}
	}

}
