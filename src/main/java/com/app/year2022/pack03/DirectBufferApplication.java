package com.app.year2022.pack03;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DirectBufferApplication {

    public static void main(String[] args) throws MalformedObjectNameException, IntrospectionException, ReflectionException, InstanceNotFoundException, IOException, AttributeNotFoundException, MBeanException {
        String jmxURL = "service:jmx:rmi:///jndi/rmi://10.8.4.190:9999/jmxrmi" ;
        JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
        Map map = new HashMap() ;
        String[] credentials = new String[] { "monitorRole" , "QED" } ;
        map.put( "jmx.remote.credentials" , credentials) ;
        JMXConnector connector = JMXConnectorFactory.connect(serviceURL , Collections.emptyMap());
        MBeanServerConnection mbsc = connector.getMBeanServerConnection() ;
        ObjectName objectName = new ObjectName("java.nio:type=BufferPool,name=direct" ) ;
        MBeanInfo mbInfo = mbsc.getMBeanInfo(objectName) ;
        for(MBeanAttributeInfo i : mbInfo.getAttributes()) {
            System.out .println(i.getName() + ":" + mbsc.getAttribute(objectName , i.getName()));
        }
    }


}
