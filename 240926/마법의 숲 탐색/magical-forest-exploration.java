import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	private static final int EMPTY = 0;
	private static final int WALL = -1;
	private static final int[] dx = {-1, 0, 1, 0};
	private static final int[] dy = {0, 1, 0, -1};

	private static int r;
	private static int c;
	private static int x;
	private static int y;
	private static int d;
	private static int sum;
	private static int[] low;
	private static int[][] map;

	private static void getLow(int idx, int nx, int ny) {
		int i;
		int nnx;
		int nny;

		for (i = 0; i < 4; i++) {
			nnx = nx + dx[i];
			nny = ny + dy[i];
			if (map[nnx][nny] == WALL || map[nnx][nny] == EMPTY) {
				continue;
			}
			low[idx] = Math.max(low[idx], low[map[nnx][nny]]);
		}
	}

	private static void insert(int idx) {
		int i;
		int j;

		for (;;) {
			if (map[x + 1][y - 1] == EMPTY
					&& map[x + 1][y + 1] == EMPTY
					&& map[x + 2][y] == EMPTY) {
				x++;
			} else if (map[x - 1][y - 1] == EMPTY
					&& map[x][y - 2] == EMPTY
					&& map[x + 1][y - 1] == EMPTY
					&& map[x + 1][y - 2] == EMPTY
					&& map[x + 2][y - 1] == EMPTY) {
				x++;
				y--;
				d = (d + 3) & 3;
			} else if (map[x - 1][y + 1] == EMPTY
					&& map[x][y + 2] == EMPTY
					&& map[x + 1][y + 1] == EMPTY
					&& map[x + 2][y + 1] == EMPTY
					&& map[x + 1][y + 2] == EMPTY) {
				x++;
				y++;
				d = (d + 1) & 3;
			} else {
				break;
			}
		}
		if (x < 4) {
			for (i = 3; i < r + 3; i++) {
				System.arraycopy(map[0], 2, map[i], 2, c);
			}
			return;
		}
		map[x][y] = idx;
		map[x - 1][y] = idx;
		map[x][y + 1] = idx;
		map[x + 1][y] = idx;
		map[x][y - 1] = idx;
		low[idx] = x - 1;
		getLow(idx, x + dx[d], y + dy[d]);
		sum += low[idx];
	}

	public static void main(String[] args) throws IOException {
		int k;
		int i;
		BufferedReader br;
		StringTokenizer st;

		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine(), " ", false);
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		map = new int[r + 5][c + 4];
		for (i = 0; i < r + 3; i++) {
			map[i][0] = WALL;
			map[i][1] = WALL;
			map[i][c + 2] = WALL;
			map[i][c + 3] = WALL;
		}
		for (i = 0; i < c + 4; i++) {
			map[r + 3][i] = WALL;
			map[r + 4][i] = WALL;
		}
		sum = 0;
		low = new int[k + 1];
		for (i = 1; i <= k; i++) {
			st = new StringTokenizer(br.readLine(), " ", false);
			x = 1;
			y = Integer.parseInt(st.nextToken()) + 1;
			d = Integer.parseInt(st.nextToken());
			insert(i);
		}
		System.out.print(sum);
	}
}