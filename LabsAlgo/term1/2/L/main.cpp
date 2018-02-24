#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>

using namespace std;
double eps=1e-6;

void msort(vector<int> &a, int l, int r){
    if(l>=r){
        return;
    }
    int mid=(r+l)/2;
    msort(a,l,mid);
    msort(a,mid+1,r);
    vector <int> b(r-l+1);

    int j1, j2;
    j1=l;
    j2=mid+1;
    for(int i=l; i<=r; i++){
        if(j1<=mid && (j2>r || (a[j1]<=a[j2]))){
            b[i-l]=a[j1];
            j1++;
        }
        else{
            b[i-l]=a[j2];
            j2++;
        }
    }

    for(int i=l; i<=r; i++){
        a[i]=b[i-l];
    }
}

void qfind(vector<pair<double,int>> &a, int first, int last, int k){

    if(first>=last){
        return;
    }

    int f=first;
    int l=last;
    double mid = a[f+rand()%(l-f+1)].first;
    while(f<=l){
        while(a[f].first>mid){
            f++;
        }
        while(a[l].first<mid){
            l--;
        }
        if(f<=l){
            pair<double,int> tmp=a[f];
            a[f]=a[l];
            a[l]=tmp;
            f++;
            l--;
        }
    }
    if(first<=k && k<=l){
        return qfind(a,first,l,k);
    }
    else if(f<=k && k<=last){
        return qfind(a,f,last,k);
    }
    else{
        return;
    }
}



int main()
{
    freopen("kbest.in", "r", stdin);
    freopen("kbest.out", "w", stdout);

    int n,k;
    scanf("%d%d", &n, &k);
    if(n==1){
        cout << 1;
        return 0;
    }

    double b,c;
    vector<pair<double,double>> a(n);
    vector<pair<double,int>> s(n);
    for(int i=0; i<n; i++){
        scanf("%lf%lf", &b, &c);
        a[i]=make_pair(b,c);
    }

    vector<int> p(k),ans(k);

    double l=-1, r=1e5;
    double mid=0;
    int zz=0;
    while(zz<100){
        mid=(r+l)/2;
        if(abs(l)+abs(r)<eps || mid==r || mid==l){
            break;
        }
        for(int i=0; i<n; i++){
            s[i]=make_pair(a[i].first-mid*a[i].second, i+1);
        }
        qfind(s,0,n-1,k-1);
        double res=0;
        for(int i=0; i<k; i++){
            res+=s[i].first;
            p[i]=s[i].second;
        }
        if(res>0){
            l=mid;
            ans=p;
        }
        else{
            r=mid;
        }
        zz++;
    }


    msort(ans,0,k-1);

    for(int i=0; i<k; i++){
        printf("%d ", ans[i]);
    }

    return 0;
}
