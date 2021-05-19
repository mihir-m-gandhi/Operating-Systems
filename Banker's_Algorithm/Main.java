import java.util.*;

class Main {
  public static int m,n;
  public static int total[], claim[][], allocated[][], need[][], available[];
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("\nEnter the no. of resources: ");
    n = sc.nextInt();
    System.out.print("\nEnter the maximum instances of each resource: ");
    total = new int[n];
    for(int i=0;i<n; i++){
      total[i] = sc.nextInt();
    }
    System.out.print("\nEnter the no. of processes: ");
    m = sc.nextInt();
    System.out.println("\nEnter the maximum instances of each resource a process may require: ");
    claim = new int[m][n];
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        claim[i][j] = sc.nextInt();
      }
    }
    System.out.println("\nEnter the instances of each resource allocated to the processes: ");
    allocated = new int[m][n];
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        allocated[i][j] = sc.nextInt();
      }
    }
    need = new int[m][n];
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        need[i][j] = claim[i][j] - allocated[i][j];
      }
    }
    available = new int[n];
    for(int i=0;i<n;i++){
      available[i] = total[i];
      for(int j=0;j<m;j++){
        available[i] = available[i] - allocated[j][i];
      }
    }
    System.out.println("\nTotal: ");
    for(int i=0;i<n;i++){
      System.out.print(total[i]+" ");
    }
    System.out.println("\n\nClaim: ");
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        System.out.print(claim[i][j]+" ");
      }
      System.out.print("\n");
    }
    print();
    int op;
    do{
      System.out.println("\n\n1. Alter Allocation matrix \n2. Check if the current state is safe or not \n3. Display the current state of system \n4. Exit \nEnter your choice: ");
      op = sc.nextInt();
      if(op==1){
        alter();
      }
      else if(op==2){
        int t = check();
      }
      else if(op==3){
        print();
      }
    }while(op!=4);
    sc.close();
    System.out.println("\nProgram by Mihir Gandhi B1 1611077");
  }

  public static void alter(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the process no.: ");
    int x = sc.nextInt();
    System.out.println("Enter the additional resources to be allocated to process "+x+": ");
    int flag=0;
    int temp[] = new int[n];
    for(int i=0;i<n;i++){
      temp[i] = sc.nextInt();
      if(temp[i]+allocated[x][i]>claim[x][i]){
        flag=1;
      }
    }
    if(flag==1){
      System.out.print("Invalid request, makes allocation greater than claim");
    }
    else{
    for(int i=0;i<n;i++){
      allocated[x][i] = allocated[x][i] + temp[i];
      need[x][i] = claim[x][i] - allocated[x][i];
      available[i] = available[i] - temp[i];
    }
    int t = check();
    if(t==1){
      System.out.println("Thus, we can grant the request for process "+x);
      print();
    }
    else{
      System.out.println("Thus, we cannot grant the request for process "+x);
      for(int i=0;i<n;i++){
        allocated[x][i] = allocated[x][i] - temp[i];
        need[x][i] = claim[x][i] - allocated[x][i];
        available[i] = available[i] + temp[i];
      } 
      print();    
    }
    }
  }

  public static int check(){
    ArrayList<Integer> sequence = new ArrayList<Integer>();
    int mark[] = new int[m];
    int tempavailable[] = new int[n];
    int tempallocated[][] = new int[m][n];
    int tempneed[][] = new int[m][n];
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        tempallocated[i][j] = allocated[i][j];
        tempneed[i][j] = need[i][j];
      }
    }
    for(int i=0;i<n;i++){
      tempavailable[i] = available[i];
    }
    for(int k=0;k<m;k++){
      int z=-1;
      int flag=0;
      for(int i=0;i<m;i++){
      flag=0;
      if(mark[i]==1){
        continue;
      }
      for(int j=0;j<n;j++){
        if(tempneed[i][j]>tempavailable[j]){
          flag=1;
          break;
        }
      }
      if(flag==0){
        z=i;
        break;
      }
      }
      if(z!=-1){
      mark[z] = 1;
      sequence.add(z);
      for(int j=0;j<n;j++){
        tempavailable[j] = tempavailable[j] + tempallocated[z][j]; 		//modification here, subtracted need here previously but that was not necessary
        tempallocated[z][j] = 0;
        tempneed[z][j] = 0;
      }
    }
    } 
    int flag = 0;
    for(int i=0;i<m;i++){
      if(mark[i]==0){
        flag=1;
      }
    }
    if(flag==0){
      System.out.println("\nSystem is in SAFE state");
      System.out.print("Possible sequence of execution of processes: ");
      for(int i=0;i<sequence.size();i++){
        System.out.print(sequence.get(i)+" ");
      }
      System.out.print("\n");
      return 1;
    }
    else{
      System.out.println("\nSystem is in UNSAFE state");
      System.out.print("Processes that are deadlocked and cannot be executed are: ");
      for(int i=0;i<m;i++){
        if(mark[i]==0){
          System.out.print(i+" ");
        }
      }
      System.out.print("\n");
      return 0;
    }
  }

  public static void print(){
    System.out.println("\nAllocated: ");
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        System.out.print(allocated[i][j]+" ");
      }
      System.out.print("\n");
    }
    System.out.println("\nNeed: ");
    for(int i=0;i<m;i++){
      for(int j=0;j<n;j++){
        System.out.print(need[i][j]+" ");
      }
      System.out.print("\n");
    }
    System.out.println("\nAvailable: ");
    for(int i=0;i<n;i++){
      System.out.print(available[i]+" ");
    }
  }
}
