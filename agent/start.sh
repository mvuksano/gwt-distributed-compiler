#!/bin/bash
java -agentpath:/home/markovuksanovic/bin/yjp-9.0.5/bin/linux-x86-64/libyjpagent.so=onexit=snapshot -Xmx1024m -Xms1024m -classpath /home/markovuksanovic/devel/open-source/gwt-distributed-compiler/agent/dist/*:lib/*:/home/markovuksanovic/devel/gwt/trunk/build/staging/gwt-0.0.0/* com.google.gwt.dist.compiler.agent.Agent

