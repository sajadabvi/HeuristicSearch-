
package project_2;

import java.awt.image.Kernel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.time.Clock.system;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import static project_2.Project_2.split;
import sun.misc.CRC16;

class node{
    
    private node parent;
    private int cost;
    private int depth;
    boolean visited;
    private int heur;
    private String move;
    private int count;
    private int A;
    String[][] map= new String[6][6];
    
    public void setA(int theA){
        this.A = theA;
    }
    public void setA(){
        this.A= this.getCost() + this.getHeur();
    }
    public int getA(){
        return this.A;
    }
    public void setHeur(int theHeur){
        this.heur= theHeur;
    }
    public int getHeur(){
        return this.heur;
    }
    public void setCount(int theCount){
        this.count = theCount;
    }
    public int getCount(){
        return this.count;
    }
    public void setMove(String theMove){
        this.move=theMove;
    }
    public String getMove(){
        return this.move;
    }
    public void setParent(node theNode){
        this.parent = theNode;
    }
    public node getParent(){
        return this.parent;
    }
    
    public void setCost(int theCost){
        if(theCost > 0){
            this.cost = theCost;
        }
    }
    public int getCost(){
        return this.cost;
    }
    
    public void setDepth(int theDepth){
        if(theDepth>0){
            this.depth= theDepth;
        }
    }
    public int getDepth(){
        return this.depth;
    }
    
    private void applyToMap(String code){
        String[] splited = code.trim().split("\\s+");
        int number=Integer.parseInt(splited[0]);
        int len=Integer.parseInt(splited[4]);
        int R=Integer.parseInt(splited[1])-1;
        int C=Integer.parseInt(splited[2])-1;
        boolean horizDirection;
        horizDirection = "h".equals(splited[3]);
        if(horizDirection ){
            for(int i=0;i<len;i++){
                map[R][C+i] = splited[0]+" "+splited[3]+" "+splited[4];
            }
        }
        else{
            for(int i=0;i<len;i++){
                map[R+i][C] = splited[0]+" "+splited[3]+" "+splited[4];
            }
        }
            
    }
    
    static int heur(node d, int[][] best, int N){

        int[][] curr= new int[18][2];
        int x=0,y=0;
        for (int n=1;n<=N;n++){
            for(int id=0;id<6;id++){
                for(int jd=0;jd<6;jd++){
                    String[] splitedD= split(d.map[id][jd]);
                    if(splitedD != null && Integer.parseInt(splitedD[0])==n){
                        curr[n][0]=id;
                        curr[n][1]=jd;
                    }
                }
            }
        }
        
        for(int u=0;u<N;u++){
            x= x + java.lang.Math.abs(best[u][0] - curr[u][0]);
            y= y + java.lang.Math.abs(best[u][1] - curr[u][1]);
        }
        return x+y;
    }
    
    public node(int theCost,int theDepth,node theParent, String[][] theMap, String theMove, int[][] best){
        this.setHeur(heur(this,best,this.parent.count));
        this.setCost(theCost);
        this.setDepth(theDepth);
        this.parent= theParent;
        this.setMove(theMove);
        this.map=theMap;
        this.setCount(this.parent.getCount());
    }
    
    public node(int theCost,int theDepth,node theParent, String[][] theMap, String theMove){
        this.setCost(theCost);
        this.setDepth(theDepth);
        this.parent= theParent;
        this.setMove(theMove);
        this.map=theMap;
    }
    
    public node(File file){
        
        setCost(0);
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            line = bufferedReader.readLine();
            int N = Integer.parseInt(line);
            this.setCount(N);
            for(int i = 0; i < N;i++){
                line = bufferedReader.readLine();
                applyToMap(line);

            }
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
    }  
}

class PriorityQH {
 
  private int maxSize;
  private node[] queArray;
  private static  int nItems;
  public PriorityQH(int s) {
    maxSize = s;
    queArray = new node[maxSize];
    nItems = 0;
  }
  public void insert(node item) {
    int i;

    if (nItems == 0)
      queArray[nItems++].setHeur(item.getHeur()); 
    else 
    {
      for (i = nItems - 1; i >= 0; i--) 
      {
        if (item.getHeur() > queArray[i].getHeur()) 
          queArray[i + 1] = queArray[i]; 
        else
          
          break; 
      }
      queArray[i + 1].setHeur(item.getHeur());
      nItems++;
    } 
  }
  public node remove(){
    return queArray[--nItems];
  }
  public node peekMin(){
    return queArray[nItems - 1];
  }
  public boolean isEmpty(){
    return (nItems == 0);
  }
  public boolean isFull(){
    return (nItems == maxSize);
  }
}

class PriorityQGH {
 
  private int maxSize;
  private node[] queArray;
  private static  int nItems;
  public PriorityQGH(int s) {
    maxSize = s;
    queArray = new node[maxSize];
    nItems = 0;
  }
  public void insert(node item) {
    int i;

    if (nItems == 0)
      queArray[nItems++].setA(item.getA()); 
    else 
    {
      for (i = nItems - 1; i >= 0; i--) 
      {
        if (item.getA() > queArray[i].getA()) 
          queArray[i + 1] = queArray[i]; 
        else
          
          break; 
      }
      queArray[i + 1].setA(item.getA());
      nItems++;
    } 
  }
  public node remove(){
    return queArray[--nItems];
  }
  public node peekMin(){
    return queArray[nItems - 1];
  }
  public boolean isEmpty(){
    return (nItems == 0);
  }
  public boolean isFull(){
    return (nItems == maxSize);
  }
}


class Queue
{
    int front, rear, size;
    int  capacity;
    node array[];
      
    public Queue(int capacity) {
         this.capacity = capacity;
         front = this.size = 0; 
         rear = capacity - 1;
         array = new node[this.capacity];
           
    }
    boolean isFull(Queue queue)
    {  return (queue.size == queue.capacity);
    }
    boolean isEmpty(Queue queue)
    {  return (queue.size == 0); }
    void enqueue( node item)
    {
        if (isFull(this))
            return;
        this.rear = (this.rear + 1)%this.capacity;
        this.array[this.rear] = item;
        this.size = this.size + 1;
        System.out.println(item+ " enqueued to queue");
    }
    node dequeue()
    {
        if (isEmpty(this))
            return null;
          
        node item = this.array[this.front];
        this.front = (this.front + 1)%this.capacity;
        this.size = this.size - 1;
        return item;
    }
    node front()
    {
        if (isEmpty(this))
            return null;
          
        return this.array[this.front];
    }
    node rear()
    {
        if (isEmpty(this))
            return null;
          
        return this.array[this.rear];
    }
}

public class Project_2 {

    
    public static void main(String[] args) {
       // Scanner input = new Scanner(System.in);
        //System.out.println("input the map directory:\n");
        //File firstState = new File(input.nextLine());
        File firstState = new File("/Users/sajad/Documents/Under Grad/8th semester"
                + " /Artificial Intelligence/Project 1/maps/easy1.txt");
        
        
        node initial = new node(firstState);
        int h;
        int[][] best= new int[18][2];
        node bfsGoal= BFS(initial);
        
        for(int i=0;i<18;i++){
            for(int j=0;j<2;j++){
                best[i][j]= -1;
            }
        }
        for(int n = 1; n<= bfsGoal.getCount();n++){
            for(int ig=0;ig<6;ig++){
                for(int jg=0;jg<6;jg++){
                    String[] splitedG= split(bfsGoal.map[ig][jg]);
                    if(splitedG != null && Integer.parseInt(splitedG[0])==n){
                        best[n][0]=ig;
                        best[n][1]=jg;
                    }
                }
            }
        }
        
        h=heur(initial, bfsGoal, best, bfsGoal.getCount());
        
        //ArrayList<node> test = succ(initial);
        System.out.println("input the map directory:\n");
        // BFS(initial);
        //DFS(initial);
        //UCS(initial);
        //IDS(initial);
        GBFS(initial, best);
        
       
    }
    static void ASTAR(node init, int[][] best){
         PriorityQGH queue = new PriorityQGH(1000);
        queue.insert(init);
        init.visited=true;
        ArrayList<node> nextStates = new ArrayList<node>();
        ArrayList<node> visitedList = new ArrayList<node>();
        ArrayList<String> moves = new ArrayList<String>();
        //visitedList.add(init);
        node parent= null;
        while(!queue.isEmpty()){
            node n = queue.remove();
            if(visitedList.contains(n))
                continue;
            if(goalTest(n)){
                try{
                    File fout = new File("ucsout.txt");
                    FileOutputStream fos = new FileOutputStream(fout);
 
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                    moves.add(n.getMove());
                    parent=n.getParent();
                    while(parent != null){
                        moves.add(parent.getMove());
                        parent=n.getParent();   
                    }
                    Collections.reverse(moves);
                    for (int i = 0; i < moves.size(); i++) {
                        System.out.print(moves.get(i));
                        //bw.write(moves.get(i));
                        //bw.newLine();
                    }
                    bw.close();
                    return;
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            nextStates= succH(n,best);
            for (int i = 0; i < nextStates.size(); i++) {
			queue.insert(nextStates.get(i));
            }
            visitedList.add(n);
            
        }
    }
    
    static void GBFS(node init, int[][] best){
        PriorityQH queue = new PriorityQH(1000);
        queue.insert(init);
        init.visited=true;
        ArrayList<node> nextStates = new ArrayList<node>();
        ArrayList<node> visitedList = new ArrayList<node>();
        ArrayList<String> moves = new ArrayList<String>();
        //visitedList.add(init);
        node parent= null;
        while(!queue.isEmpty()){
            node n = queue.remove();
            if(visitedList.contains(n))
                continue;
            if(goalTest(n)){
                try{
                    File fout = new File("ucsout.txt");
                    FileOutputStream fos = new FileOutputStream(fout);
 
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                    moves.add(n.getMove());
                    parent=n.getParent();
                    while(parent != null){
                        moves.add(parent.getMove());
                        parent=n.getParent();   
                    }
                    Collections.reverse(moves);
                    for (int i = 0; i < moves.size(); i++) {
                        System.out.print(moves.get(i));
                        //bw.write(moves.get(i));
                        //bw.newLine();
                    }
                    bw.close();
                    return;
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            //nextStates= (ArrayList<node>) succ(n).clone();
            nextStates= succH(n,best);
            for (int i = 0; i < nextStates.size(); i++) {
			queue.insert(nextStates.get(i));
            }
            visitedList.add(n);
            
        }
        
        
    }
    
    static int heur(node d, node goal, int[][] best, int N){
        //int[][] best= new int[18][2];
        int[][] curr= new int[18][2];
        int x=0,y=0;
//        Arrays.fill(best, -1);
//        for(int n = 1; n<= N;n++){
//            for(int ig=0;ig<6;ig++){
//                for(int jg=0;jg<6;jg++){
//                    String[] splitedG= split(d.map[ig][jg]);
//                    if(splitedG != null && Integer.parseInt(splitedG[0])==n){
//                        best[n][0]=ig;
//                        best[n][1]=jg;
//                    }
//                }
//            }
//        }
        
        for (int n=1;n<=N;n++){
            for(int id=0;id<6;id++){
                for(int jd=0;jd<6;jd++){
                    String[] splitedD= split(d.map[id][jd]);
                    if(splitedD != null && Integer.parseInt(splitedD[0])==n){
                        curr[n][0]=id;
                        curr[n][1]=jd;
                    }
                }
            }
        }
        
        for(int u=0;u<N;u++){
            x= x + java.lang.Math.abs(best[u][0] - curr[u][0]);
            y= y + java.lang.Math.abs(best[u][1] - curr[u][1]);
        }
        return x+y;
    }
    static String writeMove(node d){
        return d.getParent().getMove();
    } 
    
    static node BFS (node init){
        Queue queue = new Queue(1000);
        queue.enqueue(init);
        init.visited=true;
        ArrayList<node> nextStates = new ArrayList<node>();
        ArrayList<node> visitedList = new ArrayList<node>();
        ArrayList<String> moves = new ArrayList<String>();
        //visitedList.add(init);
        node parent= null;
        loop:
        while(!queue.isEmpty(queue)){
            node n = queue.dequeue();
            
            for(int i=0;i<visitedList.size();i++){
                if(java.util.Arrays.deepEquals(visitedList.get(i).map,n.map)){
                   continue loop;
                }
            }
            if(goalTest(n)){
                try{
                    node goal = n;
                    File fout = new File("bfsout.txt");
                    FileOutputStream fos = new FileOutputStream(fout);
 
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                    moves.add(n.getMove());
                    parent=n.getParent();
                    while(parent != null){
                        moves.add(parent.getMove());
                        parent=parent.getParent();   
                    }
                    Collections.reverse(moves);
                    for (int i = 1; i < moves.size(); i++) {
                        //System.out.print(moves.get(i));
                        bw.write(moves.get(i));
                        bw.newLine();
                    }
                    bw.close();
                    return n;
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            //nextStates= (ArrayList<node>) succ(n).clone();
            nextStates= succ(n);
            for (int i = 0; i < nextStates.size(); i++) {
			queue.enqueue(nextStates.get(i));
            }
            visitedList.add(n);
            
        }
        
        return null;
    }
    
    public static String[] split(String msg){
        if(msg==null){
            return null;
        }
        else{
            String[] splited = msg.trim().split("\\s+");
            return splited;
        }
    }
    
    static boolean goalTest(node d){
        String[] splited = split(d.map[2][5]);

        if(splited==null){
            return false;
        }
        else{
            return Integer.parseInt(splited[0])==1;
        }
        
    }
        
    public static void arrayCopy(String[][] aSource, String[][] aDestination) {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }
    
    static ArrayList<node>  succH(node d, int[][] best){
         ArrayList<node> states = new ArrayList<node>();
         ArrayList<Integer> moved = new ArrayList<Integer>();
         
         int next=0;
         int pre=1;
         for(int i =0;i<6;i++){
             for(int j=0;j<6;j++){
                 next=0;
                 pre=1;
                 String lastMove;
                 if(d.map[i][j] != null){
                    String[] splited = split(d.map[i][j]);
                    if(!moved.contains(Integer.parseInt(splited[0]))){
                        if("h".equals(splited[1])){
                            while((j+Integer.parseInt(splited[2])+next)<6 && 
                                    d.map[i][j+Integer.parseInt(splited[2])+next] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                     newMap[p] = d.map[p].clone();      
                                }
                                for(int k=0; k<=next;k++){
                                    String temp1 = d.map[i][j];
                                    newMap[i][j+Integer.parseInt(splited[2])+k]=temp1;
                                    newMap[i][j+k]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" r "+(next+1);
                                if(Integer.parseInt(splited[2])==2){
                                    states.add(new node(d.getCost()+1,d.getDepth()+1,d,newMap, lastMove ,best));
                                }
                                else{
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap, lastMove ,best));
                                }
                                next++;
                            }
                            while((j-pre)>=0 && d.map[i][j-pre] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                     newMap[p] = d.map[p].clone();      
                                }
                                for(int k=1; k<=pre;k++){
                                    String temp2 = d.map[i][j];
                                    newMap[i][j-k]=temp2;
                                    newMap[i][j+Integer.parseInt(splited[2])-k]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" l "+(pre);
                                if(Integer.parseInt(splited[2])==2){
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap,lastMove ,best));
                                }
                                else{
                                    states.add(new node(d.getCost()+3,d.getDepth()+1,d,newMap,lastMove,best ));
                                }
                                pre++;
                            }
                            moved.add(Integer.parseInt(splited[0]));
                        }
                        else{
                            if(i==0 && Integer.parseInt(splited[0])==5 && j==2){
                                System.out.println("statst here");
                            }
                            while((i+Integer.parseInt(splited[2])+next)<6 &&
                                    d.map[i+Integer.parseInt(splited[2])+next][j] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                    newMap[p] = d.map[p].clone();      
                                }
                                for(int k=0; k<=next;k++){
                                    String temp3 = d.map[i][j];
                                    newMap[i+Integer.parseInt(splited[2])+k][j]=temp3;
                                    newMap[i+k][j]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" d "+(next+1);
                                if(Integer.parseInt(splited[2])==2){
                                states.add(new node(d.getCost()+1,d.getDepth()+1,d,newMap,lastMove,best  ));
                                }
                                else{
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap,lastMove ,best));
                                }   
                                next++;
                            }
                            while( (i-pre)>=0 && d.map[i-pre][j] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                    newMap[p] = d.map[p].clone();      
                                }
                                for(int k=1; k<=pre;k++){
                                    if((i-k)>5 || j>5 || (i+Integer.parseInt(splited[2])-k)>5 ){
                                        System.out.println("here");
                                    }
                                    String temp4 = d.map[i][j];
                                    newMap[i-k][j]=temp4;
                                    newMap[i+Integer.parseInt(splited[2])-k][j]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" u "+(pre);
                                //for(int ck=0;ck<states.size();ck++){
                                //     if(java.util.Arrays.deepEquals(states.get(ck).map,newMap)){
                                //         continue loop1;
                                //     }
                                //}
                                
                                    if(Integer.parseInt(splited[2])==2){
                                        states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap, lastMove ,best));
                                    }
                                    else{
                                        states.add(new node(d.getCost()+3,d.getDepth()+1,d,newMap, lastMove,best ));
                                    }
                                
                                pre++;
                            }
                            moved.add(Integer.parseInt(splited[0]));
                        }
                    }
                 }
                 
             }
         }
         
    return states;
            
    }
    
     static ArrayList<node>  succ(node d){
         ArrayList<node> states = new ArrayList<node>();
         ArrayList<Integer> moved = new ArrayList<Integer>();
         
         int next=0;
         int pre=1;
         for(int i =0;i<6;i++){
             for(int j=0;j<6;j++){
                 next=0;
                 pre=1;
                 String lastMove;
                 if(d.map[i][j] != null){
                    String[] splited = split(d.map[i][j]);
                    if(!moved.contains(Integer.parseInt(splited[0]))){
                        if("h".equals(splited[1])){
                            while((j+Integer.parseInt(splited[2])+next)<6 && 
                                    d.map[i][j+Integer.parseInt(splited[2])+next] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                     newMap[p] = d.map[p].clone();      
                                }
                                for(int k=0; k<=next;k++){
                                    String temp1 = d.map[i][j];
                                    newMap[i][j+Integer.parseInt(splited[2])+k]=temp1;
                                    newMap[i][j+k]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" r "+(next+1);
                                if(Integer.parseInt(splited[2])==2){
                                    states.add(new node(d.getCost()+1,d.getDepth()+1,d,newMap, lastMove ));
                                }
                                else{
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap, lastMove ));
                                }
                                next++;
                            }
                            while((j-pre)>=0 && d.map[i][j-pre] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                     newMap[p] = d.map[p].clone();      
                                }
                                for(int k=1; k<=pre;k++){
                                    String temp2 = d.map[i][j];
                                    newMap[i][j-k]=temp2;
                                    newMap[i][j+Integer.parseInt(splited[2])-k]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" l "+(pre);
                                if(Integer.parseInt(splited[2])==2){
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap,lastMove ));
                                }
                                else{
                                    states.add(new node(d.getCost()+3,d.getDepth()+1,d,newMap,lastMove ));
                                }
                                pre++;
                            }
                            moved.add(Integer.parseInt(splited[0]));
                        }
                        else{
                            if(i==0 && Integer.parseInt(splited[0])==5 && j==2){
                                System.out.println("statst here");
                            }
                            while((i+Integer.parseInt(splited[2])+next)<6 &&
                                    d.map[i+Integer.parseInt(splited[2])+next][j] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                    newMap[p] = d.map[p].clone();      
                                }
                                for(int k=0; k<=next;k++){
                                    String temp3 = d.map[i][j];
                                    newMap[i+Integer.parseInt(splited[2])+k][j]=temp3;
                                    newMap[i+k][j]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" d "+(next+1);
                                if(Integer.parseInt(splited[2])==2){
                                states.add(new node(d.getCost()+1,d.getDepth()+1,d,newMap,lastMove ));
                                }
                                else{
                                    states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap,lastMove ));
                                }   
                                next++;
                            }
                            while( (i-pre)>=0 && d.map[i-pre][j] == null ){
                                String [][] newMap = new String[6][6];
                                for(int p = 0; p < 6; p++){
                                    newMap[p] = d.map[p].clone();      
                                }
                                for(int k=1; k<=pre;k++){
                                    if((i-k)>5 || j>5 || (i+Integer.parseInt(splited[2])-k)>5 ){
                                        System.out.println("here");
                                    }
                                    String temp4 = d.map[i][j];
                                    newMap[i-k][j]=temp4;
                                    newMap[i+Integer.parseInt(splited[2])-k][j]=null;
                                }
                                lastMove=Integer.parseInt(splited[0])+" u "+(pre);
                                //for(int ck=0;ck<states.size();ck++){
                                //     if(java.util.Arrays.deepEquals(states.get(ck).map,newMap)){
                                //         continue loop1;
                                //     }
                                //}
                                
                                    if(Integer.parseInt(splited[2])==2){
                                        states.add(new node(d.getCost()+2,d.getDepth()+1,d,newMap, lastMove ));
                                    }
                                    else{
                                        states.add(new node(d.getCost()+3,d.getDepth()+1,d,newMap, lastMove ));
                                    }
                                
                                pre++;
                            }
                            moved.add(Integer.parseInt(splited[0]));
                        }
                    }
                 }
                 
             }
         }
         
    return states;
            
    }
    
}