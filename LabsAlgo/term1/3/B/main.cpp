#include <iostream>
#include <vector>
#include <string>
#include <fstream>

using namespace std;
vector<int> p,r,mn,mx,kol;

int get(int i){
    if(p[i]!=i){
        p[i]=get(p[i]);
    }
    return p[i];
}

void un(int a, int b){
    a=get(a);
    b=get(b);
    if(a==b){
        return;
    }

    if(r[a]==r[b]){
        r[a]++;
    }
    if(r[a]<r[b]){
        p[a]=b;
        mn[b]=min(mn[b],mn[a]);
        mx[b]=max(mx[b],mx[a]);
        kol[b]+=kol[a];
    }
    else{
        p[b]=a;
        mn[a]=min(mn[b],mn[a]);
        mx[a]=max(mx[b],mx[a]);
        kol[a]+=kol[b];
    }
}


int main()
{
    freopen("dsu.in", "r", stdin);
    freopen("dsu.out", "w", stdout);

    int n;
    cin >> n;

    for(int i=0; i<n; i++){
        p.push_back(i);
        r.push_back(0);
        kol.push_back(1);
        mn.push_back(i+1);
        mx.push_back(i+1);
    }

    string s;
    int a,b;
    while(cin >> s){
        if(s=="union"){
            cin >> a >> b;
            un(a-1,b-1);
        }
        else{
            cin >> a;
            a=get(a-1);
            cout << mn[a] << " " << mx[a] << " " << kol[a] << "\n";
        }
    }
    return 0;
}
