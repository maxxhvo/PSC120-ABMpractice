package evolutionaryGames;

import java.util.ArrayList;
/**
 * This class implements a simple memory for cooperative games in which agents use strategies that require them 
 * to remember an opponents previously used strategy and/or their own strategy.  A memory is a is a basic packet of 
 * memory, the first element is an opponents id, the second, an opponent's last strategy, and the third is the 
 * agent's strategy used against that opponent.
 * @author jcschankadmin
 *
 */
public class Memory {
	ArrayList<Triple> memory;
	int size = 7; //default memory size

	/**
	 * Creates a memory with a default size of 7.
	 */
	public Memory() {
		memory = new ArrayList<Triple>(size); //create the memory
	}
	/**
	 * Creates the memory and sets is maximum size.
	 * @param size
	 */
	public Memory(int size) {
		this.size = size;
		memory = new ArrayList<Triple>(this.size); //create the memory
	}

	/**
	 * Stores a memory, it takes the agent and saves its id and strategy along with the
	 * strategy played by the agent. The 
	 * @param agent
	 * @param opponentStrategy
	 * @param myStrategy
	 */
	public void storeMemory(Agent agent, Strategy opponentStrategy,Strategy myStrategy) {
		if(memory.size() > 0) {//clear any previous memories of agents
			long id = agent.id;
			for(int i=0;i<memory.size();i++) {//clear older memories
				if(memory.get(i).id == id) {
					memory.remove(i);
				}	
			}
		}
		Triple p = new Triple(agent.id, opponentStrategy, myStrategy); //create memory
		memory.add(p);
		forget(); //If too many items in memory, delete the oldest one
	}

	/**
	 * Gets the last memory of an opponent, if it exists and returns it.
	 * @param agent
	 * @return
	 */
	public Triple getLastMemory(Agent agent) {
		if(memory.size() == 0)
			return null; //no memory of agent
		long id = agent.id;
		for(int i=0;i<memory.size();i++) {
			if(memory.get(i).id == id) {
				return memory.get(i);//found memory of last strategy
			}
		}
		return null; //no memory of agent
	}
	/**
	 * Voids the memory
	 */
	public void forget() {
		if(memory.size() >= size) {
			while(memory.size() >= size) {
				memory.remove(0);//remove oldest memory
			}
		}
	}
	
	public void flush() {
		memory.clear();
	}

}
