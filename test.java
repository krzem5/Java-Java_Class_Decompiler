import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Arrays;



public class test extends BufferedImage implements Test_i{
	private static String[][][] hdj=new String[5][0][];
	private final transient Field f=null;



	public static void main(String[] args){
		new test(args,0,-1,null,null,null);
	}



	public test(String[] args,final int k,long fjdjf,Thread[]... thrg){
		super(0,0,0);
		System.out.println(Arrays.toString(new int[]{0,6}));
		int it=5983;
		long lg=10L;
		float ft=0.1f;
		double db=1.55e-3;
		test[] al=new test[39];
		int[] ilt=new int[48];
		test.hdj=new String[5][it][];
		synchronized (this){
			outer:
			for (int i = 2; i < 1000; i++) {
				for (int j = 2; j < i; j++) {
					if (i % j == 0)
						continue outer;
				}
				System.out.printf("Hello, %d %s!\n",it+i,hdj);
			}
		}
		switch (it){
			case 0:
			case 1:
				System.out.println("HEy!");
				break;
			case 3:
				System.err.printf("%,d",lg*100000L);
			default:
				System.out.println("DEF!");
				break;
		}
	}



	public String[][] method1() throws NoSuchMethodError{
		System.out.println("hey");
		return null;
	}



	private class inner extends Object{
		public inner(){

		}
	}
}



interface Test_i{
	public static final int kldnrhdufidjsmhdrbsnidmdjeok=33334;



	public String[][] method1() throws NoSuchMethodError;
}