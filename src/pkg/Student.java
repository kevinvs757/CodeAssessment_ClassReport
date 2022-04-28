package pkg;

public class Student {

	private String Name;
	private double Grade;
	
	Student(String name, String grade) 
	{
		setName(name.trim());
		setGrade(Double.parseDouble(grade.trim()));
	}
	
	public String getName()  { return Name; }

	public void setName(String name)  { Name = name; }

	public double getGrade()  { return Grade; }

	public void setGrade(double grade)  { Grade = grade; }

}
