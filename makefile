install:
	mvn install -Dmaven.test.skip=true
push:
	hg push https://scm.xhomestudio.org/xhome/xhome-auth
package:clean
	mvn package -Dmaven.test.skip=true
clean:
	mvn clean
deploy:
	mvn javadoc:jar source:jar deploy -Dmaven.test.skip=true
update:
	hg pull && hg update
