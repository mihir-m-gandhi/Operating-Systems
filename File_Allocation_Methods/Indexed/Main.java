import java.util.*;

class Main extends Thread {
  public static int total=7, count=1, currentBlock=0, blockSize, m, blocksUsed=0, flag=0;
  public static ArrayList<Integer>[] blocks;
  public static ArrayList<File> fileTable = new ArrayList<File>();
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("\rEnter the number of blocks: ");
    m = sc.nextInt();
    blocks = new ArrayList[m]; 
    for(int i=0;i<m;i++){
      blocks[i] = new ArrayList<Integer>(); 
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
    flag = 1;
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
        if(newFileSize>f.size){
          double noOfBlocks = Math.ceil((double)newFileSize/(double)Main.blockSize);
          int diff = (int)noOfBlocks-blocks[f.startBlock].size();
          if(Main.m-Main.blocksUsed>=diff){
          f.size = newFileSize;
          System.out.print("\n"+f.fileName + " " + f.startBlock + " " + f.size);
          int temp;
          for(int i=0;i<diff;i++){
            do{
              temp = r.nextInt(Main.m);
            }while(Main.blocks[temp].size()!=0);
            Main.blocks[temp].clear();
            Main.blocksUsed++;
            Main.blocks[temp].add(Integer.parseInt(f.fileName));
            Main.blocks[f.startBlock].add(temp);
          }
          }
          else{
            System.out.println("\nNew File could not be accomodated");

          }
        }
        else{
          f.size = newFileSize;
          double noOfBlocks = Math.ceil((double)f.size/(double)Main.blockSize);
           if(newFileSize==0){
            noOfBlocks = 0;
          }
          for(int k=blocks[f.startBlock].size()-1;k>=(int)noOfBlocks;k--){
            Main.blocksUsed--;
            Main.blocks[Main.blocks[f.startBlock].get(k)].clear();
            blocks[f.startBlock].remove(k); 
            //  for(int j=0;j<blocks[f.startBlock].size();j++){
            //   System.out.print(blocks[f.startBlock].get(j)+" ");
            // }
          }
          if(f.size==0){
            Main.blocksUsed--;
            Main.blocks[f.startBlock].clear();
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
     System.out.print("Sr. No. \tFile Name \tStart Block \tFile Size");
    for(int i=0;i<fileTable.size();i++){
      File f = fileTable.get(i);
      System.out.print("\n" + i + "\t\t\t" + f.fileName + "\t\t\t" + f.startBlock + "\t\t\t\t" + f.size+"\t\t\t");
    }
    System.out.println("\n\nBLOCKS: ");
   System.out.println("Block No. \tFile");    
   for(int i=0;i<m;i++){
        System.out.print(i+"\t\t\t");
        for(int j=0;j<blocks[i].size();j++){
          System.out.print(blocks[i].get(j)+" ");
        }
        System.out.print("\n");
    }
  }
}

class NewFile extends Thread{
  Random r = new Random();
  public void run(){
    File f = new File(Integer.toString(Main.count), 0, r.nextInt(1000)+200);
    double noOfBlocks = Math.ceil((double)f.size/(double)Main.blockSize) + 1;
    if(Main.m-Main.blocksUsed>=(int)noOfBlocks){
    System.out.print("\n"+f.fileName + " " + f.size);
    Main.fileTable.add(f);
    int temp;
    int flag2=0;
    for(int i=0;i<(int)noOfBlocks;i++){
      do{
        temp = r.nextInt(Main.m);
      }
      while(Main.blocks[temp].size()!=0);
      Main.blocks[temp].clear();
      Main.blocksUsed++;
        if(flag2==0){
          f.startBlock=temp;
          flag2=1;
        }
        else{
           Main.blocks[temp].add(Main.count);
           Main.blocks[f.startBlock].add(temp);
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