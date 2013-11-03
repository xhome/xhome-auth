run:
	./hotdeploy.sh > /dev/null 2>&1 &
	mvn tomcat7:run-war -Dmaven.test.skip=true
package:
	mvn package -Dmaven.test.skip=true
clean:
	mvn clean
update:
	hg pull && hg update
