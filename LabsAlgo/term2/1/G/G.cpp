#include <iostream>

using namespace std;

const long long MD = 1'000'000'000;

long long randll() {
    long long res = rand();
    for (int i = 0; i < 3; i++) {
        res <<= 16;
        res += rand();
    }
    return res;
}

struct node {
    long long sum, k, y;
    node *l;
    node *r;
};

node *root;

long long sum_of(node *t) {
    if (t == 0) {
        return 0;
    } else {
        return (*t).sum;
    }
}

void update(node *t) {
    if (t != 0) {
        (*t).sum = (t->k) + sum_of((*t).l) + sum_of((*t).r);
    }
}

void split(node *t, node *&l, node *&r, long long x) {
    if (t == 0) {
        l = 0;
        r = 0;
        return;
    }

    if (x < t->k) {
        split((*t).l, l, (*t).l, x);
        r = t;
    } else {
        split((*t).r, (*t).r, r, x);
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

node *exists(long long x) {
    node *tec = root;
    while (tec != 0 && tec->k != x) {
        if (x > tec->k) {
            tec = tec->r;
        } else {
            tec = tec->l;
        }
    }

    return tec;
}

node *gen(long long k) {
    node *nw = new node;
    nw->y = randll();
    nw->k = k;
    nw->sum = k;
    nw->l = 0;
    nw->r = 0;

    return nw;
}

int main() {
    int n;
    long long x, a, b;
    cin >> n;

    string s;

    node *l = 0;
    node *r = 0;

    root = 0;

    long long sum = 0;
    for (int i = 0; i < n; ++i) {
        cin >> s;
        if (s == "+") {
            cin >> x;
            x = (x + sum) % MD;
            sum = 0;
            if (exists(x)) {
                continue;
            }

            split(root, root, r, x);
            merge(root, root, gen(x));
            merge(root, root, r);

        } else if (s == "?") {
            cin >> a >> b;
            split(root, l, root, a - 1);
            split(root, root, r, b);
            sum = sum_of(root);
            merge(root, l, root);
            merge(root, root, r);

            cout << sum << "\n";
        }
    }

    return 0;
}
