import java.util.*;

class Main extends Thread {
  public static int blocks[];
  public static int total=4, count=1, currentBlock=0, blockSize, m, blocksUsed, flag=0;
  public static ArrayList<File> fileTable = new ArrayList<File>();
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the number of blocks: ");
    m = sc.nextInt();
    blocks = new int[m];
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
            int blocks2[] = new int[m];
            for(int k=0;k<m;k++){
              blocks2[k] = blocks[k];
            }
            for(int k=f.startBlock;k<f.startBlock + noOfBlocks;k++){
              blocks[k] = Integer.parseInt(f.fileName);
            }
            for(int k=f.startBlock+(int)noOfBlocks;k<Main.m;k++){
              blocks[k] = blocks2[k-diff];
            }
            
            for(int k=fileNo+1;k<fileTable.size();k++){
              File f2 = fileTable.get(k);
              f2.startBlock = f2.startBlock + diff;
            }
            currentBlock = currentBlock + diff;
            blocksUsed = blocksUsed + diff;
          }
          else{
            System.out.println("\nNew File could not be accomodated");
          }
        }
        else{
          if(newFileSize==0){
            noOfBlocks = 0;
          }
          int diff = (int)prevNoOfBlocks-(int)noOfBlocks;
          f.size = newFileSize;
          int blocks2[] = new int[m];
          for(int k=0;k<m;k++){
            blocks2[k] = blocks[k];
          }
          System.out.print("\n"+f.fileName + " " + f.startBlock + " " + f.size);
          for(int k=f.startBlock+(int)noOfBlocks;k<Main.m-diff;k++){
            blocks[k] = blocks2[k+diff];
          }
          for(int k=Main.m-diff;k<Main.m;k++){
            blocks[k] = 0;
          }
          
          currentBlock = currentBlock - diff;
          for(int k=fileNo+1;k<fileTable.size();k++){
            File f2 = fileTable.get(k);
            f2.startBlock = f2.startBlock - diff;
          }
          blocksUsed = blocksUsed - diff;
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
      System.out.println(i+ "\t\t\t" + f.fileName + "\t\t\t" + f.startBlock + "\t\t\t\t" + f.size);
    }

    System.out.println("\nBLOCKS:");
    System.out.println("Block No. \tFile");
    for(int i=0;i<m;i++){
      System.out.println(i+"\t\t\t"+blocks[i]);
    }
    System.out.println("");
  }
}

class NewFile extends Thread{
  Random r = new Random();
  public void run(){
    File f = new File(Integer.toString(Main.count), Main.currentBlock, r.nextInt(1000)+200);
    double noOfBlocks = Math.ceil((double)f.size/(double)Main.blockSize);
    if(Main.m-Main.blocksUsed>=(int)noOfBlocks){
    System.out.print("\n"+f.fileName + " " + f.size);
    Main.fileTable.add(f);
    for(int i=0;i<(int)noOfBlocks;i++){
      Main.blocks[Main.currentBlock] = Main.count;
      Main.currentBlock++;
      Main.blocksUsed++;
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