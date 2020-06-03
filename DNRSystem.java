//Author: Michael Montgomery
//DNRSystem class initiates HashMap storage for domain objects and handles all modifications to data.

//import section
import java.lang.Boolean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DNRSystem{
    int maxNumProviders;           //maximum number of providers loaded in from provider.txt.  Can be less if not enough providers in file.
    int maxNumFormats;             //maximum number of formats for each provider. Can be less if not provided.
    int maxNumDomains;             //maximum number of domains the registry can hold.  Change value in Main.java to extend or contract capacity.
    HashMap<String, Domain> map;   //map to hold registered domains.
    String[][] verifiedProviders;  //verified providers list and associated formats for registering domains.
    
    public DNRSystem(int numProviders, int numFormats, int numDomains){
	maxNumProviders = numProviders;
	maxNumFormats = numFormats;
	maxNumDomains = numDomains;
    }

    //startDNRSystem handles program control and calls associated functions.
    //Checks for valid commands and correct amount of inputs, spits useful help message otherwise.
    public void startDNRSystem(){
	map = new HashMap<String, Domain>(maxNumDomains);
	verifiedProviders  = new String[maxNumProviders][maxNumFormats+1];
	
	try{
	    File f = new File("providers.txt");              //Getting providers.txt file and loading in contents
	    Scanner scan = new Scanner(f);                   //Providers are seperated by a space character followed
	    int i = 0;                                       //by one or multiple id formats seperated by a colon(:)
	    int j = 1;                                       //This allows for multiple formats per provider
	    while(scan.hasNextLine() && i < maxNumProviders){
		String toAdd = scan.nextLine();
		if(!toAdd.contains("//")){ //skip comments in file
		    String rawProviderString = toAdd;
		    String[] splitProviderString = rawProviderString.split(":");
		    verifiedProviders[i][0] = splitProviderString[0];
		    while(j < maxNumFormats + 1 &&
			  j < splitProviderString.length &&
			  (splitProviderString[j] != null && !splitProviderString[j].equals(""))){
			verifiedProviders[i][j] = splitProviderString[j];
			j++;
		    }
		    j = 1;
      	 	    i++;
		}
	    }
	}catch(FileNotFoundException f){
	    System.out.println("Error: providers list not found in current directory.");
	    return;
	}
        
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	String input; //raw input string
	
	try{
	    help(); //show valid commands on startup
	    System.out.print("Please enter a command: ");
	    input = reader.readLine();	    
	    while(!input.equalsIgnoreCase("quit")){  //Exit command: terminates execution of function and returns to main
		String[] splitInput = input.split(" ");

		//Command section checks for correct amount of inputs and passes to function.
		if(splitInput[0].equalsIgnoreCase("register")){  //register
		    if(splitInput.length == 6){
			registerDomain(splitInput[1], Integer.parseInt(splitInput[2]), splitInput[3], splitInput[4], splitInput[5]);
		    }else{
			System.out.println("Error: Usage");
			System.out.println("REGISTER [Name] [period value] [period unit(day,month,year)] [provider] [contactID]");
		    }
		}else if(splitInput[0].equalsIgnoreCase("info")){  //info
		    if(splitInput.length == 2){
			infoDomain(splitInput[1]);
		    }else{
			System.out.println("Error: Usage");
			System.out.println("INFO [Name]");
		    }
		}else if(splitInput[0].equalsIgnoreCase("renew")){  //renew
		    if(splitInput.length == 4){
			renewDomain(splitInput[1], Integer.parseInt(splitInput[2]), splitInput[3]);
		    }else{
			System.out.println("Error: Usage");
			System.out.println("RENEW [Name] [Period value] [Period unit(day,month,year)]");
		    }
		}else if(splitInput[0].equalsIgnoreCase("delete")){  //delete
		    if(splitInput.length == 2){
			deleteDomain(splitInput[1]);
		    }else{
			System.out.println("Error: Usage");
			System.out.println("DELETE [Name]");
		    }
		}else if(splitInput[0].equalsIgnoreCase("help")){  //help
		    help();
		}else{  //not a valid command
		    System.out.println("Please enter a valid command.  Enter HELP for a list of valid commands.");
		}
		System.out.println();
		System.out.println("Currently registered domains: " + map.keySet());
		System.out.print("Please enter a command: ");
		input = reader.readLine();
	    }
	}catch(IOException i){
	    System.out.println("Error: could not read input");
	    System.out.println(i);
	}
    }

    //registerDomain function
    //checks if registry is full or name already registered
    //checks for domain name length of ten(10) or more
    //checks for credential validation
    //if passed all checks, create and store new Domain object
    public void registerDomain(String name, int period, String unit, String provider, String contactID){
	if(map.size() < maxNumDomains && !map.containsKey(name)){
	    if(name.length() >= 10){  //check that domain name meets length requirement
		if(unit.equalsIgnoreCase("year") || 
		   unit.equalsIgnoreCase("years")||
		   unit.equalsIgnoreCase("month")|| 
		   unit.equalsIgnoreCase("months")||
		   unit.equalsIgnoreCase("day")  ||
		   unit.equalsIgnoreCase("days")){  //validate units
		    if(validateID(provider, contactID)){  //provider verification, last step before creating a new domain
			Domain d = new Domain(name, period, unit, provider, contactID);
			map.put(d.domainName, d);
			System.out.println(name + " has been registered.");
			d.printInfo();
		    }else{
			System.out.println("Error: invalid provider or contactID");
		    }
		}else{
		    System.out.println("Error: day(s), month(s), or year(s) expected. " + unit + " recieved instead.");
		}
	    }else{
		System.out.println("Error: Domain name must be at least ten(10) characters long");
	    }	    
	}else{
	    if(!(map.size() < maxNumDomains)){
		System.out.println("Error: maximum registry size reached, delete a registry to continue.");
	    }else{
		System.out.println("Error: " + name  + " has already been registered.");
	    }
	}
    }

    //infoDomain function
    //finds specified domain object and prints domain name and expiration date
    public void infoDomain(String name){
	Domain d = map.get(name);
	if(d != null){
	    d.printInfo();
	}else{
	    System.out.println(name + " not found in domain name registry.");
	}
    }

    //renewDomain function
    //finds domain object and extends the expiration date by days, months, or years.
    //prints domain name and new expiration date 
    public void renewDomain(String name, int period, String unit){
	Domain d = map.get(name);
	if(d != null){
	    if(unit.equalsIgnoreCase("years") || unit.equalsIgnoreCase("year")){ //years
		d.years += period;
		d.printInfo();
	    }else if(unit.equalsIgnoreCase("months") || unit.equalsIgnoreCase("month")){ //months
		d.months += period;
		d.printInfo();
	    }else if(unit.equalsIgnoreCase("days") || unit.equalsIgnoreCase("day")){  //days
		d.days += period;
		d.printInfo();
	    }else{ //invalid unit
		System.out.println("Error: day(s), month(s), or year(s) expected. " + unit + " recieved instead.");
	    }
	}else{
	    System.out.println(name + " not found in domain name registry.");
	}
    }

    //deleteDomain function
    //removes domainObject from registry hashmap
    //prints success or failure message
    public void deleteDomain(String name){
	if(map.remove(name) != null){
	    System.out.println(name + " has been removed");
	}else{
	    System.out.println("Error: could not delete " + name);
	}
    }

    //validateID function
    //iterates through provider matrix to find specified provider and passes formats to matchFormat function
    //returns true if both provider found and format matched, otherwise returns false
    public Boolean validateID(String provider, String contactID){
	for(int i = 0; i < maxNumProviders; i++){
	    if(verifiedProviders[i][0].equals(provider)){
		int j = 1;
	        while(j < maxNumFormats + 1 && (verifiedProviders[i][j] != null && !verifiedProviders[i][j].equals(""))){
		    if(matchFormat(verifiedProviders[i][j], contactID)){
			System.out.println("Cedentials Validated");
			return true;
		    }
		    j++;
		}
		j = 1;
	    }
	}
	return false;
    }

    //matchFormat function
    //checks if the contactID provided matches the providers format
    //looks at format to determine which dictionary to use
    //if dictionary contains contactID character, move onto next character
    //if all characters pass, return true, else false
    //can be expanded with additional dictionaries
    public Boolean matchFormat(String format, String contactID){
	String alphabetLowerCase = "abcdefghijklmnopqrstuvwxyz";
	String alphabetUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String integers = "123456789";
	
	if(format.length() == contactID.length()){
	    String[] formatSplit = format.split("");
	    String[] contactIDSplit = contactID.split("");
	    	    
	    for(int i = 0; i < formatSplit.length; i++){
		if(formatSplit[i].equals("C")){  //format dictates next char must be a capital
		    if(!alphabetUpperCase.contains(contactIDSplit[i])){
			return false;
		    }
	        }else if(formatSplit[i].equals("c")){  //format dictates next char must be lowercase
		    if(!alphabetLowerCase.contains(contactIDSplit[i])){
			return false;
		    }
		}else if(formatSplit[i].equals("i")){  //format dictates next char must be an integer
		    if(!integers.contains(contactIDSplit[i])){
			return false;
		    }
		}else{ //character does not match format
		    return false;
		}
	    }
	    return true;
	}
	return false;
    }

    //help function.
    //simple function that prints valid commands with their arguments
    public void help(){
	System.out.println("");
	System.out.println("Valid commands include: REGISTER, INFO, RENEW, DELETE");
	System.out.println("REGISTER [Name] [period value] [period unit(day,month,year)] [provider] [contactID]");
	System.out.println("INFO [Name]");
	System.out.println("RENEW [Name] [Period value] [Period unit(day,month,year)]");
	System.out.println("DELETE [Name]");
	System.out.println("Enter HELP to see this list again");
	System.out.println("");
    }
}
