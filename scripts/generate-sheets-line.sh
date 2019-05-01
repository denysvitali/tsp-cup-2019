#!/bin/bash
FILE=$1
BEST=$(cat $FILE | jq '.rl' | sort | head -n 1);
BEST_T=$(cat $FILE | jq "select(.rl == $BEST) | .time_elapsed" | sort -n | head -n 1)
cat $FILE | jq "select (.rl == $BEST) | select(.time_elapsed==$BEST_T) | {problem: .problem, alg: .algorithms[2], perf: .rl}" | jq -r '"\(.problem)'$'\t''\(.alg.mode)'$'\t''\(.alg.seed)'$'\t''\(.alg.params.alpha)'$'\t''\(.alg.params.start_temperature)'$'\t''\(.alg.params.r)'$'\t''\(.perf)'$'\t'$BEST_T'"'
