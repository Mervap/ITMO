#include <iostream>
#include <vector>

using namespace std;

struct node {
    long long y, k, cnt;
    bool f;
    node *p;
    node *l;
    node *r;
    node *nx;
};

node *get(node *t) {
    if (t->nx != t) {
        t->nx = get(t->nx);
    }
    return t->nx;
}

long long randll() {
    long long res = rand();
    for (int i = 0; i < 3; i++) {
        res <<= 16;
        res += rand();
    }
    return res;
}

int cnt(node *t) {
    if (t == 0) {
        return 0;
    } else {
        return t->cnt;
    }
}

void update(node *t) {
    if (t != 0) {
        if (t->l != 0) {
            t->l->p = t;
        }
        if (t->r != 0) {
            t->r->p = t;
        }

        t->p = 0;

        t->cnt = 1 + cnt(t->l) + cnt(t->r);
    }
}

void split(node *t, node *&l, node *&r, long long x, long long add) {
    if (t == 0) {
        l = 0;
        r = 0;
        return;
    }

    int kol = add + cnt(t->l);
    if (x <= kol) {
        split(t->l, l, t->l, x, add);
        r = t;
    } else {
        split(t->r, t->r, r, x, add + 1 + cnt(t->l));
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
    if (l->y > r->y) {
        merge(l->r, l->r, r);
        t = l;
    } else {
        merge(r->l, l, r->l);
        t = r;
    }
    update(t);
}

long long find(node *t) {
    long long n = cnt(t->l) + 1;
    node *p;
    while (t->p != 0) {
        p = t->p;
        if (t == p->r) {
            n += cnt(p->l) + 1;
        }
        t = p;
    }

    return n;
}

node *gen(long long x) {
    node *t = new node;
    t->k = x;
    t->l = 0;
    t->r = 0;
    t->p = 0;
    t->f = 0;
    t->nx = t;
    t->y = randll();
    t->cnt = 1;
    t->r = 0;

    return t;
}

void out(node *v, vector<long long> &ans) {
    if (v == 0) {
        return;
    }
    out(v->l, ans);
    ans.push_back(v->k);
    out(v->r, ans);
}

int main() {

    cin.tie(0);
    cout.tie(0);

    int n, m;
    cin >> n >> m;

    node *root = gen(0);

    for (int i = 1; i <= m + n + 1; ++i) {
        node *w = gen(0);
        merge(root, w, root);
    }

    node *l = 0;
    node *r = 0;
    long long a;
    for (int i = 1; i <= n; ++i) {
        cin >> a;
        split(root, l, root, a - 1, 0);
        split(root, root, r, 1, 0);

        node *t = r;
        while (t->l != 0) {
            t = t->l;
        }

        if (root->k == 0) {
            root->k = i;
            root->nx = get(t);
            merge(root, l, root);
            merge(root, root, r);
        } else {
            node *nx = get(root);
            merge(root, root, r);
            node *w = gen(i);
            w->nx = get(t);
            merge(l, l, w);
            merge(root, l, root);


            int b = find(nx);
            split(root, l, root, b - 1, 0);
            split(root, root, r, 1, 0);

            t = r;
            while (t->l != 0) {
                t = t->l;
            }

            root->nx = get(t);
            merge(root, l, r);
        }

    }

    vector<long long> ans;
    out(root, ans);

    n = ans.size() - 1;
    while (ans[n] == 0) {
        --n;
    }

    cout << n + 1 << "\n";
    for (int i = 0; i <= n; ++i) {
        cout << ans[i] << " ";
    }
    return 0;
}
