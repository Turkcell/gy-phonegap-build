/*
 * Copyright © 2013 Turkcell Teknoloji Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ttech.cordovabuild.infrastructure.queue;

import com.hazelcast.config.Config;
import com.hazelcast.config.Join;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.ttech.cordovabuild.domain.BuildInfo;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigProperty;


@ApplicationScoped
public class HazelcastProducer {

    @Inject
    @ConfigProperty(name = "build.queue.mapname")
    private String queueMap;
    @Inject
    @ConfigProperty(name = "build.queue.name")
    private String sourceQueue;

    @Produces
    @ApplicationScoped
    HazelcastInstance createHazelCast() {
        Config cfg = new Config();

        cfg.setProperty("hazelcast.logging.type", "Slf4j");

        NetworkConfig network = cfg.getNetworkConfig();
        network.setPort(5900);
        network.setPortAutoIncrement(false);
        Join join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);



        MapConfig mapCfg = new MapConfig();
        mapCfg.setName(queueMap);
        mapCfg.setBackupCount(1);
        mapCfg.getMaxSizeConfig().setSize(1000);
        mapCfg.getMaxSizeConfig().setMaxSizePolicy(MaxSizeConfig.POLICY_CLUSTER_WIDE_MAP_SIZE);
        mapCfg.setTimeToLiveSeconds(300);

        QueueConfig qConfig = new QueueConfig();
        qConfig.setName(sourceQueue);
        qConfig.setBackingMapRef(queueMap);

        cfg.addMapConfig(mapCfg);
        return Hazelcast.newHazelcastInstance(cfg);
    }

    @Produces
    @BuildQueue
    IQueue<BuildInfo> createBuildQueue(HazelcastInstance hazelcast) {
        return hazelcast.getQueue(sourceQueue);
    }
}
