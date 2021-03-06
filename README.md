# Artemis Engineering Client

This is a Java client for [Artemis Space Bridge Simulator](http://artemis.eochu.com/). It attempts to provide an better experience for those playing the Engineering station by providing various usability improvements and advanced functionality.

More information on specific functionality will be available as development progresses.

## Persistent Custom Presets
TODO: document me!

## Keybindings

### Power vs. Coolant
If `Shift` is pressed, allocation keys affect **coolant** allocation, otherwise they affect **power** allocation.

### Custom Keybindings
Define custom keybindings in the file `input.cfg` at the root of the repository.  Each line defines a binding, and the format for each line is `<system enum name> <positive key> <negative key>` (each componnet is separated by one space).  Duplicate bindings are not allowed.  Order and capitalization are irrelevant.

Valid system names are:

- BEAMS
- TORPEDOES
- SENSORS
- MANEUVERING
- IMPULSE
- WARP_JUMP_DRIVE
- FORE_SHIELDS
- AFT_SHIELDS

Keys must match a `VK_` key constant in [java.awt.KeyEvent](https://docs.oracle.com/javase/8/docs/api/java/awt/event/KeyEvent.html), without the `VK_` prefix.  Example of some custom keybindings:

	WARP_JUMP_DRIVE F6 F5
	beams BACK_slash braceright
	FORE_Shields H comMA

Keys may not be one of the following reserved (i.e. not re-bindable) keys:
	BACK_SLASH			(Debugging command)
	SPACE				(Reset energy and coolant settings)
	0-9					(Apply preset)

Keys may conflict with a default binding (see defaults below) only if they also define an input for the system that the default binding belonged to.  Setting the same value as a default is okay.  For example:

	TORPEDOES W A		(A is the default for Primary Beam decrease)
	BEAMS F1 F2			(Because we've defined a new key for Primary Beam decrease, it's okay)

This hasn't been tested on a keyboard using a character set other than the Western Latin standard.

### Defaults
For each input that does not have a custom key binding specified in `input.cfg`, the following defaults are used:

	  SYSTEM		INCREASE	DECREASE
	  ----------------------------------
	  Primary Beam 		Q			A
	  Torpedoes			W			S
	  Sensors			E			D
	  Maneuver			R			F
	  Impulse			T			G
	  Warp				Y			H
	  Front Shield		U			J
	  Rear Shield		I			K

## Contributing

### General
The entry point to the program is located in the `ClientMain.java` file.

### Developer Modes
The client has two special modes that are useful only to developers.

**Fake mode**: Fake mode runs a local simulation without actually connecting a real Artemis server. Highly recommended for basic UI development, as you
will not need to continuously connect and reconnect to a real server, speeding cycle times. A lot of functionality is simulated (e.g. overheating a 
system works), but some is not (damcon teams don't really work). You can put the client in this mode by passing the `--fake` command line flag.

**Proxy mode**: Proxy mode allows a vanilla engineering client to connect to this client, which in turn connects to the server. With this setup, both clients will render the same data from the server, and either client can issue commands. This is a great way to check the behavior of the vanilla client
against this improved client without running afoul of the "one engineering console per ship" limit. You can put the client in this mode by passing 
the `--proxy` command line flag.

You can also supply the server hostname/IP and optionally the port as command line arguments.

### IDE Configuration

#### Dependencies
Dependencies should be checked in, so you shouldn't have to download anything extra. This isn't the greatest thing to do, but we need a dependency management system before we can get rid of this.

#### Eclipse

These instruction assume you are using Eclipse Mars (4.5.1).
    
Open Eclipse and choose `File -> New Project`. Enter the name of this repository exactly as the name of the project. If done correctly, Eclipse will realize this folder already exists and will configure automatically.

Then, right click on the `lib/artclientlib-2.6.0.jar` file and choose `Build Path -> Add to Build Path`. At this point, the project should build correctly with no errors.

Finally, to attach the source, once again right-click the jar file and choose `Build Path -> Configure Build Path`. Navigate to the `Library` tab. Locate `artclientlib-2.6.0.jar` in the list and click the triangle to see more details. Click the `Source Attachment` entry and then click the `Edit` button on the right. Now point to the folder containing the source files. You should now be able to click on library classes such as `ShipSystem` and see their source.


#### IntelliJ IDEA
These instructions assume you are using IntelliJ IDEA 14.1.4.

1. Select `Import Project`.
1. Select the root directory of your clone.
1. Select `Create project from existing sources`.
1. Continue selecting defaults until the project loads.
1. In the project window's tree, locate the lib directory under `ArtemisEngineeringClient`.
1. Right click on artclientlib-2.6.0.jar, select `Add as source...`, and select `Build -> Rebuild Project`.
	- You should now have no compilation errors.
1. Right click on `ArtemisEngineeringClient`, select `Open module settings...`, and navigate to `Project Settings -> Libraries`
1. Select `artclientlib-2.6.0`.  Select the `+` in the bottom of the UI (not the one at the top to add a new library) and select the `lib/ArtClientLib-2.6.0` directory.
	- You should now be able to navigate directly to the ArtClientLib source code from within your source files and see no compilation errors in the `ArtClientLib` directory.
1. In the project window's tree, right click on `lib` and select `Mark Directory As -> Excluded`.  This will cause any compilation errors in ArtClientLib to go away (you only need that directory for code navigation, not compilation).
	- If you do wish to actually compile (or run the tests) for ArtClientLib, you'll probably have to do something like this: select the `+` at the top of the UI (to add a new library), select `Java`, and select `ArtemisEngineeringClient/lib/ArtClientLib-2.6.0/junitlib/junit-4.11.jar`.  Do the same for `hamcrest-core-1.3.jar` as well.
	

### 3D DAMCON Models
WIP...

#### Creating OBJ models JOGL will like
1. Open the relevant model files up in DelEd (free old game model editing tool)
1. Export them as OBJ files.
1. Open in Blender.
1. Export as OBJ (again).
1. Copy OBJ and MTL file to `art/models` directory.
1. ...

#### Misc.
- http://www.java-gaming.org/index.php?topic=27429.0
- http://www.java-gaming.org/index.php?topic=27061.0
- https://github.com/Danny02/DarwinsBox/tree/master/Geometrie/src/main/java/darwin/geometrie/io

#### Picking Code Links
- https://www.opengl.org/archives/resources/faq/technical/selection.htm
- http://www.java-tips.org/other-api-tips-100035/112-jogl/1674-another-picking-example-in-jogl.html
- http://stackoverflow.com/questions/4485821/jogl-picking-example

#### JOGL Links
- http://forum.jogamp.org/Loading-and-drawing-obj-models-td2708428.html
- http://forum.jogamp.org/Any-3d-model-loader-for-jogl-td4031409.html
- http://stackoverflow.com/questions/3686942/3ds-with-jogl?rq=1 (JoglUtils)
- http://stackoverflow.com/questions/3691935/load-a-obj-file-with-java3d-and-use-it-in-jogl
- https://jogamp.org/wiki/index.php/Setting_up_a_JogAmp_project_in_your_favorite_IDE
- https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL
- https://jogamp.org/wiki/index.php/Using_JOGL_in_AWT_SWT_and_Swing#JOGL_in_Swing
- http://jogamp.org/jogl/doc/HowToBuild.html
- https://jogamp.org/wiki/index.php/Jogl_Tutorial
- https://github.com/elect86/jogl-samples/tree/master/jogl-samples/src/helloTriangle

#### Ardor3D
- https://github.com/Renanse/Ardor3D
- https://github.com/Renanse/Ardor3D/blob/master/ardor3d-extras/src/main/java/com/ardor3d/extension/model/obj/ObjImporter.java
- https://github.com/Renanse/Ardor3D/blob/master/ardor3d-examples/src/main/java/com/ardor3d/example/ExampleBase.java
- https://github.com/Renanse/Ardor3D/blob/master/ardor3d-examples/src/main/java/com/ardor3d/example/pipeline/SimpleObjExample.java

#### LWJGL
- http://wiki.lwjgl.org/index.php?title=Using_a_Resizeable_AWT_Frame_with_LWJGL
- http://forum.lwjgl.org/index.php?topic=4275.0
- https://github.com/OskarVeerhoek/YouTube-tutorials
- https://www.youtube.com/watch?v=izKAvSV3qk0&list=PL19F2453814E0E315&index=24
- https://github.com/OskarVeerhoek/YouTube-tutorials/blob/master/src/episode_24/ModelDemo.java