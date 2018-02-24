#include <iostream>
#include <fstream>
#include <vector>
#include <math.h>

using namespace std;
vector<int> p;

int n,m,k=0;

int find_set(int v){
    k++;
    if(p[v]!=v){
        p[v]=find_set(p[v]);
    }
    return p[v];
}
void pr(){
    freopen("input.txt", "r", stdin);
    scanf("%d%d ",&n, &m) ;
    p.assign(n+1,0);
    for(int i = 1; i<=n; i++){
        p[i]=i;
    }
    for(int i = 1;i<=m; i++){
        int x,y,z ;
        scanf("%d%d%d ",&z,&x,&y);
        if(z==1) {
            p[y]=x ;
        }
        else {
            if(find_set(x)==find_set(y)){
                printf("YES\n ");
            }
            else{
                printf("NO\n ");
            }
        }
    }
}

void gener(){
    freopen("input.txt", "w", stdout);
    cin >> n >> m;

    cout << n << " " << m << endl;
    if(m == 1){
        cout << "0 1 1";
        return;
    }
    if(m == 2){
        cout << "1 1 2\n";
        cout << "0 1 2";
        return;
    }
    if(m == 3){
        cout << "1 1 2\n";
        cout << "1 1 3\n";
        cout << "0 2 3\n";
        return;
    }

    cout << "1 3 1\n";
    cout << "1 3 2\n";

    m -= 2;
    int i = 4;
    while(m != 0){
        cout << "1 " <<  i << " " << i-1 << "\n";
        --m;
        if(i % 2 == 0){
            int j = 1;
            while(m != 0 && j < i-2){
                cout << "0 " << j << " " << j+1 << "\n";
                j += 2;
                --m;
            }
        } else{
            int j = 2;
            while(m != 0 && j < i-2){
                cout << "0 " << j << " " << j+1 << "\n";
                j += 2;
                --m;
            }
        }
        ++i;
    }

}

int main()
{
    /*int i;
    cin >> i;
    if(i==1){
        gener();
    }
    else{
        pr();
        cout << k;
    }*/

    int n, m ;
    cin >> n >> m;

    if(m == 1){
        printf("0 1 1");
        return 0;
    }
    if(m == 2){
        printf("1 1 2\n");
        printf("0 1 2");
        return 0;
    }
    if(m == 3){
        printf("1 1 2\n");
        printf("1 1 3\n");
        printf("0 2 3\n");
        return 0;
    }

    printf("1 3 1\n");
    printf("1 3 2\n");

    m -= 2;
    int i = 4;
    while(m != 0){
        printf("1 %d %d\n", i, i-1);
        --m;
        if(i % 2 == 0){
            int j = 1;
            while(m != 0 && j < i-2){
                printf("0 %d %d\n", j, j+1);
                j += 2;
                --m;
            }
        } else{
            int j = 2;
            while(m != 0 && j < i-2){
                printf("0 %d %d\n", j, j+1);
                j += 2;
                --m;
            }
        }
        ++i;
    }
    return 0;
}
