/*

유적지 : 5 x 5 격자 형태
각 칸에는 1-7 의 유물 존재

1. 탐사 진행

    3 x 3 격자를 선택하여 회전시킬 수 있다.
    시계 방향으로 90도, 180도, 270도 중 하나로 회전 가능하다.
    -> 선택된 격자는 '항상' 회전을 진행해야만 한다.

    회전 목표 
    1. 유물 1차 획득 가치를 최대화하고, 그러한 방법이 여러가지인 경우
    2. 회전한 각도가 가장 작은 방법을 선택
    3. 그것도 여러가지면, 회전 중심 좌표의 열이 가장 작은 구간을,
        열이 같다면 행이 가장 작은 구간을 선택
    
    1차 획득 가치 -> 회전한 각도 작은 것 -> 좌표의 열이 가장 작은 것 -> 행이 가장 작은 것


2. 유물 획득

    상하좌우로 인접한 같은 종류의 유물 조각은 서로 연결되어 있다. 
    3개 이상 연결된 경우, 조각이 모여 유물이 되고 사라진다. 
    유물의 가치  = 모인 조각의 개수

    유적의 벽면엔 1-7사이 숫자가 M개 적혀 있다.
    유적에서 조각이 사라졌을 떄 새로 생겨나는 조각에 대한 정보이다.

    사라진 공간에 벽면에 새겨진 숫자의 순서대로 새로운 조각이 생긴다.
    1) 열 번호가 작은 순으로 조각이 생긴다. 
    2) 열 번호가 같다면 행번호가 큰 순으로 조각이 생긴다. 


3. 탐사 반복

    탐사 진행 ~ 유물 연쇄 획득의 과정까지가 1턴
    총 K 번의 턴에 걸쳐 진행

    각 턴마다 획득한 유물의 가치의 총합을 출력
    K번의 턴을 진행하기  유물을 획득할 방법이 없다면 종료.
*/

import java.util.*;
import java.io.*;

public class Main {

    // 회전 용도
    static int[] X1 = {1,1,-1,-1};
    static int[] Y1 = {-1,1,1,-1}; 
    static int[] X2 = {1,0,-1, 0};
    static int[] Y2 = {0,1,0, -1};

    // 유물 가치획득 시 위치 우선순위 용도
    static int[] X3 = {-1,-1,-1, 0,0,1,1,1};
    static int[] Y3 = {-1,0,1,-1,1,-1,0,1};

    static boolean[][] visited;



    static int[][] map;
    static int K, M;
    
    static int count;



    public static void main(String[] args) throws IOException {
        // Please write your code here.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));


        StringTokenizer st = new StringTokenizer(br.readLine());
        
        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[5][5];

        count = 0;
        visited = new boolean[5][5];


        for(int y=0; y<5; y++) {
            st = new StringTokenizer(br.readLine());

            for (int x=0; x<5; x++) {
                map[y][x] = Integer.parseInt(st.nextToken());
            }
        }

        // 유물 조각 저장
        int[] NextNum = new int[M+1];
        int index = 0;

        st = new StringTokenizer(br.readLine());
        for (int i=0; i<M; i++) {
            NextNum[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i< K; i++) {

            PriorityQueue<Value> pq = new PriorityQueue<>((o1, o2) -> {
                if (o1.val != o2.val) {
                    return o2.val - o1.val;
                } else if (o1.degree != o2.degree) {
                    return o1.degree - o2.degree;
                } else if (o1.centerX != o2.centerX) {
                    return o1.centerX - o2.centerX;
                } else {
                    return o1.centerY - o2.centerY;
                }
            });

            // (1,1) ~ (3,3)까지 돌면서 3x3 격자를 구하는 과정
            for(int cy = 1; cy<4; cy++) {
                for (int cx = 1; cx<4; cx++) {
                    for (int deg=1; deg<4; deg++) { // deg는 90, 180, 270 의미
                        revolve(cx, cy);  // 4번 돌려서 제자리로 놓기 

                        // 돌린 후에 그 상태에서 3개 이상의 조각이 모인 유물들을 지우는 것. 
                        // 3개 이상인 유물들을 구하고 그 값을 peices 에 저장 
                        int peices = 0;

                        visited = new boolean[5][5];

                        for (int pos=0; pos<8; pos++) { // pos는 팔방
                            int mx = cx + X3[pos];
                            int my = cy + Y3[pos];

                            Queue<Point> queue = new LinkedList<>();

                           

                            // map [my][mx]에서 사방을 탐색하면서  갯수 세기
                            int cnt = 1;

                            queue.add(new Point(mx, my));
                            visited[my][mx] = true;
                            int s = map[my][mx];

                            while(!queue.isEmpty()) {
                                Point p1 = queue.poll();
                        
                                //System.out.println("cnt = " + cnt + ", deg = " + deg + ", pos = " + pos + ", cx = " + cx + ", cy = " + cy);
                               
                                for(int r = 0; r<4; r++) {
                                    int px = p1.x + X2[r];
                                    int py = p1.y + Y2[r];

                                    if (px >= 0 && px <5 && py >=0 && py <5 && visited[py][px] == false) {
                                        if(map[py][px] == s) {
                                            cnt++;
                                            queue.add(new Point(px, py));
                                            visited[py][px] = true;
                                        }
                                    }
                                }
                            }

                            

                            // cnt를 PriorityQueue pq에 저장
                            if (cnt >= 3) {
                                 peices += cnt;
                            }
                           
                            cnt = 1;

                        }

                        if (peices >= 3) {
                            pq.add(new Value(peices, deg,  cx, cy));
                            peices = 0;
                        }
                    }
                    revolve(cx, cy);
                }
            }
             // 만약 아무것도  지우지 못했다면 멈추기
            if (pq.isEmpty()) {
               
                break;
            }

            

            Value v = pq.poll();
            int ax = v.centerX;
            int ay = v.centerY;
            int adeg = v.degree;
            int aval = v.val;


            count += aval;
 

            //System.out.println( "ax = " + ax + ", ay = " + ay + ", adeg = " + adeg + ", aval = " + aval  );
            
            // (ax, ay) 를 중심으로 adeg를 회전
            // 이후 탐색하며 지우기
            for (int rot=0; rot<adeg; rot++) {
                revolve(ax, ay);
            }

            visited = new boolean[5][5];

            for (int pos=0; pos<8; pos++) { // pos는 팔방
                int mx = ax + X3[pos];
                int my = ay + Y3[pos];

                Queue<Point> queue = new LinkedList<>();

                

                queue.add(new Point(mx, my));
                visited[my][mx] = true;

                int cnt = 1;

                int s = map[my][mx];

                while(!queue.isEmpty()) {
                    Point p1 = queue.poll();
                      
                    
                    for(int r = 0; r<4; r++) {
                        int px = p1.x + X2[r];
                        int py = p1.y + Y2[r];

                        if (px >= 0 && px <5 && py >=0 && py <5 && visited[py][px] == false) {
                            if(map[py][px] == s) {
                                cnt++;
                                queue.add(new Point(px, py));
                                visited[py][px] = true;
                                
                            }
                        }
                    }
                }

                // 3개 이상이면 지우기
                if(cnt >= 3) {
                    visited = new boolean[5][5];

                    queue.add(new Point(mx, my));
                    visited[my][mx] = true;
                    map[my][mx] = 0;

                    while(!queue.isEmpty()) {
                        Point p1 = queue.poll();    
                        
                        for(int r = 0; r<4; r++) {
                            int px = p1.x + X2[r];
                            int py = p1.y + Y2[r];

                            if (px >= 0 && px <5 && py >=0 && py <5 && visited[py][px] == false) {
                                if(map[py][px] == s) {
                                    
                                    queue.add(new Point(px, py));
                                    visited[py][px] = true;
                                    map[py][px] = 0;
                                    
                                }
                            }
                        }
                    }
                }
            }

            // 빈 공간 NextNum[]으로 채우기
            
            for(int col = 0; col < 5; col++) {

                for (int row = 4; row >=0; row--) {

                    if(map[row][col] == 0) {
                        map[row][col] = NextNum[index++];

                    }
                }
            }

            //연쇄 획득
            while(true) {

                boolean remove = false;

                visited = new boolean[5][5];

                
                // 유물을 지울 수 있으면  지욱고 다시 숫자 채우기
                for (int pos=0; pos<8; pos++) { // pos는 팔방
                    int mx = ax + X3[pos];
                    int my = ay + Y3[pos];

                    Queue<Point> queue = new LinkedList<>();

                    
                    queue.add(new Point(mx, my));
                    visited[my][mx] = true;

                    int cnt = 1;

                    int s = map[my][mx];
                    if(s == 0) {        // 0으로 바뀐 것도 또 0인 것들을 지우려고 하므로 이를 제외해줘야한다. 
                        continue;
                    }

                    while(!queue.isEmpty()) {
                        Point p1 = queue.poll();
                        
                        
                        for(int r = 0; r<4; r++) {
                            int px = p1.x + X2[r];
                            int py = p1.y + Y2[r];

                            if (px >= 0 && px <5 && py >=0 && py <5 && visited[py][px] == false) {
                                if(map[py][px] == s) {
                                    cnt++;
                                    queue.add(new Point(px, py));
                                    visited[py][px] = true;
                                    
                                }
                            }
                        }
                    }

                    // 3개 이상이면 지우기
                    if(cnt >= 3) {
                        count += cnt;
                        remove = true;
                        visited = new boolean[5][5];

                        queue.add(new Point(mx, my));
                        visited[my][mx] = true;
                        map[my][mx] = 0;

                        while(!queue.isEmpty()) {
                            Point p1 = queue.poll();    
                            
                            for(int r = 0; r<4; r++) {
                                int px = p1.x + X2[r];
                                int py = p1.y + Y2[r];

                                if (px >= 0 && px <5 && py >=0 && py <5 && visited[py][px] == false) {
                                    if(map[py][px] == s) {
                                        
                                        queue.add(new Point(px, py));
                                        visited[py][px] = true;
                                        map[py][px] = 0;
                                        
                                    }
                                }
                            }
                        }
                    }
                }
                
                if(remove == false ) {
                    break;
                }

                for(int col = 0; col < 5; col++) {

                    for (int row = 4; row >=0; row--) {

                        if(map[row][col] == 0) {
                            map[row][col] = NextNum[index++];
                        }
                    }
                }
            }
            bw.write(count + " ");
            count = 0;
        }

        bw.flush();
        bw.close();
        

    }
    
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Value {

        int val;
        int degree;

        int centerX;
        int centerY;

        public Value(int val, int degree, int centerX, int centerY) {
            this.val = val;
            this.degree = degree;
            this.centerX = centerX;
            this.centerY = centerY;
        }



    }

    static void revolve(int x, int y) {
        int v;
        int tv;
        int mx, my;

        v = map[y-1][x-1];
        for(int i=0; i<4; i++) {
            mx = x + X1[i];
            my = y + Y1[i];
            tv = map[my][mx];
            map[my][mx] = v;
            v = tv;
        }

        v = map[y-1][x];
        for (int i=0; i<4; i++){
            mx = x + X2[i];
            my = y + Y2[i];
            tv = map[my][mx];
            map[my][mx] = v;
            v = tv;
        }
        
    }


}