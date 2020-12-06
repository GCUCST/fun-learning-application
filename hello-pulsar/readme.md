##docker env
docker run -d -it -p 6650:6650 -p 8080:8080 -v pulsardata:/pulsar/data -v pulsarconf:/pulsar/conf --name pulsar-standalone apachepulsar/pulsar:latest bin/pulsar standalone
