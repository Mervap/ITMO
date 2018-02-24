#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <string>

using namespace std;

int main()
{
    freopen("mtf.in", "r", stdin);
    freopen("mtf.out", "w", stdout);
    string s;
    cin >> s;

    vector<int> val(26);
    for(int i = 0; i < 26; ++i){
        val[i] = i;
    }

    int h = 0;

    for(int i = 0; i < s.length(); ++i){

        int k, val1;
        for(int j = 0; j < 26; ++j){
            if(val[j] == s[i] - 'a'){
                k=j+1;
                cout << k << " ";
                break;
            }
        }

        for(int j = k-1; j >= 0; --j){
            val[j]=val[j-1];
        }
        val[0] = s[i] - 'a';
    }

    return 0;
}
