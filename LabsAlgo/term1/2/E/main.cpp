#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

void radixSort(vector<string> &a, int k, int m, int n);

void radixSort(vector<string> &a, int k, int m, int n){

    vector<string> b(n);
    for(int i=m-1; i>=m-k; i--){
        vector<int> s(27);
        for(int j=0; j<n; j++){
            s[a[j][i]-'a']++;
        }
        int cnt=0;
        for(int j=0; j<=27; j++){
            int tmp=s[j];
            s[j]=cnt;
            cnt+=tmp;
        }

        for(int j=0; j<n; j++){
            b[s[a[j][i]-'a']]=a[j];
            s[a[j][i]-'a']++;
        }
        a=b;
    }

}

int main()
{
    freopen("radixsort.in", "r", stdin);
    freopen("radixsort.out", "w", stdout);

    int n,m,k;
    cin >> n >> m >> k;

    vector<string> a(n);

    for(int i=0; i<n; i++){
        cin >> a[i];
    }

    radixSort(a,k,m,n);

    for(int i=0; i<n; i++){
        cout <<  a[i] << "\n";
    }
    return 0;
}
