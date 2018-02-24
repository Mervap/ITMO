#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

struct v{
    int wh;
    long long w;
};

vector<int> head;
vector<int> nx;
vector<v> edg;
vector<long long> ans1;
vector<long long> ans2;

long long dfs(int i, int p){
    int h = head[i];
    while(h != -1){
        if(edg[h].wh != p){
            long long l = dfs(edg[h].wh, i);
            ans1[i] = max(ans1[i], ans2[edg[h].wh] + edg[h].w - l);
            ans2[i] += l;
        }
        h = nx[h];
    }
    ans1[i] += ans2[i];
    return max(ans1[i], ans2[i]);
}

int main()
{
    freopen("matching.in", "r", stdin);
    freopen("matching.out", "w", stdout);

    int n;
    cin >> n;

    head.assign(n + 1, -1);
    nx.assign(2*n, 0);
    v vv;
    edg.assign(2*n, vv);

    int a, b, p = 0;
    long long w;
    for(int i = 0; i < n - 1; ++i){
        cin >> a >> b >> w;
        nx[p] = head[a];
        head[a] = p;
        edg[p].wh = b;
        edg[p].w = w;
        ++p;

        nx[p] = head[b];
        head[b] = p;
        edg[p].wh = a;
        edg[p].w = w;
        ++p;
    }

    ans1.assign(n+1, 0);
    ans2.assign(n+1, 0);

    cout << dfs(1, -1);
    return 0;
}
