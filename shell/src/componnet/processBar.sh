#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2017/11/07
# Author:  wj
# Email:   285477597@qq.com
# Description:  processBar
############################################################

processbar() {
  local current=$1; local total=$2;
  local maxlen=80; local barlen=66; local perclen=14;
  local format="%-${barlen}s%$((maxlen-barlen))s"
  local perc="[$current/$total]"
  local progress=$((current*barlen/total))
  local prog=$(for i in `seq 0 $progress`; do printf '#'; done)
  printf "\r$format" $prog $perc
}

# Usage(Client)
for i in `seq 1 100`; do
  processbar $i 100
  sleep 1
done
echo ""