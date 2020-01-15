///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title: Sokoban Game
// Course: CS 200, Fall, 2019
//
// Author: Jim Williams and Marc Renault
// Editor: Alexander Ulate
// Email: ulate@wisc.edu
// Lecturer's Name: Marc Renault
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// Description: This is the code for a game called Sokoban. The goal of the game
//              is to get all of the boxes in a "maze" onto goal points using a
//              worker character. In this project, you can load levels from a
//              file, save your moves to a new file, and load your moves from
//              the file(s) you create.
//
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This is the code for a game called Sokoban. The goal of the game
 * is to get all of the boxes in a "maze" onto goal points using a
 * worker character. In this project, you can load levels from a
 * file, save your moves to a new file, and load your moves from
 * the file(s) you create.
 * 
 * @author Alexander Ulate
 *
 */
public class Sokoban {
    final static int COLUMN = 1; // Setting up the constants for position
    final static int ROW = 0; // Makes reading the position easier
    final static char HELP_CHAR = '?';
    final static char LOAD_CHAR = 'l';
    final static char SAVE_CHAR = 's';

    /**
     * Prompts the user for a value by displaying prompt. Note: This method should not add a new
     * line to the output of prompt.
     *
     * After prompting the user, the method will consume an entire line of input while reading an
     * int. If the value read is between min and max (inclusive), that value is returned. Otherwise,
     * "Invalid value." terminated by a new line is output and the user is prompted again.
     *
     * @param sc     The Scanner instance to read from System.in.
     * @param prompt The name of the value for which the user is prompted.
     * @param min    The minimum acceptable int value (inclusive).
     * @param min    The maximum acceptable int value (inclusive).
     * @return Returns the value read from the user.
     */
    public static int promptSokobanLevelNum(Scanner sc, String prompt, int min, int max) {
        boolean error = false;
        int toReturn = 0;
        do {
            if (error == true) {
                System.out.println("Invalid value.");
                error = false;
            }
            System.out.print(prompt);
            if (sc.hasNextInt() == true) {
                toReturn = sc.nextInt();
            } else {
                error = true;
            }
            sc.nextLine();
        } while (error == true || (error = (toReturn < min || toReturn > max)) == true);
        return toReturn;
    }

    /**
     * Prompts the user for an char value by displaying prompt.
     * Note: This method should not be a new line to the output of prompt. 
     *
     * After prompting the user, the method will read an entire line of input and return the first
     * non-whitespace character converted to lower case.
     *
     * @param sc The Scanner instance to read from System.in
     * @param prompt The user prompt.
     * @return Returns the first non-whitespace character (in lower case) read from the user. If 
     *         there are no non-whitespace characters read, the null character is returned.
     */
    public static char promptChar(Scanner sc, String prompt) {
        String in = promptString(sc, prompt).toLowerCase();
        if (in.length() <= 0)
            return '\0';
        else
            return in.charAt(0);
    }

    /**
     * Prompts the user for a string value by displaying prompt.
     * Note: This method should not be a new line to the output of prompt. 
     *
     * After prompting the user, the method will read an entire line of input, remove any leading and 
     * trailing whitespace.
     *
     * @param sc The Scanner instance to read from System.in
     * @param prompt The user prompt.
     * @return Returns the string entered by the user, converted to lower case with leading and trailing
     *         whitespace removed.
     */
    public static String promptString(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /**
     * This method performs the following tests (in order):
     * 1 - levelNum > 0
     * 2 - levels is not shorter than levelNum, that the 2-d array at index level exists and that 
     *     it contains at least 1 row.
     * 3 - goals is not shorter than levelNum, the 1-d array at index level exists and that it
     *     contains an even number of cells.
     * 4 - the number of workers is exactly 1.
     * 5 - the number of boxes is more than 0.
     * 6 - the number of boxes equals the number of goals.
     * 7 - the coordinate of each goal is valid for the given levelNum and does not
     *     correspond to a wall cell.
     * 8 - the goal is not counted twice
     */
    public static int checkLevel(int levelNum, ArrayList<char[][]> levels, ArrayList<int[]> goals) {
        // Checks if levelNum is greater than zero
        if (levelNum < 0)
            return 0;
        // Checks if levels is shorter than levelNum
        if (levels.size() <= levelNum || levels.get(levelNum) == null
            || levels.get(levelNum)[0] == null)
            return -1;
        // Checks that goals is longer than levelNum
        if (goals.size() <= levelNum || goals.get(levelNum) == null
            || goals.get(levelNum).length % 2 == 1)
            return -2;
        // Checks that there is only one worker
        int numWorker = 0;
        int boxCount = 0;
        for (int i = 0; i < levels.get(levelNum).length; i++)
            for (int j = 0; j < levels.get(levelNum)[i].length; j++) {
                if (levels.get(levelNum)[i][j] == Config.BOX_CHAR)
                    boxCount++;
                if (levels.get(levelNum)[i][j] == Config.WORKER_CHAR
                    || levels.get(levelNum)[i][j] == Config.WORK_GOAL_CHAR)
                    numWorker++;
            }
        if (numWorker != 1)
            return -3;
        // Checks that there is at least 1 box
        if (boxCount == 0)
            return -4;
        // Checks that the number of boxes equals the number of goals
        if (boxCount != goals.get(levelNum).length / 2)
            return -5;
        // Checks that the coordinate of each goal is not overriding a wall cell
        for (int i = 0; i < goals.get(levelNum).length - 1; i += 2) {
            int row = goals.get(levelNum)[i];
            int col = goals.get(levelNum)[i + 1];
            if (row < 0 || row >= levels.get(levelNum).length || col < 0
                || col >= levels.get(levelNum)[row].length
                || levels.get(levelNum)[row][col] == Config.WALL_CHAR)
                return -6;
        }

        // Check to see if the goal is counted twice
        for (int i = 0; i < goals.get(levelNum).length - 1; i += 2) {
            for (int j = i + 2; j < goals.get(levelNum).length - 1; j += 2) {
                if (goals.get(levelNum)[i] == goals.get(levelNum)[j]
                    && goals.get(levelNum)[i + 1] == goals.get(levelNum)[j + 1]) {
                    return -7;
                }
            }
        }

        return 1;
    }

    /**
     * This method calculates the distance that the user wants to move
     * 
     * Algorithm: Takes the first number in the moveStr as the direction and the 
     *      second number in moveStr as the magnitude of the moves. The output 
     *      is the movement that the player wants.
     * 
     * @param moveStr 
     * @return An integer array showing the new distance
     * 
     * @magnitude The amount of times that the player moves
     */
    public static int[] calculateDistance(String moveStr) {
        char move = moveStr.length() < 1 ? '\0' : moveStr.charAt(0);
        Scanner magnitudeScnr = new Scanner(moveStr.substring(1));
        int magnitude = magnitudeScnr.hasNextInt() ? magnitudeScnr.nextInt() : 1;
        switch (move) {
            case Config.UP_CHAR:
                magnitudeScnr.close(); // Avoiding errors in the buffer
                return new int[] {-1 * magnitude, 0};
            case Config.DOWN_CHAR:
                magnitudeScnr.close();
                return new int[] {1 * magnitude, 0};
            case Config.LEFT_CHAR:
                magnitudeScnr.close();
                return new int[] {0, -1 * magnitude};
            case Config.RIGHT_CHAR:
                magnitudeScnr.close();
                return new int[] {0, 1 * magnitude};
            default:
                magnitudeScnr.close();
                return new int[] {0, 0};
        }
    }

    /**
     * This method checks if the movement that the user picked is valid. If
     * there is a wall or a box against another box/wall, the method will return
     * that it is not a valid movement
     * 
     * @param board The board that the user is playing on
     * @param position The position of the worker
     * @param distance The distance the worker will travel
     * @param matchingChar Used to check that the worker character is still present
     * @return If the move is not possible, will return something other than 1
     */
    public static int isMovementValid(char[][] board, int[] position, int[] distance,
        char[] matchingChar) {
        if (position == null || position.length != 2 || position[ROW] < 0 || position[COLUMN] < 0
            || position[ROW] >= board.length || position[COLUMN] >= board[position[ROW]].length)
            return -1;
        boolean matchChar = false;
        for (int i = 0; i < matchingChar.length; i++) { // Checks that the worker character is present
            if (board[position[ROW]][position[COLUMN]] == matchingChar[i])
                matchChar = true;
        }
        if (!matchChar)
            return -2; // Worker is not present
        if (distance == null || distance.length != 2)
            return -3; // Distance is not valid
        int[] newPosition = new int[] {position[ROW] + distance[ROW], position[COLUMN] + distance[COLUMN]};
        // Checks if the worker is trying to move through a wall
        if (newPosition[ROW] < 0 || newPosition[ROW] >= board.length || newPosition[COLUMN] < 0
            || newPosition[COLUMN] >= board[newPosition[ROW]].length
            || board[newPosition[ROW]][newPosition[COLUMN]] == Config.WALL_CHAR)
            return -4;
        // Checks if the worker is trying to move through boxes (when not pushing)
        if (board[newPosition[ROW]][newPosition[COLUMN]] == Config.BOX_CHAR
            || board[newPosition[ROW]][newPosition[COLUMN]] == Config.BOX_GOAL_CHAR)
            return -5;
        return 1; // Valid movement
    }

    /**
     * This method replaces the character from the spot that you moved the box
     * from. This makes sure that when the box moves, the spot behind it returns
     * to the character that it once was.
     * 
     * @param board The current board the player is using
     * @param position The position of the character on the board
     * @param original The character you want to check
     * @param replaceTrue The character you are replacing the original character
     *                    with
     * @param replaceFalse The character that replaces it if the new position
     *                      doesn't match the checked character
     */
    public static void replaceCharacter(char[][] board, int[] position, char original,
        char replaceTrue, char replaceFalse) {
        board[position[ROW]][position[COLUMN]] =
            (board[position[ROW]][position[COLUMN]] == original ? replaceTrue : replaceFalse);
    }

    /**
     * This method moves the box in the direction of the workers movement as well
     * as the distance indicated by the player input. Before moving, the method
     * checks if the movement is valid and if so, moves the box to the desired
     * location in front of the worker.
     * 
     * @param board The board the user is playing on
     * @param position The position of the box
     * @param distance The distance the box is being pushed
     * @return Tells the method that called it if the movement is valid or not
     */
    public static int shiftBox(char[][] board, int[] position, int[] distance) {
        int check = isMovementValid(board, position, distance,
            new char[] {Config.BOX_CHAR, Config.BOX_GOAL_CHAR}); // Check the validity of the
                                                                 // movement
        if (check < 1)
            return check; // The movement was not valid
        int[] newPosition = new int[] {position[ROW] + distance[ROW], position[COLUMN] + distance[COLUMN]}; // Change
                                                                                              // the
                                                                                              // location
                                                                                              // of
                                                                                              // the
                                                                                              // box
        replaceCharacter(board, newPosition, Config.GOAL_CHAR, Config.BOX_GOAL_CHAR,
            Config.BOX_CHAR);
        replaceCharacter(board, position, Config.BOX_GOAL_CHAR, Config.GOAL_CHAR,
            Config.EMPTY_CHAR);
        return 1;
    }

    /**
     * This method checks in what direction the player wants to move. Once this
     * is found, the method calls moveWorker to change the position of the worker
     * in the direction found by this method. If the player did not move,
     * the method returns 0.
     * 
     * @param board The board the user is playing on
     * @param position The current position of the worker
     * @param distance The distance the player input
     * @return If the player moved or not
     */
    public static int moveInDirection(char[][] board, int[] position, int[] distance) {
        int index = (distance[ROW] != 0) ? 0 : 1;
        int direction = Integer.signum(distance[index]); // checks if it is negative, positive, or
                                                         // zero distance
        if (direction == 0)
            return 0;
        for (int i = 0; i != distance[index]; i += direction) {
            int[] step = new int[2]; // Sets up the coordinates of the move
            step[index] = direction; // tells the direction of the move
            step[(index + 1) % 2] = 0;
            int moveCheck = 0;
            if ((moveCheck = moveWorker(board, position, step)) < 1)
                return moveCheck;
        }
        return 1;
    }

    /**
     * This method checks to make sure that all of the goals are covered. If
     * this is not the case, the method will return false.
     * 
     * @param board The board that player is used
     * @return The status of the goals
     */
    public static boolean allGoalsCovered(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == Config.GOAL_CHAR || board[i][j] == Config.WORK_GOAL_CHAR) {
                    return false; // if one of the goals isn't covered
                }
            }
        }
        return true;
    }

    /**
     * This method saves the moves of the player to a file for later use
     * 
     * Algorithm: Get an file name from the user and output a file with that name
     * 
     * @param input The name of the file the player wants to create
     * @param moveSet The list of all the moves the player took
     */
    public static void saveMoves(Scanner input, ArrayList<String> moveSet) {
        String fileName = promptString(input, "Enter save moves filename: ");
        try (PrintWriter fileWriter = new PrintWriter(new File(fileName))) {
            for (String savedMoves : moveSet) {
                fileWriter.println(savedMoves);
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error saving move file: " + fileName);
        }
    }

    /**
     * This method moves the worker from one position to another depending on
     * the input from the player. This method checks the movement before executing
     * to make sure that the worker can actually move to the desired location.
     * 
     * @param board The board that the user is playing on
     * @param position The current position of the worker
     * @param distance The distance you want the worker to move
     * @return
     */
    public static int moveWorker(char[][] board, int[] position, int[] distance) {
        int check = isMovementValid(board, position, distance,
            new char[] {Config.WORKER_CHAR, Config.WORK_GOAL_CHAR});
        if (check < 1 && check != -5)
            return check;
        int[] newPosition = new int[] {position[ROW] + distance[ROW], position[COLUMN] + distance[COLUMN]}; // Sets
                                                                                              // array
                                                                                              // with
                                                                                              // current
                                                                                              // position
        if (check == -5 && shiftBox(board, newPosition, distance) < 1)
            return 0;
        replaceCharacter(board, newPosition, Config.GOAL_CHAR, Config.WORK_GOAL_CHAR,
            Config.WORKER_CHAR);
        replaceCharacter(board, position, Config.WORK_GOAL_CHAR, Config.GOAL_CHAR,
            Config.EMPTY_CHAR);
        position[ROW] = newPosition[ROW]; // Sets the new positions
        position[COLUMN] = newPosition[COLUMN];
        return 1;
    }

    /**
     * This method builds the maze board that will be used to play the game. The
     * values of how to build the maze come from a file imported by the user or
     * as the default.
     * 
     * @param nextMaze Has the overall shape of the map from the file
     * @param nextGoals The location of the goals
     * @param parseMaze The file that the code is reading from to build the maze
     */
    public static void createMaze(char[][] nextMaze, int[] nextGoals, Scanner parseMaze) {
        int i = 0, k = 0;
        while (parseMaze.hasNextLine()) { // Read from file until no values remain
            String row = parseMaze.nextLine();
            nextMaze[i] = row.toCharArray();
            for (int j = -1; j < nextMaze[i].length; j++) {
                if (j >= 0) { // builds the maze, sets up the goals, boxes, walls and worker
                    if (nextMaze[i][j] == Config.BOX_GOAL_CHAR
                        || nextMaze[i][j] == Config.WORK_GOAL_CHAR
                        || nextMaze[i][j] == Config.GOAL_CHAR) {
                        nextGoals[k++] = i; // sets the row
                        nextGoals[k++] = j; // sets the column
                        if (nextMaze[i][j] == Config.BOX_GOAL_CHAR) {
                            nextMaze[i][j] = Config.BOX_CHAR;
                        } else if (nextMaze[i][j] == Config.WORK_GOAL_CHAR) {
                            nextMaze[i][j] = Config.WORKER_CHAR;
                        }
                    }
                }
            }
            i++; // increments the row position to build the maze
        }
    }
    
    /**
     * This method loads all of the mazes from a file into the code to play the
     * Sokoban game with different levels. It keeps track of how many goals
     * are in each maze and gets the characters for each maze to print out when
     * accessed by other methods.
     * 
     * Algorithm: Takes the file given and loads all of the mazes inside into 
     *              a database for the code to access for different levels of
     *              the game.
     * 
     * @param fileName The name of the file the user wants to load
     * @param levels The number of levels in the maze
     * @param goals The number of goals in the maze
     * @throws FileNotFoundException The error if the file doesn't exist
     */
    public static void loadLevels(String fileName, ArrayList<char[][]> levels,
        ArrayList<int[]> goals) throws FileNotFoundException {
        levels.clear(); // resets the number of levels
        goals.clear(); // resets the number of goals
        try (Scanner fileIn = new Scanner(new File(fileName))) {
            String nextLevel = "";
            int numLines = 0;
            int numGoals = 0;
            while (fileIn.hasNextLine()) { // loop until there are no more values in the file
                String nextLine = fileIn.nextLine();
                if (nextLine.contains(Config.WALL_CHAR + "")) {
                    for (char goalCheck : nextLine.toCharArray()) { // Gets the amount of goals in the maze
                        if (goalCheck == Config.BOX_GOAL_CHAR || goalCheck == Config.WORK_GOAL_CHAR
                            || goalCheck == Config.GOAL_CHAR) {
                            numGoals++; 
                        }
                    }
                    nextLevel += nextLine + "\n";
                    numLines++; // Increases the amount of lines the map will print in the nextMaze array
                } else {
                    if (nextLevel.isEmpty() == false) {
                        char[][] nextMaze = new char[numLines][];
                        int[] nextGoals = new int[numGoals * 2];
                        Scanner parseMaze = new Scanner(nextLevel);
                        createMaze(nextMaze, nextGoals, parseMaze); // Creates the maze
                        levels.add(nextMaze); // Adds the maze to the levels array
                        goals.add(nextGoals); // Adds the goals corresponding to the maze
                        nextLevel = "";
                        numLines = 0;
                        numGoals = 0;
                        parseMaze.close(); // Avoiding errors in the buffer
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            throw fnfe; // Throws exception back to main method
        }

    }

    /**
     * This method prints out the help menu for Sokoban
     */
    public static void helpPrint() {
        System.out.println("Sokoban Help:");
        System.out.println("----------------------------------------------------------------");
        System.out.println("You need to push all the boxes so that they cover all the goals.");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Board Legend:");
        System.out.println("\tEmpty floor: " + Config.EMPTY_CHAR);
        System.out.println("\tWall: " + Config.WALL_CHAR);
        System.out.println("\tGoal: " + Config.GOAL_CHAR);
        System.out.println("\tBox: " + Config.BOX_CHAR);
        System.out.println("\tBox on a goal: " + Config.BOX_GOAL_CHAR);
        System.out.println("\tWorker: " + Config.WORKER_CHAR);
        System.out.println("\tWorker on a goal: " + Config.WORK_GOAL_CHAR);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Moving the worker:");
        System.out.println("\tMove up: " + Config.UP_CHAR);
        System.out.println("\tMove down: " + Config.DOWN_CHAR);
        System.out.println("\tMove left: " + Config.LEFT_CHAR);
        System.out.println("\tMove right: " + Config.RIGHT_CHAR);
        System.out.println("\tMultiple moves: direction followed by a magnitude.");
        System.out.println("\t\tExample: " + Config.UP_CHAR + "8 moves up 8 spots.");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Other commands:");
        System.out.println("\tQuit: " + Config.QUIT_CHAR);
        System.out.println("\tLoad moves: l");
        System.out.println("\tSave moves: s");
        System.out.println("\tHelp menu: ?");
    }
    

    /**
     * This method takes the given board and prints it out to the console
     * 
     * Algorithm: takes the character array and outputs the character image to the screen
     *      Different character arrays will print out different level boards
     * 
     * @param board
     */
    public static void printBoardMap(char[][] board) {
        for (int i = -1; i <= board[0].length; i++) {
            System.out.print(Config.WALL_CHAR);
        }
        for (int i = 0; i < board.length; i++) {
            System.out.print("\n" + Config.WALL_CHAR);
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.print(Config.WALL_CHAR);
        }
        System.out.println();
        for (int i = -1; i <= board[board.length - 1].length; i++) {
            System.out.print(Config.WALL_CHAR);
        }
        System.out.println();
    }
    

    /**
     * This method prints out the error message for if the file fails to load
     * 
     * Algorithm: Takes the level test and checks which error message to print out
     * 
     * @param levelTest
     * @param level
     */
    public static void printFileLoadError(int levelTest, int level) {
        System.out.println("Error loading level!");
        String errorMessage = "";
        switch (levelTest) {
            case 0:
                errorMessage = "Level " + level + " must be 0 or greater!";
                break;
            case -1:
                errorMessage = "Error with Config.LEVELS";
                break;
            case -2:
                errorMessage = "Error with Config.GOALS";
                break;
            case -3:
                errorMessage = "Level " + level + " has 0 or more than 1 worker(s).";
                break;
            case -4:
                errorMessage = "Level " + level + " does not contain any boxes.";
                break;
            case -5:
                errorMessage =
                    "Level " + level + " does not have the same " + "number of boxes as goals.";
                break;
            case -6:
                errorMessage = "Level " + level + " has a goal location that is a wall.";
                break;
            case -7:
                errorMessage = "Level " + level + " has duplicate goals.";
                break;
            default:
                errorMessage = "Unknown error.";
                break;
        }
        errorMessage += "\nMaze:\n";
        for (char[] arr : Config.LEVELS.get(level)) {
            errorMessage += Arrays.toString(arr) + "\n";
        }
        errorMessage += "\nGoals:\n" + Arrays.toString(Config.GOALS.get(level));
        System.out.println(errorMessage);
    }
    

    /**
     * This method sets up the goals for the level that the player is loading in
     * 
     * @param board The board image that the player is setting up
     * @param level The current level that the player is setting up
     */
    public static void setUpGoalCharacters(char[][] board, int level) {
        for (int i = 0; i < Config.GOALS.get(level).length; i += 2) {
            int rowGoal = Config.GOALS.get(level)[i];
            int columnGoal = Config.GOALS.get(level)[i + 1];
            if (board[rowGoal][columnGoal] == Config.BOX_CHAR) { // if the setup box is already on a
                                                                 // goal
                board[rowGoal][columnGoal] = Config.BOX_GOAL_CHAR;
            } else if (board[rowGoal][columnGoal] == Config.WORKER_CHAR) { // if the worker is
                                                                           // already on a goal
                                                                           // during the setup
                board[rowGoal][columnGoal] = Config.WORK_GOAL_CHAR;
            } else {
                board[rowGoal][columnGoal] = Config.GOAL_CHAR; // Setup the regular goal characters
            }
        }
    }
    
    /**
     * This method sets up the worker position pointer on the level board.
     * 
     * Algorithm: Checks every position on the board to see if it contains the
     * worker character. If so, it sets the current position to that of the
     * worker character.
     * 
     * @param board The board that the player is using
     * @param position The position of the worker 
     * @param level The level that the user wants to use
     */
    public static void setUpWorker(char[][] board, int[] position, int level) {
        for (int rowNum = 0; rowNum < Config.LEVELS.get(level).length; rowNum++) {
            board[rowNum] = new char[Config.LEVELS.get(level)[rowNum].length];
            for (int columnNum = 0; columnNum < Config.LEVELS.get(level)[rowNum].length; columnNum++) {
                board[rowNum][columnNum] = Config.LEVELS.get(level)[rowNum][columnNum];
                if (board[rowNum][columnNum] == Config.WORKER_CHAR) {
                    position[ROW] = rowNum; // Sets the position to match where the worker char was found
                    position[COLUMN] = columnNum;
                }
            }
        }
    }
    
    /**
     * This method loads the moves from a file to use in the current game and 
     * executes the loaded moves.
     * 
     * Algorithm: Takes the file from the user input, loads, and executes the
     * moves, returning the number of moves this method executed. 
     * 
     * @param board The board being played on
     * @param position The position of the worker
     * @param move Reading the move from the file
     * @param moves The list of moves that will be executed
     * @param sc The Scanner reading the moves from the file
     * @param moveCount The counter for the number of moves
     * @return The number of moves from the file
     */
    public static int loadMoves(char[][] board, int[] position, String move, ArrayList<String> moves, Scanner sc, int moveCount) {
        String fileName = promptString(sc, "Enter file containing moves: ");
        try (Scanner fileIn = new Scanner(new File(fileName))) {
            while (fileIn.hasNextLine()) {
                move = fileIn.nextLine();
                int[] distance = calculateDistance(move);
                if (distance[ROW] != distance[COLUMN]) {
                    moveInDirection(board, position, distance);
                    moves.add(move);
                    moveCount += Math.abs(distance[ROW]) + Math.abs(distance[COLUMN]);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error loading move file: " + fileName);
        }
        return moveCount;
    }
    
    /**
     * This method executes the Sokoban game and takes the user inputs to play
     * different levels. This method can call methods to save and load moves
     * from the player and import level files to allow for multiple levels
     * and user created levels. The game itself consists of a worker that can
     * move around boxes in certain number of moves. The goal is to get all of
     * the boxes on the goal characters in as few moves as possible.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Sokoban!"); // Set up the title
        String levelFile =
            promptString(sc, "Enter file contain level list (blank for default levels): ");
        if (levelFile.isEmpty() == false) {
            try { // Testing the loading file
                loadLevels(levelFile, Config.LEVELS, Config.GOALS);
            } catch (Exception e) { // Catch if the file is unreadable
                System.out.print("Error while reading file: " + levelFile + "\n");
                e.printStackTrace();
                return;
            }
        }
        char playAgain = 'n'; // Preset the playAgain check as 'n'
        do {
            int level = promptSokobanLevelNum(sc,
                "Choose a level between 0 and " + (Config.LEVELS.size() - 1) + ": ", 0,
                Config.LEVELS.size() - 1);
            int levelTest = checkLevel(level, Config.LEVELS, Config.GOALS); // Checking to make sure
                                                                            // the level is valid
            if (levelTest < 1) {
                printFileLoadError(levelTest, level);

            } else {
                int moveCount = 0;
                ArrayList<String> movesList = new ArrayList<String>(); // The array of moves
                int[] position = {0, 0}; // Starting position for the worker pointer
                char[][] board = new char[Config.LEVELS.get(level).length][]; // The board to play on
                setUpWorker(board, position, level);
                setUpGoalCharacters(board, level);

                System.out.println("Sokoban Level " + level);
                boolean printBoard = true;
                boolean won = false;

                // Main portion of the game
                while (!(won = allGoalsCovered(board))) {
                    if (printBoard) { // Check to see if the board should print
                        printBoardMap(board);
                    }
                    printBoard = true;
                    String move = promptString(sc, "(? for help) : ");
                    if (move.length() < 1)
                        continue;
                    else if (move.charAt(0) == Config.QUIT_CHAR) // Quit if user enters 'q'
                        break;
                    else if (move.charAt(0) == '?') {
                        helpPrint(); // Prints out the help menu
                        printBoard = false; // Keeps it from printing out the board again
                    } else if (move.toLowerCase().charAt(0) == 'l') {
                        // If the player wants to load their moves
                        moveCount += loadMoves(board, position, move, movesList, sc, moveCount);
                    } else if (move.toLowerCase().charAt(0) == 's') {
                        // If the player wants to save their moves
                        saveMoves(sc, movesList);
                    } else {
                        int[] distance = calculateDistance(move);
                        if (distance[ROW] != distance[COLUMN]) {
                            int ret = moveInDirection(board, position, distance);
                            if (ret > 0) { // This is the move counter
                                movesList.add(move);
                                moveCount += Math.abs(distance[ROW]) + Math.abs(distance[COLUMN]);
                            }
                        }
                    }
                }
                if (won) {
                    System.out.println("Congratulations! You won in " + moveCount + " moves!");
                    printBoardMap(board);
                    char saveWin = promptChar(sc, "Save your winning strategy? (y/n)");
                    if (saveWin == 'y') {
                        saveMoves(sc, movesList);
                    }
                }
            }
            do { // Makes sure the input is only 'y' or 'n'
                playAgain = promptChar(sc, "Play again? (y/n) ");
                if (playAgain == 'y' || playAgain == 'n') {
                    break;
                }
            } while (true);
        } while (playAgain == 'y');
        System.out.println("Thanks for playing!");
        sc.close(); // Avoid errors in the buffer

    }
}
