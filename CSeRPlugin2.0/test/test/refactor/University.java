package test.refactor;



import java.util.ArrayList;
import java.util.List;


public class University {
	String name ="";
	String president_Name ="";
	String location="";
	 List<Student> students = new ArrayList<Student>();
	 
	public String getPresident_Name() {
	
		return president_Name;
	}
	public void setPresident_Name(String president_Name) {
		this.president_Name = president_Name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStsudents(List<Student> students) {
		this.students = students;
	}
	public void addStsudent(Student student) {
		this.students.add(student);
	}

}
