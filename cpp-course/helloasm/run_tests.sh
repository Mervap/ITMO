#!/bin/bash -e

for i in test*.txt
do
    if test -f "$i" 
    then
        if ./add < "$i" | cmp -s - $(basename $i .txt).gold
        then
            echo "$i: OK"
        else
            echo "$i: FAIL"
        fi
    fi
done
