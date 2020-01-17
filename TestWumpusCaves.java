import java.util.Random;
import java.util.Scanner;

public class TestWumpusCaves {

	/**
	 * Uncomment testing methods to have them run.
	 * @param args unused
	 */
	public static void main(String[] args) {
		testMove();
		testBatPickUp();
		testCaveNumber();
		
	}
	
	
	/**
	 *  This method tests the batPickUp method to make sure the bat moved the player
	 */
	public static void testBatPickUp() {
        boolean error = false;
        Random rand = new Random(Config.SEED);
        { //test 1
            int[] location = {1, 1};
            char[][] cave = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
            
            WumpusCaves.batPickUp(rand, cave, location);
            if ((location[0] == 1 && location[1] == 1)) { // make sure the bat moved the player
                System.out.println("testBatPickUp 1: location expected not 1, 1; actual " + location[0] + "," + location[1]);
                error = true;
            }
        }  
        if (error) {
            System.out.println("Error in testBatPickUp.");
        } else {
            System.out.println("All tests in testBatPickUp passed.");
        }
    }
	
	public static void testCaveNumber() {
        boolean error = false;
       Scanner scnr = new Scanner("1"); 
        { //test 1
            int cave = WumpusCaves.caveNumber(scnr);
            if (!(cave == 1)) { // 
                System.out.println("testCaveNumber 1: number expected 1; actual: "+ cave);
                error = true;
            }
        }  
        
        { //test 2
            scnr = new Scanner("2");
            int cave = WumpusCaves.caveNumber(scnr);
            if (!(cave == 2)) { // 
                
                System.out.println("testCaveNumber 2: number expected 2; actual: "+ cave);
                error = true;
            }
        }  
        if (error) {
            System.out.println("");
            System.out.println("Error in testCaveNumber.");
        } else {
            System.out.println("");
            System.out.println("All tests in testCaveNumber passed.");
        }
    }
	
	/**
	 * This method tests the move method.
	 * 1. Check whether the move north from 1,1 works correctly.
	 * 2. Check whether the move west from 1,1 works correctly.
	 *
	 * 3.  Verify other moves such as
	 *     over an edge of the array which should wrap. 
	 */
	private static void testMove() {
		boolean error = false;

        {   //test 1
            int[] location = {1, 1};
            char[][] cave = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};

            WumpusCaves.move(cave, location, "n");
            if ( !(location[0] == 0 && location[1] == 1)) {
                System.out.println("testMove 1: location expected 0,1; actual " + location[0] + "," + location[1]);
                error = true;
            }
        }
		
        {   //test 2
            int[] location = {1, 1};
            char[][] cave = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};

            WumpusCaves.move(cave, location, "w");
            if (!(location[0] == 1 && location[1] == 0)) {
                System.out.println(
                    "testMove 2: location expected 1,0; actual " + location[0] + "," + location[1]);
                error = true;
            }
        }
        
        {   //test 3 edge loop
            int[] location = {0, 0};
            char[][] cave = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};

            WumpusCaves.move(cave, location, "n");
            if ( !(location[0] == 2 && location[1] == 0)) {
                System.out.println(
                    "testMove 3: location expected 2,0; actual " + location[0] + "," + location[1]);
                error = true;
            }
        }
        
        {   //test 4 Edge loop
            int[] location = {0, 0};
            char[][] cave = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};

            WumpusCaves.move(cave, location, "w");
            if ( !(location[0] == 0 && location[1] == 2)) {
                System.out.println(
                    "testMove 4: location expected 0,2; actual " + location[0] + "," + location[1]);
                error = true;
            }
        }
		
		if ( error) {
			System.out.println("Error in testMove.");
		} else {
			System.out.println("All tests in testMove passed.");
		}
	}
}
