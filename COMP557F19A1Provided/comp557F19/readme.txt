Comp557 Assignment 1
by Chen He 260743776

Character Name : Rainbow Worm
It is from catepillar family, with a mutant teapot like detachable head(free joint).
the root node is the joint at the bottom, since the worm is considered immobilized.

the video at ./rainbow_worm.avi demonstrate one of its basic movements
Profile picture at ./Capture.png

code changes:
added create directory if not exists functionality in CanvasRecorder.saveCanvasToFile
added create directory if not exists functionality in KeyFrameAnimatedScene.saveKeyFrames
modified the file directory in KeyFrameAnimatedScene.loadKeyFrames from "a2data" to "a1data"
modified the sequential if/else by switch statement in CharacterFromXML.createJoint and CharacterFromXML.createGeom to make the codes more readable
modified CharacterFromXML.getTuple3dAttr to CharacterFromXML.getVector3dAttr in order to simplify type castings for other codes

