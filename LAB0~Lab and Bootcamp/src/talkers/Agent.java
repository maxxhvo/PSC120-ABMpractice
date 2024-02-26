package talkers;

public class Agent {
	public String[] months = new String[] {
			"January",
			"Feburary",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"
	};

	public Agent() {
		
	}
	
	public String getMonth (int month) {
		// Prints out the string name for the month corresponding to the int
		String monthString;
		switch(month) {
		case 1: monthString = months[0]; break;
		case 2: monthString = months[1]; break;
		case 3: monthString = months[2]; break;
		case 4: monthString = months[3]; break;
		case 5: monthString = months[4]; break;
		case 6: monthString = months[5]; break;
		case 7: monthString = months[6]; break;
		case 8: monthString = months[7]; break;
		case 9: monthString = months[8]; break;
		case 10: monthString = months[9]; break;
		case 11: monthString = months[10]; break;
		case 12: monthString = months[11]; break;
		default: monthString = "Invalid month"; break;
		}
		
		return monthString;
	}
	
	public void printRandomList(int length) {
		// Prints out a list of randomly generated months
		
		for (int i = 0; i < length; i++) {
			int randomNum = (int) (Math.random() * (12));
			System.out.println((i + 1) + ". " + months[randomNum]);
		}
	}
	
	public void printMonths() {
		// Prints all the months in order
		for (int i = 0; i < months.length; i++) {
			System.out.println((i + 1) + ". " + months[i]);
		}
	}
	
	public void printRangeOfMonths(int start, int end) {
		// make sure it checks start and end
		if ((start < 1) || (start > 12)) {
			System.out.println("Invalid start. Out of Range");
			return;
		} else if ((end < 1) || (end > 12)) {
			System.out.println("Invalid end. Out of Range");
			return;
		} else if (start > end) {
			System.out.println("start is bigger than end. Invalid input");
			return;
		}
		
		start -= 1;
		end -= 1;
		for (int i = start; i <= end; i++) {
			System.out.println((i + 1) + ". " + months[i]);
		}
	}
	
	public void printMonthNumber(String monthName) {
		// Prints the number of the month
		int monthN = 0;
		String m = monthName.toLowerCase();
		
		switch(m) {
		case "january": monthN = 1; break;
		case "february": monthN = 2; break;
		case "march": monthN = 3; break;
		case "april": monthN = 4; break;
		case "may": monthN = 5; break;
		case "june": monthN = 6; break;
		case "july": monthN = 7; break;
		case "august": monthN = 8; break;
		case "september": monthN = 9; break;
		case "october": monthN = 10; break;
		case "november": monthN = 11; break;
		case "december": monthN = 12; break;
		default: monthN = -1; System.out.println("Invalid string input"); break;
		}
		
		System.out.println(monthN);
	}

}
