#include <iostream>
#include <vector>
#include <fstream>
#include <stdlib.h>

using namespace std;

int qfind(int first, int last);
int n,k;
vector<int> a;

int qfind(int first, int last){

    if(first>=last){
        return a[first];
    }

    int f=first;
    int l=last;
    int mid = a[f+rand()%(l-f+1)];
    while(f<=l){
        while(a[f]<mid){
            f++;
        }
        while(a[l]>mid){
            l--;
        }
        if(f<=l){
            int tmp=a[f];
            a[f]=a[l];
            a[l]=tmp;
            f++;
            l--;
        }
    }
    if(first<=k && k<=l){
        return qfind(first,l);
    }
    else if(f<=k && k<=last){
        return qfind(f,last);
    }
    else{
        return mid;
    }
}

int main()
{
    freopen("kth.in", "r", stdin);
    freopen("kth.out", "w", stdout);

    scanf("%d%d",&n,&k);
    a.assign(n+1,0);
    int A,B,C;

    scanf("%d%d%d",&A,&B,&C);
    if(n==1){
        scanf("%d",&a[1]);
    }
    else{
        scanf("%d%d",&a[1], &a[2]);
        for (int i=3; i<=n; i++){
            a[i]=A*a[i-2]+B*a[i-1]+C;
        }
    }

    cout << qfind(1,n);
    return 0;
}
