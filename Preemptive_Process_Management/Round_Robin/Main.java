import java.util.*;
import java.util.List;
import java.util.ArrayList;

class Main extends Thread{

  public static ArrayList<Process> list = new ArrayList<Process>();
  public static ArrayList<Process> q = new ArrayList<Process>();
  public static ArrayList<Process> done = new ArrayList<Process>();
  public static int time=0;
  public static int count=0;
  public static int total=-100;
  public static int ts=1;
  public static int f = 0;
  public static int b = 5;

  public static void main(String[] args) {
    
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter the time slice: ");
      ts=sc.nextInt();

      NewProcess object = new NewProcess();
      object.start();
    
     System.out.println("\nProcess \tArrival Time \tBurst Time \tTime Left");

    while(q.size()!=0||count<10)
    {
      int temp=0;
      int flag=0;
      Process temp2=null;
      if(q.size()!=0)
      {
        /*if(count==1&&total<0)
        {
          total = 0;
          /*try
          {
            Thread.sleep(980);
          }
          catch(InterruptedException e)
          {
            System.out.println("main thread interrupted");
          } */
        //}
        print();
        if(q.get(0).timeLeft<=ts)
        {
          int a = q.get(0).timeLeft;
          temp = a*1000;
          total = total + a;
          q.get(0).timeLeft = 0;
          /*if(q.get(0).name.equalsIgnoreCase("1"))
          {
              q.get(0).totalTime = total-1; 
          }
          else
          {
              q.get(0).totalTime = total; 
          }*/
           q.get(0).totalTime = total; 
          done.add(q.get(0));
          q.remove(0);
        }
        else
        {
          temp = ts*1000;
          total = total + ts;
          flag=1;
          q.get(0).timeLeft = q.get(0).timeLeft - ts;
          temp2 = q.get(0);
          q.remove(0);
        }
      }
      else
      {
         temp = 1000;
         total = total + 1;
         //System.out.println("else");
      }
      System.out.print("\ntime= "+total);
      try
      {
        Thread.sleep(temp);
      }
      catch(InterruptedException e)
      {
        System.out.println("main thread interrupted");
      } 
      if(flag==1)
      {
        q.add(temp2);
      }
    }

done.sort(Comparator.comparingInt(Process::getArrivalTime));
 System.out.println("\nDONE\nProcess \tArrival Time \tBurst Time \tWait Time \tTurnaround Time"); 
    for(int i=0;i<done.size();i++)
    {
        done.get(i).turnaroundTime = done.get(i).totalTime - done.get(i).arrivalTime;
        done.get(i).waitTime = done.get(i).turnaroundTime - done.get(i).burstTime;
        System.out.println(done.get(i).name+"\t\t\t"+done.get(i).arrivalTime+"\t\t\t\t"+done.get(i).burstTime+"\t\t\t"+done.get(i).waitTime+"\t\t\t"+done.get(i).turnaroundTime);
    }

    float avgWaitTime = 0;
    float avgTurnaroundTime = 0;
    int totalTime = done.get(done.size()-1).arrivalTime + done.get(done.size()-1).burstTime + done.get(done.size()-1).waitTime;
    for(int i=0;i<done.size();i++)
    {
      avgWaitTime += (float)done.get(i).waitTime;
      avgTurnaroundTime += (float)done.get(i).turnaroundTime;
    }
    System.out.println("\n\nAvg. Waiting Time = "+ avgWaitTime/done.size() + "\nAvg. Turnaround Time = "+ avgTurnaroundTime/done.size() + "\nTotal Time Elapsed = "+ total);
    System.out.println("\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void print()
  {
    System.out.print("\n");
    for(int i=0;i<q.size();i++)
    {
        System.out.println(q.get(i).name+"\t\t\t"+q.get(i).arrivalTime+"\t\t\t\t"+q.get(i).burstTime+"\t\t\t"+ q.get(i).timeLeft);
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
        Main msr = new Main();
        int a = r.nextInt(Main.b) + 1;
        if(Main.time!=0)
        {
          Main.time = Main.time + a;
        }
        try
        {
          long x = a*1000;
          if(Main.count==1)
          {
            x=x+1000;
          }
          Thread.sleep(x);
        }
        catch(InterruptedException e)
        {
          System.out.println("main thread interrupted");
        } 
        Main.b = r.nextInt(10)+1;
        Main.list.add(new Process(Integer.toString(Main.count+1),Main.time, Main.b));
        Main.list.get(Main.list.size()-1).timeLeft = Main.list.get(Main.list.size()-1).burstTime;
        /*if(Main.count==0&&Main.total<1)
        {
          Main.total = 0;
        }*/
        /*if(Main.f==0&&Main.count==0&&Main.total<1)
        {
          Main.total = 0;
          Main.f=1;
        }*/
        Main.count++;
        if(Main.count==1)
        {
          Main.total = 0;
        }
        Main.q.add(Main.list.get(Main.list.size()-1));
        //System.out.print("Added "+ Main.count + " at time "+ Main.total);
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
  int waitTime;
  int turnaroundTime;
  int totalTime;
  int timeLeft;
  Process(String name, int arrivalTime, int burstTime)
  {
    this.name = name;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
  }
  int getArrivalTime()
  {
    return this.arrivalTime;
  }
}
