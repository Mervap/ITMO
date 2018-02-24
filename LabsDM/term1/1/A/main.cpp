#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;
vector<bool> ans;
vector<vector<int>> gr;
bool f=false;
int n,m;

int main()
{
    int a,b;
    scanf("%d%d", &n, &m);
    gr.assign(2*(n+1), vector<int> (2*(n+1)));
    ans.assign(2*(n+1), false);

    for (int i=0; i<m; i++){
        scanf("%d%d", &a, &b);
        //a=rand()%15+1;
        //b=-(rand()%15+1);
        gr[-a+n][b+n]=1;
        gr[-b+n][a+n]=1;
    }

    for(int i=0; i<=2*n; i++){
        if(i!=n){
            queue<int> q;
            vector<bool> was(2*n+1);
            was[i]=true;
            q.push(i);
            int t;
            while(!q.empty()){
                t=q.front();
                q.pop();
                if(t==2*n-i){
                    ans[i]=true;
                    break;
                }
                for(int j=0; j<=2*n; j++){
                    if(gr[t][j]==1 && was[j]==false){
                        q.push(j);
                        was[j]=true;
                    }
                }
            }
        }
    }

    for(int i=0; i<n; i++){
        if(ans[i] && ans[2*n-i]){
            cout << "YES";
            return 0;
        }
    }
    cout << "NO";
    return 0;
}
