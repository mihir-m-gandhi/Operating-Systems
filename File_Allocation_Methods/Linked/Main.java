import java.util.*;

class Main extends Thread {
  public static int blocks[][];
  public static int total=5, count=1, currentBlock=0, blockSize, m, blocksUsed=0, flag=0;
  public static ArrayList<File> fileTable = new ArrayList<File>();
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the number of blocks: ");
    m = sc.nextInt();
    blocks = new int[m][2];
    for(int i=0;i<m;i++){
      blocks[i][0] = -1;
      blocks[i][1] = -1;
    }
    System.out.print("Enter the size of one block: ");
    blockSize = sc.nextInt();
    NewFile nf = new NewFile();
    nf.start();
    try{
      sleep(20000);
    }catch(Exception e){
      System.out.print("Thread Interrupted");
    }
    print();
    flag=1;
    int op;
    Random r = new Random();
    do{
      System.out.print("\n1. Modify/Delete \n2. Create new file \n3. Exit \nEnter your choice: ");
      op = sc.nextInt();
      if(op==1){
        System.out.print("Enter the file serial number: ");
        int fileNo = sc.nextInt();
        File f = fileTable.get(fileNo);
        System.out.print("Enter the new file size: ");
        int newFileSize = sc.nextInt();
        double prevNoOfBlocks = Math.ceil((double)f.size/(double)Main.blockSize);
        double noOfBlocks = Math.ceil((double)newFileSize/(double)Main.blockSize);
        if(newFileSize>f.size){
          int diff = (int)noOfBlocks-(int)prevNoOfBlocks;
          if(Main.m-Main.blocksUsed>=diff){
          f.size = newFileSize;
          System.out.print("\n"+f.fileName + " " + f.startBlock + " " + f.size);
          int temp;
          int prev=0;
          int start = f.startBlock;
          for(int k=0;k<prevNoOfBlocks-1;k++){
            prev = blocks[start][1];
            start = blocks[start][1];
          }
          System.out.println("prev is: "+prev);
          for(int i=0;i<(int)diff;i++){
            do{
              temp = r.nextInt(Main.m);
            }while(Main.blocks[temp][0]!=-1);
            Main.blocksUsed++;
            Main.blocks[temp][0] = Integer.parseInt(f.fileName);
            Main.blocks[prev][1] = temp;
            prev = temp;
          }
          }
          else{
            System.out.println("\nNew File could not be accomodated");
          }
        }
        else{
           if(newFileSize==0){
            noOfBlocks = 0;
          }
          f.size = newFileSize;
          System.out.print("\n"+f.fileName + " " + f.startBlock + " " + f.size);
          int arr[] = new int[(int)prevNoOfBlocks];
          int start = f.startBlock;
          arr[0] = f.startBlock;
          for(int k=1;k<prevNoOfBlocks;k++){
            arr[k] = blocks[start][1];
            start = blocks[start][1];
          }
          if((int)noOfBlocks!=0){
             blocks[arr[(int)noOfBlocks-1]][1] = -1;
          } 
          for(int k=(int)noOfBlocks;k<(int)prevNoOfBlocks;k++){
             blocks[arr[k]][0] = -1;
             blocks[arr[k]][1] = -1;
             Main.blocksUsed--;
          }
          if(f.size==0){
            String x = f.fileName;
            fileTable.remove(fileNo);
            System.out.println("\nFile "+ x +" deleted.");
          }
        }
        print();
      }
      else if(op==2){
        NewFile nf2 = new NewFile();
        nf2.start();
        try{
          sleep(3000);
        }
        catch(Exception e){
          System.out.println("System Interrupted");
        }
        print();
      }
    }while(op!=3);
    System.out.println("\n\nProgram by Mihir Gandhi B1 1611077");
  }

  static void print(){
    System.out.println("\n\nFILE ALLOCATION TABLE");    
    System.out.println("Sr. No. \tFile Name \tStart Block \tFile Size");
    for(int i=0;i<fileTable.size();i++){
      File f = fileTable.get(i);
      System.out.println(i + "\t\t\t" + f.fileName + "\t\t\t" + f.startBlock + "\t\t\t\t" + f.size);
    }

    System.out.println("\nBLOCKS: ");
    System.out.println("Block No. \tFile \tNext Block");
    for(int i=0;i<m;i++){
      System.out.print(i+"\t\t\t");
      for(int j=0;j<2;j++){
        System.out.print(blocks[i][j]+"\t\t");
      }
      System.out.print("\n");
    }
  }
}

class NewFile extends Thread{
  Random r = new Random();
  public void run(){
    File f = new File(Integer.toString(Main.count), 0, r.nextInt(1000)+200);
    double noOfBlocks = Math.ceil((double)f.size/(double)Main.blockSize);
    if(Main.m-Main.blocksUsed>=(int)noOfBlocks){
    System.out.print("\n"+f.fileName + " " + f.size);
    Main.fileTable.add(f);
    int temp;
    for(int i=0;i<(int)noOfBlocks;i++){
      do{
        temp = r.nextInt(Main.m);
      }
      while(Main.blocks[temp][0]!=-1);
      Main.blocksUsed++;
        if(Main.count==Main.blocks[Main.currentBlock][0]){
           Main.blocks[temp][0] = Main.count;
           Main.blocks[Main.currentBlock][1] = temp;
           Main.currentBlock = temp;
        }
        else{
          f.startBlock=temp;
          Main.blocks[temp][0] = Main.count;
          Main.currentBlock = temp;
        }
      }
    Main.count++;
    try{
      sleep((r.nextInt(2)+1)*1000);
    }
    catch(Exception e){
      System.out.print("Thread Interrupted");
    }
    if(Main.count<Main.total && Main.flag==0){
      NewFile nf = new NewFile();
      nf.start();
    }
    }
    else{
      System.out.println("\nNew File could not be accomodated");
    }
  }
}

class File{
  String fileName;
  int startBlock;
  int size;
  File(String fileName, int startBlock, int size){
    this.fileName = fileName;
    this.startBlock = startBlock;
    this.size = size;
  }
}