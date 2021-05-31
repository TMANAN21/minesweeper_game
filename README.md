Contributors: I had been provided with several classes that implement part of the game already: a TitleScreen class that provides the initial splash screen; the less interesting parts of the Game and Cell classes, which implement most of the game graphics and minesweeper-specific functionality; a few other "helper" classes; and a Minesweeper class that contains the main() function. I added code to finish writing missing methods and missing classes. Kevin Walsh, as stated in the code, had written most of the code and had missing information for a class project. 

I wrote the code to paint onto a virtual canvas, which allows the program to be displayed graphically. Furthermore, in the Game object and many Cell objects, I made the grid of the minesweeper, then I made different tile colors depending on if the user cliks or not, then I installed mines and the numbers under each of the tiles. and it is by looking at the state of these objects (that is, the value of their member variables) that your code will know where to draw what. 
Lastly, I also edited the Timer, which starts when the game starts, Helpbox and StatusBox, which counts the number of mines deployed and number of cells remaining to be deployed. 

Information:

Minesweeper.java - This contains the main() function. It initializes the random number generator, creates and shows the title screen using the TitleScreen class, then creates and shows the game board using the Game class. The GUI library takes over from there, calling the functions whenever the user clicks the screen and periodically redrawing the contents of the game window as the code makes changes to the drawings.

StatusBox.java, HelpBox.java, and Timer.java - Code for managing and drawing parts of the user interface. As stated in the name they are the features of the game that starts once the user starts the game. More explanation are provided in th file. 

TitleScreen.java - A class that manages the title "splash" screen. Like Game.java, this file involves a class that extends GUI.Window. This class provides the interface for the beginning of the screen asking user if they want easy, medium or hard game. 

Game.java and Cell.java - The Cell.java creates the grid and the interface of the game. Then, the game.java deploy mines, puts numbers under the tiles etc. More information can be found within the files. 

