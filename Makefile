all: %.class

.PHONY: check-syntax

check-syntax:
	javac -cp .:bin -Xlint $(CHK_SOURCES)	
%.class:
	find src -name "*.java" > sources.txt
	javac -d bin @sources.txt
clean:
	touch bin/dumy.class; rm -r bin/*
