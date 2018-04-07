//Work program, not mine

#define _CRT_SECURE_NO_WARNINGS
#define _GLIBCXX_DEBUG
//#pragma comment(linker, "/STACK:256000000")

#include <iostream>
#include <fstream>
#include <cstdlib>
#include <cstdio>
#include <cassert>
#include <vector>
#include <algorithm>

using namespace std;

#define TASK ""

struct Binomial_tree {
    Binomial_tree *parent, *son, *sibling;
    int op;
    int rank;

    Binomial_tree(int _op) {
        op = _op;
        parent = son = sibling = nullptr;
        rank = 1;
    }
};

vector<int> val(1);
vector<Binomial_tree *> what_tree(1);
struct Binomial_heap;
vector<Binomial_heap *> what_heap(1);

bool cmp(int op1, int op2) {
    if (val[op1] == val[op2]) {
        return op1 < op2;
    }
    return val[op1] < val[op2];
}

Binomial_tree *tree_merge(Binomial_tree *t1, Binomial_tree *t2) {
    assert(t1->rank == t2->rank);
    if (cmp(t2->op, t1->op)) swap(t1, t2);
    t2->sibling = t1->son;
    t1->son = t2;
    t2->parent = t1;
    t1->rank++;
    return t1;
}

struct Binomial_heap {
    vector<Binomial_tree *> trees;

    Binomial_heap(Binomial_tree *t) {
        trees = {t};
        what_heap[t->op] = this;
    }

    Binomial_heap() {}

    Binomial_tree *get_min() {
        assert(!trees.empty());
        Binomial_tree *ans = trees[0];
        for (Binomial_tree *t : trees) {
            if (cmp(t->op, ans->op)) ans = t;
        }
        return ans;
    }
};

void upd(Binomial_heap *h) {
    for (Binomial_tree *t : h->trees) {
        what_heap[t->op] = h;
        t->parent = t->sibling = nullptr;
    }
}

Binomial_heap *heap_merge(Binomial_heap *h1, Binomial_heap *h2) {
    Binomial_heap *new_heap = new Binomial_heap;
    auto cur1 = h1->trees.begin(), cur2 = h2->trees.begin();
    auto end1 = h1->trees.end(), end2 = h2->trees.end();
    Binomial_tree *buf = nullptr;

    while (cur1 != end1 || cur2 != end2) {
        if (cur1 != end1 && cur2 != end2) {
            if ((*cur2)->rank == (*cur1)->rank) {
                if (buf == nullptr) {
                    buf = tree_merge(*cur1, *cur2);
                    cur1++, cur2++;
                    continue;
                }
                if (buf->rank < (*cur1)->rank) {
                    new_heap->trees.push_back(buf);
                    buf = nullptr;
                    continue;
                }
                if (buf->rank == (*cur1)->rank) {
                    new_heap->trees.push_back(buf);
                    buf = nullptr;
                    continue;
                }
                assert(0);
            }
            if ((*cur2)->rank < (*cur1)->rank) {
                swap(cur1, cur2);
                swap(end1, end2);
            }
        }
        if (cur1 == end1) {
            swap(cur1, cur2);
            swap(end1, end2);
        }
        if (buf == nullptr) {
            new_heap->trees.push_back(*cur1);
            cur1++;
            continue;
        }
        if (buf->rank < (*cur1)->rank) {
            new_heap->trees.push_back(buf);
            buf = nullptr;
            continue;
        }
        if (buf->rank > (*cur1)->rank) {
            new_heap->trees.push_back(*cur1);
            cur1++;
            continue;
        }
        if (buf->rank == (*cur1)->rank) {
            buf = tree_merge(buf, *cur1);
            cur1++;
            continue;
        }
    }
    if (buf != nullptr) {
        new_heap->trees.push_back(buf);
    }
    upd(new_heap);
    return new_heap;
}

Binomial_heap *remove(Binomial_tree *t) {
    Binomial_heap *parent_heap = what_heap[t->op];

    while (t->parent != nullptr) {
        swap(t->op, t->parent->op);
        what_tree[t->op] = t;
        parent_heap = what_heap[t->op];
        t = t->parent;
    }
    Binomial_heap *new_heap = new Binomial_heap;
    for (Binomial_tree *cur = t->son; cur != nullptr; cur = cur->sibling) {
        new_heap->trees.push_back(cur);
    }
    reverse(new_heap->trees.begin(), new_heap->trees.end());
    upd(new_heap);
    auto &order = parent_heap->trees;
    order.erase(find(order.begin(), order.end(), t));
    *parent_heap = *heap_merge(parent_heap, new_heap);
    upd(parent_heap);
    return parent_heap;
}

Binomial_heap **H;

int main() {
    srand(0);
    freopen("test.txt", "w", stderr);
#ifdef _DEBUG
    freopen("input.txt", "r", stdin), freopen("output.txt", "w", stdout);
#else
    //freopen(TASK".in", "r", stdin), freopen(TASK".out", "w", stdout);
#endif
    int n, m, index = 0;
    scanf("%d%d", &n, &m);
    //Binomial_heap **H = new Binomial_heap*[n + 1];
    H = new Binomial_heap *[n + 1];
    for (int i = 0; i < n + 1; i++) {
        H[i] = new Binomial_heap();
    }
    for (int i = 0; i < m; i++) {
        int type;
        scanf("%d", &type);
        switch (type) {
            case 0: {
                index++;
                int v, number;
                scanf("%d%d", &number, &v);
                val.push_back(v);
                what_heap.push_back(H[number]);
                what_tree.push_back(new Binomial_tree(index));
                Binomial_heap *new_heap = new Binomial_heap(what_tree.back());
                H[number] = heap_merge(H[number], new_heap);
                upd(H[number]);
                break;
            }
            case 1: {
                int num1, num2;
                scanf("%d%d", &num1, &num2);
                if (num1 == num2) continue;
                H[num2] = heap_merge(H[num1], H[num2]);
                upd(H[num2]);
                H[num1] = new Binomial_heap;
                break;
            }
            case 2: {
                int op;
                scanf("%d", &op);
                remove(what_tree[op]);
                break;
            }
            case 3: {
                int op, v;
                scanf("%d%d", &op, &v);
                Binomial_heap *buf = remove(what_tree[op]);
                val[op] = v;
                what_tree[op] = new Binomial_tree(op);
                Binomial_heap *new_heap = new Binomial_heap(what_tree[op]);
                *buf = *heap_merge(buf, new_heap);
                upd(buf);
                break;
            }
            case 4: {
                int num;
                scanf("%d", &num);
                int op = H[num]->get_min()->op;
                int minimum = val[op];
                printf("%d\n", minimum);
                break;
            }
            case 5: {
                int num;
                scanf("%d", &num);
                Binomial_tree *t = H[num]->get_min();
                remove(t);
                break;
            }
        }
    }
    return 0;
}
