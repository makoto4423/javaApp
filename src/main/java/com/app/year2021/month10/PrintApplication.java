package com.app.year2021.month10;

public class PrintApplication {

    public static void main(String[] args){

        System.out.println("global:\n  resolve_timeout: \"5m\"\ninhibit_rules:\n- equal:\n  - \"alertname\"\n  source_match_re:\n    cluster_id: \"afa-cluster\"\n    policy_id: \"alaPolicy-4LbqKeGV|alaPolicy-BxC5sT1W\"\n    level: \"info\"\n  target_match_re:\n    cluster_id: \"afa-cluster\"\n    policy_id: \"alaPolicy-4LbqKeGV|alaPolicy-BxC5sT1W\"\n    level: \"emerg|warning\"\nreceivers:\n- webhook_configs:\n  - max_alerts: 15\n    url: \"http://10.8.6.125:8097/event/pushEvent\"\n  - max_alerts: 15\n    url: \"http://10.8.1.1:8097/event/pushEvent\"\n  name: \"web.hook\"\n- webhook_configs:\n  - max_alerts: 3\n    url: \"http://10.8.6.125:8097/event/heartbeat\"\n  #  url: \"http://10.8.4.208:8882/TestPro/HttpGet?App-Code=TestPro&Trade-Code=HttpGet\"\n  name: \"heartbeat\"\nroute:\n  group_interval: \"1m\"\n  receiver: \"web.hook\"\n  group_wait: \"30s\"\n  group_by:\n  - \"instance\"\n  - \"cluster_id\"\n  - \"policy_name\"\n  repeat_interval: \"3m\"\n  routes:\n  - receiver: \"heartbeat\"\n    group_wait: \"10s\"\n    match:\n      type: heartbeat\n");

    }

}
