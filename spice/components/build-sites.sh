#!/bin/sh
# quick stupid script by LSD
# this script is in the public domain

# update these projects
PROJECTS="alchemist configkit jndikit loggerstore netserve salt threadpool"
MULTI_PROCESS="no"
ANT_COMMAND="maven site:run-reports xdoc site:sshdeploy"

for module in ${PROJECTS}
do
echo "    - ${module}"
done
echo "*---------------------------------------------------*"
echo ""

CURRDIR=${PWD}

for module in ${PROJECTS}
do
	echo "updating module: " ${module}
	if [ "${MULTI_PROCESS}" = "yes" ]
	then
		$(cd ${CURRDIR}/${module}; ${ANT_COMMAND}) &
	else
		cd ${CURRDIR}/${module}; ${ANT_COMMAND}
	fi
done
