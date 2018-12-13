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


import com.chenxy.config.*;
import com.chenxy.repository.JDBCOrderItemRepositoryImpl;
import com.chenxy.repository.JDBCOrderRepositoryImpl;
import com.chenxy.service.CommonService;
import com.chenxy.service.RawPojoService;
import com.chenxy.type.ShardingType;

import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * 这个例子是可行的
 */
public class JavaConfigurationExample {
    
    //private static ShardingType type = ShardingType.SHARDING_DATABASES;
    //private static ShardingType type = ShardingType.SHARDING_TABLES;
    private static ShardingType type = ShardingType.SHARDING_DATABASES_AND_TABLES;
//    private static ShardingType type = ShardingType.MASTER_SLAVE;
//    private static ShardingType type = ShardingType.SHARDING_MASTER_SLAVE;
    
    public static void main(final String[] args) throws SQLException {
        process(getDataSource());
    }
    
    private static DataSource getDataSource() throws SQLException {
        ExampleConfiguration exampleConfig;
        switch (type) {
            case SHARDING_DATABASES:
                exampleConfig = new ShardingDatabasesConfiguration();
                break;
            case SHARDING_TABLES:
                exampleConfig = new ShardingTablesConfiguration();
                break;
            case SHARDING_DATABASES_AND_TABLES:
                exampleConfig = new ShardingDatabasesAndTablesConfiguration();
                break;
            case MASTER_SLAVE:
                exampleConfig = new MasterSlaveConfiguration();
                break;
            case SHARDING_MASTER_SLAVE:
                exampleConfig = new ShardingMasterSlaveConfiguration();
                break;
            default:
                throw new UnsupportedOperationException(type.name());
        }
        return exampleConfig.getDataSource();
    }
    
    private static void process(final DataSource dataSource) {
        CommonService commonService = getCommonService(dataSource);
        commonService.initEnvironment();
        commonService.processSuccess();
        commonService.cleanEnvironment();
    }
    
    private static CommonService getCommonService(final DataSource dataSource) {
        return new RawPojoService(new JDBCOrderRepositoryImpl(dataSource), new JDBCOrderItemRepositoryImpl(dataSource));
    }
}
