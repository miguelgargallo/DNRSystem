//Author: Michael Montgomery 
//Domain class represents each individual domain and all its associated information.

public class Domain{
    String domainName;
    int years;
    int months;
    int days;
    String provider;
    String contactID;
    
    //Constructor
    public Domain(String name, int period, String unit, String provider, String ID){
	domainName = name;
	if(unit.equalsIgnoreCase("years") || unit.equalsIgnoreCase("year")){
	    years = period;
	}else if(unit.equalsIgnoreCase("months") || unit.equalsIgnoreCase("month")){
	    months = period;
	}else{
	    days = period;
	}
	provider = provider;
	contactID = ID;
    }

    //printInfo prints domain name and expiration date in nice format.
    public void printInfo(){
	System.out.println("Domain Name: " + domainName);
	System.out.println("Expiration: " + years + " years, " + months + " months, " + days + " days");
    }
}
