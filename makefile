CC = javac

default:
	$(CC) Domain.java
	$(CC) DNRSystem.java
	$(CC) Main.java

run:
	java Main.java

test:
	java Main.java < test.txt

clean:
	rm  *.class
