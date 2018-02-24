#include <iostream>
#include <fstream>
#include <stdlib.h>
using namespace std;

int f,l;
void push(int k);
long long pop();
long long *a;
long long *b;
int s;

int tec;

int main()
{
    freopen("queue1.in", "r", stdin);
    freopen("queue1.out", "w", stdout);

    f=l=0;

    a = (long long*) malloc(100*sizeof(long long));
    s=100;
    tec=0;
    int n,k;
    char c;


    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    cin >> n;
    for(int i=0; i<n; i++){
        cin >> c;
        if(c=='+'){
            cin >> k;
            push(k);
        }
        else{
            cout << pop() << "\n";
        }
    }

    return 0;
}

void push(int k){
    if(l==s){
        s = (l-f+1)*2;
        b=a;
        a = (long long*) malloc(s*sizeof(long long));
        for(int i=f; i<l; i++){
            a[i-f]=b[i];
        }

        l-=f;
        f=0;
        free(b);

        a[l]=k;
        l++;
    }
    else{
        a[l]=k;
        l++;
    }


}

long long pop(){
    long long z = a[f];
    f++;
    if(l-f<=s/4){
        s=(l-f+1)*2;
        b=a;
        a = (long long*) malloc(s*sizeof(long long));
        for(int i=f; i<l; i++){
            a[i-f]=b[i];
        }
        free(b);

        l=l-f;
        f=0;
    }
    return z;
}
