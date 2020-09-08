# ExamScanner Mobile Application
## Ben-Gurion University of the Negev, Software Engineering Project 2020
### Android application for scanning & grading multiple choice tests.

Requirments:
- device with Android operation system
- internet connectivity

Initial SetUp:
- download the .apk file to your device and install it.
- on your PC, from the 'app' folder run 'firebase deploy' to use the remote DB, storage and functions (one time only)

Usage guidelines:
- click on the + button to create new exam
- upload versions keys in PDF format
- in the url container please insert a public Google spreadsheet url **or** 
  a private one with editing premissions to the app service account: examscanner-de46e@appspot.gserviceaccount.com
- to scan student solution click on the newly created exam.
- watch the spreadsheet filled with data.

# Abstract Design
Here we can see the Use-Case diagram of the project.

![Image of Yaktocat](https://github.com/rotba/ExamScanner/blob/master/usecase.png)

We designed our system in a way that each use case, has a corresponding MVVM "instance" as described in the following diagram, meaning that each use-case *aUseCase* has an associated ***view*** class (probably named aUseCaseFragment.java), associated ***view-mode*** class (aUseCaseViewModel.java) and associated ***model*** classes that are associated to the logic of the use-case. We belive that the use-case diagram is intuitive and a comfortable way to devide the system, while building it and on maintanence.

![Image of Yaktocat](https://github.com/rotba/ExamScanner/blob/master/design.png)

A more detailed description of the system can be found in the project [site.](https://rotemb271.wixsite.com/examscanner)

