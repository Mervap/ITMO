#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <queue>
#define int long long
#define f first
#define s second

using namespace std;
vector< vector< pair <int, int> > > parent;

void out(int sum, int mod){
    if(sum == 0){
        return;
    }
    int psum = parent[sum][mod].f;
    int pmod = parent[sum][mod].s;
    out(psum, pmod);
    cout << sum - psum;
}

int_fast32_t main()
{
    //freopen("number.in", "r", stdin);
    //freopen("number.out", "w", stdout);

    int n;
    cin >> n;
    queue< pair<int, int> > v;
    vector< vector<bool> > was;
    was.assign(1010, vector<bool> (1010));
    parent.assign(1010, vector< pair <int, int> > (1010));

    v.push(make_pair(0, 0));
   // pair< pair <int, bool>,  string> g;
    //g = {1, true, "odin"};
    //cout << g.f.f << " " << g.f.s << " " << g.s;

    pair<int, int> t;
    while(true){
        t = v.front();
        v.pop();

        if(t.f == n && t.s == 0){
            out(t.f, t.s);
            break;
        }

        for(int i = 0; i < 10; ++i){
            int nsum = t.f + i;
            int nmod = (t.s * 10 + i) % n;
            if(was[nsum][nmod] || nsum > n){
                continue;
            }
            was[nsum][nmod] = true;
            v.push(make_pair(nsum, nmod));
            parent[nsum][nmod] = make_pair(t.f, t.s);
        }
    }
    return 0;
}
