#client
##How to upload the data?
* First, in ChooseActivity, all the answer will be saved in a String variable saveAnswer and questions will be saved in a array of string. The The total number of questions and the id of the questionnaire are also stored in the variables. They will be put into an intent pasted to the ReportActivity
```java
 CtoR.putExtra("answerJSON",saveAnswer);
        CtoR.putExtra("count",count);
        CtoR.putExtra("survey_id",Survey_id);
        String[] quesArr=quesList.toArray(new String[quesList.size()]);
        CtoR.putExtra("quesArr",quesArr);
```
* Second, ReportActivity will get the intent from ChooseActivity and get all the data to show them in a TextView. In this activity, the app will ask the user to get the permission of ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION and READ_PHONE_STATE to get the information of user's location and imei. 
  * function of getting data and time when the user uploads the result
  ```java
    public String getTime(){...}
  ```
  * function of getting location when the user uploads the result
  ```java
    public String getLocation() {...}
  ```
  * function of getting the imei of user's phone
  ```java
   public String[] getIMEI() throws Exception {
   ```
All the data will be saved in the sqlite of the client and the database of the server. Once a data cannot be properly obtained it will be set to null. If the server receives the uploaded data, it will prompt the user to upload successfully. Otherwise, it will prompt the user to upload failed and ask the user to check the information of network Settings
    
 
