import java.util.*;
import java.io.*;

public class Main {

    static int R, C, K;
    static int[][] map;
    static Point[] info;
    static int[] destination;
    static boolean[] visited;
    static Queue<Integer> queue;
    static int[] direction = {0,1,2,3};
    static int[] MX = {1,-1, 0, 0};
    static int[] MY = {0, 0, 1, -1};

    static int count;

    public static void main(String[] args) throws IOException {
        // Please write your code here.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[R][C];
        info = new Point[K+1];

        destination = new int[K+1];

        int c, d;
        int golem = 0;
        int row=0;
        count = 0;
       
        for(int i=0; i<K; i++) {
            golem++;
            st = new StringTokenizer(br.readLine());
            c = Integer.parseInt(st.nextToken());
            d = Integer.parseInt(st.nextToken());

            int x = c-1;
            int y = -2;
            // 처음에 갈 수 있는지
            boolean flag = true;
            for (int j=0; j<3; j++) {
                if (goSouth(x, y)) {
                    y = y+1;
                } else if (goWestSouth(x, y)) {
                    y = y+1;
                    x = x-1;
                    d = counterClock(d);

                } else if (goEastSouth(x, y)) {
                    y = y+1;
                    x = x+1;
                    d = clock(d);
                } else {
                    map = new int[R][C];
                    destination = new int[K+1];
                    flag = false;
                    break;
                }

            }

            //이동

            while(flag) {
                //System.out.println("이동 전: " + x + ", " + y );
                if (goSouth(x, y)) {
                    
                    y = y+1;
                } else if (goWestSouth(x, y)) {
                    y = y+1;
                    x = x-1;
                    d = counterClock(d);

                } else if (goEastSouth(x, y)) {
                    y = y+1;
                    x = x+1;
                    d = clock(d);
                } else {
                    map[y][x] = golem;
                    map[y+1][x] = golem;
                    map[y][x-1] = golem;
                    map[y][x+1] = golem;
                    map[y-1][x] = golem;
                    
                    // 정보 표시 (골렘 중앙, 출구)

                    info[golem] = new Point(x, y, d);


                    // map, exit을 참고하여 bfs로 탐색
                    visited = new boolean[K+1];

                    queue = new LinkedList<>();

                    queue.add(golem);

                    int maxRow = 0;

                    while( !queue.isEmpty()) {

                        int n = queue.poll();

                        Point p = info[n];

                        if (!checkGolem(p.x, p.y, p.d)) {
                            maxRow = Math.max(maxRow, p.y+1);


                        } else {
                            maxRow = Math.max(maxRow, p.y+1);
                        }

                       

                    }

                    count += maxRow+1;
                    break;

                    

                    // 출구 주위에 골렘이 있는지 확인하는 함수.. 있으면 그 골렘 방문 표시, 없으면 끝 -> 최댓값 비교 후 저장

                

                    
                }
            }
        }

        System.out.println(count);
    


    }
    
    static class Point {

        int x;
        int y;
        int d;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }


    }

    static boolean checkGolem(int x, int y, int d) {

        boolean flag = false;

        if ( d == 0) {
            y -= 1;
        } else if ( d == 1) {
            x += 1;
        } else if (d == 2 ) {
            y += 1;
        } else if (d == 3) {
            x -= 1;
        }

        // x, y는 출구 위치
        for (int i=0;  i<4; i++) {
            int tx= x+ MX[i];
            int ty = y + MY[i];

            if ( tx >=0 && tx < C-1 && ty >= 0 && ty < R-1) {
                int n = map[ty][tx];
                if (n != map[y][x] && n != 0 && visited[n] == false) {
                    queue.add( n);
                    visited[n] = true;
                    flag = true;

                }
            }
        }

        return flag;

    }


    static boolean goSouth(int x, int y) {
        if(x < 1 || x >C-1 || y == R -2) {
            return false;
        }
        if (y >= -1 ){
            if (map[y+2][x] == 0 && map[y+1][x-1]==0 && map[y+1][x+1] == 0) {
                return true;
            } else {
                return false;
            }

        } else {
            if (map[y+2][x] == 0 ) {
                return true;
            } else {
                return false;
            }
        }
        
    }

    static boolean goEastSouth(int x, int y) {

        if ( x>= C-2 || y == R -2) {
            return false;
        }
        if (y >= 1) {
            if(map[y-1][x+1] == 0 && map[y][x+2] == 0 && map[y+1][x+1] == 0 && map[y+2][x+1] == 0 && map[y+1][x+2] == 0 ){
                return true;
            } else {
                return false;
            }
        } else if (y>= 0) {

            if(map[y][x+2] == 0 && map[y+1][x+1] == 0 && map[y+2][x+1] == 0 && map[y+1][x+2] == 0 ){
                return true;
            } else {
                return false;
            }
        } else if (y>= -1) {
            if( map[y+1][x+1] == 0 && map[y+2][x+1] == 0 && map[y+1][x+2] == 0 ){
                return true;
            } else {
                return false;
            }
        } else {
            if( map[y+2][x+1] == 0  ){
                return true;
            } else {
                return false;
            }
        }
        

        
    }

    static boolean goWestSouth(int x, int y) {

        if( x<= 1 || y == R -2 ) {
            return false;
        }

        if(y>=1) {
            if(map[y-1][x-1] == 0 && map[y][x-2] == 0 && map[y+1][x-1] == 0 && map[y+2][x-1] == 0 && map[y+1][x-2] == 0) {
                return true;
             } else {
                return false;
            }

        } else if (y>= 0 ) {
            if( map[y][x-2] == 0 && map[y+1][x-1] == 0 && map[y+2][x-1] == 0 && map[y+1][x-2] == 0) {
                return true;
             } else {
                return false;
            }
        } else if (y >= -1) {
             if(  map[y+1][x-1] == 0 && map[y+2][x-1] == 0 && map[y+1][x-2] == 0) {
                return true;
             } else {
                return false;
            }
        } else {
             if(   map[y+2][x-1] == 0 ) {
                return true;
             } else {
                return false;
            }
        }

        
    }

    static int enter(int c) {
        
       
        if (goSouth(c, -2)){
            return c;
        } else if (goWestSouth(c, -2)) {
            return c-1;
        } else if ( goEastSouth(c, -2)) {
            return c+1;
        } else {
            return 0;
        }

    } 

    // 동서남북 함수
    static int clock(int n) {
        if(n == 3) {
            return 0;
        } else {
            return n+1;
        }
    }

    static int counterClock(int n) {
        if (n== 0) {
            return 3;
        } else {
            return n-1;
        }
    }
}