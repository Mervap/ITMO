#include <iostream>
#include <vector>
#include <algorithm>

#define f first
#define s second

using namespace std;

struct node {
    int x, y, n;

    node *l;
    node *r;
    node *p;
};

struct answer {
    int p, l, r;
};

node *gen(int x, int y, int n, node *l, node *r, node *p) {
    node *nw = new node;
    nw->x = x;
    nw->y = y;
    nw->n = n;
    nw->l = l;
    nw->r = r;
    nw->p = p;
    return nw;
}

int num(node *t) {
    if (t == 0) {
        return 0;
    } else {
        return t->n;
    }
}

void out(vector<answer> &ans, node *t) {
    if (t == 0) {
        return;
    }

    int n = t->n;
    ans[n].p = num(t->p);
    ans[n].l = num(t->l);
    ans[n].r = num(t->r);

    out(ans, t->l);
    out(ans, t->r);
}

int main() {
    int n;
    cin >> n;

    int x, y;
    vector<pair<int, pair<int, int> > > v(n);
    for (int i = 0; i < n; ++i) {
        cin >> x >> y;
        v[i] = {x, {y, i + 1}};
    }

    sort(v.begin(), v.end());
    node *t = gen(v[0].f, v[0].s.f, v[0].s.s, 0, 0, 0);

    for (int i = 1; i < n; ++i) {
        x = v[i].f;
        y = v[i].s.f;
        int k = v[i].s.s;

        if (t->y < y) {
            t->r = gen(x, y, k, 0, 0, t);
            t = t->r;
        } else {
            node *tmp = t;
            while (tmp->p != 0 && tmp->y >= y) {
                tmp = tmp->p;
            }

            if (tmp->y >= y) {
                t = gen(x, y, k, tmp, 0, 0);
                tmp->p = t;
            } else {
                t = gen(x, y, k, tmp->r, 0, tmp);
                tmp->r = t;
                t->p = tmp;
                t->l->p = t;
            }
        }
    }

    while (t->p != 0) {
        t = t->p;
    }

    vector<answer> ans(n + 1);

    out(ans, t);

    cout << "YES\n";
    for (int i = 1; i <= n; ++i) {
        cout << ans[i].p << " " << ans[i].l << " " << ans[i].r << "\n";
    }

    return 0;
}
