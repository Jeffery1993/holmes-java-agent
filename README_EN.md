# holmes-java-agent #

<a href="http://lucene.apache.org/">
    <img src="https://img.shields.io/badge/lucene-7.4.0-brightgreen.svg" alt="lucene">
</a>
<a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.10-brightgreen.svg" alt="vue">
</a>
<a href="https://github.com/ElemeFE/element">
    <img src="https://img.shields.io/badge/element--ui-2.8.2-brightgreen.svg" alt="element-ui">
</a>
<a href="https://github.com/Jeffery1993/holmes-java-agent/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/mashape/apistatus.svg" alt="license">
</a>
<a href="https://github.com/Jeffery1993/holmes-java-agent/releases">
    <img src="https://img.shields.io/github/release/Jeffery1993/holmes-java-agent.svg" alt="GitHub release">
</a>
  
Holmes-java-agent is a tool for java application performance monitoring, which is based on bytecode enhancement.

## Screenshots ##
### Trace ###
![Image text](screenshots/trace.png)

### Monitor ###
![Image text](screenshots/monitor.png)

## Features ##
- [x] Non-invasive distributed-tracing and Java application performance monitor
- [x] No middle-wares are needed except JRE
- [x] A web page is developed for searching and visualization

## Tutorial ##
```
// unzip the downloaded package
unzip holmes-java-agent.zip
// goto the unzipped directory
cd holmes-java-agent
// start the server and visit "http://localhost:8080"
java -jar holmes-server.jar
// start the demo with agent and visit "http://localhost:1234/swagger-ui.html"
java -javaagent:holmes-agent.jar -DclusterId=demo -jar holmes-demo.jar
```

## Build ##
```
// clone the code
git clone https://github.com/Jeffery1993/holmes-java-agent.git
// goto the web module
cd holmes-java-agent/server/src/main/web
// install web dependencies
npm install
// build the web module
npm run build
// copy all the files from dist to resource directory
mv dist/* ../resources/common/static

// goto the root directory
cd holmes-java-agent
// build the whole project
mvn clean install
```
