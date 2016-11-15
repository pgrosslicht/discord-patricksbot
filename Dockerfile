FROM openjdk:8-jre
ADD ./build/distributions/PatricksBot.tar /usr/src/
WORKDIR /usr/src/PatricksBot
CMD ["/usr/src/UnitBot/bin/PatricksBot"]
