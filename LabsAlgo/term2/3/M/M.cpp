#include <iostream>
#include <vector>

using namespace std;

struct p {
    int min, ind;
};

par min(par &a, par &b) {
    if(a.min < b.min) {
        return a;
    } else {
        return b;
    }
}

int n, m;
vector<int> lg;
vector<int> vrt;
vector<int> arr;
vector<int> ind;
vector<bool> was;
vector<vector<par>> st;
vector<vector<int>> gr;

int tree = 0;

void dfs(int v, int d) {
    was[v] = true;
    ind[v] = static_cast<int>(arr.size());

    arr.push_back(d);
    vrt.push_back(v);
    for (int u : gr[v]) {
        if (!was[u]) {
            dfs(u, d + 1);
            arr.push_back(d);
            vrt.push_back(v);
        }
    }
}

int main() {

    freopen("lca_rmq.in", "r", stdin);
    freopen("lca_rmq.out", "w", stdout);

    scanf("%d %d", &n, &m);

    was.resize(n);
    gr.resize(n);
    ind.resize(n);

    int ll;
    for (int i = 1; i < n; ++i) {
        scanf("%d", &ll);
        gr[i].push_back(ll);
        gr[ll].push_back(i);
    }

    dfs(0, 0);
    int MD = n;
    n = arr.size();

    lg.resize(n);
    lg[1] = 0;
    for (size_t i = 2; i < n + 1; ++i) {
        lg[i] = lg[i / 2] + 1;
    }

    st.assign(n + 1, vector<par>(lg[n] + 1, {0, 0}));

    for (size_t j = 0; j < lg[n] + 1; ++j) {
        for (size_t i = 0; i + (1 << j) <= n; ++i) {
            if (j == 0) {
                st[i][j] = {arr[i], i};
                continue;
            }
            st[i][j] = min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
        }
    }


    long long a, a1, x, y, z, ans = 0, c = 0;
    scanf("%lld %lld %lld %lld %lld", &a, &a1, &x, &y, &z);
    for (size_t i = 0; i < m; ++i) {
        if (i != 0) {
            a = (x * a + y * a1 + z) % MD;
            a1 = (x * a1 + y * a + z) % MD;
        }

        int l = ind[(a + c) % MD];
        int r = ind[a1];
        if (l > r) {
            int j = lg[l - r + 1];
            c = vrt[min(st[r][j], st[l + 1 - (1 << j)][j]).ind];
        } else {
            int j = lg[r - l + 1];
            c = vrt[min(st[l][j], st[r + 1 - (1 << j)][j]).ind];
        }

        ans += c;
    }

    cout << ans;
    return 0;
}