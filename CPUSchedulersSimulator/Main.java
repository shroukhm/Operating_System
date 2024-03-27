import java.util.Scanner;  // Import the Scanner class
public class Main
{
    public static void main(String[] args)
    {
        // To display a menu to allow the user to choose the type of scheduler used
        System.out.println("Choose from the menu the number of scheduling algorithm to use : \\r\\n ");
        System.out.println("1- Preemptive Shortest- Job First. \\r\\n ");
        System.out.println("2- Round Robin. \\r\\n ");
        System.out.println("3- Preemptive Priority Scheduling. \\r\\n ");
        System.out.println("4- AG scheduling. \\r\\n ");
        System.out.println("5- Exit\\r\\n. ");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes : ");
        int no_processes = sc.nextInt();
        System.out.println("Enter Context switching : ");
        int Context_switching = sc.nextInt();

        process[] arr = new process[no_processes];
        for (int i = 0; i < no_processes; i++)
        {
            arr[i]=new process();
            System.out.println("Enter name of process : ");
            String name = sc.next();
            arr[i].setName(name);

            System.out.println("Enter Processes Arrival Time  : ");
            int AT = sc.nextInt();
            arr[i].setArrivalTime(AT);

            System.out.println("Enter Processes Burst Time  : ");
            int BT = sc.nextInt();
            arr[i].setBurstTime(BT);
        }
        while ( true )
        {
            System.out.println("Enter the number of your choice :  ");
            int scheduler_type = sc.nextInt() ;
            if ( scheduler_type == 1 )   // Upon choosing Preemptive Shortest Job First (SJF) scheduling
            {
                SJF.findAvgTime(arr,no_processes,Context_switching);
                System.out.println('\n' ); // To add a new line
            }
            else if (scheduler_type == 2 ) // Upon choosing Round Robin scheduling
            {
                System.out.println("Enter Process Quantum Time  : ");
                int QT = sc.nextInt();
                RoundRobin.calc_RoundRobin(arr,no_processes,QT,Context_switching);
                System.out.println('\n' ); // To add a new line
            }
            else if ( scheduler_type == 3 ) // Upon choosing Preemptive Priority scheduling
            {
                for (int i = 0; i < no_processes; i++)
                {
                    System.out.println("Enter Process "+(i+1)+" Priority  : ");
                    int Priority = sc.nextInt();
                    arr[i].setPriority(Priority);
                }
                priority_schedule.findAvgTime(arr,no_processes);
                System.out.println('\n' ); // To add a new line
            }

            else if (scheduler_type == 5 )  // Upon choosing to exit from the program
            {
                break ;
            }
            else // Upon entering any number < 1 and > 5
            {
                System.out.println( "Invalid number, please enter a number from 1 to 5. " + '\n' );
            }
        }
        sc.close() ;
    }
}
// Process class which stores and returns the name , priority , arrival time and burst time for each process
class process
{
    public String name;
    public int Arrival_Time;
    public int Burst_Time;
    public int Priority;
    public int quantum_Time;
    process()  // Constructor
    {
        name=" ";
        Arrival_Time=0;
        Burst_Time=0;
        quantum_Time = 0 ;
    }
    public void setName(String N)
    {
        this.name = N;
    }
    public void setArrivalTime(int ArrivalTime)
    {
        this.Arrival_Time=ArrivalTime;
    }
    public void setBurstTime(int BurstTime)
    {
        this.Burst_Time=BurstTime;
    }
    public void setPriority(int Priority)
    {
        this.Priority=Priority;
    }
    public void setQuantumTime(int QuantumTime )
    {
        this.quantum_Time = QuantumTime ;
    }
    public String getName( ) {return name;}
    public int getArrivalTime() {return Arrival_Time;}
    public int getBurstTime() {return Burst_Time;}
    public int getPriority() {return Priority;}
    public int getQuantumTime() {return quantum_Time ;}
}

//The 1st type of process scheduling
class SJF
{
    static void findWaitingTime(process arr[], int n,int waiting_time[],String order[],int Context_switching)
    {

        int bt[] = new int[n]; //copy the burst time in this array
        for (int i = 0; i < n; i++)
        {
            bt[i] = arr[i].getBurstTime();
        }

        int complete = 0, time = 0, min = Integer.MAX_VALUE,shortest_process = 0;
        int finish_time;
        boolean check = false;

        while (complete != n) // while all process are complete
        {
            for (int i = 0; i < n; i++) //find the shortest process in the current time
            {
                if ((arr[i].getArrivalTime() <= time) && (bt[i] < min) && (bt[i] > 0))
                {
                    min = bt[i];
                    shortest_process = i;
                    check = true;
                }
            }//make array to put Processes execution order
            boolean boo = false;
            for (int j=0;j<n;j++)
            {
                if (order[j]==arr[shortest_process].getName())
                {
                    boo=true;
                    break;
                }
            }
            if (boo==false)
            {
                for (int j = 0; j < n; j++)
                {
                    if (order[j] == " ")
                    {
                        order[j] = arr[shortest_process].getName();
                        break;
                    }
                }
            }

            if (check == false) // continue in same process
            {
                time += Context_switching;
                continue;
            }
            // Reduce remaining time by one
            bt[shortest_process]-=Context_switching;
            min = bt[shortest_process]; // put the current shortest process
            if (min == 0)
                min = Integer.MAX_VALUE;
            if (bt[shortest_process] == 0)
            {
                // this process is complete
                complete++;
                check = false;
                finish_time = time + 1;
                waiting_time[shortest_process]=finish_time - arr[shortest_process].getBurstTime() - arr[shortest_process].getArrivalTime();
                if (waiting_time[shortest_process] < 0)
                    waiting_time[shortest_process] = 0;
            }
            time += Context_switching;
        }
    }
    // function to calculate turnaround time
    static void findTurnAroundTime(process arr[], int n,int waiting_time[], int turn_around_time[])
    {
        // calculating turnaround time
        for (int i = 0; i < n; i++)
            turn_around_time[i] = arr[i].getBurstTime() + waiting_time[i];
    }
    static void findAvgTime(process arr[], int n,int Context_switching)
    {

        int waiting_time[] = new int[n], turn_around_time[] = new int[n];
        String order[ ]= new String[n];
        for (int i = 0; i < n; i++) {order[i]=" ";}
        findWaitingTime(arr, n, waiting_time,order,Context_switching);//to find waiting to all process
        findTurnAroundTime(arr, n, waiting_time, turn_around_time);// to find turnaround time to all process

        int  total_wt = 0, total_tat = 0,order_of_process=0;
        System.out.println("Processes " +" execution order"+ " Waiting time " + " Turn around time");
        for (int i = 0; i < n; i++)
        {
            total_wt = total_wt + waiting_time[i];
            total_tat = total_tat + turn_around_time[i];
            for (int j = 0; j < n; j++)
            {
                if(order[j]==arr[i].getName())
                {
                    order_of_process= j+1;
                }
            }
            System.out.println(" " + arr[i].getName() + "\t\t\t\t" + order_of_process + "\t\t\t\t" + waiting_time[i]+ "\t\t\t\t" + turn_around_time[i]);

        }
        System.out.println("Average waiting time = " +(double)total_wt/(double) n);
        System.out.println("Average turn around time = " +(double)total_tat/(double) n);
    }
}

// The 2nd type of process scheduling
class RoundRobin
{
    static void calc_RoundRobin(process arr[],int n,int qt,int cs)
    {
        arr =sortByArrivalTime(arr,n); // sort current processes by there arrival time
        int timer = 0, maxProccessIndex = 0;
        int queue[] = new int[n],turn[] = new int[n],wait[] = new int[n];
        boolean complete[] = new boolean[n];
        int bt[] = new int[n]; //copy the burst time in this array
        for (int i = 0; i < n; i++)
        {
        	bt[i] = arr[i].getBurstTime();
        }
        for(int i = 0; i < n; i++)
        {    //Initialize the queue and complete array
            complete[i] = false;
            queue[i] = 0;
        }
        while(timer < arr[0].getArrivalTime())    //Incrementing Timer until the first process arrives
            timer++;
        queue[0] = 1;

        while(true)
        { // first process is arrive
            boolean flag = true;
            for(int i = 0; i < n; i++)
            {
                if(bt[i] != 0)
                {
                    flag = false;
                    break;
                }
            }
            if(flag)
                break;
            for(int i = 0; (i < n) && (queue[i] != 0); i++) 
            {
                int counter = 0;
                while ((counter < qt) && (bt[queue[0] - 1] > 0)) 
                { //Incrementing Timer till process reach qt
                    bt[queue[0] - 1] -= 1;
                    timer += 1;
                    counter++;

                    //Updating the ready queue until all the processes arrive
                    checkNewArrival(timer, arr, n, maxProccessIndex, queue);
                }
                wait[i] += cs; // cs ---> context switching 
                timer += cs;
                if((bt[queue[0]-1] == 0) && (complete[queue[0]-1] == false))
                {
                    turn[queue[0]-1] = timer;        //turn currently stores exit times
                    complete[queue[0]-1] = true;
                }
                //checks if CPU is used or not
                boolean used = true;
                if(queue[n-1] == 0)
                {
                    for(int k = 0; k < n && queue[k] != 0; k++)
                    {
                        if(complete[queue[k]-1] == false)
                        {
                            used = false;
                        }
                    }
                }
                else used=false;
                if(used)
                {
                    timer++;
                    checkNewArrival(timer, arr, n, maxProccessIndex, queue);
                }
                queueMaintainence(queue,n);
            }
            System.out.println("1");
        }
        for(int i = 0; i < n; i++)
        {
            turn[i] = turn[i] - arr[i].getArrivalTime();
            wait[i] = turn[i] - arr[i].getBurstTime();
        }
        System.out.print(" process\torder\tWait Time\tTurnAround Time" + "\n");
        for(int i = 0; i < n; i++)
        {
            System.out.print(" \t"+arr[i].getName()+"\t\t "+(i+1)+"\t\t\t"+wait[i]+"\t\t\t"+turn[i]+ "\n");
        }
        int totalwait=0,totalTAT=0;
        for(int i =0; i< n; i++)
        {
            totalwait += wait[i];
            totalTAT += turn[i];
        }
        System.out.print("\nAverage wait time : "+(totalwait/n) +"\nAverage TurnAround Time : "+(totalTAT/n));

    }
    public static process[] sortByArrivalTime(process[] arr,int n)
    {
        for(int i = 0 ; i < n;  i++)
        {
            for(int j = 0 ; j < n-1; j++)
            {
                if(arr[j].getArrivalTime() > arr[j+1].getArrivalTime())
                {
                    process temp ;
                    temp = arr[j]  ;
                    arr[j] = arr[j+1];
                    arr[j+1] = temp ;
                }
            }
        }
        return arr ;
    }
    public static void queueUpdate(int queue[],int n, int maxProccessIndex)
    {
        int firstIndex = -1;
        for(int i = 0; i < n; i++)
        {
            if(queue[i] == 0)
            {
                firstIndex = i;
                break;
            }
        }
        if(firstIndex == -1)
            return;
        queue[firstIndex] = maxProccessIndex + 1;
    }
    public static void checkNewArrival(int timer, process arr[], int n, int maxProccessIndex,int queue[])
    {
        if(timer <= arr[n-1].getArrivalTime())
        {
            boolean newArrival = false;
            for(int j = (maxProccessIndex+1); j < n; j++)
            {
                if(arr[j].getArrivalTime() <= timer)
                {
                    if(maxProccessIndex < j)
                    {
                        maxProccessIndex = j;
                        newArrival = true;
                    }
                }
            }
            if(newArrival)    //adds the index of the arriving process if exist
                queueUpdate(queue,n, maxProccessIndex);
        }
    }
    public static void queueMaintainence(int queue[], int n)
    {
        for(int i = 0; (i < n-1) && (queue[i+1] != 0) ; i++)
        {
            int temp = queue[i];
            queue[i] = queue[i+1];
            queue[i+1] = temp;
        }
    }
}


//The 3rd type of process scheduling
class priority_schedule
{
    static void findWaitingTime(process arr[], int n,int waiting_time[],String order[])
    {
        int bt[] = new int[n]; //copy the burst time in this array
        for (int i = 0; i < n; i++)
        {
            bt[i] = arr[i].getBurstTime();
        }
        int pr[] = new int[n]; //copy the Priority in this array
        for (int i = 0; i < n; i++)
        {
            pr[i] = arr[i].getPriority();
        }
        int complete = 0, time = 0, h_pr= Integer.MAX_VALUE, highest_Priority = 0,finish_time;
        boolean check = false;

        while (complete != n) // while all process are complete
        {
            for (int i = 0; i < n; i++) //find the highest Priority process in the current time
            {
                if ((arr[i].getArrivalTime() <= time) && (pr[i] < h_pr) && (bt[i] > 0))
                {
                    h_pr= pr[i];
                    highest_Priority = i;
                    check = true;
                }
            }//make array to put Processes execution order
            boolean boo = false;
            for (int j = 0; j < n; j++)
            {
                if (order[j] == arr[highest_Priority].getName())
                {
                    boo = true;
                    break;
                }
            }
            if (boo == false)
            {
                for (int j = 0; j < n; j++)
                {
                    if (order[j] == " ")
                    {
                        order[j] = arr[highest_Priority].getName();
                        break;
                    }
                }
            }
            if (check == false)  // continue in same process
            {
                time ++;
                continue;
            }
            // Reduce remaining burst time by one
            bt[highest_Priority] --;

            if (bt[highest_Priority] == 0)
            {
                // this process is complete
                h_pr= Integer.MAX_VALUE;
                complete++;
                check = false;
                finish_time = time + 1;
                waiting_time[highest_Priority] = finish_time - arr[highest_Priority].getBurstTime() - arr[highest_Priority].getArrivalTime();
                if (waiting_time[highest_Priority] < 0)
                    waiting_time[highest_Priority] = 0;
            }
            time ++;
            if (time >=30)  //solve the starvation problem
            {
                for (int i=0;i<n;i++)
                {
                    if (bt[i]==arr[i].getBurstTime() )  // this mean process does not make any action (starvation)
                    {
                        pr[i]--;
                    }
                }
            }
        }
    }
    // function to calculate turnaround time
    static void findTurnAroundTime(process arr[], int n,int waiting_time[], int turn_around_time[])
    {
        // calculating turnaround time
        for (int i = 0; i < n; i++)
        {
            turn_around_time[i] = arr[i].getBurstTime() + waiting_time[i];
        }
    }
    static void findAvgTime(process arr[], int n)
    {
        int waiting_time[] = new int[n], turn_around_time[] = new int[n];
        String order[ ]= new String[n];
        for (int i = 0; i < n; i++) {order[i]=" ";}
        findWaitingTime(arr, n, waiting_time,order);//to find waiting to all process
        findTurnAroundTime(arr, n, waiting_time, turn_around_time);// to find turnaround time to all process

        int  total_wt = 0, total_tat = 0,order_of_process=0;
        System.out.println("Processes " +" execution order"+" Waiting time " + " Turnaround time");
        for (int i = 0; i < n; i++)
        {
            total_wt = total_wt + waiting_time[i];
            total_tat = total_tat + turn_around_time[i];
            for (int j = 0; j < n; j++)
            {
                if(order[j]==arr[i].getName())
                {
                    order_of_process= j+1;
                }
            }
            System.out.println(" " + arr[i].getName() + "\t\t\t\t" + order_of_process +
                    "\t\t\t\t" + waiting_time[i]+ "\t\t\t\t" + turn_around_time[i]);
        }
        System.out.println("Average waiting time = " +(double)total_wt/(double) n);
        System.out.println("Average turn around time = " +(double)total_tat/(double) n);
    }

}