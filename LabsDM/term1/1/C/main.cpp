#include <iostream>
#include <vector>

using namespace std;

bool check_t0(int s, vector<int> &a){
    if(a[0]==0){
        return false;
    }
    else{
        return true;
    }
}

bool check_t1(int s, vector<int> &a){
    if(a[a.size()-1]==1){
        return false;
    }
    else{
        return true;
    }
}

bool check_ts(int s, vector<int> &a){
    int l=a.size();
    for(int i=0; i<l/2+l%2; i++){
        if(a[i]==a[l-i-1]){
            return true;
        }
    }

    return false;
}

bool check_tm(int s, vector<int> &a){
    int l=a.size();
    for(int i=0; i<l; i++){
        int p=1;
        for(int j=0; j<s; j++){
            if(a[i]>a[i|p]){
                return true;
            }
            p*=2;
        }
    }

    return false;
}

bool check_tl(int s, vector<int> &a){
    int l=a.size();

    vector<vector<int>> b;
    b.assign(l,vector<int> (l));

    for(int i=0; i<l; i++){
        b[0][i]=a[i];
    }

    for(int i=1; i<l; i++){
        for(int j=0; j<l-i; j++){
            b[i][j]=b[i-1][j]^b[i-1][j+1];
        }
    }

    int p=1;
    for(int i=1; i<l; i++){
        if(i==p){
            p = p << 1;
            continue;
        }
        if(b[i][0]!=0){
            return true;
        }
    }

    return false;
}

int main()
{
    int n;
    cin >> n;

    bool t0,t1,ts,tm,tl;
    int s;
    string c;
    for(int i=0; i<n; i++){
        cin >> s >> c;
        vector <int> a;
        for(int j=0; j<c.length(); j++){
            a.push_back(c[j]-'0');
        }
        if(!t0){
            t0=check_t0(s,a);
        }
        if(!t1){
            t1=check_t1(s,a);
        }
        if(!ts){
            ts=check_ts(s,a);
        }
        if(!tm){
            tm=check_tm(s,a);
        }
        if(!tl){
            tl=check_tl(s,a);
        }

        if(t0 && t1 && ts && tm && tl){
            cout << "YES";
            return 0;
        }
    }


    cout << "NO" << endl;
    return 0;
}
