#include <iostream>

using namespace std;

struct node {
    int y, k, cnt;
    node *l;
    node *r;
};

int cnt(node *t) {
    if (t == 0) {
        return 0;
    } else {
        return (*t).cnt;
    }
}

void update(node *t) {
    if (t != 0) {
        (*t).cnt = 1 + cnt((*t).l) + cnt((*t).r);
    }
}

void split(node *t, node *&l, node *&r, int x, int add) {
    if (t == 0) {
        l = 0;
        r = 0;
        return;
    }

    int kol = add + cnt((*t).l);
    if (x <= kol) {
        split((*t).l, l, (*t).l, x, add);
        r = t;
    } else {
        split((*t).r, (*t).r, r, x, add + 1 + cnt((*t).l));
        l = t;
    }
    update(t);
}

void merge(node *&t, node *l, node *r) {
    if (l == 0) {
        t = r;
        return;
    }
    if (r == 0) {
        t = l;
        return;
    }
    if ((*l).y > (*r).y) {
        merge((*l).r, (*l).r, r);
        t = l;
    } else {
        merge((*r).l, l, (*r).l);
        t = r;
    }
    update(t);
}

void out(node *v) {
    if (v == 0) {
        return;
    }
    out((*v).l);
    cout << (*v).k << " ";
    out((*v).r);
}

int main() {
    int n, m;
    cin >> n >> m;
    node *root = new node;
    (*root).k = n;
    (*root).l = 0;
    (*root).r = 0;
    (*root).y = n;
    (*root).cnt = 1;

    for (int i = n - 1; i >= 1; --i) {
        node *w = new node;
        (*w).k = i;
        (*w).l = 0;
        (*w).r = 0;
        (*w).y = rand();
        (*w).cnt = 1;
        merge(root, w, root);
    }

    node *l = 0;
    node *r = 0;
    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        split(root, l, root, a - 1, 0);
        split(root, root, r, b - a + 1, 0);
        merge(root, root, l);
        merge(root, root, r);
    }

    out(root);
    return 0;
}
