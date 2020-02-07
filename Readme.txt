Report Card Generator

NOTE-
1)Input file names should not change and their location inside 'java-report-card-generator' folder as currently they are.
2)To run the project on commandline-
	-Go inside 'java-report-card-generator' using 'cd' command.
	-Run following command : javac reportcard/ReportCard.java
	-Run following command : java reportcard.ReportCard.
	-The required '.txt' file will be generated at the same location.
	
Description:
The purpose of this project is to generate a text file of student report card based on the data inside 
4 csv files provided.

Following are the steps which this project does to get the required result-
1)Get data from all 4 csv files and places them into 4 class objects.
2)Calculates averge and final grade for every student by combining data in tests.csv and marks.csv
3)Stores data in final Student Report Card class object.
4)Generates text file with required data.
