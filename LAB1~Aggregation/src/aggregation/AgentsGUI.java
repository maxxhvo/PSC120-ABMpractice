package aggregation;
// 

import java.awt.Color;

import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class AgentsGUI extends GUIStateSweep {

	public AgentsGUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}

	public AgentsGUI(SimStateSweep state) {
		super(state);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AgentsGUI.initialize(Environment.class, null, AgentsGUI.class, 400, 400, Color.WHITE, Color.BLUE, true, Spaces.SPARSE);
	}

}
