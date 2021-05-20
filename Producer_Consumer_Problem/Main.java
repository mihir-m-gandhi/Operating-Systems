import java.util.*;

public class Main extends Thread
{
  public static int storage;
  public static int count=0;
  public static int c=15;
  public static int max = 5;
  public static int prod_time = 5;
  public static int cons_time = 5;
  public static int process_time = 1;
  public static int waitt_time = 2;
  public static ArrayList<Integer> readyQ = new ArrayList<Integer>();

  public static void main(String[] args)
  {
    System.out.println("\nIn the queue, 0 represents a producer call and 1 represents a consumer call. ");
    System.out.print("\nEnter the size of buffer: ");
    Scanner sc = new Scanner(System.in);
    max = sc.nextInt();
    System.out.print("\n");

    Produce pro = new Produce();
    pro.start();
    count++;
    
    Request req = new Request();
    req.start();
    count++;

    long t = (prod_time+cons_time+waitt_time+process_time+30)*1000;
    try{
      sleep(t);
    }
    catch(Exception e)
    {
      System.out.println("Thread interrupted");
    }

    while(readyQ.size()!=0)
    {
      queueRoutine();
    }
    System.out.println("\n\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void request()
  {
    Random r = new Random();
    long a;
    if(Main.storage==0)
          {
             System.out.println("No item available");
          }
          else
          {
            Main.storage--;
            System.out.println("Item removed \nTotal no. of items in storage is: "+Main.storage+"\n");
          }
          try
          {
            Thread.sleep(Main.process_time*1000);
          }
          catch(Exception e)
          {
            System.out.println("\nSystem interrupted");
          }
          Semaphore.semSignal();
  }

  public static void produce()
  {
    Random r = new Random();
    long a;
    if(Main.storage==max)
          {
             System.out.println("Storage full");
          }
          else
          {
            Main.storage++;
            System.out.println("Item added \nTotal no. of items in storage is: "+Main.storage+"\n");
          }
          try
          {
            Thread.sleep(Main.process_time*1000);
          }
          catch(Exception e)
          {
            System.out.println("\nSystem interrupted");
          }
          Semaphore.semSignal();       
  }

  public static void queueRoutine()
  {
    System.out.print("Queue is: ");
    for(int i=0;i<Main.readyQ.size();i++)
    {
      System.out.print(Main.readyQ.get(i)+" ");
    }
     System.out.println("");
    
    if(readyQ.get(0)==0)
    {
      produce();
    }
    else
    {
      request();
    }
    Main.readyQ.remove(0);
  }
}

class Semaphore
{
  public static int flag;
  public static void semWait(int x)
  {
    flag=1;
  }
  public static void semSignal()
  {
    flag=0;
  }
  public static boolean check()
  {
    if(flag==0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}

class Produce extends Thread
  {
    Random r = new Random();
    public void run()
    {
      System.out.println("\nPRODUCER CALL");
      int flag2=0;
      System.out.println("");
        long a;
        if(Semaphore.check())
        {
          Semaphore.semWait(0);
          if(Main.readyQ.size()==0)
          {
            Main.produce();
          }
          else
          {
            Main.readyQ.add(0);
            System.out.println("Adding this call to queue and processing the 1st call present in queue");
            Main.queueRoutine();
          }   
          Semaphore.semSignal();     
        }
        else
        {
          System.out.println("System busy, couldn't add item");
          Main.readyQ.add(0);
          try
          {
            Thread.sleep(Main.waitt_time*1000);
          }
          catch(Exception e)
          {
            System.out.println("\nSystem interrupted");
          }
        }
         a = (r.nextInt(Main.prod_time)+1)*1000;
          try
          {
            Thread.sleep(a);
          }
          catch(Exception e)
          {
            System.out.println("System interrupted");
          }
         if(Main.count<Main.c)
          {
            Produce pro = new Produce();
            pro.start();
            Main.count++;
          }
    }
  }

class Request extends Thread
  {
    Random r = new Random();
    public void run()
    {
      System.out.println("\nCONSUMER CALL");
      int flag2=0;
      System.out.println("");
        long a;
        if(Semaphore.check())
        {
          if(Main.readyQ.size()==0)
          {
            Main.request();
          }
          else
          {
            Main.readyQ.add(1);
            System.out.println("Adding this call to queue and processing the 1st call present in queue");
            Main.queueRoutine();
          }   
          Semaphore.semSignal();          
        }
        else
        {
          System.out.println("System busy, couldn't remove item");
          Main.readyQ.add(1);
          try
          {
            Thread.sleep(Main.waitt_time*1000);
          }
          catch(Exception e)
          {
            System.out.println("\nSystem interrupted");
          }
        }
          a = (r.nextInt(Main.cons_time)+1)*1000;
          try
          {
            Thread.sleep(a);
          }
          catch(Exception e)
          {
            System.out.println("System interrupted");
          }
         if(Main.count<Main.c)
          {
            Request req = new Request();
            req.start();
            Main.count++;
          }
    }
  }
