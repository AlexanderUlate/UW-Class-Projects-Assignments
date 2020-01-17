///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title: Play a game called Wumpus Caves
// Course: CS 200, Fall, 2019
//
// Author: Jim Williams
// Editor: Alexander Ulate
// Email: ulate@wisc.edu
// Lecturer's Name: Marc Renault
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
//  Description: Plays a maze style game that the player can travel through and
//  do various different things, such as hunt for the dreaded wumpus or help
//  rescue a child
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * This project is an adventure game inspired by both the
 * classic Hunt the Wumpus game and the Tham Luang cave rescue.
 *     https://en.wikipedia.org/wiki/Hunt_the_Wumpus
 *     https://en.wikipedia.org/wiki/Tham_Luang_cave_rescue
 * 
 * @author Jim Williams
 * @author Alexander Ulate
 */
public class WumpusCaves {

    /**
     * Whether the game is search and rescue or hunt the wumpus.
     */
    enum GameMode {
        hunt, rescue
    };

    /**
     * Update the location parameter based on the direction. The cave is
     * in the shape of a torus meaning it wraps all directions. Movement
     * in any direction (n,s,e,w) is handled by this method.
     *  
     * @param cave The cave being explored.
     * @param location The current row and column that are changed, 
     *    based on the direction, to the new row and column.
     *     
     * @param direction Either "n","s","e" or "w"
     */
    public static void move(char[][] cave, int[] location, String direction) {

        switch (direction) {
            case "n":
                --location[Config.Y_DIRECTION]; // Decrease the index in the y direction to travel north
                if (location[Config.Y_DIRECTION] < Config.DIRECTION_MAX) { // Check if you are at farthest point north
                    location[Config.Y_DIRECTION] = cave.length - 1; // loop back around
                }
                break;
            case "s":
                ++location[Config.Y_DIRECTION]; // Increase the index in the y direction to travel south
                if (location[Config.Y_DIRECTION] >= cave.length) { // Check if at farthest point south
                    location[Config.Y_DIRECTION] = 0; // loop back around
                }
                break;
            case "e":
                ++location[Config.X_DIRECTION];// Increase the index in the x direction to travel east
                if (location[Config.X_DIRECTION] >= cave.length) { // Check if at farthest point east
                    location[Config.X_DIRECTION] = Config.DIRECTION_MAX; // loop back around
                }
                
                break;
            case "w":
                --location[Config.X_DIRECTION]; // Decrease the index in the x direction to travel east
                if (location[Config.X_DIRECTION] < 0) // Check if at farthest point
                    location[Config.X_DIRECTION] = cave[Config.DIRECTION_MAX].length - 1; //Loop back arround
                break;
        }
    }
    
   
    
    /**
     * Takes the player to a random location when picked up by bat
     * 
     * @param rand a random number
     * @param cave The cave that you are exploring
     * @param The location of the player
     */
    public static void batPickUp(Random rand, char[][] cave, int[] player) {
        do {
            player[Config.RESCUE_LOCATION] = rand.nextInt(cave.length);
            player[Config.CHILD_LOCATION] = rand.nextInt(cave[Config.RESCUE_LOCATION].length);
        } while (cave[player[Config.RESCUE_LOCATION]][player[Config.CHILD_LOCATION]] != ' ');
        System.out.println("A huge bat picked you up and dropped you in another room...");
    }
    
    
    /**  This method checks all of the perceptions of the environment
     * 
     *  Algorithm: take the location of the player and output the warnings of their
     *  surroundings (if there are any)
     * 
     * @param The cave you are exploring
     * @param The location of the player
     * @param The game mode you are playing (Hunt/Rescue)
     */
    public static void perceptions(char[][] cave, int[] player, GameMode mode) {
        boolean[] perception = new boolean[Config.NUM_PERCEPTIONS];
        String[] directions = {"n", "s", "e", "w"};

        for (int i = 0; i < directions.length; i++) {
            int[] temp = Arrays.copyOf(player, player.length);
            move(cave, temp, directions[i]);

            switch (cave[temp[0]][temp[1]]) {
                case Config.PIT:
                    perception[Config.PERCEIVE_PIT] = true;
                    break;
                case Config.BAT:
                    perception[1] = true;
                    break;
                case Config.WUMPUS:
                    perception[2] = true;
                    break;
            }
        }

        if (perception[Config.PERCEIVE_BAT]) { // if there is a bat nearby
            System.out.println("you hear a rustling");
        }
        if (perception[Config.PERCEIVE_PIT]) { // if there is a pit nearby
            System.out.println("you feel a draft");
        }
        if (perception[Config.PERCEIVE_CHILD]) { // if there is a child or wumpus nearby
            if (mode == GameMode.rescue) {
                System.out.println("you hear a child snoring");
            } else {
                System.out.println("there's an awful smell");
            }
        }
    }
    

    /**
     * Prints out the result of the action of moving to the current location.
     * 
     * Algorithm:
     *  Takes the status of the rooms around the player and checks it with different
     *  Perceptions of the surroundings. Once the surrounding environment is calculated
     *  the method with return if the player is still alive.
     * 
     * @param rand  A random number generator.
     * @param cave The cave being explored.
     * @param player The current location of the player
     * @param mode Whether rescuing a child or hunting the wumpus
     * @return true if alive, otherwise false.
     */
    public static boolean status(Random rand, char[][] cave, int[] player, GameMode mode) {
        if (cave[player[Config.RESCUE_LOCATION]][player[Config.CHILD_LOCATION]] == Config.BAT) {
            batPickUp(rand, cave, player);

        } else if (cave[player[Config.RESCUE_LOCATION]][player[Config.CHILD_LOCATION]] == Config.PIT) {
            System.out.println("You fell into a pit.");
            return false;
        }

        System.out.println("room " + player[Config.X_COORDINATE] + "" + player[Config.Y_COORDINATE]);

        if (cave[player[Config.RESCUE_LOCATION]][player[Config.CHILD_LOCATION]] == 'w') {
            if (mode == GameMode.rescue) {
                System.out.println("You've found the child safe and happy to see you!");
            } else {
                System.out.println("You've been eaten by the Wumpus.");
            }
            return false;
        } else {
            perceptions(cave, player, mode);
        }
        return true;
    }

    
    
    /**
     * Sets the game mode to hunt or rescue
     * 
     * @param scnr input
     * @return The game mode you wish to play
     */
    public static GameMode huntOrRescue(Scanner scnr) {
        GameMode mode = GameMode.hunt; // Preset to hunt
        while (true) {
            char input = scnr.nextLine().toLowerCase().charAt(0);
            if (input == 'r') {
                mode = GameMode.rescue; // Set to rescue game
                break;
            } else if (input == 'h') { 
                break; // Already set to hunt
            } else { // to account for when an input is not 'h' or 'r'
                System.out.print("Would you like to go on a hunt or rescue a child (h/r): ");
                continue;
            }
        }
        return mode;
    }
    
    /** This method figures out the number of the cave the player will use.
     * 
     * @param scnr
     * @return The number of the cave that you want to explore
     */
    public static int caveNumber(Scanner scnr) {
        int caveNumber = 0;
        while (true) { // Get the cave number, repeat if invalid input
            System.out.print("Please enter the number of the cave to enter: ");
            caveNumber = scnr.nextInt();
            
            if (caveNumber >= 1 && caveNumber <= Config.CAVES.length) { // Make sure it is a valid cave
                break;
            }
        }
        return caveNumber;
    }
    
    /** Give out the result of taking an action with equipment and check if it is being used
     * in the right spot
     * 
     * @param cavernSelect
     * @param mode
     * @param equipmentLocation
     * @return
     */
    public static boolean equipmentResult(char[][] cavernSelect, GameMode mode, int[] equipmentLocation) {
        if (cavernSelect[equipmentLocation[Config.RESCUE_LOCATION]][equipmentLocation[Config.CHILD_LOCATION]] == 'w') { // If the player uses the
            if (mode == GameMode.rescue) {
                System.out.println("Congratulations! The child grabbed the rope and "
                    + "you brought safely out of the cave!");
            } else {
                System.out.println(
                    "Congratulations! You killed the Wumpus and saved the villagers"
                        + " from their nightly terror.");
            }
            return true;
        } else { // if the action does not hit the Wumpus or save the child
            if (mode == GameMode.rescue) {
                System.out.println("The rope got nothing.");
            } else {
                System.out.println("The arrow shot nothing");
            }
            return false;
        }
    }
    
    /**
     * Runs through the different options of using the equipment and returns if it was successful
     * 
     * @param scnr
     * @param mode
     * @param playerLocation
     * @param cavernSelect
     * @return success, whether using the equipment worked or not
     */
    public static boolean useEquipment(Scanner scnr, GameMode mode, int[] playerLocation, char[][] cavernSelect) {
        if (mode == GameMode.rescue) { // Grab the rope for Rescue
            System.out.print("What direction to throw rope (nsew): ");
        } else { // Grab the arrow for Hunt
            System.out.print("What direction to fire arrow (nsew): ");
        }
        // Choose the direction to use the equipment
        String equipmentDirection = scnr.nextLine().trim().toLowerCase();
        boolean success = false; // Initialize to unsuccessful
        if (mode == GameMode.rescue) {
            System.out.println("Rope flies " + equipmentDirection + "");
        } else {
            System.out.println("Arrow flies " + equipmentDirection + "");
        }

        int[] equipmentLocation = Arrays.copyOf(playerLocation, playerLocation.length);
        move(cavernSelect, equipmentLocation, equipmentDirection); // Get the pointer to read whatever 

        success = equipmentResult(cavernSelect, mode, equipmentLocation);

        return success;
}
    
    
    /**
     * Summary: This is where the game begins and runs. It starts off with asking whether the
     * player wants hunt the Wumpus or rescue the child. The player can travel north
     * south, east, and west. At each point, they can chose to use equipment, which
     * is a rope or an arrow depending on if it is the rescue game or hunting game.
     * 
     * Algorithm: The main method gets the user input and calls different methods or does different
     * actions depending on the result of the input. 
     * 
     * @param args  unused
     */
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        Random rand = new Random(Config.SEED);

        System.out.print("Would you like to go on a hunt or rescue a child (h/r): ");
        GameMode mode = huntOrRescue(scnr);

        System.out.println("Thank you for coming to help us at Wumpus Caves.");

        if (mode == GameMode.rescue) { // Print out if the player chooses Rescue
            System.out.println("A child wandered into the cave and has not returned.");
            System.out.println("Please help us find our child!\n");
        } else { // Print out if the player chooses Hunt
            System.out.println("A Wumpus comes out of the cave at night and");
            System.out.println("attacks the villagers. Please hunt it down.\n");
        }

        char[][] cavernSelect = Config.CAVES[0];

        int caveNumber = caveNumber(scnr); // get the cave number
        scnr.nextLine();
        cavernSelect = Config.CAVES[caveNumber - 1];

        System.out.println();
        System.out.print("Use your senses to find your way in the cave. ");
        System.out.println("Beware of the huge bats");
        System.out.println("and the bottomless pits. Good Luck!\n");
        System.out.println("You enter the cave...");

        int[] playerLocation = {0, 0}; // Starting location

        while (status(rand, cavernSelect, playerLocation, mode)) { // Start the main loop of the game
            
            System.out.print("action: ");

            String decision = scnr.nextLine().trim().toLowerCase(); // Get the decision input
            switch (decision) {
                case "n": // Each movement input falls through to the move method
                case "s":
                case "e":
                case "w":
                    move(cavernSelect, playerLocation, decision);
                    break;
                case Config.GRAB_EQUIPMENT: // Grab equipment to use
                    boolean success = useEquipment(scnr, mode, playerLocation, cavernSelect);
                    if (success) {
                        System.out.println("Hopefully, you can now find your way out of the cave....");
                        return;
                    }
                    break;
                default: // If the user enters an invalid input
                    if (mode == GameMode.rescue) {
                        System.out.println("Move (nsew) or get rope (g).");
                    } else {
                        System.out.println("Move (nsew) or get arrow (g).");
                    }
                    break;
            }
        }
        System.out.println("Thanks for playing Wumpus Caves!");
        scnr.close(); // End the program
    }
}
