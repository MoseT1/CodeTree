import java.util.*;
import java.io.*;

public class Main {

    static int[] costArr;
    static ArrayList<Node>[] map;
    static Program[] programInfo;
    static int programCount;
    static PriorityQueue<Program> earningPQ;
    static int n,m, v, u, w, Q;
    static int op;

    static ArrayList<Integer> idlist;
    static boolean[] isID;

    public static void main(String[] args) throws IOException {
        // Please write your code here.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st =new StringTokenizer(br.readLine());

        //초기화
        programCount = 0;
        earningPQ  = new PriorityQueue<>((o1, o2) -> {
            if(o1.earning != o2.earning) {
                return o2.earning - o1.earning;
            } else {
                return o1.id - o2.id;
            }
        });

        idlist = new ArrayList<>();
        isID = new boolean[30001];

        programInfo = new Program[30001];

        int Q = Integer.parseInt(st.nextToken());

        for(int i =0; i<Q; i++) {

            st = new StringTokenizer(br.readLine());
            op = Integer.parseInt(st.nextToken());


            if(op == 100) {
                n = Integer.parseInt(st.nextToken());
                m = Integer.parseInt(st.nextToken());

                costArr = new int[n];
                
                map = new ArrayList[n];
                for (int j=0; j<n; j++) {
                    map[j] = new ArrayList<>();
                }

                for (int j=0; j<m; j++) {
                    v = Integer.parseInt(st.nextToken());
                    u = Integer.parseInt(st.nextToken());
                    w = Integer.parseInt(st.nextToken());

                    map[v].add(new Node(u, w));
                    map[u].add(new Node(v, w));

                }

                
                dijkstra(0);

            }
            else if (op == 200) {
                int id, revenue, dest;

                id = Integer.parseInt(st.nextToken());
                revenue = Integer.parseInt(st.nextToken());
                dest = Integer.parseInt(st.nextToken());

                int sellingPossible;
                int isRemoved = 0;
                int earning;
                if(costArr[dest] == Integer.MAX_VALUE) {
                    sellingPossible = 0;
                    earning = -100000;
                } else {
                    earning = revenue - costArr[dest];
                    if(earning < 0) {
                        sellingPossible = 0;
                    } else {
                        sellingPossible = 1;
                    }
                }

                programInfo[id] = new Program(id, revenue, dest, earning, sellingPossible, isRemoved );
                idlist.add(id);
                isID[id] = true;

                if(sellingPossible == 1){
                    earningPQ.add(programInfo[id]);
                }
                programCount++;

            }

            else if (op == 300) {

                int id = Integer.parseInt(st.nextToken());

                if(isID[id] == false ) {
                    continue;
                } else {
                    programInfo[id].isRemoved = 1;
                }
        
            }

            // 여기까지 2시간 30분 

            else if (op == 400){
                if(earningPQ.isEmpty()) {
                    System.out.println(-1);

                } else {
                    boolean flag = false;

                    while(!earningPQ.isEmpty()) {
                        Program program1 = earningPQ.poll();

                        if(program1.isRemoved == 0 ) {
                            System.out.println(program1.id);
                            program1.isRemoved = 1;
                            flag = true;
                            break;
                        } 
                    }

                    if(flag == false) {
                        System.out.println(-1);
                    }
                    
                   


                }
            }

            else if (op == 500) {
                int s = Integer.parseInt(st.nextToken());

                dijkstra(s);

                earningPQ  = new PriorityQueue<>((o1, o2) -> {
                    if(o1.earning != o2.earning) {
                        return o2.earning - o1.earning;
                    } else {
                        return o1.id - o2.id;
                    }
                });

                for(int j=0; j<idlist.size(); j++) {
                
                    Program p1 = programInfo[idlist.get(j)];

                    if(p1.isRemoved == 1) {
                        continue;
                    } else {
                        if(costArr[p1.dest] != Integer.MAX_VALUE) {
                            p1.earning = p1.revenue - costArr[p1.dest];

                            if( p1.earning > 0) {
                                p1.sellingPossible = 1;
                                earningPQ.add(p1);
                            } else {
                                p1.sellingPossible = 0;
                            }
                        

                        } else {
                            p1.earning = -100000;
                            
                            p1.sellingPossible = 0;

                        }
                    
                    }
                }
            }


        }


    }

    static void dijkstra(int s) {

        PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) ->  o1.dist - o2.dist);

        boolean[] visited = new boolean[n];

        Arrays.fill(costArr, Integer.MAX_VALUE);

        pq.add(new Node(s, 0));
        costArr[s] = 0;
 
        while(!pq.isEmpty()) {

            Node n1 = pq.poll();
            int currentNode = n1.to;
            int currentWeight = n1.dist;


            if(visited[currentNode] == true) {
                continue;
            }

            visited[currentNode] = true;

            for (Node neighbor : map[currentNode]) {
                
                int newDist = costArr[currentNode] + neighbor.dist;
                if(newDist < costArr[neighbor.to]) {
                    costArr[neighbor.to] = newDist;
                    pq.add(new Node(neighbor.to, newDist));

                }
            }

        }

    }

    static class Node {
        int to;
        int dist;

        public Node (int to, int dist) {
            this.to = to;
            this.dist = dist;
        }
    }

    static class Program {
        int id;
        int revenue;
        int dest;
        int earning;
        int sellingPossible;
        int isRemoved;

        public Program (int id, int revenue, int dest,  int earning, int sellingPossible, int isRemoved) {
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;
            this.earning = earning;
            this.sellingPossible = sellingPossible;
            this.isRemoved = isRemoved;
        }
    }
}