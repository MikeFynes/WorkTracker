Front End Client

Device requirements

Android JellyBean 4.1 or higher

Download the application from GitHub and run the following in your git terminal.

cd my/project/path 
git clone https://github.com/MikeFynes/HourKeeper.git


Open the project in Android Studio or your chosen IDE and edit the class named Track-erApp, amend the following string:
private final String SERVER_URL = “PATH TO YOUR SERVLET HERE”


for example:

private final String SERVER_URL = "http://my.server.com/Servlet/Servlet";


You can then build an APK that meets your criteria and use the application using the codes defined in the back end implementation.
