package test.refactor;

import org.eclipse.jface.text.Position;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

       String txt = "This is fsomething dfdfodmfdfd fd f df d f dfdfdf" ;
          int size = txt.length();
           int nuBlocks =0;
           if(size %5 ==0) nuBlocks = size/5;
           else nuBlocks = size/5 +1;
           System.out.println("size" + size +"bloc" + nuBlocks);
           
           
		
			}
	
}
