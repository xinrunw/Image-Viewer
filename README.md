University of Toronto, CSC207 Team Project - Image-Viewer
Team members: Xinrun Wang, Zhifan Wang, Silun Chen, Tiger
Instruction to run from command line:

    Open terminal, cd to group_0458/phase2/src and copy the following line to terminal and press enter to run

        javac Main/Main.java && java Main.Main

    After closing the program, cd to group_0458/phase2/src , type in the following and press enter to run again
	
	java Main.Main

To run UnitTest from command line, JUnit5 need to be imported. Test classes are src/UnitTest/DatabaseTest.java and src/UnitTest/TagTest.java
  Note: To run UnitTest in Intellij, Working directory need to be set to phase2/src/

Instruction for using the program:

1 To open a root directory and use program to edit photos:

1.1 Click the “search” button on the right to find the directory or type in the path you want to process.

1.2 Click “open directory”.

1.3 On the left, there are tags that are used under that directory. If you want to view all the photos, press All photos, otherwise press one of tha tags to view all photos under that tag.

1.4 Select an image click “open image”.

1.5 On the left, there is the information of this image, and on the right, there are a few buttons to edit the image.

1.5.1 To add a tag to this image:
     Type in a new tag in the text-box on the right or select an existing tag from the data record (including those that are not used under the directory) from the drop-down list and click “Add Tag” button.
     Note: - empty name or name with only spaces cannot be added as tags.
           - Same tags cannot be added twice or more (so then drop-down list will not include tags that already belong to the photo) 
           - If you include a tag within the tag you are adding, it will be considered to be one single tag

1.5.2. To delete a tag from this image:
     Click “Delete Tags” and select one or more existing tag(s). Then click “Delete” to delete the tag.

1.5.3. To go back to older sets of tags for the image:
     Click “Rename Photo” and select the older sets of tags you want to go back to. Then click the “Rename” button.

1.5.4. To move a photo:
     Click “Move” and select the directory which the image will be moved. Then click “Move” button. 
     Note: - This image's information will still be saved
	   - If you move the photo to a location that is inside of the directory you first open, then it will still be shown in the program, otherwise it will not be shown.

1.5.5 To open the OS directory of the photo:
     Click "Open Directory" and the file explorer will pop out. 

2 To open and use Tag Manager:

2.1 Click "Tag Manager".

2.2 Type in a new tag and click "Add Tag" to add a tag.

2.3 Choose one existed tag and click "Delete Tag" to delete a tag.
      Note: If the tag is attached to some photos, then a warning window will pop out so as to confirm user's action.

3 To open Log file:

3.1 Click "Log" and the file will automatically be opened.



Warning:
1. All the data of this program is saved in file data.ser under phase2/src. Please do not delete this file, otherwise all the data in this program will be lost.

2. If you rename or move the image or directory in your operating system, all the data under the image or directory will be lost.

3. If you did what warning 1 and 2 mentions, the next time you open the program they will be created as new images (all the data will be lost)

4. If you remove or rename photos using OS while using the program, program will fail to locate the image so that image will not be shown while other functions still work. So if you accidentally edit the image while using it, just move back to the original location and edit the name back to how it was.

5. If a photo initially has duplicated tags within its name (before it is opened using the program and stored in Database), then the program will automatically rename the photo to eliminate duplicated tags.
