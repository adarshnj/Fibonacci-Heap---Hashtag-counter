JFLAGS = -g
JC = javac
JAR = jar
.SUFFIXES: .java .class

CLASSES = \
        HashTagCounter.java\
        FibHeap.java \
        Node.java

classes: $(CLASSES:.java=.class)


.java.class:
	$(JC) $(JFLAGS) $*.java
	$(JAR) cvfe hashtagcounter HashTagCounter HashTagCounter.class FibHeap.class Node.class

default: classes

clean:
	$(RM) *.class *.jar
