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
		if(frozen) return;
			move((Environment)state);
	}
	
	public void placeAgent(Environment state) {
		if (frozen) {
			state.sparseSpace.setObjectLocation(this, x, y);
		    frozen = true;
		    return;
	    }

	    if (state.shouldAgentFreeze(this, x, y)) {
	        frozen = true;
	        return;
	    }

	    if (!state.isToroidal()) {
	        // Apply toroidal adjustments only if toroidal is false
	        x = bx(x + xdirRandom, state);
	        y = by(y + ydirRandom, state);
	    } else {
	        // Toroidal adjustments
	        x = state.sparseSpace.stx(x + xdirRandom);
	        y = state.sparseSpace.sty(y + ydirRandom);
	    }

	    if (state.isOneAgentPerCell()) {
	        int tempx = bx(x + xdirRandom, state);
	        int tempy = by(y + ydirRandom, state);

	        Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
	        if (b != null) {
	            return;
	        }
	        x = tempx;
	        y = tempy;
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
	        // Check if the rule is broad or narrow
	        if (state.isUseBroadRule()) {
	            placeAgent(state);
	        } else {
	        	// Narrow rule
	        	int nextX;
	            int nextY;

	            if (!state.isToroidal()) {
	                // Apply toroidal adjustments only if toroidal is false
	                nextX = bx(x + xdirRandom, state);
	                nextY = by(y + ydirRandom, state);
	            } else {
	                // Toroidal adjustments
	                nextX = state.sparseSpace.stx(x + xdirRandom);
	                nextY = state.sparseSpace.sty(y + ydirRandom);
	            }

	            if (state.shouldAgentFreeze(this, nextX, nextY)) {
	                setFrozen(true);  // Freeze the agent
	                return;
	            }

	            // Move only if not frozen
	            placeAgent(state);
	        }
	    }
	}
	//public void aggregate(Environment state) {
	//	Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.TOROIDAL, false);
	//	xdirRandom = decideX(state, neighbors);
	//	ydirRandom = decideY(state, neighbors);
	//	placeAgent(state);
	//}
	
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
	
	public int bx(int x, Environment state) {
	    // Adjust x to stay within bounds for a bounded space
	    return Math.max(0, Math.min(x, state.getGridWidth() - 1));
	}

	public int by(int y, Environment state) {
	    // Adjust y to stay within bounds for a bounded space
	    return Math.max(0, Math.min(y, state.getGridHeight() - 1));
	}
	
	//public void placeFrozenAgent(Environment state) {
    //    state.sparseSpace.setObjectLocation(this, x, y);
    //    frozen = true;
    //}


}
