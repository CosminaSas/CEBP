package common;

import java.util.Random;

public class ChanceGenerator {


	public static boolean getChance(double what,double in){
		Random r = new Random();
		return (r.nextDouble()*in) <= what;
	}
	
}
