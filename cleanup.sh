#!/bin/bash 

# remove the directory of common build artifacts that sometimes need to be regenerated.

pwd

function clean {
	
	echo "Running search for '$1' of type '$2'." ;
	
	find . -type $2 -iname  $1 | while read l ; do
		echo "  deleting $l.";
		rm -rf "$l" ;
	done

}   

# Eclipse stuff
clean ".settings" d

# IntelliJ
clean "*iml" f
clean "*idea" d

# Maven
clean "target" d