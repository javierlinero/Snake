/*******************************************************************************
 *
 * Compilation: javac-introcs Snake3.java
 * Execution: java-introcs Snake3
 *
 * Here we create our own version of the retro game "Snake." We use a black
 * grid space to model the playing surface and have a turquoise colored snake
 * that goes around to eat apples. The snake increments in size when it comes
 * in contact with an apple, and then a new randomly positioned apple is created.
 * The game ends if the snake touches a wall or itself. To win our game, you
 * must eat at least 10 apples.
 *
 ******************************************************************************/

public class Snake3 {
    // size of grid, makes it modifiable to bigger grid sizes
    private int size = 10;
    // Two-dimensional array to populate
    private int[][] grid = new int[size][size];
    // node to define the position of the head
    private Node head = new Node(5, 5);
    // node to define the position of the tail
    private Node tail = new Node(5, 4);
    //array that stores the x and y positions of the apple
    private int[] apple = new int[2];
    // width of each square
    private double width = 1.0 / size;
    // status of if the game is still running
    private boolean alive = true;
    // Checks how many apples have been eaten
    private int appleCount = 0;


    private Snake3() {
        // the starting position of the snake is in the middle
        grid[5][5] = 1;
        // we use a circularly linked list
        tail.next = head;

        // goes through every coordinate in the 2d array to create a square at each spot
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                StdDraw.setPenColor(0, 0, 0);
                StdDraw.filledSquare(i * width + width / 2, j * width + width / 2, width / 2);
                StdDraw.setPenColor(255, 255, 255);
                StdDraw.square(i * width + width / 2, j * width + width / 2, width / 2);
            }
        }
    }

    // returns the status of the game
    private boolean isalive() {
        return alive;
    }

    // returns how many apples have been eaten
    private int appleCount() {
        return appleCount;
    }

    // defines the nodes to store x and y coordinates. nodes are defined without a next but has a next
    // variable that can be assigned later
    private static class Node {
        private int x;
        private int y;
        private Node next;

        private Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public boolean occupied(int x, int y) {
        switch (grid[x][y]) {
            case 0:
                return false;
            default:
                return true;
        }
    }

    // move function for the snake, gets the direction input from the keyboard
    private void move(int direction) {
        // direction w = up
        String appleSound = "eatingApple.wav";
        if (direction == 0) {
            // boundary condition to not touch wall
            if (head.y != 9) {
                // new node with coordinate incremented upwards
                head.next = new Node(head.x, head.y + 1);
                // redefine head to be most recent node added to the front
                head = head.next;
                // if the grid point to an apple eat apple and draw new one
                if (grid[head.x][head.y] == 2) {
                    addapple();
                    appleCount++;
                    StdAudio.play(appleSound);
                }
                // if there is a snake in the grid position already or if the head and tail positions match up end status of gamne
                else if (grid[head.x][head.y] == 1 && head.x != tail.x && head.y != tail.y) {
                    alive = false;
                }
                // if there is nothing in the tail position, tail is the next node
                else {
                    grid[tail.x][tail.y] = 0;
                    tail = tail.next;
                }
                // the new head position is populated with a snake so its 1
                grid[head.x][head.y] = 1;
            }
            else {
                alive = false;
                // death if out of bounds (9)
            }
        }
        // A key, direction down
        if (direction == 1) {
            // cant go down if below the 0 line
            if (head.x != 0) {
                // define new node with decremented coordinates
                head.next = new Node(head.x - 1, head.y);
                // set head to the new node
                head = head.next;
                // if apple at that spot, eat and add new
                if (grid[head.x][head.y] == 2) {
                    addapple();
                    appleCount++;
                    StdAudio.play(appleSound);
                }
                // if there's already snake at that coordinate or if head touch tail then die
                else if (grid[head.x][head.y] == 1 && head.x != tail.x && head.y != tail.y) {
                    alive = false;
                }
                else {
                    grid[tail.x][tail.y] = 0;
                    tail = tail.next;
                }
                // put snake into array
                grid[head.x][head.y] = 1;
            }
            else {
                // die from out of bounds
                alive = false;
            }
        }
        // d key, right
        if (direction == 2) {
            // within bounds
            if (head.x != 9) {
                // add to the head
                head.next = new Node(head.x + 1, head.y);
                head = head.next;
                // if apple at that position, eat it so new apple
                if (grid[head.x][head.y] == 2) {
                    addapple();
                    appleCount++;
                    StdAudio.play(appleSound);
                }
                // if touching snake or the tail, kill
                else if (grid[head.x][head.y] == 1 && head.x != tail.x && head.y != tail.y) {
                    alive = false;
                }
                else {
                    // get rid of tail
                    grid[tail.x][tail.y] = 0;
                    tail = tail.next;
                }
                //add snake to grid array
                grid[head.x][head.y] = 1;
            }
            else {
                // out of bounds, die
                alive = false;
            }
        }
        // s key, left
        if (direction == 3) {
            // within bounds
            if (head.y != 0) {
                // add decremented coordinate to front
                head.next = new Node(head.x, head.y - 1);
                head = head.next;
                // if apple at that position eat and add new apple
                if (grid[head.x][head.y] == 2) {
                    addapple();
                    appleCount++;
                    StdAudio.play(appleSound);
                }
                // if already snake there or touch tail, kill
                else if (grid[head.x][head.y] == 1 && head.x != tail.x && head.y != tail.y) {
                    alive = false;
                }
                else {
                    // if tail is gone move it
                    grid[tail.x][tail.y] = 0;
                    tail = tail.next;
                }
                // add snake to array
                grid[head.x][head.y] = 1;
            }
            else {
                // die if out of bounds
                alive = false;
            }
        }
    }

    // function to add apple
    private void addapple() {
        // randomize coordinates within the size of the grid
        int x = StdRandom.uniform(0, size);
        int y = StdRandom.uniform(0, size);
        // if the coordinate is empty, apple has those coordinates
        if (!occupied(x, y)) {
            grid[x][y] = 2;
            apple[0] = x;
            apple[1] = y;
        }
        else {
            // if coordinate full, then recursively call until coordinate is filled
            addapple();
        }
        // at the coordinates of the apple, adjusted to width of the grid, put the apple image
        StdDraw.picture(apple[0] * width + width / 2, apple[1] * width + width / 2,
                        "PixelApple.png", 0.075, 0.075);
        StdDraw.show();
    }

    // making the graphic components
    private void draw() {
        // in black, draw filled squares where the tail was removed
        StdDraw.setPenColor(0, 0, 0);
        StdDraw.filledSquare(tail.x * width + width / 2, tail.y * width + width / 2, width / 2);


        // in white, outline the squares of the grid to make the game easier to navigate
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.square(tail.x * width + width / 2, tail.y * width + width / 2, width / 2);

        // Creates head and green snake
        StdDraw.setPenColor(0, 255, 255);
        // adds squares to the head of the snake
        StdDraw.filledSquare(head.x * width + width / 2, head.y * width + width / 2, width / 2);

        StdDraw.show();
    }

    public static void test() {
        // Random Apple Generation Tests
        // First test checks to see if apple location is occupied
        Snake3 testSnake = new Snake3();
        StdOut.println(testSnake.occupied(5, 5));
        StdOut.println(testSnake.occupied(0, 0));
        // Second test checks if apple populates the array correctly, should return
        // true if apple is generated correctly
        testSnake.addapple();
        int value = testSnake.grid[testSnake.apple[0]][testSnake.apple[1]];
        boolean apple = false;
        if (value == 2) {
            apple = true;
        }
        StdOut.println("Apple in array: " + apple);

        // Tests for Snake collision
        for (int i = 0; i < 10; i++) {
            testSnake.move(1);
        }
        // Keeps moving snake until out of bounds (Wall Collision) and should print false
        StdOut.println(testSnake.isalive());

        // test snake increments in length when eat apple
        int count = testSnake.appleCount();
        if (testSnake.head.x == testSnake.apple[0] && testSnake.head.y == testSnake.apple[1]) {
            count++;
        }
        if (count == testSnake.appleCount()) {
            StdOut.println("Length incrementation true");
        }
        else {
            StdOut.println("Length incrementation false");
        }
    }


    public static void main(String[] args) {
        // test();

        // defines a new object
        Snake3 snake = new Snake3();
        StdDraw.enableDoubleBuffering();
        // variable to keep track of time intervals
        long time = System.currentTimeMillis();
        // variable to store direction according to keyboard input
        int direction = 0;
        // variable to check if direction has changed
        boolean changed = false;
        // one apple to start the game
        snake.addapple();
        // condition for the duration of the game, that rules are followed
        // StdAudio.play("backgroundMusic.wav");
        while (snake.isalive()) {
            // StdOut.println(count);
            // time increment of .25 seconds or 250 milliseconds

            if (System.currentTimeMillis() - time > 250) {
                // no change in direction, snake keeps going in the direction given
                changed = false;
                time = System.currentTimeMillis();
                snake.move(direction);
                snake.draw();
            }
            // Key input Detection
            if (!changed) {
                // W or UP Keys
                if (StdDraw.isKeyPressed(87) || StdDraw.isKeyPressed(38)) {
                    if (direction != 3) {
                        changed = true;
                        direction = 0;
                    }
                }
                // A or LEFT Keys
                else if (StdDraw.isKeyPressed(65) || StdDraw.isKeyPressed(37)) {
                    if (direction != 2) {
                        changed = true;
                        direction = 1;
                    }
                }
                // D or RIGHT Keys
                else if (StdDraw.isKeyPressed(68) || StdDraw.isKeyPressed(39)) {
                    if (direction != 1) {
                        changed = true;
                        direction = 2;
                    }
                }
                // S or DOWN Keys
                else if (StdDraw.isKeyPressed(83) || StdDraw.isKeyPressed(40)) {
                    if (direction != 0) {
                        changed = true;
                        direction = 3;
                    }
                }
            }

        }

        // the while loop will break if the game is lost or the snake reaches it's
        // maximum size
        // if appleCount is at least 10 (Snake's size is 11) win game
        if (snake.appleCount >= 10) {
            StdDraw.picture(0.5, 0.5, "youWin.jpg", 0.75, 0.75);
            StdAudio.play("winMusic.wav");
            StdDraw.show();
        }
        else {
            // if appleCount isn't at least 10 therefore the user has lost
            StdDraw.picture(0.5, 0.5, "lost.jpg", 0.75, 0.75);
            StdAudio.play("deathSound.wav");
            StdDraw.show();
        }
    }
}
