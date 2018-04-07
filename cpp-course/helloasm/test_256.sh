#!/bin/bash
echo Testing ./mul
time=$(date +%s%N | cut -b1-13)
for number in {1..68}
do
    result=$(python3 generate.py $number| ./mul)

    for line in $(cat test.txt) 
    do
        if [[ "$line" == "$result" ]]; then
            echo "Test $number: OK"
        else
            echo "Test $number: Fail!"
            echo "Expected $line."
            echo "Found $result"
            echo "You failed on test $number"
            exit
        fi
    done
done
echo "Tests passed in $(($(date +%s%N | cut -b1-13) - $time)) miliseconds"
