#include <stdio.h>

#define MAX_R 75
#define MAX_C 74
#define MAX_K 1001

const int WALL = -1;
const int EMPTY = 0;
const int _dx[] = {-1, 0, 1, 0};
const int _dy[] = {0, 1, 0, -1};

int _r;
int _c;
int _sum;
int _stack_idx;
int _low[MAX_K];
int _stack[MAX_K];
int _visited[MAX_K];
int _out[MAX_K][2];
int _map[MAX_R][MAX_C];

int
max(int num1, int num2) {
	return num1 > num2 ? num1 : num2;
}

int
get_low(int idx, int out_x, int out_y)
{
	int i;
	int n_idx;

	_visited[idx] = 1;
	_stack[_stack_idx++] = idx;
	for (i = 0; i < 4; i++) {
		n_idx = _map[out_x + _dx[i]][out_y + _dy[i]];
		if (n_idx == WALL || n_idx == EMPTY || _visited[n_idx]) {
			continue;
		}
		_low[idx] = max(_low[idx], get_low(n_idx, _out[n_idx][0], _out[n_idx][1]));
	}
	return _low[idx];
}

void
insert(int idx, int x, int y, int d) {
	int i;
	int j;

	for (;;) {
		if (_map[x + 1][y - 1] == EMPTY
				&& _map[x + 1][y + 1] == EMPTY
				&& _map[x + 2][y] == EMPTY) {
			x++;
		} else if (_map[x - 1][y - 1] == EMPTY
				&& _map[x][y - 2] == EMPTY
				&& _map[x + 1][y - 1] == EMPTY
				&& _map[x + 1][y - 2] == EMPTY
				&& _map[x + 2][y - 1] == EMPTY) {
			x++;
			y--;
			d = (d + 3) & 3;
		} else if (_map[x - 1][y + 1] == EMPTY
				&& _map[x][y + 2] == EMPTY
				&& _map[x + 1][y + 1] == EMPTY
				&& _map[x + 2][y + 1] == EMPTY
				&& _map[x + 1][y + 2] == EMPTY) {
			x++;
			y++;
			d = (d + 1) & 3;
		} else {
			break;
		}
	}
	if (x < 4) {
		for (i = 3; i < _r + 3; i++) {
			for (j = 2; j <= _c + 1; j++) {
				_map[i][j] = EMPTY;
			}
		}
		return;
	}
	_map[x][y] = idx;
	_map[x - 1][y] = idx;
	_map[x][y + 1] = idx;
	_map[x + 1][y] = idx;
	_map[x][y - 1] = idx;
	_low[idx] = x - 1;
	_out[idx][0] = x + _dx[d];
	_out[idx][1] = y + _dy[d];
	_sum += get_low(idx, _out[idx][0], _out[idx][1]);
	while (_stack_idx) {
		_visited[_stack[--_stack_idx]] = 0;
	}
}

int
main() {
	int k;
	int y;
	int d;
	int i;
	int j;

	scanf("%d %d %d", &_r, &_c, &k);
	for (i = 0; i < _r + 3; i++) {
		_map[i][0] = WALL;
		_map[i][1] = WALL;
		for (j = 2; j <= _c + 1; j++) {
			_map[i][j] = EMPTY;
		}
		_map[i][_c + 2] = WALL;
		_map[i][_c + 3] = WALL;
	}
	for (i = 0; i < _c + 4; i++) {
		_map[_r + 3][i] = WALL;
		_map[_r + 4][i] = WALL;
	}
	_sum = 0;
	for (i = 0; i <= k; i++) {
		_visited[i] = 0;
	}
	_stack_idx = 0;
	for (i = 1; i <= k; i++) {
		scanf("%d %d", &y, &d);
		insert(i, 1, y + 1, d);
	}
	printf("%d", _sum);
}