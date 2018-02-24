#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <string>
#include <cstring>
#include <stdio.h>
using namespace std;

const long long mod=65536;

void push(long long  k);
long long pop();

long long  *a;
long long  *b;
long s=100,f=0,l=0;

bool flag;

string n;
string *st;
string *label;
string *tmps;
int *tmpi;
int *label_b;

long long reg[100];

int slab=100, sst=100;
int k=0;
int kol=0;

int main()
{
    freopen("quack.in", "r", stdin);
    freopen("quack.out", "w", stdout);

    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    a = (long long *) malloc(s*sizeof(long long ));
    st = new string[sst];
    label = new string[slab];
    label_b = (int *) malloc(slab*sizeof(int));

    for(int i=0;i<100;i++){
        reg[i]=0;
    }

    while(cin >> n){
        if(k==sst){
            sst = k*2;
            tmps = st;
            st = new string [sst];
            for(int i=0; i<k; i++){
                st[i]=tmps[i];
            }
            delete [] tmps;
        }
        st[k]=n;
        k++;

        if(n[0]==':'){
            if(kol==slab){
                slab = kol*2;
                tmps = label;
                label = new string [slab];
                for(int i=0; i<kol; i++){
                    label[i]=tmps[i];
                }
                delete [] tmps;

                tmpi = label_b;
                label_b = (int *) malloc(slab*sizeof(int));
                    for(int i=0; i<kol; i++){
                    label_b[i]=tmpi[i];
                }
                free(tmpi);
            }
            label[kol]=n.substr(1,n.length()-1);
            label_b[kol]=k-1;
            kol++;
        }
    }

    long long tec=0;

    while(tec<k){

        string ss=st[tec];
        switch (ss[0]){
            case '+':
                push(((pop()+pop())%mod+mod)%mod);
                break;

            case '-':
                push(((pop()-pop())%mod+mod)%mod);
                break;

            case '*':
                push(((pop()*pop())%mod+mod)%mod);
                break;

            case '/':
                if(a[f+1]==0){
                    pop();
                }
                else{
                    push(pop()/pop());
                }
                break;

            case '%':
                if(a[f+1]==0){
                    pop();
                }
                else{
                    push(pop()%pop());
                }
                break;

            case '>':
                reg[ss[1] - 'a']=pop();
                break;

            case '<':
                push(reg[ss[1] - 'a']);
                break;

            case 'P':
                if(strlen(ss.c_str())==1){
                    cout << pop() << "\n";
                }
                else{
                    cout << reg[ss[1] - 'a'] << "\n";
                }
                break;

            case 'C':
                if(strlen(ss.c_str())==1){
                    cout << (char) (pop()%256);
                }
                else{
                    cout << (char) ((reg[ss[1] - 'a'])%256);
                }
                break;

            case ':':

                break;

            case 'J':
                n=ss.substr(1,ss.length()-1);
                for(int i=0; i<k; i++){
                    if(n==label[i]){
                        tec=label_b[i];
                        break;
                    }
                }
                break;

            case 'Z':
                if(reg[ss[1]- 'a']==0){
                    n=ss.substr(2,ss.length()-2);
                    for(int i=0; i<k; i++){
                        if(n==label[i]){
                            tec=label_b[i];
                            break;
                        }
                    }
                }
                break;

            case 'E':
                if(reg[ss[1]- 'a']==reg[ss[2]- 'a']){
                    n=ss.substr(3,ss.length()-3);
                    for(int i=0; i<kol; i++){
                        if(n==label[i]){
                            tec=label_b[i];
                            break;
                        }
                    }
                }
                break;

            case 'G':
                if(reg[ss[1]- 'a']>reg[ss[2]- 'a']){
                    n=ss.substr(3,ss.length()-3);
                    for(int i=0; i<=k; i++){
                        if(n==label[i]){
                            tec=label_b[i];
                            break;
                        }
                    }
                }
                break;

            case 'Q':
                tec=k;
                break;

            default:
                push(atoll(st[tec].c_str()));
                break;

        }
        tec++;
    }

    return 0;
}

void push(long long  k){
    if(l==s){
        s = (l-f+1)*2;
        b=a;
        a = (long long *) malloc(s*sizeof(long long ));
        for(int i=f; i<l; i++){
            a[i-f]=b[i];
        }

        l-=f;
        f=0;
        free(b);
    }
    a[l]=k;
    l++;
}

long long  pop(){
    long long  z = a[f];
    f++;
    if(l-f<=s/4){
        s=(l-f+1)*2;
        b=a;
        a = (long long *) malloc(s*sizeof(long long ));
        for(int i=f; i<l; i++){
            a[i-f]=b[i];
        }
        free(b);

        l=l-f;
        f=0;
    }
    return z;
}
