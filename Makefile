all: %.class

.PHONY: check-syntax

check-syntax:
	javac -cp .:bin -Xlint $(CHK_SOURCES)
common:
	javac -d bin src/common/*.java
gui: all
	javac -cp bin -d bin src/gui/*.java
index: common
	javac -cp bin -d bin src/index/*.java
context: index
	javac -cp bin -d bin src/index/context/*.java
%.class:
	find src -name "*.java" > sources.txt
	javac -d bin @sources.txt
clean:
	touch bin/dumy.class; rm -r bin/*
