import java.util.*;

class Main extends Thread{
  public static int n;
  public static int total=0;
  public static ArrayList<Integer> queue = new ArrayList<Integer>();
  public static ArrayList<Integer> eating = new ArrayList<Integer>();  
  public static ArrayList<Philo> philoList = new ArrayList<Philo>();
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the no. of philosophers: ");
    n = sc.nextInt();
    for(int i=0; i<n; i++){
      Philo p = new Philo(i);
      philoList.add(p);
    }
    AssignChopstick ac = new AssignChopstick();
    ac.start();
    try{
      sleep(2000);
    }
    catch(Exception e){}
    AssignChopstick ac2 = new AssignChopstick();
    ac2.start();
  }

  static void queueRoutine(){
    Random r = new Random();
    Philo p = philoList.get(queue.get(0));
    while(true){
    if(Semaphore.check(p.id)){
      Semaphore.semWait(p.id);
      Main.philoList.get(queue.get(0)).status = 2;
      int x = p.id;
      int temp = Integer.valueOf(queue.get(0));
      Main.eating.add(x);
      queue.remove(0);
      System.out.println("Philo "+p.id+" eating with chopsticks "+ x + " and "+(x+1)%(Main.n));
      try{
        sleep((r.nextInt(5)+2)*1000);
      }
      catch(Exception e)
      {
        System.out.println("Thread Interrupted");
      }
      Semaphore.semSignal(p.id);
      Main.eating.remove(new Integer(x));
      Main.philoList.get(temp).status = 0;
      System.out.println("Philo "+p.id+" has given up chopsticks "+ x + " and "+(x+1)%(Main.n));
      break;
    }
   }
  }
}

class Semaphore{
  public static int flag[] = new int[Main.n];
  public static void semWait(int x){
    flag[x] = 1;
    flag[(x+1)%(Main.n)] = 1;
  }
  public static void semSignal(int x){
    flag[x] = 0;
    flag[(x+1)%(Main.n)] = 0;
  }
  public static boolean check(int x){
    if(flag[x] == 0 && flag[(x+1)%(Main.n)] == 0){
      return true;
    }
    else{
      return false;
    }
  }
}

class AssignChopstick extends Thread{
  Random r = new Random();
  public void run(){
    while(Main.total<15){
      Main.total++;
       System.out.print("\nEating: ");
      for(int k=0;k<Main.eating.size();k++){
        System.out.print(Main.eating.get(k)+" ");
      }
      System.out.print("\nQueue: ");
      for(int k=0;k<Main.queue.size();k++){
        System.out.print(Main.queue.get(k)+" ");
      }
      System.out.println("");
      int temp;
      do{
         temp = r.nextInt(Main.n);
      }while(Main.philoList.get(temp).status!=0);
      Philo p = Main.philoList.get(temp);
      Main.philoList.get(temp).status = 1;
      int flag2=0;
      if(temp==0){
        if(Main.philoList.get(temp+1).status==0&&Main.philoList.get(Main.n-1).status==0){
          flag2=1;
        }
      }
      else if(temp==Main.n-1){
        if(Main.philoList.get(temp-1).status==0&&Main.philoList.get(0).status==0){
          flag2=1;
        }
      }
      else{
        if(Main.philoList.get(temp-1).status==0 &&Main.philoList.get(temp+1).status==0){
          flag2=1;
        }
      }
      if(Main.queue.isEmpty()||flag2==1){
      if(Semaphore.check(p.id)){
        Semaphore.semWait(p.id);
        Main.philoList.get(temp).status = 2;
        int x = p.id;
        Main.eating.add(x);
        System.out.println("Philo "+p.id+" eating with chopsticks "+ x + " and "+(x+1)%(Main.n));
        try{
        sleep((r.nextInt(5)+2)*1000);
        }
        catch(Exception e)
        {
          System.out.println("Thread Interrupted");
        }
        Semaphore.semSignal(p.id);
        Main.eating.remove(new Integer(x));
        Main.philoList.get(temp).status = 0;
        System.out.println("Philo "+p.id+" has given up chopsticks "+ x + " and "+(x+1)%(Main.n));
      }
      else{
        System.out.println("Chopstick not available for philosopher "+p.id);
        Main.queue.add(p.id);
        try{
        sleep(2000);
        }
        catch(Exception e)
        {
          System.out.println("Thread Interrupted");
        }
      }
      }
      else{
        System.out.println("Chopstick not available for philosopher "+p.id);
        Main.queue.add(p.id);
        Main.queueRoutine();
        try{
        sleep(5000);
        }
        catch(Exception e)
        {
          System.out.println("Thread Interrupted");
        }
      }
    }
    while(Main.queue.size()!=0){
      Main.queueRoutine();
    }
    System.out.println("\nDONE");
    System.out.print("\nProgram by Mihir Gandhi B1 1611077\n");
    System.exit(0);
  }
}

class Philo{
  int id;
  int status; // 0:idle   1:hungry   2:eating
  Philo(int id){
    this.id = id;
    this.status = 0;
  }
}