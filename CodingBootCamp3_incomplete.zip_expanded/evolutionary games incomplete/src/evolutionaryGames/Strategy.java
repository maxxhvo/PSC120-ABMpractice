package evolutionaryGames;


public enum Strategy {
	COOPERATOR (1.0), //not we also asign a double to each strategy
	DEFECTOR (2.0),
	WALKAWAY (3.0),
	WALKAWAYD (4.0),
	TFTM(5.0),
	TFTS(6.0),
	PAVLOVM(7.0),
	PAVLOVS(8.0);

	/**
	 * This defines the strategy id
	 */
	private final double id;
	Strategy(double id){
		this.id =id;
	}
	
	/**
	 * This method returns the id.
	 * @return
	 */
	public double id() {
		return id;
	}
}

