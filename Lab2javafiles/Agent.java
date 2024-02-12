package freezing_aggregation;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {

	int x, y, xdir, ydir, xdirRandom, ydirRandom;
	boolean frozen = false;
	
	public boolean isFrozen() {
        return frozen;
    }
	
	public void setFrozen(boolean frozen) {
	    this.frozen = frozen;
	}
	
	public Agent(int x, int y, int xdirRandom, int ydirRandom) {
		super();
		this.x = x;
		this.y = y;
		this.xdirRandom = xdirRandom;
		this.ydirRandom = ydirRandom;
	}

	//@Override
	public void step(SimState state) {
		Environment eState = (Environment) state;
		//if (eState.random.nextBoolean()) {
			//aggregate(eState);
		//} else {
		move(eState);
	}
	
	public void placeAgent(Environment state) {
		if (frozen) {
			placeFrozenAgent(state);
			return;
		}
		
		if(state.isOneAgentPerCell()) {
			int tempx = state.sparseSpace.stx(x + xdirRandom);
			int tempy = state.sparseSpace.sty(y + ydirRandom);
			
			Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
			if (b != null) {
				return;
			}
			x = tempx;
			y = tempy;
		} else {
			x = state.sparseSpace.stx(x + xdirRandom);
			y = state.sparseSpace.sty(y + ydirRandom);
		}
		state.sparseSpace.setObjectLocation(this, x, y);
	}

	public void move(Environment state) {
		if (!state.random.nextBoolean(state.getpActive())) {
			// if it doesn't move, return
			return;
		}
		if (!frozen && state.random.nextBoolean(state.getP())) {
			xdirRandom = state.random.nextInt(3)-1;
			ydirRandom = state.random.nextInt(3)-1;
		}
		placeAgent(state);
	}
	public void aggregate(Environment state) {
		Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.TOROIDAL, false);
		xdirRandom = decideX(state, neighbors);
		ydirRandom = decideY(state, neighbors);
		placeAgent(state);
	}
	
	public int decideY(Environment state, Bag neighbors) {
		int posy = 0;
		int negy = 0;
		for (int i = 0; i < neighbors.numObjs; i++) {
			Agent aRandom = (Agent) neighbors.objs[i];
			
			if (aRandom.y > this.y) {
				posy++;
			} else if (aRandom.y < this.y) {
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
			Agent aRandom = (Agent) neighbors.objs[i];
			
			if (aRandom.x > this.x) {
				// right side
				posx++;
			} else if (aRandom.x < this.x) {
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
	
	public void placeFrozenAgent(Environment state) {
        state.sparseSpace.setObjectLocation(this, x, y);
        frozen = true;
    }


}
