GHI_UTF_ProstateOrderProcessing.createOrder(); 
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
Order order = [select OSM_Status__c from order where id = :$orderId];
System.assertEquals('Processing', order.OSM_Status__c);