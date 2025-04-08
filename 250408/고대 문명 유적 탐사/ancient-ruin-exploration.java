
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int K, M;
	static int[][] map;
	static int[][] visited;
	static Queue<Integer> queue;
	static LinkedList<Integer> nums;
	
	static int[] MX = {1,-1,0,0};
	static int[] MY = {0,0,1, -1};
	
	public static void main(String[] args) throws IOException {
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[5][5];
	
		
		
		for(int i=0; i<5; i++) {
			st = new StringTokenizer(br.readLine());
			
			for(int j=0; j<5; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		nums = new LinkedList<>();
		
		st = new StringTokenizer(br.readLine());
		
		for(int i=0; i<M; i++) {
			nums.add(Integer.parseInt(st.nextToken()));
		}
		
		PriorityQueue<Information> pq = new PriorityQueue<>((o1, o2) -> {
			if(o1.value != o2.value) {
				return o2.value - o1.value;
			} else if (o1.r != o2.r) {
				return o1.r - o2.r;
			} else if (o1.x != o2.x) {
				return o1.x - o2.x;
			} else {
				return o1.y - o2.y;
			}
		});
		
		int[][] original = new int[3][3];
		int[][] nmap = new int[5][5];
		
		for(int k=0; k<K; k++) {
			pq.clear();
			
			int score = 0;
			for(int i=0; i<3; i++) {
				for (int j=0; j<3; j++) {
					
					for(int r=1 ; r<4; r++) {
						
						for(int a= 0;a<3; a++) {
							for(int b = 0; b<3; b++) {
								
								original[a][b] = map[a+i][b+j];
								
							}
						}
						
						for(int a=0; a<5; a++) {
							nmap[a] = Arrays.copyOf(map[a],map[a].length);
							
						}
						
						int[][] rotated = rotate(original, r);
						
						for(int a=0; a<3; a++) {
							for(int b =0 ; b<3; b++) {
								
								nmap[a+i][b+j] = rotated[a][b];
							}
						}
						int v = getFirst(nmap);
						
						pq.add(new Information(v, r, j, i));
					
						
					}
				}
			}
			
		
			
			Information selected = pq.poll();
			
			if(selected.value == 0) {
				break;
			}
			
			
			
			int[][] rotated = new int[3][3];
			for(int a=0; a<3; a++) {
				for(int b=0; b<3; b++) {
					rotated[a][b] = map[a+selected.y][b+selected.x];
				}
			}
			
			int[][] result = rotate(rotated, selected.r);
			
			for(int a=0; a<3; a++) {
				for(int b=0; b<3; b++) {
					map[a+selected.y][b+selected.x] = result[a][b];
				}
			}
			
			
			score += getFirst(map);
			fill();
			while(true) {
				int v = getFirst(map);
				if(v != 0) {
					score += v;
					fill();
				} else {
					break;
				}
			}
			
			System.out.print(score + " ");
			
			
		}
		
		
		
		
	}
	
	static void fill() {
		for(int i=0; i<5; i++) {
			for(int j=4; j>=0; j--) {
				if(map[j][i] != 0) continue;
				map[j][i] = nums.poll();
			}
		}
	}
	
	static class Information {
		int value;
		int r;
		int x;
		int y;
		public Information(int value, int r, int x, int y) {
			super();
			this.value = value;
			this.r = r;
			this.x = x;
			this.y = y;
		}
		
		
	}
	
	static int getFirst(int[][] mymap) {
		
		boolean[][] visited =  new boolean[5][5];;
		boolean[][] remove = new boolean[5][5];
		int cnt = 0;
		queue = new LinkedList<>();
		
		// 3개 이상인지 체크
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(visited[i][j] == true) continue;
				
				cnt = 1;
				int num = mymap[i][j];
				queue = new LinkedList<>();
				queue.add(j);
				queue.add(i);
				
				
				visited[i][j] = true;
				
				
				// 같은 숫자 체크하면서 3개 이상인지 체크
				
				while(!queue.isEmpty()) {
					
					int x = queue.poll();
					int y = queue.poll();
					
					for(int a=0; a<4; a++) {
						int tx = x+MX[a];
						int ty = y + MY[a];
						
						if(tx < 0 || tx >=5 || ty <0 || ty >=5) continue;
						if(visited[ty][tx] == true) continue;
						if(mymap[ty][tx] != num) continue;
						
						queue.add(tx);
						queue.add(ty);
						visited[ty][tx] = true;
						cnt++;
					}
					
				}
				
				
				if(cnt >= 3) {
					remove[i][j] = true;
				}
			}
		}
		
		
		// 3개 이상이었던 것을 사제
		int total = 0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(remove[i][j] == false) continue;
				total++;
				
				int num = mymap[i][j];
				
				queue = new LinkedList<>();
				queue.add(j);
				queue.add(i);
				mymap[i][j] = 0;
				
				while(!queue.isEmpty()) {
					int x = queue.poll();
					int y = queue.poll();
					
					for(int a=0; a<4; a++) {
						int tx = x + MX[a];
						int ty = y + MY[a];
						if(tx < 0 || tx >=5 || ty <0 || ty >=5) continue;
						if(mymap[ty][tx] != num) continue;
						
						queue.add(tx);
						queue.add(ty);
						mymap[ty][tx] = 0;
						total++;
					}
				}
			}
		}
		
		return total;
	}
	
	static int[][] rotate(int[][] smap, int r) {
		
		int N = smap.length;
		int[][] result = smap;
		
		for(int i=0; i<r; i++) {
			int[][] temp = new int[5][5];
			for(int a=0; a<N; a++) {
				for(int b=0; b<N; b++) {
					temp[b][N-a-1] = result[a][b];
				}
			}
			result = temp;
		}
	
		return result;
	}

}
