#include <iostream>
#include <vector>
#include <map>
#include <set>

using namespace std;

struct diag {
    int x, y, type, id, color;

    diag() : x(-1), y(-1), type(-1), id(-1), color(-1) {};

    diag(int x, int y, int type, int id, int color) : x(x), y(y), type(type), id(id), color(color) {};

    bool operator<(const diag &other) const {
        return id < other.id;
    }
};

struct cell {
    int x, y, color;

    cell(int x, int y, int color) : x(x), y(y), color(color) {};

    bool operator<(const cell &other) const {
        return x < other.x || (x == other.x && y < other.y);
    }
};

vector<vector<int>> gr;
vector<vector<int>> edg;
map<diag, set<cell>> cells;
map<int, diag> id;
vector<diag> diags;
vector<int> p;
vector<bool> used;
vector<vector<int>> edgMatching;
set<int> rp;
set<int> lm;

int n, m;
int l_cnt, r_cnt;

void calc_cells(int C) {
    int cur_id = 0;
    int cur_c = C;
    diags.clear();
    cells.clear();
    id.clear();
    for (int i = 0; i < n; ++i) {
        int x = i, y = 0;
        diag d = diag(x, y, 2, cur_id, cur_c);
        id.insert({cur_id++, d});
        diags.push_back(d);
        cells[d] = set<cell>();
        while (x < n && y < m) {
            cells[d].insert(cell(x, y, cur_c));
            ++x;
            ++y;
        }
        cur_c = 1 - cur_c;
    }

    cur_c = 1 - C;
    for (int i = 1; i < m; ++i) {
        int x = 0, y = i;
        diag d = diag(x, y, 2, cur_id, cur_c);
        id.insert({cur_id++, d});
        diags.push_back(d);
        cells[d] = set<cell>();
        while (x < n && y < m) {
            cells[d].insert(cell(x, y, cur_c));
            ++x;
            ++y;
        }
        cur_c = 1 - cur_c;
    }
    l_cnt = cur_id;

    cur_c = C;
    for (int i = 0; i < m; ++i) {
        int x = 0, y = i;
        diag d = diag(x, y, 1, cur_id, cur_c);
        id.insert({cur_id++, d});
        diags.push_back(d);
        cells[d] = set<cell>();
        while (x < n && y >= 0) {
            cells[d].insert(cell(x, y, cur_c));
            ++x;
            --y;
        }
        cur_c = 1 - cur_c;
    }

    for (int i = 1; i < n; ++i) {
        int x = i, y = m - 1;
        diag d = diag(x, y, 1, cur_id, cur_c);
        id.insert({cur_id++, d});
        diags.push_back(d);
        cells[d] = set<cell>();
        while (x < n && y >= 0) {
            cells[d].insert(cell(x, y, cur_c));
            ++x;
            --y;
        }
        cur_c = 1 - cur_c;
    }

    r_cnt = cur_id - l_cnt;
}


bool matching(int v) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (auto e : edg[v]) {
        if (p[e - l_cnt] == -1 || matching(p[e - l_cnt])) {
            p[e - l_cnt] = v;
            return true;
        }
    }
    return false;
}

void dfs(int v) {
    used[v] = true;
    if (v < l_cnt) {
        lm.erase(v);
    } else {
        rp.insert(v);
    }

    for (auto e : edgMatching[v]) {
        if (!used[e]) {
            dfs(e);
        }
    }
}

void solve(vector<diag> &ans, int C) {
    calc_cells(C);
    edg.clear();
    edg.assign(l_cnt, vector<int>());

    for (auto l : diags) {
        for (auto r : diags) {
            if (l.type == 2 && r.type == 1 && l.color == r.color) {
                auto it = cells[l].begin();
                while (it != cells[l].end() && cells[r].find(*it) == cells[r].end()) {
                    ++it;
                }

                if (it == cells[l].end()) {
                    continue;
                }

                if ((*it).color != gr[(*it).x][(*it).y]) {
                    edg[l.id].push_back(r.id);
                }
            }
        }
    }

    p.assign(r_cnt, -1);
    for (int i = 0; i < l_cnt; ++i) {
        used.assign(l_cnt, false);
        matching(i);
    }

    set<int> bad;
    for (int i = 0; i < l_cnt; ++i) {
        bad.insert(i);
    }

    edgMatching.assign(l_cnt + r_cnt, vector<int>());
    for (int i = 0; i < l_cnt; ++i) {
        for (auto e : edg[i]) {
            if (i == p[e - l_cnt]) {
                edgMatching[e].push_back(i);
            } else {
                edgMatching[i].push_back(e);
            }
        }
    }


    rp.clear();
    lm.clear();
    for (int i = 0; i < r_cnt; ++i) {
        if (p[i] != -1) {
            bad.erase(p[i]);
        }
    }

    for (int i = 0; i < l_cnt; ++i) {
        lm.insert(i);
    }

    used.assign(l_cnt + r_cnt, false);
    for (auto v : bad) {
        if (!used[v]) {
            dfs(v);
        }
    }

    for (int i : lm) {
        ans.push_back(id[i]);
    }

    for (int i : rp) {
        ans.push_back(id[i]);
    }
}

int main() {
    cin >> n >> m;
    gr.resize(n, vector<int>(m));
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            char c = 0;
            while (c != 'W' && c != 'B') {
                cin >> c;
            }
            gr[i][j] = (c == 'W' ? 0 : 1);
        }
    }

    vector<diag> ans1, ans2;
    solve(ans1, 0);
    solve(ans2, 1);
    if (ans1.size() < ans2.size()) {
        cout << ans1.size() << "\n";
        for (auto d : ans1) {
            cout << d.type << " " << d.x + 1 << " " << d.y + 1 << " " << (d.color == 0 ? "W" : "B") << "\n";
        }
    } else {
        cout << ans2.size() << "\n";
        for (auto d : ans2) {
            cout << d.type << " " << d.x + 1 << " " << d.y + 1 << " " << (d.color == 0 ? "W" : "B") << "\n";
        }
    }
    return 0;
}