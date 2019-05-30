FROM tomcat:8.5.15-jre8

RUN rm -rf /usr/local/tomcat/webapps/ROOT

RUN mkdir /root/.aws

COPY credentials /root/.aws

COPY config /root/.aws

ADD jobche-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

ENV spring.profiles.active=dev

CMD ["catalina.sh", "run"]
