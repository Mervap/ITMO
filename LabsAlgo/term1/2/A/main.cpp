#include <iostream>
#include <vector>
#include <fstream>

using namespace std;
vector<int> a;

void msort(int l, int r);

void msort(int l, int r){
    if(l>=r){
        return;
    }
    int mid=(r+l)/2;
    msort(l,mid);
    msort(mid+1,r);
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

int main()
{
    freopen("sort.in", "r", stdin);
    freopen("sort.out", "w", stdout);

    int n;
    cin >> n;

    a.assign(n, 0);

    for(int i=0; i<n; i++){
        cin >> a[i];
    }

    msort(0,n-1);

    for(int i=0; i<n; i++){
        cout << a[i] << " ";
    }
    return 0;
}
