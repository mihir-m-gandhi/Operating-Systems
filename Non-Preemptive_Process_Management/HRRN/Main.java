import java.util.*;
import java.util.List;
import java.util.ArrayList;

class Main extends Thread{

  public static ArrayList<Process> list = new ArrayList<Process>();
  public static ArrayList<Process> q = new ArrayList<Process>();
  public static ArrayList<Process> done = new ArrayList<Process>();
  public static int time=0;
  public static int count=0;
  public static int total=0;
  
  public static void main(String[] args) {
    
      NewProcess object = new NewProcess();
      object.start();
    
     System.out.println("\nProcess \tArrival Time \tBurst Time \tResponse Ratio");

    while(count<10||q.size()!=0)
    {
      int temp=0;
      if(q.size()!=0)
      {
        int t=0;
        if(done.size()!=0)
        {
          t = done.get(done.size()-1).arrivalTime + done.get(done.size()-1).burstTime + done.get(done.size()-1).waitTime;
        }
        for(int k=0;k<q.size();k++)
        {
          q.get(k).hrrn = ((float)((t-q.get(k).arrivalTime)+q.get(k).burstTime)/((float)q.get(k).burstTime));  
        }
        int n = q.size();
        for (int i=1; i<n; ++i)
        {
            Process key = q.get(i);
            int j = i-1;
            while (j>=0 && q.get(j).hrrn < key.hrrn)
            {
                q.set(j+1,q.get(j));
                j = j-1;
            }
            q.set(j+1,key);
        }
        print();
        temp = q.get(0).burstTime*1000;
        remove(q.get(0).name);
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

 System.out.println("\nDONE\nProcess \tArrival Time \tBurst Time \tWait Time \tTurnaround Time"); 
    for(int i=0;i<done.size();i++)
    {
        System.out.println(done.get(i).name+"\t\t"+done.get(i).arrivalTime+"\t\t"+done.get(i).burstTime+"\t\t"+done.get(i).waitTime+"\t\t"+done.get(i).turnaroundTime);
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
        System.out.println(q.get(i).name+"\t\t"+q.get(i).arrivalTime+"\t\t"+q.get(i).burstTime+"\t\t"+q.get(i).hrrn);
    }
  }

  public static void remove(String name)
  {
    done.add(q.get(0));
    if(Main.done.size()==1)
        {
          Main.done.get(Main.done.size()-1).waitTime = 0;
          Main.done.get(Main.done.size()-1).turnaroundTime = Main.done.get(Main.done.size()-1).burstTime;
        }
        else
        {
          
          if(Main.total - Main.done.get(Main.done.size()-1).arrivalTime<0)
          {
            System.out.println("System idle from "+ Main.total+ " to "+done.get(done.size()-1).arrivalTime);
            Main.done.get(Main.done.size()-1).waitTime = 0;
          }
          else
          {
            Main.done.get(Main.done.size()-1).waitTime = Main.total - Main.done.get(Main.done.size()-1).arrivalTime;
          }
          Main.done.get(Main.done.size()-1).turnaroundTime = Main.done.get(Main.done.size()-1).waitTime + Main.done.get(Main.done.size()-1).burstTime;
        }
        Main.total = Main.done.get(Main.done.size()-1).burstTime + Main.done.get(Main.done.size()-1).arrivalTime + Main.done.get(Main.done.size()-1).waitTime; 
    q.remove(0);
  }
}

class NewProcess extends Thread
{
    Random r = new Random();
    public void run()
    {
        Main msr = new Main();
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
        Main.list.add(new Process(Integer.toString(Main.count+1),Main.time,r.nextInt(10)+1));
        Main.count++;
        Main.q.add(Main.list.get(Main.list.size()-1));
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
  int priority;
  int waitTime;
  int turnaroundTime;
  float hrrn;
  
  Process(String name, int arrivalTime, int burstTime)
  {
    this.name = name;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
  }
  
  float getHrrn()
  {
    return this.hrrn;
  }
}
