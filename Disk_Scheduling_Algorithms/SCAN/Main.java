import java.util.*;

class Main extends Thread{
  public static int count=1;
  public static int max=15;
  public static int processTime = 3;
  public static int totalTracks = 200;
  public static int head = 0;
  public static int initialHead = 0;
  public static int flag, flag3 = 0;
  public static int next;
  public static int dir = 1;
  public static ArrayList<Job> queue = new ArrayList<Job>();
  public static ArrayList<Job> result = new ArrayList<Job>();
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the total no. of tracks: ");
    totalTracks = sc.nextInt();
    Random r = new Random();
    head = r.nextInt(totalTracks);
    initialHead = head;
    System.out.println("Initial position of head is at track "+head);
    for(int i=0;i<10;i++){
      Job obj = new Job();
    }
    int flag2=0;
    print();
    for(int i=0;i<queue.size();i++){
      if(head<=queue.get(i).trackNo){
        next = i;
        flag2=1;
        break;
      }
    }
    if(flag2==0){
      next=queue.size()-1;
      flag=1;
    }
    while(queue.size()!=0){// || count<max){
      schedule();
    }
    int totalTracksAccessed=0;
    System.out.println("\n\nDONE\n\nInitial position of head was at track "+initialHead+"\n");
    System.out.println("Id \tTrack No. \tTracks Accessed");
    for(int i=0;i<result.size();i++){
      System.out.println(result.get(i).id + "\t" + result.get(i).trackNo + "\t\t\t" + result.get(i).tracksAccessed);
      totalTracksAccessed = totalTracksAccessed + result.get(i).tracksAccessed;
    }
    float avgSeekLength = (float)totalTracksAccessed/(float)(count-1);
    System.out.println("\nAverage Seek Length is "+avgSeekLength);
    System.out.println("\n\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void schedule(){
    int flag2=0;
    for(int i=0;i<queue.size();i++){
      if(head<=queue.get(i).trackNo){
        next = i;
        flag2=1;
        break;
      }
    }
    Random r = new Random();
    if(flag==1){
      Job j = queue.get(next);
      if(flag3==0){
        j.tracksAccessed = j.tracksAccessed + (totalTracks - head - 1);
        head = totalTracks - 1;
        flag3=1;
      }
      j.tracksAccessed = j.tracksAccessed + Math.abs(j.trackNo - head);
      head = j.trackNo;
      result.add(queue.get(next));
      queue.remove(next);
      next--;
    }
    if(flag==0){
      Job j = queue.get(next);
      j.tracksAccessed = Math.abs(j.trackNo - head);
      head = j.trackNo;
      result.add(queue.get(next));
      if(queue.size()-1==next){
        flag=1;
      }
      queue.remove(next);
      if(flag==1){
        next--;
      }
    }
    if(count<max){
      int temp = r.nextInt(3)+1;
      for(int i=0;i<temp;i++){
        Job obj = new Job(Main.head);
      }
    } 
    try{
      Thread.sleep(Main.processTime*1000);
    }
    catch(Exception e){
      System.out.println(e);
    }
    print();
  }

  public static void print(){
     System.out.println("\nNew position of head is at track "+head);
     System.out.println("QUEUE:");
     System.out.println("Id \tTrack No.");
     for(int i=0;i<queue.size();i++){
       System.out.println(queue.get(i).id + "\t" + queue.get(i).trackNo);
     }
  }
}

class Job{
  Random r = new Random();
  int id;
  int trackNo;
  int tracksAccessed;
  Job(){
    this.id = Main.count;
    this.trackNo = r.nextInt(Main.totalTracks);
    Main.count++;
    Main.queue.add(this);
    Main.queue.sort(Comparator.comparingInt(Job ::getTrackNo));
  }
  Job(int x){
    this.id = Main.count;
    if(Main.flag==0){
       this.trackNo = r.nextInt(x);
    }
    else{
      this.trackNo = r.nextInt(Main.totalTracks);
    }
    Main.count++;
    Main.queue.add(this);
    Main.next++;
    Main.queue.sort(Comparator.comparingInt(Job ::getTrackNo));
  }
  int getTrackNo()
  {
    return this.trackNo;
  }
}