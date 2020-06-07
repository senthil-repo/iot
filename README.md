Disclaimer:
 Please install Java, Maven & other basic softwares that are needed to run any java based applications.

Please follow the steps mentioned below to run the application
1) Download the source code from the URL below :
https://github.com/senthil-repo/iot.git

2) Build the source code : Go to the folder /iot/iottracker/ and run the following command. This creates a jar file 'iot-tracker-1.0.jar' under the folder '/iot/iottracker/target/'
	
	mvn clean install
	
3) Run the application & start the service: Go to the folder '/iot/iottracker/target/', and run the following command
Note: If you are running any other applications underport 8090, please change the port below.
	java -jar -Dserver.port=8090 iot-tracker-1.0.jar

4) There are 2 services that the application provides.
Go to the following URL to get the swagger documentation which will give you more details about invoking the services.

http://localhost:8090/swagger-ui.html

5) Other useful information:
 Place the data.csv file under any location and keep a note of the full path. You would need to supply that in the HTTP post body while running the 'Service 1 (i.e) Data Load' service. (Please check the sample file 'data-sample.csv' added next to README.md file)
 Note: The first row of the 'data.csv' should be the column names, followed with other records.

- To run the 1st service, please use the following URL and supply the filePath in the HTTP POST body field.
URL - http://localhost:8090/iot/event/v1
Body - C:\Users\XXXX\Desktop\data.csv
- To run the 2nd service, please supply the following (sample) parameter in the HTTP GET request
