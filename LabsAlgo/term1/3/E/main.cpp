#include <iostream>
#include <vector>
#include <fstream>

#define next nex
using namespace std;

vector<int> next,p,r;

int find_root(int i){
    if(p[i] != i){
        p[i] = find_root(p[i]);
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

void un_more(int a, int b){
    vector<int> z;

    int x = next[a];
    while(x <= b){
        z.push_back(a);
        un(a,x);
        a = x;
        x = next[a];
    }

    for(int i = (int)z.size()-1; i >= 0; --i){
        next[z[i]] = next[next[z[i]]];
    }
}

int main()
{

    freopen("restructure.in", "r", stdin);
    freopen("restructure.out", "w", stdout);

    int n,q;
    scanf("%d%d", &n, &q);
    next.assign(n+2, 0);
    p.assign(n+2, 0);
    r.assign(n+2, 0);

    for(int i = 0; i < n+2; ++i){
        next[i] = i+1;
        p[i] = i;
    }

    int t,a,b;
    for(int i = 0; i < q; ++i){
        scanf("%d%d%d", &t, &a, &b);

        if(t == 1){
            un(a,b);
        } else if(t == 2){
            un_more(a,b);
        } else{
            int x = find_root(a);
            int y = find_root(b);

            if(x == y){
                printf("YES\n");
            } else{
                printf("NO\n");
            }
        }
    }

    return 0;
}
