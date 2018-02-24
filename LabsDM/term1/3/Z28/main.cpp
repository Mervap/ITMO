#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    freopen("nextmultiperm.in", "r", stdin);
    freopen("nextmultiperm.out", "w", stdout);


    int n;
    cin >> n;
    vector<int> a(n);

    for(int i = 0; i < n; ++i){
        cin >> a[i];
    }

    int i = n - 1;
    while(i > 0 && a[i] <= a[i-1]){
        --i;
    }
    --i;

    if(i < 0){
        for(int i = 0; i < n; ++i){
            cout << "0 ";
        }
        cout << "\n";
    } else{
        for(int j = 0; j < i; ++j){
            cout << a[j] << " ";
        }

        int mn = i + 1;
        for(int j = i + 1; j < n; ++j){
            if(a[j] < a[mn] && a[j] > a[i]){
                mn = j;
            }
        }
        swap(a[i], a[mn]);
        sort(a.begin() + i + 1, a.end());

        cout << a[i] << " ";
        for(int j = i+1; j < n; ++j){
            cout << a[j] << " ";
        }
        cout << "\n";
    }

    return 0;
}
