/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.chenxy;


import com.chenxy.repository.JDBCOrderItemRepositoryImpl;
import com.chenxy.repository.JDBCOrderRepositoryImpl;
import com.chenxy.service.CommonService;
import com.chenxy.service.RawPojoService;
import com.chenxy.type.ShardingType;
import io.shardingsphere.shardingjdbc.api.yaml.YamlMasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * sharding-jdbc测试
 * @author chenxy
 */
public class YamlConfigurationExample {
    /** 分库  */
    //private static ShardingType type = ShardingType.SHARDING_DATABASES;
    /** 分表 */
    //private static ShardingType type = ShardingType.SHARDING_TABLES;
     /** 分库分表 */
      //private static ShardingType type = ShardingType.SHARDING_DATABASES_AND_TABLES;
    /**  读写分离 */
    private static ShardingType type = ShardingType.MASTER_SLAVE;
    /** 分库分表加读写分离*/
    //private static ShardingType type = ShardingType.SHARDING_MASTER_SLAVE;
    
    public static void main(final String[] args) throws SQLException, IOException {
        process(getDataSource());
    }
    
    private static DataSource getDataSource() throws IOException, SQLException {
        return ShardingType.MASTER_SLAVE == type ? YamlMasterSlaveDataSourceFactory.createDataSource(getYamlFile()) : YamlShardingDataSourceFactory.createDataSource(getYamlFile());
    }
    
    private static File getYamlFile() {
        String result;
        switch (type) {
            case SHARDING_DATABASES:
                result = "/META-INF/sharding-databases.yaml";
                break;
            case SHARDING_TABLES:
                result = "/META-INF/sharding-tables.yaml";
                break;
            case SHARDING_DATABASES_AND_TABLES:
                result = "/META-INF/sharding-databases-tables.yaml";
                break;
            case MASTER_SLAVE:
                result = "/META-INF/master-slave.yaml";
                break;
            case SHARDING_MASTER_SLAVE:
                result = "/META-INF/sharding-master-slave.yaml";
                break;
            default:
                throw new UnsupportedOperationException(type.name());
        }
        return new File(YamlConfigurationExample.class.getResource(result).getFile());
    }
    
    private static void process(final DataSource dataSource) {
        CommonService commonService = getCommonService(dataSource);
        commonService.initEnvironment();
        commonService.processSuccess();
        //commonService.cleanEnvironment();
    }
    
    private static CommonService getCommonService(final DataSource dataSource) {
        return new RawPojoService(new JDBCOrderRepositoryImpl(dataSource), new JDBCOrderItemRepositoryImpl(dataSource));
    }
}
