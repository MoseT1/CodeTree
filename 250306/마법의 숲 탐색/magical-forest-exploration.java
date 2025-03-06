import java.util.*;
import java.io.*;

public class Main {

    static int R, C, K;
    static int[][] map;
    static int[] destination;
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

        destination = new int[K+1];

        int c, d;
        int golem = 0;
        int row=0;
       
        for(int i=0; i<K; i++) {
            golem++;
            st = new StringTokenizer(br.readLine());
            c = Integer.parseInt(st.nextToken());
            d = Integer.parseInt(st.nextToken());

            int x = c-1;
            int y = -2;
            // 처음에 갈 수 있는지
            boolean flag = true;
            for (int j=0; j<2; j++) {
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
                    if(y>=1) {
                        map[y-1][x] = golem;
                    }

                    // 출구의 주위에 본인 아닌 0 아닌 숫자가 있는지 확인
                    if (d == 0 ) {
                       row =  check(x, y-1, y);
                        
                    } else if (d ==1) {
                        row =  check(x+1, y, y);
                    } else if (d==2) {
                        row = check(x, y+1, y);
                    } else if (d==3) {
                        row = check(x-1, y, y);
                    }

                    destination[golem] = row;
                    count += row+1;
                    //System.out.println(count);

                    break;
                }
            }
        }

        System.out.println(count);
    


    }

    static int check( int x, int y, int originalY) {
        
        int maxRow = originalY+1;

        for (int i=0; i<4; i++) {
            int tx = x + MX[i];
            int ty = y + MY[i];

            if(tx>= 0 && ty >= 0 && tx <C && ty <R) {
                if (map[ty][tx] != 0) {
                    int n = map[ty][tx];
                    maxRow = Math.max(destination[n], maxRow);
                }
            }
        }

        return maxRow;




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