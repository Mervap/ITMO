#include <iostream>
#include <vector>

using namespace std;

int main()
{
    int n, k;
    cin >> n >> k;

    vector<vector<int>> p;
    p.assign(k,vector<int> (n));

    vector<vector<int>> z;
    z.assign(k,vector<int> (n,-1));

    vector<bool> was(k);

    for(int i=0; i<k; i++){
        for(int j=0; j<n; j++){
            cin >> p[i][j];
        }
    }

    bool f=true;
    while(f){
        f=false;
        for(int i=0; i<k; i++){
            if(was[i]){
                continue;
            }

            int l=0;
            int w=-1;
            for(int j=0; j<n; j++){
                if(p[i][j]!=-1){
                    l++;
                    w=j;
                }
            }
            if(l==1){
                f=true;
                was[i]=true;
                for(int j=0; j<k; j++){
                    if(was[j]==false && p[j][w]!=-1){
                        z[j][w]=p[i][w];
                        int zz=0,mi=0;
                        for(int m=0; m<n; m++){
                            if(p[j][m]==-1){
                                mi++;
                                continue;
                            }
                            if(z[j][m]==p[j][m]){
                                was[j]=true;
                                break;
                            }
                            if(z[j][m]!=-1 && z[j][m]!=p[j][m]){
                                zz++;
                            }
                        }
                        if(zz+mi==n){
                            cout << "YES";
                            return 0;
                        }
                        p[j][w]=-1;
                    }
                }
            }
        }
    }

    cout << "NO";

    return 0;
}
