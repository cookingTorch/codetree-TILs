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
struct node_s {
    int color;
    int max_depth;
    node_t *children;
    node_t *next;
};

int _sum;
node_t *_nodes[MAX_ID];

node_t *
new_node(int color, int max_depth)
{
    node_t *node;

    node = (node_t *) malloc(sizeof(node_t));
    node->color = color;
    node->max_depth = max_depth;
    node->children = NULL;
    return node;
}

void
add_node(node_t *parent, int m_id, int color, int max_depth)
{
    if (parent->max_depth == 1) {
        return;
    }
    _nodes[m_id] = new_node(color, max_depth < parent->max_depth - 1 ? max_depth : parent->max_depth - 1);
    _nodes[m_id]->next = parent->children;
    parent->children = _nodes[m_id];
}

void
change_color(node_t *curr, int color)
{
    node_t *child;

    curr->color = color;
    for (child = curr->children; child; child = child->next) {
        change_color(child, color);
    }
}

int
dfs(node_t *curr)
{
    int i;
    int cnt;
    int colors;
    node_t *child;

    colors = 1 << curr->color - 1;
    for (child = curr->children; child; child = child->next) {
        colors |= dfs(child);
    }
    cnt = 0;
    for (i = colors; i; i >>= 1) {
        if (i & 1) {
            cnt++;
        }
    }
    _sum += cnt * cnt;
    return colors;
}

int
get_score()
{
    node_t *child;

    _sum = 0;
    for (child = _nodes[ROOT]->children; child; child = child->next) {
        dfs(child);
    }
    return _sum;
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
    
    _nodes[0] = new_node(0, INF);
    scanf("%d", &q);
    while (q--) {
        scanf(" %c00", &query);
        if (query == QUERY200) {
            scanf("%d %d", &m_id, &color);
            change_color(_nodes[m_id], color);
        } else if (query == QUERY100) {
            scanf("%d %d %d %d", &m_id, &p_id, &color, &max_depth);
            add_node(_nodes[p_id == IS_ROOT ? ROOT : p_id], m_id, color, max_depth);
        } else if (query == QUERY300) {
            scanf("%d", &m_id);
            printf("%d\n", _nodes[m_id]->color);
        } else {
            printf("%d\n", get_score());
        }
    }
}