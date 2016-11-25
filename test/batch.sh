#!/bin/bash

while true; do
    for ((i = 0; i < 300; ++i)); do
        read cityid query
        if [[ -z "$cityid" || -z "$query" ]]; then
            exit
        fi

        python client.py $cityid $query &
        #python diff_client.py $cityid $query &
    done
    wait
done
