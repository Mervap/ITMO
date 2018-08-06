#include <iostream>

using namespace std;

struct node {
    int k, lvl, size;
    node *l;
    node *r;
    node *p;
};

node *root;
int let = 0;

int size_of(node *t) {
    if (t == 0) {
        return 0;
    }

    return (t->size);
}

node *skew(node *t) {
    if (t == 0 || t->l == 0) {
        return t;
    }

    node *l = t->l;
    node *p = t->p;
    if (l->lvl == t->lvl) {
        l->p = p;
        t->l = l->r;
        if (t->l != 0) {
            t->l->p = t;
        }
        l->r = t;
        t->p = l;
        if (p != 0) {
            if (p->l == t) {
                p->l = l;
            } else {
                p->r = l;
            }
        }
        t->size = size_of(t->l) + size_of(t->r) + 1;
        l->size = size_of(l->l) + size_of(l->r) + 1;
        return l;
    } else {
        return t;
    }
}

node *split(node *t) {
    if (t == 0 || t->r == 0 || t->r->r == 0) {
        return t;
    }

    node *r = t->r;
    node *rr = r->r;
    node *p = t->p;
    if (t->lvl == rr->lvl) {
        r->p = t->p;
        t->r = r->l;
        if (t->r != 0) {
            t->r->p = t;
        }
        r->l = t;
        t->p = r;
        ++(r->lvl);
        if (p != 0) {
            if (p->l == t) {
                p->l = r;
            } else {
                p->r = r;
            }
        }

        t->size = size_of(t->l) + size_of(t->r) + 1;
        r->size = size_of(r->l) + size_of(r->r) + 1;

        return r;
    } else {
        return t;
    }
}

node *ins(node *t, int x) {

    if (t == 0) {
        node *nw = new node;
        nw->r = 0;
        nw->l = 0;
        nw->p = 0;
        nw->k = x;
        nw->lvl = 1;
        nw->size = 1;
        return nw;
    }

    ++(t->size);
    if (x < t->k) {
        t->l = ins(t->l, x);
        t->l->p = t;
    } else if (x > t->k) {
        t->r = ins(t->r, x);
        t->r->p = t;
    }

    t = skew(t);
    t = split(t);

    return t;
}

node *del(node *t, int x) {
    if (t == 0) {
        return t;
    }

    --(t->size);
    if (x < t->k) {
        t->l = del(t->l, x);
        if (t->l != 0) {
            t->l->p = t;
        }
    } else if (x > t->k) {
        t->r = del(t->r, x);
        if (t->r != 0) {
            t->r->p = t;
        }
    } else {
        if (t->l == 0 && t->r == 0) {
            delete t;
            return 0;
        }

        if (t->l == 0) {
            node *r = t->r;
            while (r->l != 0) {
                r = r->l;
            }
            t->k = r->k;
            t->r = del(t->r, r->k);
            if (t->r != 0) {
                t->r->p = t;
            }
        } else {
            node *l = t->l;
            while (l->r != 0) {
                l = l->r;
            }
            t->k = l->k;
            t->l = del(t->l, l->k);
            if (t->l != 0) {
                t->l->p = t;
            }
        }
    }

    if ((t->l != 0 && (t->l->lvl) < (t->lvl) - 1) || (t->r != 0 && (t->r->lvl) < (t->lvl) - 1)) {
        --(t->lvl);
        if (t->r != 0 && (t->r->lvl) > t->lvl) {
            t->r->lvl = t->lvl;
        }

        t = skew(t);
        t->r = skew(t->r);
        if (t->r != 0) {
            t->r->r = skew(t->r->r);
        }
        t = split(t);
        t->r = split(t->r);
    }

    return t;
}

int kth(node *t, int x) {
    if (size_of(t->l) + 1 == x) {
        return t->k;
    }

    if (size_of(t->l) >= x) {
        return kth(t->l, x);
    } else {
        return kth(t->r, x - size_of(t->l) - 1);
    }
}

int main() {
    cin.tie(0);
    cout.tie(0);


    int x, a;
    cin >> x;
    while (cin >> a) {
        cin >> x;
        if (a == 1) {
            root = ins(root, x);
            ++let;
            continue;
        }
        if (a == -1) {
            root = del(root, x);
            --let;
            if (root != 0) {
                root->p = 0;
            }
            continue;
        }
        if (a == 0) {
            cout << kth(root, let - x + 1) << "\n";
            continue;
        }
    }


    return 0;
}
