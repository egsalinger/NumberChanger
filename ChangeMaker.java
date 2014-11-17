import java.util.HashSet;
import java.util.Iterator;
public class ChangeMaker {

	public static void main(String[] args) {
		
		// we are finding the number of ways to make change from n cents.
		int n = 25;
		new ChangeMaker (n);
	}


	public ChangeMaker (int baseAmount)
	{
		HashSet<Change> attempts;
		attempts = new HashSet <Change>();	
		attempts.add(new Change (0,0,0,0,0));
		System.out.println ("Number of ways found to make change for " + baseAmount +" centss: "+ calculateChange (baseAmount, attempts));
	}

	/**
	 * This is where we do all our work.
	 * The solution is iterative, rather than recursive, to cut down on memory usage.
	 * @param goal the $amount to make (in cents: eg $1.00 == 100)
	 * @param toProcess A set of Change Objects less than our goal. Note that initially, this contains one empty change object
	 * @return The number of unique ways to produce goal pennies given dollars, quarters, dimes, nickels, and pennies.
	 */
	public int calculateChange (int goal, HashSet <Change> toProcess)
	{
		HashSet <Change> completed = new HashSet<Change>();
		// While we have some work to do:
		while (toProcess.size() != 0)
		{
			// initialize iterator in this loop.
			Iterator <Change> itr = toProcess.iterator();
			// Having two sets lets us use an iterator (can't modify the first set from in here)
			// it also lets us run things in 'steps'.
			HashSet <Change> nextRound = new HashSet <Change> ();
			// go through the iterator:
			while (itr.hasNext())
			{
				// This is for some degree of clarity. We could save ourselves a little memory
				// and use direct references to methods, but that would make the code less readable.
				Change cur = itr.next();

				int pennies = cur.getPennies();
				int nickels = cur.getNickels();
				int dimes = cur.getDimes();
				int quarters = cur.getQuarters();
				int dollars = cur.getDollars();
				
				// This is how far we are from our goal.
				int diff = goal - cur.getValue();
				
				// From here, the task is simple. We make a new change object based on the current
				// change object, using one (or more in the case of pennies) coins.
				//
				// The if statements are nested so we don't check a number of conditions that must
				// be false (eg: if diff < 5, diff is also < 10)
				//
				// If we get to our 'goal' value, then we add the number to the completed list.
				// otherwise, we add it to the list to compute next.
				//
				// One other interesting thing about this set of statements is that it only
				// permits creation of change combinations with coins of the lowest unused
				// denomination (eg, you can't add quarters if you have a dime, you can't add a
				// dime if you have a nickel). This is done (again) to keep the number of checks
				// down. The HashSet should catch identical entries.
				if (diff > 0)
				{
					Change toAdd = new Change (pennies+diff, nickels, dimes, quarters, dollars);
					// this should always be true
					if (toAdd.getValue() == goal)
						completed.add(toAdd);
					else
						nextRound.add(toAdd);

					if (diff >= 5 && cur.getPennies() == 0)
					{

						toAdd = new Change (pennies, nickels+1, dimes, quarters, dollars);
						if (toAdd.getValue() == goal)
							completed.add(toAdd);
						else
							nextRound.add(toAdd);

						if (diff >= 10 && cur.getNickels() == 0)
						{

							toAdd = new Change (pennies, nickels, dimes+1, quarters, dollars);
							if (toAdd.getValue() == goal)
								completed.add(toAdd);
							else
								nextRound.add(toAdd);

							if (diff >= 25 && cur.getDimes() == 0)
							{
								toAdd = new Change (pennies, nickels, dimes, quarters+1, dollars);
								if (toAdd.getValue() == goal)
									completed.add(toAdd);
								else
									nextRound.add(toAdd);

								if (diff >= 100 && cur.getQuarters() == 0)
								{
									toAdd = new Change (pennies, nickels, dimes, quarters, dollars+1);
									if (toAdd.getValue() == goal)
										completed.add(toAdd);
									else
										nextRound.add(toAdd);
								}
							}
						}
					}
				}

			}
			toProcess = nextRound;
		}
		return completed.size();
	}
	
	/**
	 * Utility Class for Change. Holds coinage, creates hashcode values for map. I could have
	 * used an ArrayList or something for the keyset, but this seemed cleaner.
	 * @author Eric Salinger
	 *
	 */
	public class Change {
		private int pennies, nickels, dimes, quarters, dollars;
		public Change (int p, int n, int di, int q, int d)
		{
			pennies = p;
			nickels = n;
			dimes = di;
			quarters = q;
			dollars = d;
		}
		public int getValue()
		{
			int toReturn =  100 * dollars;
			toReturn += 5 * nickels;
			toReturn += 10 * dimes;
			toReturn += 25 * quarters;
			toReturn += 1 * pennies;
			return toReturn;
		}

		/**
		 * Assuming you believe the 
		 * @param other The Change to compare it to. 
		 * @return True if other is equal to this object (has the same number/type of coins), false otherwise.
		 */
		public boolean equals (Change other)
		{
			if (other == null)
			{
				return false;
			}
			return this.hashCode() == other.hashCode();
		}

		public int getPennies() {
			return pennies;
		}

		public int getNickels() {
			return nickels;
		}

		public int getDimes() {
			return dimes;
		}

		public int getQuarters() {
			return quarters;
		}

		public int getDollars() {
			return dollars;
		}

		@Override
		public String toString()
		{
			return "p" +pennies + "n" +nickels + "d" + dimes + "q" + quarters + "$" + dollars + "\n"; 
		}
		@Override
		public int hashCode()
		{
			return toString().hashCode();

		}
	}
}


