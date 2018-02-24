#include <iostream>
#include <vector>
#include <string>

using namespace std;
int n;
unsigned long long k,m,r;

string bld(unsigned long long i){
    if(i==0){
        return "1|~1";
    }

    string ans="";
    unsigned long long p=1;
    for(int j=0; j<n; j++){
        if((i&p)!=0){
            if(ans.length()==0){
                ans=to_string(n-j);
            }
            else{
                ans+="&"+to_string(n-j);
            }
        }
        p = p << 1;
    }
    return ans;
}

int main()
{
    cin >> n;
    k=1;

    unsigned long long s,z,sum;

    for(int i=0; i<n; i++){
        k*=2;
    }

    vector<unsigned long long> b(n);
    for(int i=0; i<n; i++){
        cin >> b[i];
    }

    cin >> s;

    z=1;
    vector<vector<int>> a;
    a.assign(k+1,vector<int> (k+1,-1));
    for(int i=0; i<64; i++){
        sum=0;
        for(int j=0; j<n; j++){
            sum = sum << 1;
            sum+=(b[j]&z)!=0;
        }
        if(a[0][sum]!=-1 && a[0][sum]!=((s&z)!=0)){
            cout << "Impossible";
            return 0;
        }
        a[0][sum]=((s&z)!=0);
        z = z << 1;
    }

    for(int i=0; i<k; i++){
        if(a[0][i]==-1){
            a[0][i]=0;
        }
    }

    for(int i=1; i<k; i++){
        for(int j=0; j<k-i; j++){
            a[i][j]=(a[i-1][j]^a[i-1][j+1])!=0;
        }
    }

    string ans="";

    vector<string> an;
    for(int i=0; i<k; i++){
        if(a[i][0]!=0){
            an.push_back(bld(i));
        }
    }

    if(an.size()==0){
        cout << "1&~1";
        return 0;
    }

    ans=an[0];
    for(int i=1; i<an.size(); i++){
        ans= "(~("+ans+")&("+an[i]+"))|((" + ans + ")&~("+an[i]+"))";
    }

    cout << ans <<"\n";
    return 0;
}
