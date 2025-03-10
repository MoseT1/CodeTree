/*

트리 : 다양한 색의 노드
각 노드 : 특정 색깔, color와 최대 깊이 max(depth) 속성

동적으로 노드 추가
색을 변결할 수 있는 명령을 처리

1. 노드 추가
각 노드 : 고유한 번호 m, 부모 노드 번호 p, 색깔 color, 최대 깊이 max
color : 1 - 빨간색
        2 - 주황색
        3 - 노랑색
        4 - 초록색
        5 - 파란색
p : -1 - 새로운 트리의 루트 노드가 된다.
max : 해당 노드를 루트로 하는 서브트리의 최대 깊이  
    자기 자신은 1

* 기존 노드들의 max 값으로 인해 새로운 노드가 추가됨으로써 모순이 발생한다면, 현재 노드는 추가하지 않음
--> 주의

2. 색 변경

특정 노드 m을 루트로 하는 서브트리의 모든 노드의 색은 지정된 색 color로 변경한다.

각 노드의 가치 : 해당 노드를 루트로 하는 서브트리 내 서로 다른 색의 수



*/

import java.io.*;
import java.util.*;

public class Main {

    static int[] currentDepth;
    static ArrayList<Node>[] graph;
    static int Q;
    static Node[] info;
    static long answer;

    public static void main(String[] args) throws IOException {
        // Please write your code here.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());

        Q = Integer.parseInt(st.nextToken());

        currentDepth = new int[100001];
        graph = new ArrayList[100001];
        info = new Node[100001];
        answer = 0;
        ArrayList<Integer> root = new ArrayList<>();

        for (int i=0; i< 100001; i++) {
            graph[i] = new ArrayList<>();
        }

        int op, m, p, color, max;
        
        Queue<Integer> queue = new LinkedList<>();

        for(int i=0; i<Q; i++) {
            st = new StringTokenizer(br.readLine());

            op = Integer.parseInt(st.nextToken());
            

            if(op == 100) {
                m = Integer.parseInt(st.nextToken());
                p = Integer.parseInt(st.nextToken());
                color = Integer.parseInt(st.nextToken());
                max = Integer.parseInt(st.nextToken());

                if (p == -1) {
                    currentDepth[m] = 1;
                    info[m] = new Node(m,p,color,max);
                    root.add(m);
                    
                   
                } else {


                    boolean flag = true;

                    int parent = p;
                    int current = m;
                    int depth = currentDepth[p] + 1;
                    // 위의 노드들과 currentDepth, max를 이용하여 비교
                    while (flag) {
                        
                        //System.out.println("parent = " + parent + ", current = " + current);
                        
                        

                        if(depth - currentDepth[parent] +1 <= info[parent].max) {
                            parent = info[parent].p;
                            
                            
                            if (parent == -1 ) {
                                break;
                            }
                        } else {
                            flag = false;
                        }
                    }
                    if (flag == true) {
                        
                        currentDepth[m] = currentDepth[p] + 1;
                        info[m] = new Node(m,p,color,max);
                        graph[p].add(info[m]);
                        //System.out.println("노드 추가 : p = " + p + " m = " + m );

                    }
                   

                }


            } else if (op == 200) {
                m = Integer.parseInt(st.nextToken());
                color = Integer.parseInt(st.nextToken());

                queue.add(m);

                while (!queue.isEmpty()) {

                    int id = queue.poll(); // 방문할 노드;
                    info[id].color = color;

                    for (int j=0; j<graph[id].size(); j++) {
                        
                        queue.add(graph[id].get(j).m);
                    }
                }



            } else if (op == 300) {
                m = Integer.parseInt(st.nextToken());
                bw.write(info[m].color + "\n");
                


            } else if (op == 400) {

                for(int j=0; j<root.size(); j++) {
                    countColor(root.get(j));
                }
                bw.write(answer + "\n");
               
                answer = 0;

            }

        }

        bw.flush();
        bw.close();

    }
    static HashSet<Integer> countColor(int m) {

        HashSet<Integer> colors = new HashSet<>();

        colors.add(info[m].color);
        //System.out.println(" countColor 중 m = " + m + ", colors = " + info[m].color);

        if (graph[m].size() == 0) {
            answer += 1;
            return colors;
        }

        for(int i=0; i < graph[m].size(); i++) {
            int id = graph[m].get(i).m;
            colors.addAll(countColor(id));
        }

        int k = colors.size();
        answer += k*k;
        return colors; 

    }


    static class Node {
        int m;
        int p;
        int color;
        int max;

        public Node (int m, int p, int color, int max) {
            this.m = m;
            this.p = p;
            this.color = color;
            this.max = max;
        }

        public void setColor (int color) {
            this.color = color;
        }
    }
}