#include <stdio.h>
#include <stdlib.h>

#define MAX_ID 100001

const int INF = 2147483647;
const int IS_ROOT = -1;
const int ROOT = 0;
const char QUERY100 = '1';
const char QUERY200 = '2';
const char QUERY300 = '3';

typedef struct node_s node_t;
struct node_s { // 노드
    int color;
    int color_time;
    int max_depth;
    node_t *parent;
    node_t *children; // 자식 노드들 (Linked List)
    node_t *next; // Linked List 상에서 다음 노드
};

int _sum;
int _time;
node_t *_root;
node_t *_nodes[MAX_ID]; // m_id-node 매핑

node_t *
new_node(int color, int max_depth) // 인스턴스 생성
{
    node_t *node;

    node = (node_t *) malloc(sizeof(node_t));
    node->color = color;
    node->color_time = _time++;
    node->max_depth = max_depth;
    node->children = NULL;
    return node;
}

void
add_node(node_t *parent, int m_id, int color, int max_depth) // 노드 추가
{
    if (parent->max_depth == 1) { // 부모의 최대 깊이 제한
        return;
    } // 자식 노드의 최대 깊이 = min(입력값, 부모 노드의 최대 깊이 - 1)
    _nodes[m_id] = new_node(color, max_depth < parent->max_depth - 1 ? max_depth : parent->max_depth - 1);
    _nodes[m_id]->next = parent->children; // 부모 노드의 자식 Linked List 앞쪽에 삽입
    parent->children = _nodes[m_id];
    _nodes[m_id]->parent = parent;
}

void
change_color(node_t *curr, int color) // 색깔 변경
{
    curr->color = color; // 색깔 설정
    curr->color_time = _time++; // 색깔이 지정된 시간
}

int
get_color(node_t *curr) // 색깔 조회
{
    int max;
    int color;

    max = 0;
    for (; curr != _root; curr = curr->parent) { // 부모 순회
        if (curr->color_time > max) { // 가장 최신 색깔
            max = curr->color_time;
            color = curr->color;
        }
    }
    return color; // 최신 색깔 반환
}

char
dfs(node_t *curr, int color, int time)
{
    int i;
    int cnt;
    char colors;
    node_t *child;

    if (time > curr->color_time) { // 색깔 갱신 필요
        curr->color = color; // 색깔 갱신
        curr->color_time = time;
    } else {
        color = curr->color;
        time = curr->color_time;
    }
    colors = 1 << curr->color - 1; // 현재 노드 color 비트 위치로 변환
    for (child = curr->children; child; child = child->next) { // 자식 노드 순회
        colors |= dfs(child, color, time); // 비트 OR합
    }
    cnt = 0; // 가치 = OR합에서의 1 비트 개수
    for (i = colors; i; i >>= 1) {
        if (i & 1) { // 1 비트 카운트
            cnt++;
        }
    }
    _sum += cnt * cnt; // 가치 제곱
    return colors; // OR합 반환
}

int
get_score() // 점수 조회
{
    node_t *child;

    _sum = 0;
    for (child = _nodes[ROOT]->children; child; child = child->next) { // 더미 노드에서 자식(루트 노드들) 순회
        dfs(child, ROOT, ROOT); // 각 루트 노드에서 DFS
    }
    return _sum; // 가치 제곱 합
}

int
main()
{
    int q;
    int m_id;
    int p_id;
    int color;
    int max_depth;
    char query;

    _root = _nodes[0] = new_node(ROOT, INF); // 더미 노드 (모든 루트 노드의 부모 노드)
    _time = 0; // 시계
    scanf("%d", &q);
    while (q--) {
        scanf(" %c00", &query); // if문 chaining은 최대 실행 수 많은 쿼리 순으로 배치
        if (query == QUERY200) { // 200 쿼리
            scanf("%d %d", &m_id, &color);
            change_color(_nodes[m_id], color); // 색깔 변경
        } else if (query == QUERY100) { // 100 쿼리
            scanf("%d %d %d %d", &m_id, &p_id, &color, &max_depth); // p_id == -1이면 0으로 변환
            add_node(_nodes[p_id == IS_ROOT ? ROOT : p_id], m_id, color, max_depth); // 노드 추가
        } else if (query == QUERY300) { // 300 쿼리
            scanf("%d", &m_id);
            printf("%d\n", get_color(_nodes[m_id])); // 색깔 조회
        } else { // 400 쿼리
            printf("%d\n", get_score()); // 점수 조회
        }
    }
}