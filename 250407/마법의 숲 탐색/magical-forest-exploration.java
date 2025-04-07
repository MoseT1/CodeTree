
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int[][] map;
	static int[][] exits;
	static boolean[][] visited;
	
	
	static Golemn[] info;
	static int R, C, K;
	static int c;
	static int d;
	
	static int[] MX = {-1, 1, 0, 0};
	static int[] MY = {0, 0, 1, -1};
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		map = new int[R+4][C+2];
		exits = new int[R+4][C+2];
		
		visited = new boolean[R+4][C+2];
		
		//초기화
		initMap();
		info = new Golemn[K+2];
		int sum = 0;
		
		
		for(int i=1; i<= K; i++) {
			
			visited = new boolean[R+4][C+2];
			
			
			st = new StringTokenizer(br.readLine());
			c = Integer.parseInt(st.nextToken());
			d = Integer.parseInt(st.nextToken());
			int golemnNum = i+1;
			
			info[golemnNum] = new Golemn(c, 1, d);
			
			Golemn golemn = info[golemnNum];
			
			golemn.move();
			
			if(golemn.y<4) {
				initMap();
				continue;
			}
			
			// 지도에 기록
			int y = golemn.y;
			int x = golemn.x;
			int exit = golemn.exit;
			map[y][x] = golemnNum;
			map[y-1][x] = golemnNum;
			map[y+1][x] = golemnNum;
			map[y][x-1] = golemnNum;
			map[y][x+1] = golemnNum;
			
			if(exit == 0) {
				exits[y-1][x] = golemnNum;
			} else if (exit == 1) {
				exits[y][x+1] = golemnNum;
			} else if (exit == 2) {
				exits[y+1][x] = golemnNum;
			} else {
				exits[y][x-1] = golemnNum;
			}
			
			// 정령 이동
			
			
			Queue<Integer> queue = new LinkedList<>();
			
			queue.add(x);
			queue.add(y);
			visited[y][x] = true;
			
			int maxY = y;
			
			while(!queue.isEmpty())  {
				
				int x2 = queue.poll();
				int y2 = queue.poll();
				
				if(y2 > maxY) {
					maxY = y2;
				}
				
				if(exits[y2][x2] == 0) {
					for(int d=0; d<4; d++) {
						int tx = x2 + MX[d];
						int ty = y2 + MY[d];
						
						if(map[ty][tx] == 1) continue;
						if(visited[ty][tx] == true) continue;
						if(map[ty][tx] != map[y2][x2]) continue;
						
						visited[ty][tx] = true;
						queue.add(tx);
						queue.add(ty);
					}
				} else {
					for(int d=0; d<4; d++) {
						int tx = x2 + MX[d];
						int ty = y2 + MY[d];
						
						if(map[ty][tx] ==1) continue;
						if(map[ty][tx] == 0) continue;
						if(visited[ty][tx] == true) continue;
						
						visited[ty][tx] = true;
						queue.add(tx);
						queue.add(ty);
					}
				}
				
			}
			
			sum+= maxY-2;
		}
		
		System.out.println(sum);
		

	}
	
	static void initMap() {
		map = new int[R+4][C+2];
		for(int i=0; i<R+4; i++) {
			map[i][0] = 1;
			map[i][C+1] = 1;
		}
		
		for(int i=0; i<C+2; i++) {
			map[R+3][i] = 1;
		}
		
		exits = new int[R+4][C+2];
		
		visited = new boolean[R+4][C+2];
	}
	
	

	static class Golemn {
		int x;
		int y;
		int exit;
		
		
		public Golemn(int x, int y, int exit) {
			super();
			this.x = x;
			this.y = y;
			this.exit = exit;
		}
		
		void move() {
			
			while(true) {
				
				// 이동
				if(map[y+1][x-1] == 0 && map[y+2][x] == 0 && map[y+1][x+1] == 0) {
					y += 1;
				} else if (map[y-1][x-1] == 0 && map[y][x-2] == 0 && map[y+1][x-1] == 0 && map[y+1][x-2] == 0 && map[y+2][x-1] == 0) {
					x -= 1;
					y += 1;
					exit = counterclock(exit);
				} else if (map[y-1][x+1] == 0 && map[y][x+2] == 0 && map[y+1][x+1] == 0 && map[y+1][x+2] == 0 && map[y+2][x+1] == 0) {
					x += 1;
					y += 1;
					exit = clock(exit);
				} else {
					break;
				}
			}
			
		}

		private int clock(int d) {
			// TODO Auto-generated method stub
			
			if(d == 3) {
				d = 0;
			} else {
				d++;
			}
			return d;
		}

		private int counterclock(int d) {
			// TODO Auto-generated method stub
			
			if(d == 0) {
				d = 3;
			} else {
				d--;
			}
			return d;
		}
		
		
	}
}


