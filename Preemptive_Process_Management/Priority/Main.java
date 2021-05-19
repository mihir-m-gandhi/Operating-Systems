import java.util.*;
import java.util.List;
import java.util.ArrayList;

class Main extends Thread{

  public static ArrayList<Process> list = new ArrayList<Process>();
  public static ArrayList<Process> q = new ArrayList<Process>();
  public static ArrayList<Process> done = new ArrayList<Process>();
  public static int time=0;
  public static int count=0;
  public static int total=-10;
  public static int ts=1;

  public static void main(String[] args) {
  
      NewProcess object = new NewProcess();
      object.start();
      
     System.out.println("\nProcess \tArrival Time \tBurst Time \tPriority \tTime Left");

    while(q.size()!=0||count<10)
    {
      int temp=0;

      if(q.size()!=0)
      {
        if(count==1&&total<0)
        {
          total = 0;
        }
        print();
        if(q.get(0).timeLeft==ts)
        {        
          temp = ts*1000;
          total = total + ts;
          q.get(0).timeLeft = 0;
          q.get(0).totalTime = total;
          remove(q.get(0).name);
        }
        else
        {
          temp = ts*1000;
          total = total + ts;
          q.get(0).timeLeft = q.get(0).timeLeft - ts;
        }
      }
      else
      {
         temp = ts*1000;
         temp = temp+5;
         total = total + ts;
      }

      try
      {
        Thread.sleep(temp);
      }
      catch(InterruptedException e)
      {
        System.out.println("main thread interrupted");
      } 
    }

    for(int i=0;i<done.size();i++)
    {
        done.get(i).waitTime = done.get(i).totalTime - done.get(i).arrivalTime - done.get(i).burstTime;
        done.get(i).turnaroundTime = done.get(i).waitTime + done.get(i).burstTime;
    }

 System.out.println("\nDONE\nProcess \tArrival Time \tBurst Time \tPriority \tWait Time \tTurnaround Time"); 
    for(int i=0;i<done.size();i++)
    {
        System.out.println(done.get(i).name+"\t\t\t"+done.get(i).arrivalTime+"\t\t\t\t"+done.get(i).burstTime+"\t\t\t"+done.get(i).priority+"\t\t\t"+done.get(i).waitTime+"\t\t\t"+done.get(i).turnaroundTime);
    }

    float avgWaitTime = 0;
    float avgTurnaroundTime = 0;
    int totalTime = done.get(done.size()-1).arrivalTime + done.get(done.size()-1).burstTime + done.get(done.size()-1).waitTime;
    for(int i=0;i<done.size();i++)
    {
      avgWaitTime += (float)done.get(i).waitTime;
      avgTurnaroundTime += (float)done.get(i).turnaroundTime;
    }
    System.out.println("\n\nAvg. Waiting Time = "+ avgWaitTime/done.size() + "\nAvg. Turnaround Time = "+ avgTurnaroundTime/done.size() + "\nTotal Time Elapsed = "+ totalTime);
    System.out.println("\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void print()
  {
    System.out.print("\n");
    for(int i=0;i<q.size();i++)
    {
        System.out.println(q.get(i).name+"\t\t\t"+q.get(i).arrivalTime+"\t\t\t\t"+q.get(i).burstTime+"\t\t\t"+ q.get(i).priority +"\t\t\t"+ q.get(i).timeLeft );
    }
  }

  public static void remove(String name)
  {
    done.add(q.get(0));
    q.remove(0);
  }
}

class NewProcess extends Thread
{
    Random r = new Random();
    public void run()
    {
        int a = r.nextInt(5) + 1;
        if(Main.time!=0)
        {
          Main.time = Main.time + a;
        }
        try
        {
          long x = a*1000;
          Thread.sleep(x);
        }
        catch(InterruptedException e)
        {
          System.out.println("main thread interrupted");
        } 
        Main.list.add(new Process(Integer.toString(Main.count+1),Main.time,r.nextInt(5)+1,r.nextInt(5)+1));
        Main.list.get(Main.list.size()-1).timeLeft =  Main.list.get(Main.list.size()-1).burstTime;
        Main.count++;
        Main.q.add(Main.list.get(Main.list.size()-1));
        Main.q.sort(Comparator.comparingInt(Process::getPriority));
        if(Main.time==0)
        {
          Main.time++;
        }
        if(Main.count<10)
        {
            NewProcess object = new NewProcess();
            object.start();  
        } 
    }
}

class Process
{
  String name;
  int arrivalTime;
  int burstTime;
  int timeLeft;
  int waitTime;
  int turnaroundTime;
  int priority;
  int totalTime;
  Process(String name, int arrivalTime, int burstTime, int priority)
  {
    this.name = name;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
    this.priority = priority;
  }
  int getPriority()
  {
    return this.priority;
  }
}
