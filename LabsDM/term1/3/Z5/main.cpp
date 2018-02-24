#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

int main()
{
    freopen("telemetry.in", "r", stdin);
    freopen("telemetry.out", "w", stdout);

    int n, k;
    cin >> n >> k;

    vector<string> a;

    for(int i = 0; i < k; ++i){
        a.push_back(to_string(i));
    }


    for(int i = 1; i < n; i++){

        int z = (int) a.size();
        for(int l = 0; l < k-1; ++l){
            int s = (int) a.size();
            for(int j = s-1; j >= s-z; --j){
                a.push_back(a[j]);
            }
        }

        z = ( (int) a.size() )/k;
        for(int j = 0; j < k; ++j){
            for(int l = 0; l < z; ++l){
                a[j*z + l] = to_string(j) + a[j*z + l];
            }
        }
    }


    for(int i = 0; i < (int) a.size(); ++i){
        cout << a[i] << "\n";
    }
    return 0;
}
