package corso.rubrica;

import java.util.Scanner;

//import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
	    String inputString = scanner.nextLine();
//	    
//	    System.out.println(inputString);
//	    
	    ExecutorCmd executorCmd = new ExecutorCmd();
	    
	    while(!"quit".equals(inputString)) {

//	    	System.out.println(inputString);
	    	
	    	System.out.println(executorCmd.execute(inputString));
//	    	
	    	inputString = scanner.nextLine();
	    }
//	    
	    scanner.close();

	}

}
