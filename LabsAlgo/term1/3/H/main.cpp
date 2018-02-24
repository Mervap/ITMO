#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <algorithm>

#define f first
#define s second

using namespace std;

vector<int> color, p, r, change;

int shift = 0;

pair<int,int> find_root(int i){
    int z = 0;
    if(p[i] != i){
        pair<int,int> k = find_root(p[i]);
        p[i] = k.f;
        z = (change[i] ^ k.s);
        change[i] = z;
    } else{
        z = change[i];
    }
    return make_pair(p[i], z);
}

void un(int i, int j){
    int x = find_root(i).f;
    int y = find_root(j).f;

    if((color[i] ^ change[i]) != (color[j] ^ change[j])){
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
    } else{
        if(r[x] == r[y]){
            ++r[x];
        }
        if(r[x] < r[y]){
            change[x] = (change[x] ^ 1);
            p[x] = y;
        } else{
            change[y] = (change[y] ^ 1);
            p[y] = x;
        }
    }
}

int main()
{
    int n, m;
    cin >> n >> m;

    color.assign(n, 0);
    p.assign(n, 0);
    r.assign(n, 0);
    change.assign(n , 0);
    for(int i = 0; i < n; ++i){
        p[i] = i;
    }

    int t,a,b;
    for(int i = 0; i < m; ++i){
        cin >> t >> a >> b;
        a = (a+shift)%n;
        b = (b+shift)%n;
        if(t == 0){
            un(a,b);
        } else{
            find_root(a);
            find_root(b);
            if((color[a] ^ change[a]) == (color[b] ^ change[b])){
                shift = (shift+1)%n;
                cout << "YES\n";
            } else{
                cout << "NO\n";
            }
        }
    }
    return 0;
}
