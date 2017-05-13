/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 *
 * @author yasin
 */
public class ElasticClientFactory {
    
    public static Settings settings = null;
    private static String yaml = "elasticsearch.yml";
    
    public static ElasticClient create(String clusterName, String[] serverAddresses) {

//      Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true).put("cluster.name", clusterName).build();
//      TransportClient transportClient = new TransportClient(settings);

//      Settings settings = Settings.builder().build();
//      TransportClient transportClient = new PreBuiltTransportClient(settings);

        
        TransportClient transportClient  =  null;
        try {
            Settings.Builder builder = Settings.builder().loadFromStream(yaml, ElasticClientFactory.class.getClassLoader().getResourceAsStream(yaml));
                   // .put("client.transport.sniff", true)
                   // .put("client.transport.ignore_cluster_name", true);
            if(! Strings.isEmpty(clusterName)){
                builder.put("cluster.name", clusterName);
            }
            
            settings = builder.build();

            /*
            Settings settings = Settings.builder()
                    .put("cluster.name", "mehaz")
                    .put("client.transport.sniff", true)
                    .build();
            */        
            transportClient = new PreBuiltTransportClient(settings);
            
            String ports = settings.get("transport.tcp.port");
            String[] portPair = ports.split("-");
            
            for (int i = 0; i < serverAddresses.length; i++) {
                String[] hostPort = serverAddresses[i].trim().split(":");
                String host = hostPort[0].trim();
                int port = hostPort.length == 2 ? Integer.parseInt(hostPort[1].trim()): Integer.parseInt(portPair[0]);

//            transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
              transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            }
            transportClient.connectedNodes();

        } catch (IOException ex) {
            Logger.getLogger(ElasticClientFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return new ElasticClient(transportClient);

    }
}
