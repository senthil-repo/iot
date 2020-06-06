Disclaimer:
 Please install Java, Maven & other basic softwares that are needed to run any java based applications.

Please follow the steps mentioned below to run the application
1) Download the source code from the URL below :
https://github.com/senthil-repo/iot.git

2) Build the source code : Go to the folder /iot/iottracker/ and run the following command. This creates a jar file 'iot-tracker-1.0.jar' under the folder '/iot/iottracker/target/'
	
	mvn clean install
	
3) Run the application & start the service: Go to the folder '/iot/iottracker/target/', and run the following command
	java -jar -Dserver.port=8090 iot-tracker-1.0.jar

4) Place data.csv file under any location and keep a note of the full path. You would need to supply that in the body while running the 'Data Load' service

5) There are 2 services that the application provides.
Go to the following URL to get the swagger documentation which will give you the more details about invoking the services.
