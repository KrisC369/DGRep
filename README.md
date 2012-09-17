##############################################################################
Datagram Replayer or DGRep in short is a tool for redistributing a UDP Packet
stream to a configurable number of hosts. 

It's main use case arose from having another separate application that could
only deliver a UDP packet stream to one host without broadcast capabilities.
This tool receives one such stream and through a configuration file, sends
out the udp stream to a number of hosts in realtime.

I created this a number of years back for a school project but never
bothered to share it on any hosting site.

Maven is used to handle dependencies and further development.
After cloning the repository, just run the following commands
to build and package the application into a JAR:

    mvn validate
    mvn install

Afterwards u can run the application with:

    java -cp target/DGRep-2.0-SNAPSHOT.jar be.kriscon.dgrep.DatagramReplayer

The config file is located in src/main/resources/

I also used this a little example of using the gcj compiler to compile to
native machine code. 

After invoking the maven build system, try the following commands
to create a native executable.

    gcj -fjni --CLASSPATH=/home/kristof/gitworkspace/DGRep-3.0-SNAPSHOT.jar --no-bounds-check -O3 -c -g DGRep-2.0-SNAPSHOT.jar -o DGREP.o
    gcj -fjni --main=be.kriscon.dgrep.DatagramReplayer -o dgrep DGREP.o

############################################################################
