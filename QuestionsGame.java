
import java.util.*;
import java.io.*;

//This class sets up and allows a 20 questions like game to be played
public class QuestionsGame {
	private Scanner console;
	private QuestionNode root;
	
	//The constructor, sets the basic elements up for the game
	public QuestionsGame() {
		this.root = new QuestionNode("computer");
		console = new Scanner(System.in);
	}
	
	//Given a scanner reading from an input, this method creates a question game based
	//off of questions and answers taken from the input
	public void read(Scanner input) {
		this.root = readFile(input);
	}
	
	//Given a scanner reading from an input, this method returns a question game
	//using the data from the input read in a predetermined order
	private QuestionNode readFile(Scanner input) {
		String line = input.nextLine();
		
		if(line.equals("A:")) {
			return new QuestionNode(input.nextLine());
		}else {
			return new QuestionNode(input.nextLine(), readFile(input), readFile(input));
		}
	}
	
	//Given an output file to write to, this method creates a file of the current "game board"
	//to be saved and played later
	public void write(PrintStream output) {
		writeFile(output, this.root);
	}
	
	//Given an output file to write to and a single question node, this method
	//writes all the questions and answers to the file in a predetermined format and order
	private void writeFile(PrintStream output, QuestionNode node) {
		if(node.left == null && node.right == null) {
			output.println("A:");
			output.println(node.data);
		}else {
			output.println("Q:");
			output.println(node.data);
			writeFile(output, node.left);
			writeFile(output, node.right);
		}
	}
	
	//This method is responsible for going through one complete game.
	//It takes care of asking all the questions to finally guess an answer
	//and adds more questions and answers when the program loses the game
	public void askQuestions() {
		QuestionNode traverse = this.root;
		QuestionNode previous = this.root;
		
		while(traverse.left != null && traverse.right != null) {
			previous = traverse;
			if(yesTo(traverse.data)) {
				traverse = traverse.left;
			}else {
				traverse = traverse.right;
			}
		}
		
		if(yesTo("Would your object happen to be " + traverse.data + "?")) {
			System.out.println("Great, I got it right!");
		}else {
			System.out.print("What is the name of your object? ");
			QuestionNode object = new QuestionNode(this.console.nextLine());
			System.out.println("Please give me a yes/no question that");
			System.out.println("distinguishes between your object");
			System.out.print("and mine--> ");
			QuestionNode question = new QuestionNode(this.console.nextLine());
			
			if(previous != traverse && previous.left == traverse) {
				previous.left = question;
			}else if(previous != traverse && previous.right == traverse) {
				previous.right = question;
			}else {
				this.root = question;
			}
			
			if(yesTo("And what is the answer for your object?")) {
				question.left = object;
				question.right = traverse;
			}else {
				question.right = object;
				question.left = traverse;
			}
		}
	}
	
	// post: asks the user a question, forcing an answer of "y" or "n";
    //       returns true if the answer was yes, returns false otherwise
    public boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }
	
	
	
	
	
	
	//This class creates a node specifically designed to play the questions game
	private static class QuestionNode {
		public final String data;
		public QuestionNode left;
		public QuestionNode right;
		
		//Given a string of data, this constructor builds a leaf node
		public QuestionNode(String data) {
			this(data, null, null);
		}
		
		//Given a string of data, and two question nodes, this constructor builds
		//a node with two children represented by the two given nodes
		public QuestionNode(String data, QuestionNode left, QuestionNode right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}
}
