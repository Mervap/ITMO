#include <iostream>
#include <vector>
#include <queue>
#include <ctime>

using namespace std;

template<typename S, typename T>
struct Min_cost_max_flow {

private:
    struct edge {
        int b, e;
        S c, f, p;
        edge *back_edge;

        edge(int b, int e, S c, S p) : b(b), e(e), c(c), f(0), p(p), back_edge(nullptr) {}
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

            if(!f) {
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

    void add_edge(int b, int e, S c, S p) {
        edge *e1 = new edge(b, e, c, p);
        edge *e2 = new edge(e, b, 0, -p);
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

    void find(int n, vector<vector<int>> &a) {
        for (int j = 1; j <= n; ++j) {
            for (auto e : edges[j]) {
                if (e->c == 1 && e->f == 0) {
                    a[e->b][e->e - n] = 100000001;
                }
            }
        }
    }
};

vector<int> line, column, p, from;

int main() {

    ios_base::sync_with_stdio(false);
    int n, k;
    cin >> n >> k;

    Min_cost_max_flow<int, long long> min_cost(2 * n + 2, 1000000001);

    int price;
    vector<vector<int>> a(n + 1, vector<int>(n + 1));
    for (int i = 1; i <= n; ++i) {
        for (int j = n + 1; j <= 2 * n; ++j) {
            cin >> price;
            a[i][j - n] = price;
            min_cost.add_edge(i, j, 1, price);
        }
    }

    for (int i = 1; i <= n; ++i) {
        min_cost.add_edge(0, i, k, 0);
    }

    for (int i = n + 1; i <= 2 * n; ++i) {
        min_cost.add_edge(i, 2 * n + 1, k, 0);
    }

    cout << min_cost.run(0, 2 * n + 1) << "\n";

    min_cost.find(n, a);


    for (int q = 0; q < k; ++q) {
        line.assign(n + 1, 0);
        column.assign(n + 1, 0);
        p.assign(n + 1, -1);
        from.assign(n + 1, -1);
        for (int i = 1; i <= n; ++i) {
            vector<int> cur_column_min(n + 1, 1000000000);
            vector<bool> used(n + 1);
            int cur, min, v, u = 0;
            p[u] = i;
            while (true) {
                used[u] = true;
                cur = p[u];
                min = 1000000000;
                v = -1;

                for (int j = 1; j <= n; ++j) {
                    if (!used[j]) {
                        if (a[cur][j] + line[cur] + column[j] < cur_column_min[j]) {
                            cur_column_min[j] = a[cur][j] + line[cur] + column[j];
                            from[j] = u;
                        }

                        if (cur_column_min[j] < min) {
                            min = cur_column_min[j];
                            v = j;
                        }
                    }
                }

                for (int j = 0; j <= n; ++j) {
                    if (used[j]) {
                        line[p[j]] -= min;
                        column[j] += min;
                    } else {
                        cur_column_min[j] -= min;
                    }
                }

                u = v;
                if (p[u] == -1) {
                    while (u != -1) {
                        v = from[u];
                        p[u] = p[v];
                        u = v;
                    }
                    break;
                }
            }
        }

        vector<int> anss(n);
        for (int i = 1; i <= n; ++i) {
            anss[p[i] - 1] = i;
            a[p[i]][i] = 100000001;
        }

        for (auto e : anss) {
            cout << e << " ";
        }

        cout << "\n";
    }

    return 0;
}