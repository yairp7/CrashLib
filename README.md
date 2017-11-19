# CrashLib
An Exception Reporting Library

Android Setup:
--------------
In your extended Application Singleton class override the 
attachBaseContext(Context base) function and inside call the init() function of the 
CrashLib and pass an the Application as parameter.

  ```Java
	Example:
		@Override
		protected void attachBaseContext(Context base)
		{
			super.attachBaseContext(base);

			CrashLib.init(this);
		}
  ```
		
Setting the server address:
  In the library utils.Constants class edit the URL field.

Enable/Disable:	
---------------	
  To disable the CrashLib you can do the following:
	CrashLib.setUseDefaultExceptionHandler(false);
	From anywhere in the project.

Send Caught Exception:
---------------------
To send a report on exceptions that were caught, u can use the following function:
CrashLib.getInstance().logException(Throwable exception);
Or use the custom exception CrashLibException that and call her function logException()
when caught(which does the same).

Report Contains:
----------------
The report sent to the server is built from the following data(in json format):
  1) ReportID(same as it`s filename)
  2) When(in date format)
  3) Exception Description(exception.toString())
  4) StackTrace(lines seperated by \n)
  5) Logcat(lines seperated by \n)
  6) Android Version
  7) Device 
  

Server Requirements:
--------------------
Handle POST requests containing a JSON Array of JSON reports objects.

The data should be provided in the request`s BODY.
  
Server responses: 1 for success and -1 if failed.
  
Example - Node.js + Express.js simple server:
---------------------------------------------
```javascript
  const express = require('express');
  const app = express();
  const fs = require("fs");
  const RESULT_OK = '1';
  const RESULT_FAIL = '-1';
  const REPORTS_DIR = 'Reports';

  if(!fs.existsSync(REPORTS_DIR))
  {
    fs.mkdirSync(REPORTS_DIR);
  }

  app.post('/api/exceptions', function (req, res) 
  {
    let body = [];
    var report = null;
    req.on('data', (chunk) => 
    {
      body.push(chunk);
    }).on('end', () => 
    {
    try
    {
      report = Buffer.concat(body).toString();

      report = JSON.parse(report);
      for(var i = 0; i < report.length; i++)
      {
        var reportID = report[i].ReportID;
        var jsonStr = JSON.stringify(report[i]);

        // Save file
        var path = "Reports/" + reportID + ".r";
        fs.writeFile(path, jsonStr, function(err)
        {
          if(err) 
            throw err;
        });
      }

      res.end(RESULT_OK);
    }
    catch(err)
    {
      res.end(RESULT_FAIL);
    }
    });
  });
  app.listen(9000, () => console.log('Server running on port 9000!'));
```
