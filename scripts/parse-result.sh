#!/bin/bash

# Usage: cat /tmp/file | ./script/parse-result.sh

jq "{problem: .problem, alg: .algorithms[2], perf: .rl, t: .time_elapsed}" - | jq -r '"\(.problem)'$'\t''\(.alg.mode)'$'\t''\(.alg.seed)'$'\t''\(.alg.params.alpha)'$'\t''\(.alg.params.start_temperature)'$'\t''\(.alg.params.r)'$'\t''\(.perf)'$'\t''\(.t)"'
