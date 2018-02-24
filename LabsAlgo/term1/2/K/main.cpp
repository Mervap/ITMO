#include <iostream>
#include <vector>
#include <fstream>
#include <math.h>

using namespace std;

int main()
{
    long long x,m,l,r,mid,ans,s,ar=0;
    long long con=1000000000000000000;

    cin >> x >> m;

    cout << "? 1" << endl;
    fflush(stdout);

    cin >> s;
    s--;

    r=x-s;
    if(r<1){
        r=con-abs(r);
    }
    r=min(con-m,r)+1;
    l=max((long long)0,r-m-2);

    while(r-l>1){
        mid=(r+l)/2;
        cout << "? " << mid << endl;
        fflush(stdout);
        cin >> ans;

        if(ans<x || (x<s+1 && ans>=s+1)){
            l=mid;
        }
        else{
            r=mid;
            ar=ans;
        }
    }

    if(ar!=x){
        cout << "! -1"<< endl;
        fflush(stdout);
    }
    else{
        cout << "! " << r << endl;
        fflush(stdout);
    }
    return 0;
}
