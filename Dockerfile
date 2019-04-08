FROM openjdk:8

ENV SCALA_VERSION 2.12.8
ENV SBT_VERSION 1.2.8

RUN \
 curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
 echo >> /root/.bashrc && \
 echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

CMD /root/scala-$SCALA_VERSION/bin/scala /code/app.jar