#include <iostream>
#include <vector>
#include <queue>
#include <ctime>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

template<typename S, typename T>
struct Min_cost_max_flow {

private:
    struct edge {
        int b, e;
        S c, f, p;
        int n;
        edge *back_edge;

        edge(int b, int e, S c, S p, int n) : b(b), e(e), c(c), f(0), p(p), n(n), back_edge(nullptr) {}
    };

    vector<vector<edge *>> edges;
    vector<int> d;
    vector<edge *> from;

    S INF;
    int n;

    bool bfs(int s, int t) {
        d.assign(n, INF);
        d[s] = 0;
        for (int i = 0; i < n - 1; ++i) {
            bool f = false;
            for (int j = 0; j < n; ++j) {
                for (auto e : edges[j]) {
                    if (e->c - e->f == 0) {
                        continue;
                    }

                    if (d[j] < INF) {
                        if (d[e->e] > d[e->b] + e->p) {
                            d[e->e] = d[j] + e->p;
                            from[e->e] = e;
                            f = true;
                        }
                    }
                }
            }

            if (!f) {
                break;
            }
        }

        return d[t] != INF;
    }

    pair<T, T> push(int s, int v, S c_min) {
        if (v == s) {
            return {c_min, 0};
        }

        auto e = from[v];
        auto add = push(s, e->b, std::min(c_min, e->c - e->f));
        if (add.first > 0) {
            e->f += add.first;
            e->back_edge->f -= add.first;
            add.second += add.first * e->p;
        }
        return add;
    }

public:

    explicit Min_cost_max_flow(int n, S INF) : INF(INF), n(n) {
        d.resize(n);
        from.resize(n);
        edges.resize(n);
    }

    void add_edge(int b, int e, S c, S p, int n) {
        edge *e1 = new edge(b, e, c, p, n);
        edge *e2 = new edge(e, b, 0, -p, n);
        e1->back_edge = e2;
        e2->back_edge = e1;
        edges[b].push_back(e1);
        edges[e].push_back(e2);
    }

    T run(int s, int t) {
        T price = 0;
        while (bfs(s, t)) {
            auto add = push(s, t, INF);
            price += add.second;
        }

        return price;
    }

    void ans(vector<int> &ans, int n) {
        for (int i = 2; i < n; ++i) {
            for (auto e : edges[i]) {
                if (e->n != -1 && e->f == 1) {
                    ans[e->n] = 1;
                }
            }
        }
    }
};

vector<int> line, column, p, from;


struct tmp {
    int b, e, p, n;

    bool operator<(const tmp &other) {
        return b < other.b || (b == other.b && e < other.e);
    }
};

int main() {

    ios_base::sync_with_stdio(false);
    //freopen("in.txt", "r", stdin);
    //freopen("out.txt", "w", stdout);
    int n, k;
    cin >> n >> k;

    set<int> ver;
    int b, e, p;
    vector<tmp> tasks;
    Min_cost_max_flow<int, long long> min_cost(2 * n + 3, 1000000001);

    for (int i = 0; i < n; ++i) {
        cin >> b >> e >> p;
        tasks.push_back({b, b + e, p, i});
    }

    sort(tasks.begin(), tasks.end());

    for (int i = 2; i <= n + 1; ++i) {
        b = tasks[i - 2].b;
        e = tasks[i - 2].e;
        p = tasks[i - 2].p;
        min_cost.add_edge(i, n + i, 1, -p, tasks[i - 2].n);
        min_cost.add_edge(1, i, k, 0, -1);
        min_cost.add_edge(n + i, 2 * n + 2, 1, 0, -1);
    }

    min_cost.add_edge(0, 1, k, 0, -1);

    for (int i = 2; i < n + 1; ++i) {
        min_cost.add_edge(i, i + 1, k, 0, -1);
        for (int j = i + 1; j <= n + 1; ++j) {
            if (tasks[i - 2].e <= tasks[j - 2].b) {
                min_cost.add_edge(n + i, j, 1, 0, -1);
                break;
            }
        }
    }

    cerr << min_cost.run(0, 2 * n + 2) << "\n";
    vector<int> ans(n);
    min_cost.ans(ans, n + 2);
    for (auto e : ans) {
        cout << e << " ";
    }
    return 0;
}