#include <iostream>

using namespace std;

struct node {
    int k, lvl;
    node *l;
    node *r;
    node *p;
};

node *root;
int let = 0;

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
        return r;
    } else {
        return t;
    }
}

node *ins(node *tec, int x) {

    ++let;

    if (tec == 0) {
        node *nw = new node;
        nw->r = 0;
        nw->l = 0;
        nw->p = 0;
        nw->k = x;
        nw->lvl = 1;
        return nw;
    }

    if (x < tec->k) {
        tec->l = ins(tec->l, x);
        tec->l->p = tec;
    } else if (x > tec->k) {
        tec->r = ins(tec->r, x);
        tec->r->p = tec;
    }

    tec = skew(tec);
    tec = split(tec);

    return tec;
}

node *del(node *t, int x) {
    if (t == 0) {
        return t;
    }

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

node *exists(int x) {
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

node *nx(int x) {
    node *tec = exists(x);
    if (tec->r == 0) {
        node *p = tec->p;
        while (p != 0 && tec == p->r) {
            tec = p;
            p = p->p;
        }
        return p;

    } else {
        tec = tec->r;
        while (tec->l != 0) {
            tec = tec->l;
        }
        return tec;
    }
}

node *prev(int x) {
    node *tec = exists(x);
    if (tec->l == 0) {
        node *p = tec->p;
        while (p != 0 && tec == p->l) {
            tec = p;
            p = p->p;
        }
        return p;

    } else {
        tec = tec->l;
        while (tec->r != 0) {
            tec = tec->r;
        }
        return tec;
    }
}

int main() {
    cin.tie(0);
    cout.tie(0);

    string s;
    int x;
    while (cin >> s) {
        cin >> x;
        if (s == "insert") {
            root = ins(root, x);
            continue;
        }
        if (s == "delete") {
            root = del(root, x);
            if (root != 0) {
                root->p = 0;
            }
            continue;
        }
        if (s == "exists") {
            node *f = exists(x);
            if (f == 0) {
                cout << "false\n";
            } else {
                cout << "true\n";
            }
            continue;
        }
        if (s == "next") {
            bool was = exists(x);
            if (!was) {
                root = ins(root, x);
            }
            node *f = nx(x);
            if (f == 0) {
                cout << "none\n";
            } else {
                cout << (*f).k << "\n";
            }
            if (!was) {
                root = del(root, x);
                if (root != 0) {
                    root->p = 0;
                }
            }
            continue;
        }
        if (s == "prev") {
            bool was = exists(x);
            if (!was) {
                root = ins(root, x);
            }
            node *f = prev(x);
            if (f == 0) {
                cout << "none\n";
            } else {
                cout << f->k << "\n";
            }
            if (!was) {
                root = del(root, x);
                if (root != 0) {
                    root->p = 0;
                }
            }
            continue;
        }
    }

    return 0;
}
