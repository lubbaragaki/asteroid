if [ $(whoami) != "root" ]
then
	echo "Must be run as root"
	exit 1
fi
JAR_LINK="https://github.com/lubbaragaki/asteroid/releases/download/1.0/asteroid.jar"
mkdir -p /usr/local/share/asteroid
wget "$JAR_LINK"
mv asteroid.jar /usr/local/share/asteroid/asteroid.jar

printf "exec java -jar /usr/local/share/asteroid/asteroid.jar \$@" > /usr/local/bin/asteroid

chmod +x /usr/local/bin/asteroid
