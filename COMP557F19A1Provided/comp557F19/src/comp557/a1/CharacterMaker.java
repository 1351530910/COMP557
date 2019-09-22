package comp557.a1;

import javax.vecmath.Vector3d;


public class CharacterMaker {

	static public String name = "dude - Chen He 260743776";
	
	/** 
	 * Creates a character.
	 * @return root DAGNode
	 */
	
	static public GraphNode create() {
		
		


		// TODO: use for testing, and ultimately for creating a character​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌
		// Here we just return null, which will not be very interesting, so write
		// some code to create a charcter and return the root node.
		return CharacterFromXML.load("a1data/test.xml");
	}
}
