import java.util.*;

class Main extends Thread{
  public static int count=1;
  public static int max=15;
  public static int next=0;
  public static int processTime = 3;
  public static int totalTracks = 200;
  public static int head = 0;
  public static ArrayList<Job> queue = new ArrayList<Job>();
  public static ArrayList<Job> result = new ArrayList<Job>();
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the total no. of tracks: ");
    totalTracks = sc.nextInt();
    Random r = new Random();
    head = r.nextInt(totalTracks);
    int initialHead = head;
    System.out.println("Initial position of head is at track "+head);
    for(int i=0;i<5;i++){
      Job obj = new Job();
    }
    print();
    int flag2=0;
    for(int i=0;i<queue.size();i++){
      if(head<=queue.get(i).trackNo){
        next = i;
        flag2=1;
        break;
      }
    }
    if(flag2==0){
      next=queue.size()-1;
    }
    while(queue.size()!=0 || count<max){
      schedule();
    }
    int totalTracksAccessed=0;
    System.out.println("\n\nDONE\n\nInitial position of head was at track "+initialHead+"\n");
    System.out.println("Id \tTrack No. \tTracks Accessed");
    for(int i=0;i<result.size();i++){
      System.out.println(result.get(i).id + "\t" + result.get(i).trackNo + "\t\t\t" + result.get(i).tracksAccessed);
      totalTracksAccessed = totalTracksAccessed + result.get(i).tracksAccessed;
    }
    float avgSeekLength = totalTracksAccessed/count-1;
    System.out.println("\nAverage Seek Length is "+avgSeekLength);
    System.out.println("\n\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void schedule(){
    Random r = new Random();
    int flag2=0;
    for(int i=0;i<queue.size();i++){
      if(head<=queue.get(i).trackNo){
        next = i;
        flag2=1;
        break;
      }
    }
    if(flag2==0){
      next=queue.size()-1;
    }
    int left = totalTracks;
    if(next>0){
      left = Math.abs(queue.get(next-1).trackNo - head);
    }
    int right = Math.abs(queue.get(next).trackNo - head);
    if(left<right){
      next = next - 1;
    }
    Job j = queue.get(next);
    j.tracksAccessed = Math.abs(j.trackNo - head);
    head = j.trackNo;
    result.add(queue.get(next));
    queue.remove(next);
    if(count<max){
      int temp = r.nextInt(3)+1;
      for(int i=0;i<temp;i++){
        Job obj = new Job();
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
  int getTrackNo()
  {
    return this.trackNo;
  }
}