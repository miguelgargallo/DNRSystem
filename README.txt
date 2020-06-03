This mini Domain Registry system was created by Michael Montgomery

The mini Domain Registry system supports the following makefile commands:
	 make - easily compile all java files
	 make run - will execute the Main.java file and start the DNR system
	 make test - will activate the DNR System and execute all commands in the test.txt file

This mini Domain Name Registry (DNR) System supports the following commands and arguments:
     	 REGISTER [Name] [period value] [period unit(day,month,year)] [provider] [contactID]
	 INFO [Name]
	 RENEW [Name] [Period value] [Period unit(day,month,year)]
	 DELETE [Name]
	 HELP

REGISTER - Register a domain name with the DNR System.  Must provide domain name ten(10) characters or longer and must provide credentials matching one of the providers, found in the provider.txt file, and a contactID that matches an associated format.

INFO - displays the domain name and expiration of the specified domain.

RENEW - adds additional time in the form of days, months, or years to the specified domain.

DELETE - removes the specified domain from the DNR System.

HELP - displays the above commands and their arguments


test.txt:
	This test file was composed to demonstrate how the DNR System will handle certain cases, including:
	     -Registering a domain with valid credentials 
	     -Atempting to register a domain with a contactID of invalid format
	     -Registering the same domain with a valid contactID format
	     -Attempting to register an already registered domain
	     -Attempting to register a domain beyond the systems given capacity
	     -Tolerance of spelling (day vs days) as well as cases (Register vs REGISTER)
	     -Renewing a domains expiration
	     -Getting info about a domain
	     -Getting info about a domain that is not in the system
	     -Getting info about a domain after it has been renewed
	     -Deleting a domain from the system
	     -Tolerance of an invalid command
	     -Use of the Register command without correct arguments
	     -Use of Info command without correct arguments
	     -Use of Renew command without corrent arguments
	     -Use of Delete command without correct arguments
	     -Use of the Help command

	These cases were run with a DNR System with the following values:
	     -15 maximum providers
	     -5 formats per provider
	     -100 maximum registered domains
	     	  in Main.java: dnr = new DNRSystem(15, 5, 100)
