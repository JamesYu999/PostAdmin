**This is a Java SpringBoot application running with Java 15.**

To run this application:<br>  
	- Install OpenJDK 15(or above), install latest Maven, configure the path for JDK and Maven correctly.<br>  
	- Open a command prompt with Administrator privilege, cd to that project folder, run: mvn clean package<br>  
	- Go to \target folder and run: java -jar ProjectDemo-0.0.1-SNAPSHOT.jar<br>  
To access the application:<br>  
 	- You can test it directly on browser, type "http://localhost:8080/swagger-ui/index.html#/" and enter, then you can play with the API<br>  
 	- Type "http://localhost:8080/api/v1/admin/getUserPosts" and enter. You may also add a parameter (userId, valid range is from 1 to 10)<br>  
 	- Open a command line and run: curl http://localhost:8080/api/v1/admin/getUserPosts<br>  
	