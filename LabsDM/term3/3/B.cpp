#include <iostream>
#include <vector>
#include <algorithm>
#include <set>

using namespace std;

struct kruskal {

private:
    struct edge {
        uint64_t w;
        int b;
        int e;
        int n;

        bool operator<(edge const &other) {
            return w > other.w;
        }
    };

public:

    kruskal(int n) : n(n) {
        p.resize(n);
        r.resize(n);
    }

    void add_edge(uint64_t w, int a, int b, int n) {
        edg.push_back({w, --a, --b, n});
    }

    vector<int> findOutOfMst() {
        sort(edg.begin(), edg.end());

        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }

        vector<int> result;
        for (auto e : edg) {
            if (get(e.b) != get(e.e)) {
                un(e.b, e.e);
            } else {
                result.push_back(e.n);
            }
        }

        reverse(result.begin(), result.end());
        return result;
    }

private:
    int n;
    vector<edge> edg;
    vector<int> p;
    vector<int> r;

    int get(int v) {
        if (p[v] == v) {
            return v;
        } else {
            return get(p[v]);
        }
    }

    void un(int v, int u) {
        int pv = get(v);
        int pu = get(u);

        if (r[pv] < r[pu]) {
            p[pv] = pu;
        } else {
            p[pu] = pv;
            if (r[pv] == r[pu]) {
                ++r[pv];
            }
        }
    }
};

int n, m;
uint64_t s;

vector<uint64_t> edg;

int main() {

    freopen("destroy.in", "r", stdin);
    freopen("destroy.out", "w", stdout);

    cin >> n >> m >> s;

    kruskal kr(n);
    int b, e;
    uint64_t w;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> w;
        kr.add_edge(w, b, e, i);
        edg.push_back(w);
    }

    vector<int> good = kr.findOutOfMst();
    set<int> ans;
    uint64_t t = 0;
    for (auto i : good) {
        if (t + edg[i] <= s) {
            t += edg[i];
            ans.insert(i);
        } else {
            break;
        }
    }

    cout << ans.size() << "\n";
    for (auto a : ans) {
        cout << a + 1 << " ";
    }

    return 0;
}