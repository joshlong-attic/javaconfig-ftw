#!/bin/bash 
pwd

  
function clean {
  echo	"Running search for $1" ;
	find . -type d -iname  $1| while read l; do  echo "deleting $l." ; done 
}   


clean "*iml"
clean "target"
clean "*idea"

#find . -type f -iname  "*iml"| while read l; do  echo "deleting $l." ; rm -rf "$l"; done 

#find . -type d -iname  "target"| while read l; do  echo "deleting $l." ; rm -rf "$l"; done 

