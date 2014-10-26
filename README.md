CSeRPlugin2.0
=============

CSeRPlugin2.0 plugin for tracking and visualizing copy-paste code clones

Setting up the development environment for CSeR projects. I used Java 8 and Eclipse RCP Luna version in OSX, however the project was developed in 2009 in Eclipse Europa
So I don't see any reason the project won't run on older versions of Java and Eclipse.

1. Clone and import the CSerCore project, https://github.com/feroshjacob/CSeRCore-04-20-2009 to workspace. If you use the above settings the project could be imported without any
errors in the workspace.

2. Clone and import the this project to the same workspace. This project as above should not also have any errors. 

3. Add CSeRCore Project as a project dependency of the CSeRPlugin project (Project -> Buildpath -> Configure buildpath -> Add)

4. Double click "plugin.xml" of the Plugin project and click on the dependencies tab, add "CSeRCore (1.0.0"

5. Click on the overview tab of the "plugin.xml" and "Launch an Eclipse Application"

6. Create a Java Project called Testing (IMPORTANT: The project name is hard-coded, so please use the project name "Testing")

7. Create a class and the open with "CSer Editor"

8. Copy a code section and paste it in the same file, you should CSeR in action.

9. Please let me know.


Note: I just tried to make it work and help to set up a development environment, I didn't even do a complete testing, When I tried copying a complete Java class, it was not working.  May be that is an easy fix, but I am not sure. I was expecting many version errors, because the Eclipse has changed a lot over the years, but luckily I didn't have 
to change a lot to make it work.  One of the changes I did that can have a affect on the working is commenting (src/org/eclipse/jdt/internal/corext/refactoring/rename/RenameTypeProcessor.java, lines 671-681). Other than that, I made only build path configuration changes from the 2009 version.  


