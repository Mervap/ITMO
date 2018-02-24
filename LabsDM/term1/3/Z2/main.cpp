#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

int main()
{
    freopen("gray.in", "r", stdin);
    freopen("gray.out", "w", stdout);

    int n;
    cin >> n;

    vector<string> a;

    a.push_back("0");
    a.push_back("1");

    for(int i = 1; i < n; i++){
        for(int j = (int) a.size()-1; j >= 0; --j){
            a.push_back(a[j]);
        }

        for(int j = 0; j < (int) a.size()/2; ++j){
            a[j] = "0" + a[j];
        }
        for(int j = (int) a.size()/2; j < (int) a.size(); ++j){
            a[j] = "1" + a[j];
        }
    }


    for(int i = 0; i < (int) a.size(); ++i){
        cout << a[i] << "\n";
    }
    return 0;
}
