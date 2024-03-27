import java.util.Scanner;


public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter number of partitions : ");
        int Number_of_partition = scan.nextInt();

        partitions [] arr;
        arr = new partitions[Number_of_partition];
        for (int i=0 ;i<Number_of_partition;i++)
        {
            arr[i]=new partitions();
            System.out.print("Enter the name of the partition : ");
            String name =scan.next();
            arr[i].set_partition_name(name);
            System.out.print("Enter the size of the partition : ");
            int size =scan.nextInt();
            arr[i].setSize(size);
        }
        System.out.println('\n' ); // To print new empty line
        System.out.print("Enter number of process requests : ");
        int process_requests=scan.nextInt();
        process [] arr2;
        arr2 = new process[process_requests];
        for (int i=0 ;i<process_requests;i++)
        {
            arr2[i]=new process();
            System.out.print("Enter the name of the process : ");
            String name =scan.next();
            arr2[i].setName(name);
            System.out.print("Enter the size of the process : ");
            int size =scan.nextInt();
            arr2[i].setSize(size);
        }
        System.out.println('\n' ); // To print new empty line
        System.out.print("       The menu of choices ");
        System.out.println('\n' ); // To print new empty line
        System.out.print("1.First fit. \n");
        System.out.print("2.Worst fit.\n");
        System.out.print("3.Best fit.\n");
        System.out.print("4.Exit.\n");
        System.out.println('\n' ); // To print new empty line
        while ( true )
        {
        	 System.out.print("Enter the number of policy you want to apply : ");
             int choice = scan.nextInt();
             System.out.println('\n' ); // To print new empty line
             if ( choice == 1 )
             {
            	 FirstFit.firstFit(arr,arr2,process_requests,Number_of_partition);
             }
             else if ( choice == 2 )
             {
            	 WorstFit.worstFit(arr,arr2,process_requests,Number_of_partition);
             }
             else if ( choice == 3 )
             {
            	BestFit.bestFit(arr,arr2,process_requests,Number_of_partition);
             }
             else if ( choice == 4 )
             {
            	 break ;
             }
             else
            	 System.out.print("Invalid input, please enter a number from 1 to 4.");
        }
        scan.close(); 
    }
}


class partitions
{
    public String Name;
    public int Size;
    partitions() 
    {
        Name = " ";
        Size = 0;
    }
    public void set_partition_name(String N) 
    {
    	this.Name = N;
    }
    public void setSize(int Size) 
    {
    	this.Size=Size;
    }
    public String get_partition_name() 
    {
    	return Name;
    }
    public int getSize() 
    {
    	return Size;
    }
}



class process
{
    public String Name;
    public int Size;
    process() 
    {
        Name = " ";
        Size = 0;
    }
    public void setName(String N) 
    {
    	this.Name = N;
    }
    public void setSize(int Size) 
    {
    	this.Size=Size;
    }
    public String getName() 
    {
    	return Name;
    }
    public int getSize() 
    {
    	return Size;
    }
}



class FirstFit 
{
	//n->processes number, m->partitions number
    static void firstFit(partitions[] arr, process[] arr2, int n, int m) 
    { 
        int[] blockSize = new int[m];
        for (int i=0 ;i<m;i++)
        {
            blockSize[i]=arr[i].getSize();
        }
        int[] processSize =new int [n];
        for (int i=0 ;i<n;i++)
        {
            processSize[i]=arr2[i].getSize();
        }
        int[] blockId = new int[n];   // Stores block id
        for (int i = 0; i < n; i++) // Initially no block is assigned to any process
            blockId[i] = -1;
        for (int i = 0; i < n; i++)  // enter each process and find suitable blocks according to its size
        {
            for (int j = 0; j < m; j++)
            {
                if (blockSize[j] >= processSize[i])
                {
                    blockId[i] = j;
                    blockSize[j] -= processSize[i];     // Reduce available memory in this block.
                    break;
                }
            }
        }
        int remain=0;
        boolean check;
        for (int i=0 ;i<m;i++) 
        {
            check=false;
            System.out.print(arr[i].get_partition_name()+"( "+arr[i].getSize()+"KB) => ");
            for (int j=0 ;j<n;j++)
            {
                if (blockId[j]==i)
                {
                    System.out.print(arr2[j].getName());
                    System.out.println(" ");
                    if (blockSize[i]!=0)
                    {
                        System.out.print("partitions "+ (m+i) + "( " + blockSize[i] + 
                        		     "KB) => External fragment ");
                    }
                    System.out.println(" ");
                    check=true;
                    break;
                }
            }
            if (check==false)
            {
                System.out.println("External fragment");
            }
            remain+=blockSize[i];
        }
        for (int i=0 ;i<n;i++) 
        {
            if (blockId[i]==-1) 
            {
                System.out.println(arr2[i].getName()+" can not be allocated");
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.println('\n' ); // To print new empty line
   	    System.out.print("Do you want to compact ( Enter: 1 for yes , 2 for no ) : ");
        int flag = sc.nextInt();
        System.out.println('\n' ); // To print new empty line
        if ( flag == 1 )
        {
        	Compaction.make_Compaction(arr,arr2,remain,blockId,n,m);
        }
        sc.close() ;
    }
}




class Compaction
{
	//n->processes number, m->partitions number
    static void make_Compaction(partitions[] arr, process[] arr2, int remain, int[] blockId, int n, int m) 
    {
        int  process_id = 0;
        for (int i = 0; i < n; i++) 
        {
            if (blockId[i] == -1) 
            {
                if (arr2[i].getSize() <= remain) 
                {
                    blockId[i] = m + 1;
                    process_id = i;
                    for (int j = 0; j < m; j++)
                    {
                        System.out.print(arr[j].get_partition_name() + "( " + arr2[j].getSize() + "KB) => ");
                        for (int k = 0; k < n; k++) 
                        {
                            if (blockId[k] == j) 
                            {
                                System.out.print(arr2[k].getName());
                                System.out.println(" ");
                                break;
                            }
                        }
                    }
                    System.out.print("Partition " + (m + 1) + "( " + remain + "KB) => " + 
                                       arr2[process_id].getName());
                    System.out.println(" ");
                    remain-=arr2[i].getSize();
                }
            }
        }
    }
}


class WorstFit 
{
	//n->processes number, m->partitions number
    static void worstFit( partitions[] arr, process[] arr2, int n, int m) 
    { 
        int[] blockSize = new int[1000];
        for (int i=0 ;i<m;i++)
        {
            blockSize[i]=arr[i].getSize();
        }
        int[] processSize = new int [n];
        for (int i=0 ;i<n;i++)
        {
            processSize[i]=arr2[i].getSize();
        }
        int[] blockId = new int[n];   // Stores block id
        for (int i = 0; i < n; i++) // Initially no block is assigned to any process
        {
        	blockId[i] = -1;
        }
        for (int i = 0; i < n; i++)  // enter each process and find suitable blocks according to its size
        {
        	// Find the worst fit block for current process 
            int index = -1;
            for (int j=0; j<m; j++)
            {
                if (blockSize[j] >= processSize[i])
                {
                    if ( index == -1)
                        index = j;
                    else if (blockSize[index] < blockSize[j])
                        index = j;
                }
            }
      
            // If we could find a block for current process
            if ( index != -1)
            {
                // allocate block j to arr2[i] process
                blockId[i] = index ;
      
                // Reduce available memory in this block.
                blockSize[index] = processSize[i];
            }
        }
        	
        int remain=0;
        boolean check;
        for (int i=0 ;i<m;i++) 
        {
            check=false;
            System.out.print(arr[i].get_partition_name()+"( "+arr[i].getSize()+"KB) => ");
            for (int j=0 ;j<n;j++)
            {
                if (blockId[j]==i)
                {
                    System.out.print(arr2[j].getName());
                    System.out.println(" ");
                    if (blockSize[i]!=0)
                    {
                        System.out.print("Partition "+ (m+i) + "( " + blockSize[i] + "KB) => External fragment ");
                    }
                    System.out.println(" ");
                    check=true;
                    break;
                }
            }
            if (check==false)
            {
                System.out.println("External fragment");
            }
            remain+=blockSize[i];
        }
        for (int i=0 ;i<n;i++) 
        {
            if (blockId[i]==-1) 
            {
                System.out.println(arr2[i].getName()+" can not be allocated");
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.println('\n' ); // To print new empty line
   	    System.out.print("Do you want to compact ( Enter: 1 for yes , 2 for no ) : ");
        int flag = sc.nextInt();
        System.out.println('\n' ); // To print new empty line
        if ( flag == 1 )
        {
        	Compaction.make_Compaction(arr,arr2,remain,blockId,n,m);
        }
        sc.close() ;
    }
}

class BestFit 
{
 
    static void bestFit( partitions[] arr, process[] arr2, int m,int n)
    { 
        int[] blockSize = new int[m];
        for (int i=0 ;i<m;i++)
        {
            blockSize[i]=arr[i].getSize();
        }
        int[] processSize =new int [n];
        for (int i=0 ;i<n;i++)
        {
            processSize[i]=arr2[i].getSize();
        }
        int blockId[] = new int[n];        // Stores block id 
        for (int i = 0; i < blockId.length; i++)        // Initially no block is assigned to any process
        {
            blockId[i] = -1;
        }
        for (int i=0; i<n; i++)
        {
            int bestblockId = -1;   // Find the best fit block for current process
            for (int j=0; j<m; j++)
            {
                if (blockSize[j] >= processSize[i])
                {
                    if (bestblockId == -1)
                        bestblockId = j;
                    else if (blockSize[bestblockId] > blockSize[j])
                        bestblockId = j;
                }
            }
       
            // If we could find a block for current process
            if (bestblockId != -1)
            {
                blockId[i] = bestblockId;
                blockSize[bestblockId] -= processSize[i];    // Reduce available memory in this block.

            }
        }
        int remain=0;
        boolean check;
        for (int i=0 ;i<m;i++) 
        {
            check=false;
            System.out.print(arr[i].get_partition_name()+"( "+arr[i].getSize()+"KB) => ");
            for (int j=0 ;j<n;j++)
            {
                if (blockId[j]==i)
                {
                    System.out.print(arr2[j].getName());
                    System.out.println(" ");
                    if (blockSize[i]!=0)
                    {
                        System.out.print("Partition "+ (m+i) + "( " + blockSize[i] + "KB) => External fragment ");
                    }
                    System.out.println(" ");
                    check=true;
                    break;
                }
            }
            if (check==false)
            {
                System.out.println("External fragment");
            }
            remain+=blockSize[i];
        }
        for (int i=0 ;i<n;i++) 
        {
            if (blockId[i]==-1) 
            {
                System.out.println(arr2[i].getName()+" can not be allocated");
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.println('\n' ); // To print new empty line
   	    System.out.print("Do you want to compact ( Enter: 1 for yes , 2 for no ) : ");
        int flag = sc.nextInt();
        System.out.println('\n' ); // To print new empty line
        if ( flag == 1 )
        {
        	Compaction.make_Compaction(arr,arr2,remain,blockId,n,m);
        }
        sc.close() ;
    }
}