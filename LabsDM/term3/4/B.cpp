//How does this shit work?
//"A note on finding minimum-cost edge-disjoint spanning trees" by Roskind & Tarjan

#include <iostream>
#include <vector>
#include <set>
#include <queue>
#include <cassert>
#include <ctime>
#include <algorithm>
#include <map>
#include <stack>

using namespace std;

int n, m;

struct DSU {

    explicit DSU(int n) : p(), r() {
        r = vector<int>(n);
        p = vector<int>(n);
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
    }

    int get(int v) {
        if (p[v] == v) {
            return v;
        } else {
            return p[v] = get(p[v]);
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

private:
    vector<int> p;
    vector<int> r;
};

struct edge {
    int b;
    int e;
    int n;
};

struct d_edge {
    int to;
    int n;

    bool operator<(d_edge const &other) const {
        return to < other.to || (to == other.to && n < other.n);
    }
};

vector<edge> edg;

int max_k;
vector<DSU> dsu;
vector<set<int>> F;
vector<int> index;
vector<vector<set<d_edge>>> gr;

void dfs(int v, vector<d_edge> &p, int k) {
    for (auto e : gr[k][v]) {
        if (p[e.to].to == -1) {
            p[e.to] = {v, e.n};
            dfs(e.to, p, k);
        }
    }
}

void update(int e) {

    vector<int> label(m, -1);
    vector<vector<d_edge>> p(1, vector<d_edge>(n, {-1, -1}));
    int x = edg[e].b;
    p[0][x] = {x, -1};
    dfs(x, p[0], 0);

    queue<int> q;
    q.push(e);

    int cur_max_k = 1;
    while (cur_max_k < max_k && F[cur_max_k - 1].size() == n - 1) {
        ++cur_max_k;
        d_edge d_edge1 = {-1, -1};
        p.emplace_back(n, d_edge1);
        p[cur_max_k - 1][x] = {x, -1};
        dfs(x, p[cur_max_k - 1], cur_max_k - 1);
    }

    while (true) {
        vector<int> labeled;
        while (!q.empty()) {
            int t_id = q.front();
            auto t = edg[q.front()];
            q.pop();

            int next_k = (index[t_id] + 1) % cur_max_k;
            if (dsu[next_k].get(t.b) != dsu[next_k].get(t.e)) {
                dsu[next_k].un(t.b, t.e);
                while (label[t.n] != -1) {
                    int num = t.n;

                    gr[index[num]][t.b].erase({t.e, t.n});
                    gr[index[num]][t.e].erase({t.b, t.n});

                    gr[next_k][t.b].insert({t.e, t.n});
                    gr[next_k][t.e].insert({t.b, t.n});

                    t = edg[label[num]];
                    F[index[num]].erase(num);
                    F[next_k].insert(num);
                    std::swap(index[num], next_k);
                }

                int num = t.n;
                F[next_k].insert(num);
                gr[next_k][t.b].insert({t.e, t.n});
                gr[next_k][t.e].insert({t.b, t.n});
                index[num] = next_k;
                return;
            } else {
                int u;
                vector<d_edge> &pp = p[next_k];
                if (pp[t.b].to == t.b || label[pp[t.b].n] != -1) {
                    u = t.e;
                } else {
                    u = t.b;
                }

                stack<int> stack;
                while (pp[u].to != u && label[pp[u].n] == -1) {
                    stack.push(pp[u].n);
                    u = pp[u].to;
                }

                while (!stack.empty()) {
                    int f = stack.top();
                    stack.pop();
                    label[f] = t_id;
                    labeled.push_back(f);
                    q.push(f);
                }
            }
        }

        for (auto i : labeled) {
            q.push(i);
        }

        ++cur_max_k;
        if (cur_max_k > max_k) {
            ++max_k;
            F.emplace_back();
            dsu.emplace_back(n);
            gr.emplace_back(n);
        }

        d_edge d_edge1 = {-1, -1};
        p.emplace_back(n, d_edge1);
        p[cur_max_k - 1][x] = {x, -1};
        dfs(x, p[cur_max_k - 1], cur_max_k - 1);
    }
}

int main() {

    ios_base::sync_with_stdio(false);
    freopen("multispan.in", "r", stdin);
    freopen("multispan.out", "w", stdout);

    cin >> n >> m;

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        edg.push_back({a, b, i});
    }

    DSU zero_dsu(n);
    F.emplace_back();
    dsu.emplace_back(n);

    index.assign(m, -1);

    max_k = 1;
    gr.emplace_back(n);
    for (int i = 0; i < m; ++i) {
        update(i);
    }

    int ans = 1;
    while (ans <= max_k && F[ans - 1].size() == n - 1) {
        ++ans;
    }

    --ans;

    cout << ans << "\n";
    for (int i = 0; i < ans; ++i) {
        for (auto j : F[i]) {
            cout << j + 1 << " ";
        }

        cout << "\n";
    }

    return 0;
}