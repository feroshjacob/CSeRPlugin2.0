package test.refactor;

import java.util.HashSet;
import java.util.Set;

public class RelationshipTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RelationshipTest test = new RelationshipTest();	   Set<String> set1 = new HashSet<String>();
	   set1.add("1");
	   set1.add("2");
	   set1.add("4");
	   Set<String> set2 = new HashSet<String>();
	   set2.add("4");
	   set2.add("5");
	   set2.add("6");
	   Set<String> set3 = new HashSet<String>();
	   set3.add("9");
	   set3.add("5");
	   set3.add("23");
	   test.makeGroup("df", set1) ;
	   test.makeGroup("d", set2) ;
	   test.makeGroup("f", set3) ;
	   test.print();

	}
private void print(){
	for(Set<String> group  : groups){
		System.out.println("*");
		for(String st: group){
	 System.out.println(st);		
		}
	}
}
	
	private Set<Set<String>> groups = new HashSet<Set<String>>();
	private void makeGroup(String var, Set<String> methods) {
		Set<String> list = new HashSet<String>();
		Set<String> existingSet = new HashSet<String>();
		for(String str:methods){
			existingSet=checkString(str);
			if(existingSet!=null) {
				groups.remove(existingSet);
				list.addAll(existingSet);
			}

		list.add(str);
		}
		list.add(var);
		
		groups.add(list);
		
	}

	private Set<String> checkString(String str) {
	 for(Set<String> s : groups){
		 for(String st: s){
			 if(st.equals(str)){
				 return s;
			 }
		 }
	 }
	 return null;
		
	}

}
