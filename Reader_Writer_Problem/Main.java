import java.io.*;
import java.util.Random;

public class Main extends Thread {
	
	public static int count = 0;
	
	public static void main(String[] args)
	{
		Person1 obj = new Person1();
		obj.start();
		Person2 obj1 = new Person2();
		obj1.start();
	}
	
    public static void read() {
		
		if(Semaphore.readCheck())
		{
			Semaphore.readFlag++;
			// The name of the file to open.
			String fileName = "test.txt";

			// This will reference one line at a time
			String line = null;

			try {
				// FileReader reads text files in the default encoding.
				FileReader fileReader = 
					new FileReader(fileName);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = 
					new BufferedReader(fileReader);

					System.out.print("\n");
				while((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
				}   

				// Always close files.
				bufferedReader.close();         
			}
			catch(FileNotFoundException ex) {
				System.out.println(
					"Unable to open file '" + 
					fileName + "'");                
			}
			catch(IOException ex) {
				System.out.println(
					"Error reading file '" 
					+ fileName + "'");                  
				// Or we could just do this: 
				// ex.printStackTrace();
			}
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println("\nSystem interrupted");
			}
			Semaphore.readFlag--;
		}
		else
		{
			System.out.println("File busy, read failed");
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println("\nSystem interrupted");
			}
		}
        
    }
	
	public static void write(String str) {
		
		if(Semaphore.writeCheck())
		{
			Semaphore.waitt();
			// The name of the file to open.
			String fileName = "test.txt";

			try {
				// Assume default encoding.
				FileWriter fileWriter =
					new FileWriter(fileName, true);

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter =
					new BufferedWriter(fileWriter);

				// Note that write() does not automatically
				// append a newline character.
				bufferedWriter.append(str);
				bufferedWriter.newLine();

				// Always close files.
				bufferedWriter.close();
			}
			catch(IOException ex) {
				System.out.println(
					"Error writing to file '"
					+ fileName + "'");
				// Or we could just do this:
				// ex.printStackTrace();
			}
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println("\nSystem interrupted");
			}
			System.out.println("Write successful");
			Semaphore.signal();
		}
		else
		{
			System.out.println("File busy, write failed");
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println("\nSystem interrupted");
			}
		}
        
    }
}

class Semaphore
{
  public static int readFlag;
  public static int writeFlag;

  public static void waitt()
  {
    readFlag=1;
	writeFlag=1;
  }
  public static void signal()
  {
    readFlag=0;
	writeFlag=0;
  }
  public static boolean readCheck()
  {
    if(writeFlag==0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  public static boolean writeCheck()
  {
    if(readFlag==0 && writeFlag==0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}

class Person1 extends Thread{
	public void run()
	{
		Main.count++;
		Random r = new Random();
		int i = r.nextInt(10);
		if(i%2==0)
		{
			System.out.println("\nP1 read");
			Main.read();
		}
		else
		{
			System.out.println("\nP1 write");
			Main.write("MG from PROCESS 1");
		}
		try
		{
			Thread.sleep((long)r.nextInt(5)*1000);
		}
		catch(Exception e)
		{
			System.out.println("\nSystem interrupted");
		}
		if(Main.count<20)
		{
			Person1 obj = new Person1();
			obj.start();
		}
	}
}

class Person2 extends Thread{
	public void run()
	{
		Main.count++;
		Random r = new Random();
		int i = r.nextInt(10);
		if(i%2==0)
		{
			System.out.println("\nP2 read");
			Main.read();
		}
		else
		{
			System.out.println("\nP2 Write");
			Main.write("MG from PROCESS 2");
		}
		try
		{
			Thread.sleep((long)r.nextInt(5)*1000);
		}
		catch(Exception e)
		{
			System.out.println("\nSystem interrupted");
		}
		if(Main.count<20)
		{
			Person2 obj = new Person2();
			obj.start();
		}
	}
}

