Flat File Translator(Read ME)
=====================================================================

Tools Used:
1. Java (1.8.0_91)
2. Spiring Tool Suite (v 4.6.2)
3. Maven
6. Junit


Solution Outline:.
1)  This is simple Java Stand alone app which reads three tab-seperated flat text files (One Vender file and two mapping files) and creates an output on the consle (Console) based on values in the mapping files. 
2)	The Vendor fle is in the following format : 
	
	COL0 COL1 COL2 COL3	
	ID1 VAL11 VAL12 VAL12
	ID2 VAL21 VAL22 VAL23

3)	The ID mapping File is the format :
	
	ID2 OURIDXXX

4)	The Column Mapping file is 	in the format :
	
	COL0 OURID
	COL1 OURCOL1
	COL3 OURCOL3
	
5)	The output file looks like the follwoing :
	
	OURID OURCOL1 OURCOL3
	OURIDXXX VAL21 VAL23
	
6)	The app readers the column header list first and based on the headers filters out all the row values for the missing column(if any) based on the vendor file. 
7)	It also compares the ID;s in the vendror file with the ones in the ID mapping file and replaces all the vendor ID;s. Then it stores this result in a reqColList
8)	It then passes this list to rowMapping() which reads the ID mapping file and filters out all the rows which not available in the mapping file 
9)	The ID Mapping and Column mapping (except the first Coloumn Header Row )file can be altered and the out file will be created based on the changes made to these two files. 
9)	For JAR dependencies I have used Maven.
10)	I have used Maven Shade plugin to  package the artifacts in an uber-jar, including its dependencies.

Classes:
1.	FlatFileTranslator.java : This contanins the the business logic and the main() method.


Build and Run Instructions :

Using Command Prompt :
 
1)	Download the Zip  (This already contains the executable JAR to be executed from the CMD!)
2)	Extract the content into a directory on your machine. This should create a directory named 'flat-file-translator'
3)	Change directory to path where Jar is located at ‘root_dir’/flat-file-translator` cd $root_dir/ flat-file-translator
4)	Execute following maven command to create an executable jar `mvn clean package`. This command will 	create a jar with name ‘flat-file-translator-0.0.1-SNAPSHOT.jar in $root_dir/flat-file-translator/target directory
5)	Finally, cd to target directory and copy the directory src/main/resources where all the three files (vendor.txt, idMpping.txt and columnMapping.txt) exists to the folder where the JAR is present/exeucted 
	(You can also create this folder structure and place these three files in there). 
6)	Execute the jar using this command 	`java -jar flat-file-translator-0.0.1-SNAPSHOT.jar `



Build Using Eclipse/STS :

o	If you are running the same from Eclipse then follow these steps to build and run: 
o	Install Maven plugin in your Eclipse if not there already.
o	Import the project as an Existing Maven Project
o	Build using Project -> Build Project
o	Then right click on the project -> Run As -> Maven Build. This will pop up the Run Configuration Screen.
o	Enter In Goals: clean install
o	Click on Run to package into a Jar.


