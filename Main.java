//Author: Michael Montgomery
//Main class simply creates a DNRSystem object and executes the startDNRSystem function.
//Also prints some entry and exit blurbs.

public class Main{
    public static void main(String[] args){
	System.out.println("Welcome to the mini DNR system! Enter 'quit' to exit.");
	
	DNRSystem dnr = new DNRSystem(15, 5, 100);  //First number dictates the maximum number of providers
	                                            //Second number dictates the maximum number of formats per provider
	dnr.startDNRSystem();                       //Third number dictates the maximum size of the registry
	
	System.out.println("Thank you for using the mini DNR system! Goodbye!");
	return;
    }
}
