GHI_UTF_IBCOrderProcessing.createOrder(); 
List<Order> orderList = [select OSM_Created_By_User__c from order where id =:$orderId];
if(orderList !=null && orderList.size()>0) {
System.assert(String.isNotBlank(orderList.get(0).OSM_Created_By_User__c));
} else {
Throw new OSM_Exception('Order not found..') ;
}

