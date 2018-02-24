#include <iostream>
#include <vector>
#include <string>
#include <fstream>

using namespace std;

vector<int> p,r;

int find_root(int i){
    if(p[i]!=i){
        p[i]=find_root(p[i]);
    }
    return p[i];
}

void un(int i, int j){
    int x = find_root(i);
    int y = find_root(j);

    if(x == y){
        return;
    }

    if(r[x] == r[y]){
        ++r[x];
    }
    if(r[x] < r[y]){
        p[x] = y;
    } else{
        p[y] = x;
    }
}

int main()
{
    freopen("cutting.in", "r", stdin);
    freopen("cutting.out", "w", stdout);

    int n,m,k;
    scanf("%d%d%d", &n, &m, &k);

    vector< pair<int,int> > v;
    vector<int> type;
    vector<string> ans;
    p.assign(n+1, 0);
    r.assign(n+1, 0);

    int a,b;
    string s;

    for(int i = 1; i <= n; ++i){
        p[i] = i;
    }

    for(int i = 0; i < m; ++i){
        scanf("%d%d", &a, &b);
    }

    for(int i = 0; i < k; ++i){
        cin >> s;
        scanf("%d%d",&a, &b);
        if(s == "ask"){
            type.push_back(1);
        } else{
            type.push_back(2);
        }

        v.push_back(make_pair(a,b));
    }

    for(int i = (int) v.size()-1; i >= 0; --i){
        if(type[i] == 1){
            a = find_root(v[i].first);
            b = find_root(v[i].second);
            if(a == b){
                ans.push_back("YES");
            } else{
                ans.push_back("NO");
            }

        } else{
            un(v[i].first, v[i].second);
        }
    }

    for(int i = (int) ans.size()-1; i >= 0; --i){
        cout << ans[i] << "\n";
    }
    return 0;
}
