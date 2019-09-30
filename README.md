-----------------------------------------------------NOTESMANAGER-----------------------------------------------

Download notesManager.zip

INSTALL:

0. Preferred java version 11  (11.0.2) -> wget http://gluonhq.com/download/javafx-11-0-2-sdk-linux

1. open attached file start.sh (guarantee access if needed: chmod u+x start.sh). Substitute [JAVAFX_PATH] with a path to javafx/lib. By default: 
/usr/lib/jvm/java-11-openjdk-am64/javafx-sdk-11.0.2/lib/

Sample start.sh (watch out for redundant blank spaces!): 
#!/bin/bash
java --module-path /usr/lib/jvm/java-11-openjdk-amd64/javafx-sdk-11.0.2/lib/ --add-modules=javafx.controls,javafx.fxml --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar notesmanager.jar

2. Run start.sh script.



----------------Short description:

On the left side of the main screen, one can choose and display a sample category with its whole content. By using upper buttons one can easily manage all of the files.


NotesManager features different kinds of importance marks. Every note can be associated with one of the five particular "states", which can be accessed via the main screen. By clicking on one of those symbols, one can filter notes by a particular state. 

Description of provided states (from the top):
 - notes that are not  known/ new notes
 - notes that are familiar but not masted yet
 - notes that are mastered 
 - favorites
By default, the application does not recognize any notes as favorite. 



What is more, in the upper left corner one can search for notes using their names.
NotesManager supports default applications, so every file can be opened in two ways:
 - By an external default app (top-left)
 - Or just in the app.

 After displaying file in NotesManager, application allows to:
 - rename
 - edit
 - save
 - change state
 - use full screen (top-right) option
 - use spelling checker (top-right)

