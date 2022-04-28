package pkg;

import java.util.ArrayList;

public class Classroom 
{
	private final String Label;
	private ArrayList<Student> Students = new ArrayList<Student>();
	
	Classroom(String label)
	{
		Label = label;
	}
	
	public ArrayList<Student> getNonzeroGradeStudents()
	{
		ArrayList<Student> nonzeroGradeStudents = new ArrayList<Student>();
		for(Student student : Students)
		{
			if( ! (student.getGrade() == 0))
			{
				nonzeroGradeStudents.add(student);
			}
		}
		return nonzeroGradeStudents;
	}
	
	public ArrayList<Student> getZeroGradeStudents()
	{
		ArrayList<Student> zeroGradeStudents = new ArrayList<Student>();
		for(Student student : Students)
		{
			if(student.getGrade() == 0)
			{
				zeroGradeStudents.add(student);
			}
		}
		return zeroGradeStudents;
	}
	
	public double getNonzeroClassAverage()
	{
		double gradeSum = 0;
		double gradeAvg = 0;
		for(Student student : getNonzeroGradeStudents())
		{
			gradeSum += student.getGrade();
		}
		if(getNonzeroGradeStudents().size() > 0)
		{
			gradeAvg = gradeSum / getNonzeroGradeStudents().size();
		}
		return gradeAvg;
	}

	public void addStudent(Student student)
	{
		Students.add(student);
	}

	public String getLabel() 
	{
		return Label;
	}
	
	public int getClassSize()
	{
		return Students.size();
	}
}
