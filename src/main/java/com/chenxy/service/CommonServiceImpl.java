package com.chenxy.service;


import com.chenxy.entity.Order;
import com.chenxy.entity.OrderItem;
import com.chenxy.repository.OrderItemRepository;
import com.chenxy.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonServiceImpl implements CommonService {

    @Override
    public void initEnvironment() {
        getOrderRepository().createTableIfNotExists();
        getOrderItemRepository().createTableIfNotExists();
        getOrderRepository().truncateTable();
        getOrderItemRepository().truncateTable();
    }

    @Override
    public void cleanEnvironment() {
        getOrderItemRepository().dropTable();
        getOrderItemRepository().dropTable();
    }

    @Override
    public void processSuccess() {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        printData();
        deleteData(orderIds);
        printData();
        System.out.println("-------------- Process Success Finish --------------");
    }

    @Override
    public void processFailure() {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }
    
    private List<Long> insertData() {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList(10);
        for (int i = 1; i <= 10; i++) {
            Order order = newOrder();
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            getOrderRepository().insert(order);
            OrderItem item = newOrderItem();
            item.setOrderId(order.getOrderId());
            item.setUserId(i);
            item.setStatus("INSERT_TEST");
            getOrderItemRepository().insert(item);
            result.add(order.getOrderId());
        }
        return result;
    }
    
    private void deleteData(final List<Long> orderIds) {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            getOrderRepository().delete(each);
            getOrderItemRepository().delete(each);
        }
    }
    
    public void printData() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        System.out.println(getOrderRepository().selectAll());
        System.out.println("---------------------------- Print OrderItem Data -------------------");
        System.out.println(getOrderItemRepository().selectAll());
    }
    
    protected abstract OrderRepository getOrderRepository();
    
    protected abstract OrderItemRepository getOrderItemRepository();
    
    protected abstract Order newOrder();
    
    protected abstract OrderItem newOrderItem();
}
