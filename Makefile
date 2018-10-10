build:
	mvn clean compile package

run:
	mvn exec:java -Dexec.mainClass="ie.tcd.laveryhu.info_and_web.IndexAndSearch"
