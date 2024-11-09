import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	private static final int INF = Integer.MAX_VALUE;
	private static final int FAIL = -1;
	private static final int EMPTY = '0';
	private static final int WALL = '1';
	private static final int START = '2';
	private static final int CUBE = '3';
	private static final int OUT = '4';

	private static final class Cell {
		int time;
		int dist;
		int type;
		Cell east;
		Cell west;
		Cell south;
		Cell north;

		Cell(int type) {
			this.type = type;
			dist = INF;
			if (type == WALL) {
				time = -1;
			} else {
				if (type == START) {
					start = this;
					dist = 0;
				}
				time = INF;
			}
		}
	}

	private static Cell start;
	private static Cell[][] ceil;
	private static Cell[][] floor;
	private static Cell[][] eastWall;
	private static Cell[][] westWall;
	private static Cell[][] southWall;
	private static Cell[][] northWall;
	private static BufferedReader br;

	private static final void fillWall(int m, Cell[][] wall) throws IOException {
		int i;
		int j;

		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				wall[i][j] = new Cell(br.read());
				br.read();
			}
		}
	}

	private static final int bfs() {
		Cell curr;
		Cell next;
		ArrayDeque<Cell> q;

		q = new ArrayDeque<>();
		q.addLast(start);
		while (!q.isEmpty()) {
			curr = q.pollFirst();
			next = curr.east;
			if (curr.dist + 1 < next.dist && curr.dist + 1 < next.time) {
				next.dist = curr.dist + 1;
				if (next.type == OUT) {
					return next.dist;
				}
				q.addLast(next);
			}
			next = curr.west;
			if (curr.dist + 1 < next.dist && curr.dist + 1 < next.time) {
				next.dist = curr.dist + 1;
				if (next.type == OUT) {
					return next.dist;
				}
				q.addLast(next);
			}
			next = curr.south;
			if (curr.dist + 1 < next.dist && curr.dist + 1 < next.time) {
				next.dist = curr.dist + 1;
				if (next.type == OUT) {
					return next.dist;
				}
				q.addLast(next);
			}
			next = curr.north;
			if (curr.dist + 1 < next.dist && curr.dist + 1 < next.time) {
				next.dist = curr.dist + 1;
				if (next.type == OUT) {
					return next.dist;
				}
				q.addLast(next);
			}
		}
		return FAIL;
	}

	public static void main(String[] args) throws IOException {
		int n;
		int m;
		int f;
		int i;
		int j;
		int r;
		int c;
		int d;
		int v;
		int size;
		int eastCnt;
		int westCnt;
		int southCnt;
		int northCnt;
		Cell cell;
		StringTokenizer st;

		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine(), " ", false);
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		f = Integer.parseInt(st.nextToken());
		size = n + 2;
		floor = new Cell[size][size];
		for (i = 0; i < size; i++) {
			floor[0][i] = new Cell(WALL);
			floor[n + 1][i] = new Cell(WALL);
		}
		for (i = 1; i <= n; i++) {
			floor[i][0] = new Cell(WALL);
			for (j = 1; j <= n; j++) {
				floor[i][j] = new Cell(br.read());
				br.read();
			}
			floor[i][n + 1] = new Cell(WALL);
		}
		fillWall(m, eastWall = new Cell[m][m]);
		fillWall(m, westWall = new Cell[m][m]);
		fillWall(m, southWall = new Cell[m][m]);
		fillWall(m, northWall = new Cell[m][m]);
		fillWall(m, ceil = new Cell[m][m]);
		eastCnt = m - 1;
		westCnt = 0;
		southCnt = 0;
		northCnt = m - 1;
		for (i = 1; i < size; i++) {
			for (j = 1; j < size; j++) {
				if (floor[i][j].type == CUBE && floor[i - 1][j].type != CUBE) {
					floor[i - 1][j].south = northWall[m - 1][northCnt];
					northWall[m - 1][northCnt].north = floor[i - 1][j];
					northCnt--;
				} else if (floor[i - 1][j].type == CUBE && floor[i][j].type != CUBE) {
					floor[i][j].north = southWall[m - 1][southCnt];
					southWall[m - 1][southCnt].south = floor[i][j];
					southCnt++;
				} else {
					floor[i - 1][j].south = floor[i][j];
					floor[i][j].north = floor[i - 1][j];
				}
				if (floor[i][j].type == CUBE && floor[i][j - 1].type != CUBE) {
					floor[i][j - 1].east = westWall[m - 1][westCnt];
					westWall[m - 1][westCnt].west = floor[i][j - 1];
					westCnt++;
				} else if (floor[i][j - 1].type == CUBE && floor[i][j].type != CUBE) {
					floor[i][j].west = eastWall[m - 1][eastCnt];
					eastWall[m - 1][eastCnt].east = floor[i][j];
					eastCnt--;
				} else {
					floor[i][j - 1].east = floor[i][j];
					floor[i][j].west = floor[i][j - 1];
				}
			}
		}
		for (i = 0; i < m; i++) {
			ceil[0][i].north = northWall[0][m - 1 - i];
			northWall[0][m - 1 - i].south = ceil[0][i];
			ceil[m - 1][i].south = southWall[0][i];
			southWall[0][i].north = ceil[m - 1][i];
			ceil[i][0].west = westWall[0][i];
			westWall[0][i].east = ceil[i][0];
			ceil[i][m - 1].east = eastWall[0][m - 1 - i];
			eastWall[0][m - 1 - i].west = ceil[i][m - 1];
			southWall[i][m - 1].east = eastWall[i][0];
			eastWall[i][0].south = southWall[i][m - 1];
			eastWall[i][m - 1].north = northWall[i][0];
			northWall[i][0].east = eastWall[i][m - 1];
			northWall[i][m - 1].west = westWall[i][0];
			westWall[i][0].north = northWall[i][m - 1];
			westWall[i][m - 1].south = southWall[i][0];
			southWall[i][0].west = westWall[i][m - 1];
			for (j = 0; j < m; j++) {
				if (i > 0) {
					ceil[i - 1][j].south = ceil[i][j];
					ceil[i][j].north = ceil[i - 1][j];
					southWall[i - 1][j].south = southWall[i][j];
					southWall[i][j].north = southWall[i - 1][j];
					eastWall[i - 1][j].east = eastWall[i][j];
					eastWall[i][j].west = eastWall[i - 1][j];
					westWall[i - 1][j].west = westWall[i][j];
					westWall[i][j].east = westWall[i - 1][j];
					northWall[i - 1][j].north = northWall[i][j];
					northWall[i][j].south = northWall[i - 1][j];
				}
				if (j > 0) {
					ceil[i][j - 1].east = ceil[i][j];
					ceil[i][j].west = ceil[i][j - 1];
					southWall[i][j - 1].east = southWall[i][j];
					southWall[i][j].west = southWall[i][j - 1];
					eastWall[i][j - 1].north = eastWall[i][j];
					eastWall[i][j].south = eastWall[i][j - 1];
					westWall[i][j - 1].south = westWall[i][j];
					westWall[i][j].north = westWall[i][j - 1];
					northWall[i][j - 1].west = northWall[i][j];
					northWall[i][j].east = northWall[i][j - 1];
				}
			}
		}
		for (i = 0; i < f; i++) {
			st = new StringTokenizer(br.readLine(), " ", false);
			r = Integer.parseInt(st.nextToken()) + 1;
			c = Integer.parseInt(st.nextToken()) + 1;
			d = Integer.parseInt(st.nextToken());
			v = Integer.parseInt(st.nextToken());
			cell = floor[r][c];
			if (d == 0) {
				for (j = 0; cell.type != WALL; j += v) {
					cell.time = Math.min(cell.time, j);
					cell = cell.east;
				}
			} else if (d == 1) {
				for (j = 0; cell.type != WALL; j += v) {
					cell.time = Math.min(cell.time, j);
					cell = cell.west;
				}
			} else if (d == 2) {
				for (j = 0; cell.type != WALL; j += v) {
					cell.time = Math.min(cell.time, j);
					cell = cell.south;
				}
			} else if (d == 3) {
				for (j = 0; cell.type != WALL; j += v) {
					cell.time = Math.min(cell.time, j);
					cell = cell.north;
				}
			}
		}
		System.out.print(bfs());
	}
}