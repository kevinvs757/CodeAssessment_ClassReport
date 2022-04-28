package pkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App 
{
	private static Path InputDirectory;
	private static ArrayList<Classroom> Classrooms = new ArrayList<Classroom>();
	
	App(Path anInputDir)
	{
		InputDirectory = anInputDir;
	}
	
	public static void main(String[] args) 
	{
		App app = handleArgs(args);
		app.loadClasses();
		app.generateFullReport();		
	}
	
	private static App handleArgs(String[] args)
	{
		if(args == null || args.length != 1)
		{
			System.err.println("Incorrect number of arguments. Should be one argument: the input folder containing .csv class data");
			System.exit(0);
		}
		
		try 
		{
			Path inputPath = Paths.get(args[0]);
			App app = new App(inputPath);
			return app;
		} 
		catch (Exception e) 
		{
			System.err.println("Unable to use argument as a path; is it a valid directory?");
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	private void loadClasses()
	{
		try(DirectoryStream<Path> csvStream = Files.newDirectoryStream(InputDirectory, "*.csv"))
		{
			String delim = ",";
			for(Path csv : csvStream)
			{
				Classroom classroom = new Classroom(csv.getFileName().toString().split("\\.")[0]);
				BufferedReader br = Files.newBufferedReader(csv);
				String line;
				boolean firstLine = true;
				while((line = br.readLine()) != null)
				{
					String[] splitLine = line.split(delim);
					if(firstLine) 
					{
						if(! splitLine[0].trim().equals("Student Name")
								&& ! splitLine[1].trim().equals("Grade"))
						{
							System.err.println("Malformed header in " + csv.getFileName());
							break; // breaking while loop, skipping this file
						}
						firstLine = false;
					}
					else
					{
						Student student = splitLine.length > 1 
								? new Student(splitLine[0], splitLine[1])
								: new Student(splitLine[0], "0");
						classroom.addStudent(student);
					}
				}
				Classrooms.add(classroom);
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Unable to stream files in this directory");
			e.printStackTrace();
		}
	}
	
	private void generateFullReport()
	{
		StringWriter sw = new StringWriter();
		double highestAvg = 0;
		Classroom highestAvgClass = null;
		for(Classroom classroom : Classrooms)
		{
			generateClassroomReport(classroom, sw);
			if(classroom.getNonzeroClassAverage() > highestAvg)
			{
				highestAvg = classroom.getNonzeroClassAverage();
				highestAvgClass = classroom;
			}
		}
		sw.append("=================================================" + System.lineSeparator());
		sw.append("The highest average was in " 
				+ highestAvgClass.getLabel() 
				+ " with an average of " 
				+ String.format("%.2f", highestAvg));

		//System.out.println(sw.toString());
		try 
		{
			// Used the web to print this in one line. Still have FileWriter bouncing around in my head.
			// https://fullstackdeveloper.guru/2020/06/11/how-to-read-and-write-to-files-easily-with-java-nio-file-package/
			Files.writeString(Path.of("report.txt"), sw.toString());
		} 
		catch (IOException e) 
		{
			System.err.println("Unable to write the report.txt output file to " + Paths.get("").toAbsolutePath());
			e.printStackTrace();
		}
	}

	private void generateClassroomReport(Classroom classroom, StringWriter sw)
	{
		sw.append("--------------------- " + classroom.getLabel() + " ---------------------" + System.lineSeparator());
		sw.append("Total Number of Students: " 
				+ classroom.getClassSize() 
				+ System.lineSeparator());
		sw.append("Number of Students for Calculating Class Average: " 
				// not sure why we wanted 2 digits on what will always be an integer, but here we are
				+ String.format("%.2f", (double) classroom.getNonzeroGradeStudents().size()) 
				+ System.lineSeparator());
		sw.append("Class Average: " 
				+ String.format("%.2f", classroom.getNonzeroClassAverage())
				+ System.lineSeparator());
		
		if(classroom.getZeroGradeStudents().size() > 0) 
		{
			sw.append("Students Missing Grades:" + System.lineSeparator());
			for(Student student : classroom.getZeroGradeStudents())
			{
				sw.append("            " + student.getName() + System.lineSeparator());
			}
		}
	}
}
