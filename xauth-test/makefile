run:
	mvn jetty:run -Dmaven.test.skip=true
package:
	mvn package -Dmaven.test.skip=true
publish:package
	cp target/xauth-test.war $(CATALINA_BASE)/webapps
clean:
	mvn clean
update:
	hg pull && hg update
