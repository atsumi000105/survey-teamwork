# SURVEY-TEAMWORK
> A website to create surveys and an Android APP to fill in the surveys.

## Dependencies

## Table of contents
- [Installation](#Installation)
- [Usage](#Usage)
- [Note](#Note)
- [Team](#Team)
- [FAQ](#FAQ)
- [Code](#Code)
- [Contract](#Contract)

## <span id="Installation"> Installation</span>
 - Move the `server/root` folder to your web server.
 - Open your web server and Android DevTools to open the `android-client` folder.
 - Open the home page, for a local web server for example: `http:localhost:8080/index.html`, you are supposed to see a page like this;
![image.png](https://i.loli.net/2020/03/20/2RkbZHDAtrX6SpU.png)
 - You can just open the `app-debug.apk` in android-client root folder, or build one with your Android DevTools. You should see the Welcome page like this:
 ![insert the android img]()

## <span id="Usage">Usage</span>
- Create a survey on the web platform. After you finished one, they should show on the home page.
![image.png](https://i.loli.net/2020/03/20/lwbtJSTZiOCXpQ1.png)
- Click the `Scan QRcode` button on the specific survey, here is the QR code for this survey.
![image.png](https://i.loli.net/2020/03/20/jKohEBpWHFzPA6q.png)
- Scan the QR code with your suvey app, the specific survey will automatically display in your app.
- After finish the questions, upload it to the android database and server database. A prompt will pop up to inform you.
![insert the toast when uploading]()
- Exiting the APP requires you to unlock your system lock. Use your fingerprint or enter the passwork correctly, You can leave the app now.

## <span id="Note">Note</span>
- **Please Never Name the Survey Title in Chinese**
- **Please make sure your phone and computer in the same LAN when you test on local server**

## <span id="Team">Team</span>
|[Cherry_20176134](https://github.com/WindWaving)|[Cynthia_20175980](https://github.com/Cynthia879)|[Geralt_20175990](https://github.com/LuSylvia)|
|--|--|--|
|[![](https://avatars2.githubusercontent.com/u/39412843?s=200&u=43dab9aa9249a5abf54014813e8a9c5f7b9b9272&v=4)](https://github.com/WindWaving/survey-teamwork)  |[![](https://avatars1.githubusercontent.com/u/61367567?s=200&v=4)](https://github.com/Cynthia879/survey-teamwork)  |[![](https://avatars2.githubusercontent.com/u/40913318?s=200&v=4)](https://github.com/LuSylvia/survey-teamwork)|
|[https://github.com/WindWaving/survey-teamwork](https://github.com/WindWaving/survey-teamwork)|[https://github.com/Cynthia879/survey-teamwork](https://github.com/Cynthia879/survey-teamwork)|[https://github.com/LuSylvia/survey-teamwork](https://github.com/LuSylvia/survey-teamwork)|

## <span id="FAQ">FAQ</span>
- **1.How to create my own page dynamically？**
    - First, you have to write the XML of the page you want to create, assign an ID to the button control in the page, bind the control in the oncreate method, and then add the **onClick** attribute to the button, with the value of nextque. And a unique identifier is added to the page for representation.
    - Then, add new judgment statements in **onActivityResult** method, such as
       ```
      if(Jo.getString("type").equals("YourType")){
         ShowYourType();
      }
       ```  
    - Next, you need to judge the newly added page at the beginning of the **nextque** method, such as
       ```
       public void nextque(View view) throws JSONException {
        Boolean flag=false;
        ......
         if(style.equals("YourType")){
            flag=saveYourTypeAnswer();
         }
        ......
       }  
       ```
       Then judge whether the flag is true, and then add judgment in the branch of **if (current + 1 < count)**, such as
       ```
       if(current+1<count){
         ......
         if(Jo.getString("type").equals("YourType")){
             ShowYourType();
         }
         ......
       }
       ```
    - Finally, implement showyourtype and saveyourtypeanswer methods. There are similar methods in the source code. You can refer to them to implement your own methods.


## <span id="Code">Code</span>
This is an introduction on how the Android application works.
> How to upload the data?
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
    
## <span id="Contract">Contract</span>
Contract me if you have any problem in the following way:
- Email at 13833799573@163.com 
