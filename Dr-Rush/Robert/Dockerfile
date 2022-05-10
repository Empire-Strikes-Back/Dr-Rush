FROM adoptopenjdk/openjdk16:jdk-16.0.2_7-ubuntu as jar

RUN apt-get update && apt-get install -y sudo git

# clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.11.1.1113.sh && \
    chmod +x linux-install-1.11.1.1113.sh && \
    sudo ./linux-install-1.11.1.1113.sh

WORKDIR /avatar
COPY deps.edn .
RUN clojure -A:Ripley:Genie -Stree
COPY . .
RUN bash avatar.sh jar

FROM adoptopenjdk/openjdk16:jre-16.0.1_9-alpine

COPY --from=jar /avatar/out/*.jar .

CMD ["java", "-jar", "Dr-Rush-Robert.jar"]