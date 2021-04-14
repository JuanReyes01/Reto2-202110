package main;
import java.io.IOException;
import java.text.ParseException;

import controller.Controller;

public class Main {
	
	public static void main(String[] args) throws ParseException, IOException 
	{
		Controller controler = new Controller();
		controler.run();
	}
}
