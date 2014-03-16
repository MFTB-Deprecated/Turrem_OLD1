Turrem
  
    DO NOT modify the .gitignore (to ignore files or folders add them to ".git\info\exclude")
  
  ---To install your workspace---
  
      NOTE: This is for Eclipse. If you do not use eclipse…you will have to customize my instructions to fit.
      NOTE: This is for Windows. If you are using a Mac, I have no idea what to do because I do not own a mac.
      NOTE: In the following /Turrem/ refers to the directory of your local repo and should be the folder that contains “.git”, “src”, “bin”, “.gitignore” and so on.
      NOTE: When I say your project folder I am referring to the folder in /Turrem/ that is Eclipse’s workspace location. It will contain things like “.settings” and “.classpath”
      NOTE: [stuff goes here] refers to some directory that is entirely dependent on your own personal setup. For example, I would replace “[stuff goes here]/Turrem/” with “C:/Users/Sam Sartor/Turrem/” but you might replace it with “C:/Users/Foo/Documents/GitHub/Turrem/”
      NOTE: This does not explain every step to do everything so if you are not familiar with eclipse or git you may need to experiment some, do a few Google searches, and/or ask me a few questions.
      NOTE: I am relatively new to Git and because I have never had instruction or worked in a team, I am unfamiliar with the conventions I should be following. Keep that in mind when you are reading this.
    
    ECLIPSE PROJECT
      1. Make a local copy of the repo
      2. Create an eclipse project with the repo as the path (/Turrem/)
      3. Add your eclipse workspace location (mine is /Turrem/Turrem/) to ".git\info\exclude" so git will ignore it
    SOURCE FOLDERS
      4. Go to the source tab under Java Build Path in your project's properties. 
      5. Click link source and set the Linked folder location to /Turrem/src/Core/ and name it “core”
      6. Repeat #5 with Client, Entity, Tech, etc.
    LWJGL
      7. Create the /Turrem/bin/jars/libs/ folder
      8. Download LWJGL
      9. Put “lwjgl.jar”, “lwjgl_util.jar”, and “native” from your download of LWJGL into /Turrem/bin/jars/libs/
      10. In eclipse, create a user library that contains all the .jar files from your LWJGL download
        NOTE: Do a Google search
      11. Add your lwjgl user library to your Turrem project libraries
    EXPORT AND COMPILE
      12. Right click on “core” in the package explorer window
      13. Click export
      14. Select "Java>JAR file" and click next
      15. Your export destination should be “[stuff goes here]\Turrem\bin\jars\core.jar”
      16. Make sure “Overwrite existing files…” is checked.
      17. Next
      18. Check “Save description…” and set the file to “[your project folder]\corejar.jardesc”
      19. Repeat 12-18 with each of your source folders (core, client, entity, tech, etc.)
    RUN CONFIG
      20. Click the arrow next to the green play button with the tool box and then click “external tools configurations”
      21. Create a new program in the “external tools configurations” window
      22. Set location to your computer’s version of “C:\Program Files\Java\jdk1.7.0_40\bin\javaw.exe”
        NOTE: This will depend on which version of java you have installed
      23. Set the working directory to “[stuff goes here]\Turrem\bin\”
      24. Arguments will be complicated. Set that to your version of the following:
      
      -cp "[stuff goes here]\Turrem\bin\jars\client.jar";"[stuff goes here]\Turrem\bin\jars\entity.jar";"[stuff goes here]\Turrem\bin\jars\core.jar";"[stuff goes here]\Turrem\bin\jars\libs\lwjgl.jar";"[stuff goes here]\Turrem\bin\jars\libs\lwjgl_util.jar" -Djava.library.path="[stuff goes here]\Turrem\bin\jars\libs\native\windows" zap.turrem.ClientMain
     
        NOTE: I have found that the above does not work for everyone and I am not sure why. Changing the semicolons to colons, messing with the whitespace, and adding/changing the quotation marks might be necessary.
        NOTE: You may also know an entirely better way to do this.
        
    RUNNING TURREM
      25. After you make a change you will have to re-compile by right clicking the relevant .jardesc(s) in the package explorer and clicking “Create Jar”
      26. To run just click the green play arrow with the tool box
